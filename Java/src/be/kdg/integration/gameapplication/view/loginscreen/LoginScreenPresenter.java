package be.kdg.integration.gameapplication.view.loginscreen;

import be.kdg.integration.gameapplication.model.alertproperties.AlertSetting;
import be.kdg.integration.gameapplication.model.authservices.AuthentificationManager;
import be.kdg.integration.gameapplication.model.authservices.services.*;
import be.kdg.integration.gameapplication.model.settings.GameSettings;
import be.kdg.integration.gameapplication.model.settings.GameSoundFX;
import be.kdg.integration.gameapplication.model.settings.GameSoundManager;
import be.kdg.integration.gameapplication.model.settings.SoundException;
import be.kdg.integration.gameapplication.model.user.Guest;
import be.kdg.integration.gameapplication.model.user.User;
import be.kdg.integration.gameapplication.view.UIHandler;
import be.kdg.integration.gameapplication.view.customalert.CustomAlertCall;
import be.kdg.integration.gameapplication.view.gamesound.GameSoundPresenter;
import be.kdg.integration.gameapplication.view.gamesound.GameSoundView;
import be.kdg.integration.gameapplication.view.start.StartPresenter;
import be.kdg.integration.gameapplication.view.start.StartView;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.SQLException;

public class LoginScreenPresenter{
    private LoginScreenView loginScreenView;
    private LoginService loginService;
    private RegistrationService registrationService;
    private PasswordRecoveryService passwordRecoveryService;
    private StartView startView;
    private User user;
    private AuthentificationManager authentificationManager;
    private GameSoundManager gameSoundManager;
    private boolean noConnection;
    private Timeline loadingAnimation;
    private UIHandler uiHandler;

    public LoginScreenPresenter(LoginScreenView loginScreenView, AuthentificationManager authentificationManager, UIHandler uiHandler){
        this.loginScreenView = loginScreenView;
        this.authentificationManager = authentificationManager;
        this.uiHandler = uiHandler;
        try{
            //we are creating that one before entering, so newly created configs
            //could know how many songs are there
            //we made it like that so if we add 1 more song
            //the only changes that are required is adding flag to db
            this.gameSoundManager = new GameSoundManager();
        }catch (SoundException e){
            new CustomAlertCall(Alert.AlertType.ERROR, new AlertSetting(e.getMessage()), loginScreenView);
        }
        initializeModel();
        initializeAnimation();
        addEventHandlers();
        updateView();
        if(!noConnection){
            Platform.runLater(() -> tryLoginWithSavedCredentials());
        }
    }

    private void initializeModel(){
        try{
            if(authentificationManager == null) this.authentificationManager = new AuthentificationManager();
            authentificationManager.createConnection();
            this.loginService = authentificationManager.getLoginService();
            this.registrationService = authentificationManager.getRegistrationService();
            this.passwordRecoveryService = authentificationManager.getPasswordRecoveryService();
        }catch (AuthentificationServiceException e) {
            this.noConnection = true;
        }
    }

    private void addEventHandlers(){
        Platform.runLater(() -> {
            loginScreenView.requestFocus();
        }); //otherwise the focus will be set on the text field

        loginScreenView.getForgetPassword().setOnMouseClicked(event -> {
            String mail = loginScreenView.getLoginTextField().getText();
            boolean isExisting;
            try {
                isExisting = passwordRecoveryService.checkIfExists(mail);
            } catch (SQLException e) {
                handleException(e);
                return;
            }
            if(!isExisting){
                handleException("User could not be found or doesn't exists");
                return;
            }

            //Platform.runLater allows us to run loading animation in javaFX thread

            new Thread(() -> {
                try{
                    Platform.runLater(() -> {
                        runLoading();
                    });
                    passwordRecoveryService.sendRecoveryCode(mail); //so that one is working in separate java thread
                    //we have to split it, otherwise the sending will take all thread
                    Platform.runLater(() -> {
                    showNode(loginScreenView.getPasswordRecoveryPopUp());
                    stopLoading();
                    });
                }catch (PasswordRecoveryServiceException e) {
                    Platform.runLater(() -> {
                        PasswordRecoveryServiceException ex = e;
                        stopLoading();
                        ex.printStackTrace();
                        handleException(ex);
                    });
                }
            }).start();
        });

        loginScreenView.getCheckTheCode().setOnAction(actionEvent -> {
            String code = loginScreenView.getCodeFromMailTextField().getText();
            boolean result = passwordRecoveryService.checkCode(code);
            if(result){
                loginScreenView.getPasswordRecoveryPopUp().getChildren().add(1, loginScreenView.getHNewPasswordBox());
            }else{
                handleException("Wrong Code");
                hideNode(loginScreenView.getPasswordRecoveryPopUp());
            }
        });

        loginScreenView.getSaveNewPassword().disableProperty().bind(
                loginScreenView.getNewPasswordTextField().textProperty().length().lessThan(5)
                        .or(Bindings.createBooleanBinding(() -> noConnection)));
        loginScreenView.getSaveNewPassword().setOnAction(actionEvent -> {
            String newPassword = loginScreenView.getNewPasswordTextField().getText();
            String email = loginScreenView.getLoginTextField().getText();
            try{
                passwordRecoveryService.setNewPassword(email, newPassword);
                showConfirmation("You successfully changed password\nYou can login now!");
                hideNode(loginScreenView.getPasswordRecoveryPopUp());
            }catch (PasswordRecoveryServiceException e) {
                handleException(e);
            }
            loginScreenView.getPasswordRecoveryPopUp().getChildren().remove(1);
        });

        loginScreenView.getForgetPassword().setOnMouseEntered(mouseEvent -> {
            loginScreenView.getForgetPassword().setUnderline(true);
        });

        loginScreenView.getForgetPassword().setOnMouseExited(mouseEvent -> {
            loginScreenView.getForgetPassword().setUnderline(false);
        });

        loginScreenView.getEnterAccount().disableProperty().bind(
                loginScreenView.getLoginTextField().textProperty().isEmpty()
                        .or(loginScreenView.getPasswordTextField().textProperty().isEmpty()
                                .or(loginScreenView.getPasswordTextField().textProperty().length().lessThan(5))
                                .or(Bindings.createBooleanBinding(() -> noConnection)))
        );

        loginScreenView.getRememberMe().disableProperty().bind(Bindings.createBooleanBinding(() -> noConnection));

        loginScreenView.getEnterAccount().setOnAction(actionEvent -> {

            String email = loginScreenView.getLoginTextField().getText();
            String password = loginScreenView.getPasswordTextField().getText();
            boolean isSaved = loginScreenView.getRememberMe().isSelected();
            AuthentificationResultEntity authentificationResultEntity;
            try{
                authentificationResultEntity = loginService.loginPlayer(email, password, isSaved);
            }catch (LoginServiceException e) {
                handleException(e);
                return;
            }
            handleAuthResult(authentificationResultEntity);
        });

        loginScreenView.getRegistrationAgreeButton().setOnAction(actionEvent -> {
            String email = loginScreenView.getLoginTextField().getText();
            String password = loginScreenView.getPasswordTextField().getText();
            AuthentificationResultEntity authentificationResultEntity;
            try{
                authentificationResultEntity = registrationService.registerNewPlayer(email, password);
            }catch (RegistrationServiceException e) {
                handleException(e);
                return;
            }

            switch(authentificationResultEntity.getAuthResult()){
                case SUCCESS -> {
                    loginScreenView.getLoginTextField().setText("");
                    loginScreenView.getPasswordTextField().setText("");
                    user = authentificationResultEntity.getPlayer();
                    openMenu();
                    new CustomAlertCall(Alert.AlertType.CONFIRMATION, new AlertSetting("You successfully registered an account"), startView);
                }
                case PLAYER_EXISTS -> handleException("User already exists");
            }

            hideNode(loginScreenView.getRegistrationButtonsPopUp());
        });

        loginScreenView.getRegistrationDeclineButton().setOnAction(actionEvent -> {
            hideNode(loginScreenView.getRegistrationButtonsPopUp());
        });

        loginScreenView.getGuestButton().setOnAction(event -> {
            user = new Guest("Player");
            openMenu();
            new CustomAlertCall(Alert.AlertType.CONFIRMATION, new AlertSetting(2, "You entered as a guest", false), startView);
        });

        loginScreenView.getExitButtonPane().setOnMouseClicked(mouseEvent -> {
            closeGame();
        });
    }

    private void updateView(){
        if(noConnection){
            loginScreenView.getLoginTextAndButtons().getChildren().set(3, loginScreenView.getNoConnection());
            loginScreenView.getLoginTextField().setDisable(true);
            loginScreenView.getPasswordTextField().setDisable(true);
        }else{
            loginScreenView.getLoginTextAndButtons().getChildren().set(3, loginScreenView.getForgetPassword());
            loginScreenView.getLoginTextField().setDisable(false);
            loginScreenView.getPasswordTextField().setDisable(false);
        }
    }

    private void closeGame(){
        try{
            authentificationManager.closeConnection();
        }catch (AuthentificationServiceException e) {
            handleException(e);
        }
        Stage stage = (Stage) loginScreenView.getScene().getWindow();
        stage.close();
    }

    private void showNode(Node node){
        node.setVisible(true);
        node.setMouseTransparent(false);
    }

    private void hideNode(Node node){
        node.setVisible(false);
        node.setMouseTransparent(true);
    }

    private void initializeMenuAndBasicGameDependencies(User user){
        startView = new StartView();
        GameSettings gameSettings;
        GameSoundView gameSoundView;
        GameSoundPresenter gameSoundPresenter;
        try{
            gameSettings = new GameSettings(user, gameSoundManager);
            gameSoundView = new GameSoundView();
            gameSoundPresenter = new GameSoundPresenter(gameSoundView, gameSoundManager);
            new StartPresenter(startView, authentificationManager, gameSettings, gameSoundPresenter, uiHandler);
            gameSoundPresenter.playSoundEffect(GameSoundFX.AUTH_DONE);
            gameSoundPresenter.initializeMusicThread();

        }catch (SoundException e){
            new CustomAlertCall(Alert.AlertType.ERROR, new AlertSetting(e.getMessage()), startView);
        }
    }

    private void openMenu(){
        initializeMenuAndBasicGameDependencies(user);
        loginScreenView.getScene().setRoot(startView);
    }


    private void runLoading(){
        Circle circle = loginScreenView.getAnimationCircle();
        showNode(circle);
        showNode(loginScreenView.getScreenBlocker());
        loadingAnimation.play();
    }

    private void stopLoading(){
        Circle circle = loginScreenView.getAnimationCircle();
        hideNode(circle);
        hideNode(loginScreenView.getScreenBlocker());
        loadingAnimation.stop();
    }

    private void initializeAnimation(){
        loadingAnimation = new Timeline();
        Circle circle = loginScreenView.getAnimationCircle();
        stopLoading();

        loadingAnimation.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(circle.rotateProperty(), 0),
                        new KeyValue(circle.strokeProperty(), Color.DARKVIOLET)),
                new KeyFrame(Duration.millis(2000),
                        new KeyValue(circle.rotateProperty(), 60),
                        new KeyValue(circle.strokeProperty(), Color.DARKBLUE)),
                new KeyFrame(Duration.millis(3000),
                        new KeyValue(circle.rotateProperty(), 120),
                        new KeyValue(circle.strokeProperty(), Color.DARKGREEN)),
                new KeyFrame(Duration.millis(4000),
                        new KeyValue(circle.rotateProperty(), 180),
                        new KeyValue(circle.strokeProperty(), Color.DARKCYAN)),
                new KeyFrame(Duration.millis(5000),
                        new KeyValue(circle.rotateProperty(), 240),
                        new KeyValue(circle.strokeProperty(), Color.LIGHTGREEN)),
                new KeyFrame(Duration.millis(6000),
                        new KeyValue(circle.rotateProperty(), 300),
                        new KeyValue(circle.strokeProperty(), Color.LIGHTBLUE)),
                new KeyFrame(Duration.millis(7000),
                        new KeyValue(circle.rotateProperty(), 360),
                        new KeyValue(circle.strokeProperty(), Color.DARKVIOLET))
        );

        loadingAnimation.setOnFinished(actionEvent -> {
            runLoading();
        });
    }

    private void handleAuthResult(AuthentificationResultEntity authentificationResultEntity){
        if(authentificationResultEntity == null) return;

        switch(authentificationResultEntity.getAuthResult()){
            case AuthResult.USER_NOT_FOUND -> {
                handleException("User not found");
                showNode(loginScreenView.getRegistrationButtonsPopUp());
            }

            case AuthResult.WRONG_PASSWORD -> handleException("Invalid password provided");

            case AuthResult.SUCCESS -> {
                user = authentificationResultEntity.getPlayer();
                loginScreenView.getLoginTextField().setText("");
                loginScreenView.getPasswordTextField().setText("");
                openMenu();
            }
            case AuthResult.WRONG_EMAIL_FORMAT -> handleException("Invalid email format provided");
        }
    }

    private void tryLoginWithSavedCredentials(){
        try{
            AuthentificationResultEntity authentificationResultEntity = loginService.loginPlayer(null, null, true);
            if(authentificationResultEntity.getAuthResult() == AuthResult.SUCCESS) handleAuthResult(authentificationResultEntity);
        }catch (UserCredentialsLocalSaverException e) {
            handleException(e);
        }catch (NullPointerException e) {
            //will be thrown by colling logging service, because it won't be created without the connection
        }
    }

    private void handleException(AuthentificationServiceException e){
        new CustomAlertCall(Alert.AlertType.ERROR, new AlertSetting(e.getMessage()), loginScreenView);
    }

    private void handleException(SQLException e){
        new CustomAlertCall(Alert.AlertType.ERROR, new AlertSetting(e.getMessage()), loginScreenView);
    }

    private void handleException(String message){
        new CustomAlertCall(Alert.AlertType.ERROR, new AlertSetting(message), loginScreenView);
    }

    private void showConfirmation(String message){
        new CustomAlertCall(Alert.AlertType.CONFIRMATION, new AlertSetting(message), loginScreenView);
    }

}

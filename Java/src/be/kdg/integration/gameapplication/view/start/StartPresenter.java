package be.kdg.integration.gameapplication.view.start;

import be.kdg.integration.gameapplication.model.alertproperties.AlertSetting;
import be.kdg.integration.gameapplication.model.authservices.AuthentificationManager;
import be.kdg.integration.gameapplication.model.leaderboard.LeaderBoardException;
import be.kdg.integration.gameapplication.model.settings.GameSoundFX;
import be.kdg.integration.gameapplication.model.settings.GameSoundManager;
import be.kdg.integration.gameapplication.model.user.*;
import be.kdg.integration.gameapplication.model.stats.AllGamesManager;
import be.kdg.integration.gameapplication.model.authservices.services.LogoutHandler;
import be.kdg.integration.gameapplication.model.databaseaccess.DataBaseConnection;
import be.kdg.integration.gameapplication.model.databaseaccess.LeaderBoardDatabase;
import be.kdg.integration.gameapplication.model.leaderboard.LeaderBoardManager;
import be.kdg.integration.gameapplication.model.settings.GameSettings;
import be.kdg.integration.gameapplication.view.UIHandler;
import be.kdg.integration.gameapplication.view.customalert.CustomAlertCall;
import be.kdg.integration.gameapplication.view.gamemodeselection.GameModePresenter;
import be.kdg.integration.gameapplication.view.gamemodeselection.GameModeView;
import be.kdg.integration.gameapplication.view.allgamesscreen.GamesEntitiesPresenter;
import be.kdg.integration.gameapplication.view.allgamesscreen.GamesEntitiesView;
import be.kdg.integration.gameapplication.view.gamesound.GameSoundPresenter;
import be.kdg.integration.gameapplication.view.leaderboard.LeaderBoardPresenter;
import be.kdg.integration.gameapplication.view.leaderboard.LeaderBoardView;
import be.kdg.integration.gameapplication.view.loginscreen.LoginScreenPresenter;
import be.kdg.integration.gameapplication.view.loginscreen.LoginScreenView;
import be.kdg.integration.gameapplication.view.settingsmenu.GameSettingsPresenter;
import be.kdg.integration.gameapplication.view.settingsmenu.GameSettingsView;
import be.kdg.integration.gameapplication.view.tutorial.TutorialPresenter;
import be.kdg.integration.gameapplication.view.tutorial.TutorialView;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class StartPresenter{
    private StartView startView;
    private User user;
    private DataBaseConnection dataBaseConnection;
    private GameSettings gameSettings;
    private GameSoundPresenter gameSoundPresenter;
    private LogoutHandler logoutHandler;
    private AllGamesManager allGamesManager;
    private NicknameChangerService nicknameChangerService;
    private AuthentificationManager authentificationManager;
    private LeaderBoardManager leaderBoardManager;
    private UIHandler uiHandler;

    public StartPresenter(StartView startView, AuthentificationManager authentificationManager, GameSettings gameSettings, GameSoundPresenter gameSoundPresenter, UIHandler uiHandler){
        this.startView = startView;
        this.gameSettings = gameSettings;
        this.user = gameSettings.getUser();
        this.gameSoundPresenter = gameSoundPresenter;
        this.uiHandler = uiHandler;
        if(authentificationManager != null) initializeAuth(authentificationManager);
        addEventHandlers();
        updateView();
    }

    private void initializeAuth(AuthentificationManager authentificationManager){
        this.authentificationManager = authentificationManager;
        this.dataBaseConnection = authentificationManager.getDataBaseConnection();
        this.logoutHandler = authentificationManager.getLoginService();
        this.nicknameChangerService = authentificationManager.getNicknameChangerService();
    }

    private void addEventHandlers(){

        startView.getSettingsButtonPane().setOnMouseClicked(mouseEvent -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
            updateView();
            openSettings();
        });

        startView.getExitButton().setOnMouseClicked(mouseEvent -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
            exitGame();
        });

        setUnderLineEffect(startView.getStartGameButton());
        setUnderLineEffect(startView.getLeaderBoardButton());
        setUnderLineEffect(startView.getExitButton());
        setUnderLineEffect(startView.getTutorialButton());
        setUnderLineEffect(startView.getGamesListButton());
        setUnderLineEffect(startView.getNickname());

        startView.getStartGameButton().setOnMouseClicked(mouseEvent -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
            updateView();
            openGameSelection();
        });
        startView.getTutorialButton().setOnMouseClicked(mouseEvent -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
            updateView();
            openTutorial();
        });
        startView.getLeaderBoardButton().setOnMouseClicked(mouseEvent -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
            try{
                updateView();
                openLeaderboard();
            }catch (LeaderBoardException e) {
                handleException(e);
            }
        });
        startView.getGamesListButton().setOnMouseClicked(mouseEvent -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
            updateView();
            openGamesListScreen();
        });
        startView.getLogOutButtonPane().setOnMouseClicked(mouseEvent -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
            updateView();
            logOut();
        });
        startView.getNickname().setOnMouseClicked(mouseEvent -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
            activateNicknameChangingField();
        });

        startView.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER) && startView.getNicknameTextFieldHBox().isVisible()){
                applyNicknameChanges(startView.getNicknameTextField().getText());
                updateView();
            }
        });

        startView.getNicknameConfirmButton().setOnMouseClicked(mouseEvent -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
            applyNicknameChanges(startView.getNicknameTextField().getText());
            updateView();
        });
    }

    private void setUnderLineEffect(Label label){
        label.setOnMouseEntered(mouseEvent -> {
            label.setUnderline(true);
        });
        label.setOnMouseExited(mouseEvent -> {
            label.setUnderline(false);
        });
    }

    private void updateView(){
        startView.getNickname().setText(user.getNickname());
        hideNicknameTextField();

        if(user instanceof Player){
            initializeAllGamesModel();
            int points = allGamesManager.getPlayersPoints();
            startView.getAmountOfPoints().setText("Points: " + points);
        }else{
            startView.getAmountOfPoints().setText("Points: N/A");
        }
    }

    private void openSettings(){
        GameSettingsView gameSettingsView = new GameSettingsView();
        Scene scene = startView.getScene();
        new GameSettingsPresenter(gameSettings, gameSettingsView, gameSoundPresenter, startView.getScene(), authentificationManager, uiHandler);
        scene.setRoot(gameSettingsView);
    }

    private void openGameSelection(){
        GameModeView gameModeSelectionView = new GameModeView();
        new GameModePresenter(user, gameModeSelectionView, gameSoundPresenter, authentificationManager, gameSettings, allGamesManager, uiHandler);
        startView.getScene().setRoot(gameModeSelectionView);
    }

    private void initializeAllGamesModel(){
            try{
                if(allGamesManager == null) allGamesManager = new AllGamesManager(dataBaseConnection);
                allGamesManager.refreshGames((Player) user);
            }catch (SQLException e) {
                handleException(e);
            }
    }


    private void openGamesListScreen(){
        if(user instanceof Player player){
            initializeAllGamesModel();
            try{
                allGamesManager.refreshGames(player);
                GamesEntitiesView gamesView = new GamesEntitiesView();
                new GamesEntitiesPresenter(gamesView, allGamesManager, gameSettings, authentificationManager, uiHandler, gameSoundPresenter);
                startView.getScene().setRoot(gamesView);
                return;
            }catch (SQLException e) {
                handleException(e);
            }
        }
        // To make sure only account users can open this window
        new CustomAlertCall(Alert.AlertType.INFORMATION, new AlertSetting("Game says", "Please log in as a player to view your games"), startView);
    }

    private void exitGame(){
        if(dataBaseConnection != null){
            try{
                dataBaseConnection.closeConnection();
            }catch (SQLException e) {
                handleException(e);
            }
        }
        Stage stage = (Stage) startView.getScene().getWindow();
        stage.close();
    }

    private void openTutorial(){
        TutorialView tutorialView = new TutorialView();
        tutorialView.getStylesheets().clear();
        new TutorialPresenter(tutorialView, gameSettings, gameSoundPresenter, authentificationManager, uiHandler);
        startView.getScene().setRoot(tutorialView);
    }


    private void openLeaderboard() throws LeaderBoardException{
        try{
            this.leaderBoardManager = new LeaderBoardManager(new LeaderBoardDatabase(dataBaseConnection));

            LeaderBoardView leaderBoardView = new LeaderBoardView();
            leaderBoardView.getStylesheets().clear();
            new LeaderBoardPresenter(leaderBoardView, leaderBoardManager, authentificationManager, gameSettings, gameSoundPresenter, uiHandler);
            leaderBoardManager.refreshLeaderBoard();
            startView.getScene().setRoot(leaderBoardView);
        }catch (LeaderBoardException e) {
            throw e;
        }catch (NullPointerException e) {
            throw new LeaderBoardException("Leaderboard is not available");
        }
    }

    private void activateNicknameChangingField(){
        String previousNickname = startView.getNickname().getText();
        startView.getNicknameTextField().setText(previousNickname);
        showNicknameTextField();
    }

    private void showNicknameTextField(){
        startView.getNicknameTextFieldHBox().setVisible(true);
        startView.getNicknameTextFieldHBox().setMouseTransparent(false);
        startView.getNickname().setVisible(false);
        startView.getNickname().setMouseTransparent(true);
    }

    private void hideNicknameTextField(){
        startView.getNicknameTextFieldHBox().setVisible(false);
        startView.getNicknameTextFieldHBox().setMouseTransparent(true);
        startView.getNickname().setVisible(true);
        startView.getNickname().setMouseTransparent(false);
    }

    private void applyNicknameChanges(String nickname){
        try{
            user.setNickname(nickname);
            if(user instanceof Guest) return;
            nicknameChangerService.changeNickname(((Player) user), nickname);
        }catch (PlayerConfigurationException e) {
            handleException(e);
        }
    }

    private void logOut(){
        if(user instanceof Player){
            try{
                logoutHandler.logOut();
            }catch (IOException e) {
                handleException(e);
            }
        }

        user = null;
        gameSettings.resetAmountOfSongs();
        this.gameSoundPresenter.stopMusicAndSound();
        Stage stage = (Stage) startView.getScene().getWindow();
        LoginScreenView loginScreenView = new LoginScreenView();
        new LoginScreenPresenter(loginScreenView, authentificationManager, uiHandler);
        stage.getScene().setRoot(loginScreenView);
    }

    private void handleException(Exception e){
        new CustomAlertCall(Alert.AlertType.ERROR, new AlertSetting(e.getMessage()), startView);
    }
}


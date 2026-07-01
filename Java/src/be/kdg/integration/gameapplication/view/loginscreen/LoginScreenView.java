
package be.kdg.integration.gameapplication.view.loginscreen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
public class LoginScreenView extends StackPane{
    private Label noConnection;

    private TextField loginTextField;
    private TextField passwordTextField;
    private Button enterAccount;
    private Button guestButton;
    private Label forgetPassword;
    private CheckBox rememberMe;

    private Button registrationAgreeButton;
    private Button registrationDeclineButton;
    private VBox loginTextAndButtons;
    private BorderPane registrationButtonsPopUp;

    private VBox VPasswordRecoveryPopUp;
    private HBox HNewPasswordBox;

    private TextField codeFromMailTextField;
    private Button checkTheCode;

    private TextField newPasswordTextField;
    private Button saveNewPassword;

    private Circle animationCircle;
    private BorderPane screenBlocker;

    private StackPane exitButtonPane;

    public LoginScreenView(){
        initializeNodes();
        layoutNodes();
        super.getStylesheets().add(getClass().getResource("/css-default/login-page.css").toExternalForm());
    }

    private void initializeNodes(){
        this.screenBlocker = new BorderPane();
        this.animationCircle = new Circle(200);

        this.guestButton = new Button("Guest");
        guestButton.getStyleClass().add("btn-ghost");

        this.loginTextField = new TextField();
        this.passwordTextField = new TextField();

        this.enterAccount = new Button("Log in");
        enterAccount.getStyleClass().add("btn-primary");

        this.registrationAgreeButton = new Button("Register");
        registrationAgreeButton.getStyleClass().add("btn-ghost");

        this.registrationDeclineButton = new Button("Not today");
        registrationDeclineButton.getStyleClass().add("btn-ghost");

        this.registrationButtonsPopUp = new BorderPane();
        registrationButtonsPopUp.getStyleClass().add("popup-box");

        this.loginTextAndButtons = new VBox();

        this.VPasswordRecoveryPopUp = new VBox();
        VPasswordRecoveryPopUp.getStyleClass().add("popup-box");

        this.HNewPasswordBox = new HBox();
        HNewPasswordBox.getStyleClass().add("popup-box");

        this.noConnection = new Label("No connection");

        this.forgetPassword = new Label("Forget password?");
        forgetPassword.getStyleClass().add("forget-password");

        this.rememberMe = new CheckBox("Remember me");
        rememberMe.getStyleClass().add("remember-me");

        this.codeFromMailTextField = new TextField();
        this.newPasswordTextField = new TextField();

        this.saveNewPassword = new Button("Save new password");
        this.saveNewPassword.getStyleClass().add("btn-primary");
        this.checkTheCode = new Button("Check code");
        this.checkTheCode.getStyleClass().add("btn-primary");

        ImageView exitButtonView = new ImageView(new Image("/icons/exit_button.png"));
        this.exitButtonPane = new StackPane();
        this.exitButtonPane.getChildren().add(exitButtonView);
    }

    private void layoutNodes(){
        this.screenBlocker.setMinSize(Double.MAX_VALUE,Double.MAX_VALUE);
        this.noConnection.setTextFill(Color.RED);
        //setting main view
        setBackground(new Background(new BackgroundFill(Color.rgb(48, 48, 48, 1.0), CornerRadii.EMPTY, Insets.EMPTY)));
        VBox logoAndMenu = new VBox();
        logoAndMenu.setAlignment(Pos.CENTER);
        getChildren().add(logoAndMenu);

        Label zarocLabel = new Label("ZAROC");
        zarocLabel.getStyleClass().add("zaroc-label");

        Label gameSubLabel = new Label("THE BOARD GAME");
        gameSubLabel.getStyleClass().add("sub-label");

        VBox logoBox = new VBox(4, zarocLabel, gameSubLabel);
        logoBox.setAlignment(Pos.CENTER);

        logoAndMenu.getChildren().add(logoBox);
        VBox.setMargin(logoBox, new Insets(0, 0, 0, 0));

        //configuring text fields

        Background textFieldsBackground = new Background(new BackgroundFill(Color.rgb(82, 82, 82, 1), new CornerRadii(5), Insets.EMPTY));

        Border textFieldsBorder = new Border(new BorderStroke(Color.rgb(82, 82, 82, 1), BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(5)));


        loginTextField.setPrefHeight(40);
        loginTextField.setMaxWidth(200);
        loginTextField.setBorder(textFieldsBorder);
        loginTextField.setBackground(textFieldsBackground);
        loginTextField.setPromptText("Enter email");
        loginTextField.setStyle(
                "-fx-background-color: rgba(82,82,82,1);" +
                        "-fx-background-radius: 8px;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: rgba(206,203,246,0.5);" +
                        "-fx-border-color: rgba(127,119,221,0.4);" +
                        "-fx-border-radius: 8px;" +
                        "-fx-border-width: 0.5px;"
        );

        passwordTextField.setPrefHeight(40);
        passwordTextField.setMaxWidth(200);
        passwordTextField.setBorder(textFieldsBorder);
        passwordTextField.setBackground(textFieldsBackground);
        passwordTextField.setPromptText("Enter password");
        passwordTextField.setStyle(
                "-fx-background-color: rgba(82,82,82,1);" +
                        "-fx-background-radius: 8px;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: rgba(206,203,246,0.5);" +
                        "-fx-border-color: rgba(127,119,221,0.4);" +
                        "-fx-border-radius: 8px;" +
                        "-fx-border-width: 0.5px;"
        );

        //Login label
        Label signOrLoginLabel = new Label("Log in/Sign up");
        signOrLoginLabel.getStyleClass().add("login-title");
        signOrLoginLabel.setTextAlignment(TextAlignment.CENTER);
        VBox.setMargin(signOrLoginLabel, new Insets(0, 0, 30, 0));
        signOrLoginLabel.setFont(
                Font.font("System", FontWeight.NORMAL, FontPosture.ITALIC, 30)
        );
        signOrLoginLabel.setStyle(
                "-fx-text-fill: white"
        );

        //configuring login buttons
        Background buttonsBackGround = textFieldsBackground;
        Border buttonsBorder = textFieldsBorder;
        registrationAgreeButton.setBorder(buttonsBorder);
        registrationDeclineButton.setBorder(buttonsBorder);
        enterAccount.setBorder(buttonsBorder);
        guestButton.setBorder(buttonsBorder);

        enterAccount.setBackground(buttonsBackGround);
        guestButton.setBackground(buttonsBackGround);

        enterAccount.setMinSize(150, 40);
        guestButton.setMinSize(150, 40);

        rememberMe.setMinSize(50, 40);

        HBox logAndGuestButtons = new HBox();
        HBox.setHgrow(enterAccount, Priority.ALWAYS);
        logAndGuestButtons.getChildren().addAll(enterAccount ,guestButton);
        logAndGuestButtons.setSpacing(100);
        logAndGuestButtons.setAlignment(Pos.CENTER);

        //gathering text fields and buttons and label
        VBox.setMargin(loginTextField, new Insets(10));
        VBox.setMargin(passwordTextField, new Insets(10));
        VBox.setMargin(logAndGuestButtons, new Insets(10));

        loginTextAndButtons.getChildren().addAll(signOrLoginLabel, loginTextField, passwordTextField, forgetPassword, rememberMe, logAndGuestButtons);
        loginTextAndButtons.setAlignment(Pos.CENTER);

        exitButtonPane.setMinSize(60,60);
        exitButtonPane.setMaxSize(60, 60);
        ImageView exitButtonView = (ImageView) exitButtonPane.getChildren().get(0);
        exitButtonView.setFitHeight(60);
        exitButtonView.setFitWidth(60);
        getChildren().add(exitButtonPane);
        StackPane.setAlignment(exitButtonPane, Pos.TOP_RIGHT);
        setMargin(exitButtonPane, new Insets(20));

        //configuring border pain wth login fields, buttons and label login
        BorderPane loginFieldsAndButtonsBlock = new BorderPane();
        loginFieldsAndButtonsBlock.getStyleClass().add("login-box");
        loginFieldsAndButtonsBlock.setBackground(new Background(new BackgroundFill(Color.rgb(125, 125, 125, 1.0), new CornerRadii(10), Insets.EMPTY)));
        loginFieldsAndButtonsBlock.setMinHeight(350);
        loginFieldsAndButtonsBlock.setMinWidth(450);
        loginFieldsAndButtonsBlock.setMaxHeight(400);
        loginFieldsAndButtonsBlock.setMaxWidth(500);
        loginFieldsAndButtonsBlock.setCenter(loginTextAndButtons);
        logoAndMenu.getChildren().addAll(loginFieldsAndButtonsBlock);
        logoAndMenu.setSpacing(40);

        //the registration pop-up

        //registration buttons
        HBox hRegistrationQuestion = new HBox();
        VBox vRegistrationQuestion = new VBox();

        registrationAgreeButton.setBackground(buttonsBackGround);
        registrationAgreeButton.setBorder(buttonsBorder);
        registrationDeclineButton.setBackground(buttonsBackGround);
        registrationDeclineButton.setBorder(buttonsBorder);
        registrationAgreeButton.setMinSize(280, 80);
        registrationDeclineButton.setMinSize(280, 80);

        hRegistrationQuestion.setAlignment(Pos.CENTER);
        hRegistrationQuestion.getChildren().addAll(registrationAgreeButton, registrationDeclineButton);
        hRegistrationQuestion.setSpacing(20);
        vRegistrationQuestion.setAlignment(Pos.CENTER);
        vRegistrationQuestion.getChildren().add(hRegistrationQuestion);
        StackPane.setMargin(registrationButtonsPopUp, new Insets(0, 0, -100, 0));
        registrationButtonsPopUp.setMaxSize(600, 400);
        registrationButtonsPopUp.setBackground(new Background(new BackgroundFill(Color.rgb(30, 30, 30, 1.0), new CornerRadii(10), Insets.EMPTY)));
        registrationButtonsPopUp.setCenter(vRegistrationQuestion);
        registrationButtonsPopUp.setMouseTransparent(true);
        registrationButtonsPopUp.setVisible(false);
        registrationButtonsPopUp.setTranslateY(30);
        getChildren().add(registrationButtonsPopUp);

        //the password recovery pop-up

        HBox HCodeBox = new HBox();
        HCodeBox.getStyleClass().add("popup-box");

        codeFromMailTextField.setPromptText("Enter code here");
        checkTheCode.setMinSize(150, 40);
        checkTheCode.setBackground(buttonsBackGround);
        checkTheCode.setBorder(buttonsBorder);

        newPasswordTextField.setPromptText("Enter new password here");  //this one will be added when the check code will be confirmed
        saveNewPassword.setBackground(buttonsBackGround);
        saveNewPassword.setBorder(buttonsBorder);
        saveNewPassword.setMinSize(150, 40);


        HCodeBox.getChildren().addAll(codeFromMailTextField, checkTheCode);
        HCodeBox.setSpacing(40);
        HNewPasswordBox.getChildren().addAll(newPasswordTextField, saveNewPassword);
        HNewPasswordBox.setSpacing(40);
        VPasswordRecoveryPopUp.getChildren().addAll(HCodeBox);
        VPasswordRecoveryPopUp.setVisible(false);
        VPasswordRecoveryPopUp.setMouseTransparent(true);
        VPasswordRecoveryPopUp.setMaxSize(600, 400);
        StackPane.setMargin(VPasswordRecoveryPopUp, new Insets(0, 0, -100, 0));
        VPasswordRecoveryPopUp.setAlignment(Pos.CENTER);
        getChildren().addAll(VPasswordRecoveryPopUp);

        //animation of loading
        animationCircle.setFill(Color.TRANSPARENT);
        animationCircle.setStrokeType(StrokeType.CENTERED);
        animationCircle.setStrokeWidth(20);

        double circleLength = 2 * Math.PI * animationCircle.getRadius();
        animationCircle.getStrokeDashArray().addAll(circleLength*0.75, circleLength * 0.25);
        getChildren().addAll(animationCircle, screenBlocker);
    }

    BorderPane getScreenBlocker(){
        return screenBlocker;
    }

    Circle getAnimationCircle(){
        return animationCircle;
    }

    HBox getHNewPasswordBox(){
        return HNewPasswordBox;
    }

    Button getCheckTheCode(){
        return checkTheCode;
    }

    TextField getNewPasswordTextField(){
        return newPasswordTextField;
    }

    TextField getCodeFromMailTextField(){
        return codeFromMailTextField;
    }

    VBox getPasswordRecoveryPopUp(){
        return VPasswordRecoveryPopUp;
    }

    Button getSaveNewPassword(){
        return saveNewPassword;
    }

    Button getEnterAccount(){
        return enterAccount;
    }

    Button getGuestButton(){return guestButton;}

    TextField getLoginTextField(){
        return loginTextField;
    }

    TextField getPasswordTextField(){
        return passwordTextField;
    }

    Button getRegistrationAgreeButton(){
        return registrationAgreeButton;
    }

    Button getRegistrationDeclineButton(){
        return registrationDeclineButton;
    }

    BorderPane getRegistrationButtonsPopUp(){
        return registrationButtonsPopUp;
    }


    Label getNoConnection(){
        return noConnection;
    }
    Label getForgetPassword(){
        return forgetPassword;
    }

    CheckBox getRememberMe() {
        return rememberMe;
    }

    VBox getLoginTextAndButtons(){
        return loginTextAndButtons;
    }

    StackPane getExitButtonPane(){
        return exitButtonPane;
    }
}


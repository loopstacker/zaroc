package be.kdg.integration.gameapplication.view.start;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class StartView extends BorderPane{
    private Label startGameButton;
    private Label leaderBoardButton;

    private Label tutorialButton;
    private final Image settingsImage = new Image("/icons/settings_icon.png");
    private ImageView settingsButton;
    private StackPane settingsButtonPane;

    private Label nickname;
    private ImageView logOutButton;
    private StackPane logOutButtonPane;
    private Label amountOfPoints;

    private Label exitButton;
    private Label gameLabel;
    private Label gameSubLabel;

    private Label gamesListButton;

    private StackPane nicknameTextFieldHBox;
    private TextField nicknameTextField;
    private ImageView nicknameConfirmButton;

    public StartView(){
        initializeNodes();
        layoutNodes();
    }

    private void initializeNodes(){
        this.nicknameTextField = new TextField();
        this.nicknameConfirmButton = new ImageView(new Image("/icons/tick-circle.png"));
        this.nicknameTextFieldHBox = new StackPane(nicknameTextField, nicknameConfirmButton);
        this.startGameButton = new Label("START");
        this.exitButton = new Label("EXIT");
        this.leaderBoardButton = new Label("LEADERBOARD");
        this.settingsButton = new ImageView(settingsImage);
        this.tutorialButton = new Label("TUTORIAL");
        this.gameLabel = new Label("ZAROC");
        this.gameSubLabel = new Label("THE BOARD GAME");
        this.settingsButtonPane = new StackPane();
        this.gamesListButton = new Label("VIEW GAMES");
        this.nickname = new Label("Player");
        this.logOutButton = new ImageView(new Image("/icons/logout_icon.png"));
        this.logOutButtonPane = new StackPane();
        this.amountOfPoints = new Label("Points: N/A");
    }

    private void layoutNodes(){
        nicknameTextField.getStyleClass().add("nick-name-input-field");

        gameLabel.getStyleClass().add("zaroc-label");
        gameSubLabel.getStyleClass().add("sub-label");

        VBox logoBox = new VBox(4, gameLabel, gameSubLabel);
        logoBox.setAlignment(Pos.CENTER);

        startGameButton.getStyleClass().add("menu-button");
        leaderBoardButton.getStyleClass().add("menu-button");
        gamesListButton.getStyleClass().add("menu-button");
        tutorialButton.getStyleClass().add("menu-button");
        exitButton.getStyleClass().add("menu-button");

        VBox menu = new VBox(10, startGameButton, leaderBoardButton, gamesListButton, tutorialButton, exitButton);
        menu.setAlignment(Pos.CENTER);

        VBox center = new VBox(50, logoBox, menu);
        center.setAlignment(Pos.CENTER);
        setCenter(center);

        // settings
        ColorAdjust toWarm = new ColorAdjust();
        toWarm.setHue(0.05);
        toWarm.setBrightness(0.6);
        settingsButton.setEffect(toWarm);
        settingsButton.setFitHeight(60);
        settingsButton.setFitWidth(60);
        settingsButtonPane.getChildren().add(settingsButton);
        settingsButtonPane.setMinWidth(200);
        settingsButtonPane.setMaxSize(200, 60);
        settingsButtonPane.setPadding(new Insets(20));
        settingsButtonPane.setAlignment(Pos.TOP_LEFT);
        /*setLeft(settingsButtonPane);
        setAlignment(settingsButtonPane, Pos.TOP_LEFT);*/

        // profile area
        StackPane nicknameArea = new StackPane();
        nicknameTextFieldHBox.setVisible(false);
        nicknameTextFieldHBox.setMouseTransparent(true);
        nicknameTextFieldHBox.setAlignment(nicknameConfirmButton, Pos.CENTER_RIGHT);
        HBox.setHgrow(nicknameTextField, Priority.ALWAYS);
        nicknameTextField.setPadding(new Insets(8, 32, 8, 0));
        nicknameArea.getChildren().addAll(nickname, nicknameTextFieldHBox);
        nicknameArea.setAlignment(Pos.TOP_CENTER);
        nickname.getStyleClass().add("nickname-label");
        nicknameArea.setPadding(new Insets(10, 0, 0, 0));
        nicknameConfirmButton.setFitWidth(32);
        nicknameConfirmButton.setFitHeight(32);
        nicknameConfirmButton.minWidth(32);
        nicknameConfirmButton.minHeight(32);

        nicknameArea.setMaxWidth(Double.MAX_VALUE);
        nicknameArea.setMaxHeight(Region.USE_PREF_SIZE);
        logOutButton.setFitWidth(60);
        logOutButton.setFitHeight(60);
        logOutButtonPane.getChildren().add(logOutButton);
        logOutButtonPane.setMaxSize(60, 60);

        HBox profileArea = new HBox(16, nicknameArea, logOutButtonPane);
        profileArea.setMinWidth(100);
        profileArea.setPadding(new Insets(20));
        setRight(profileArea);
        setAlignment(profileArea, Pos.TOP_RIGHT);

        HBox topControls = new HBox();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topControls.getChildren().addAll(settingsButtonPane, spacer, profileArea);
        setTop(topControls);

        amountOfPoints.getStyleClass().add("amount-of-points-field");
        BorderPane toFixRegionsInBorderPane = new BorderPane();
        toFixRegionsInBorderPane.setBottom(amountOfPoints);
        BorderPane.setAlignment(amountOfPoints, Pos.BOTTOM_LEFT);
        toFixRegionsInBorderPane.setMinHeight(60);
        setBottom(toFixRegionsInBorderPane);
    }

    Label getExitButton(){
        return exitButton;
    }

    Label getLeaderBoardButton(){
        return leaderBoardButton;
    }

    StackPane getSettingsButtonPane(){
        return settingsButtonPane;
    }

    Label getStartGameButton(){
        return startGameButton;
    }

    TextField getNicknameTextField(){
        return nicknameTextField;
    }

    Label getTutorialButton(){
        return tutorialButton;
    }

    Label getGamesListButton(){return gamesListButton;}

    Label getNickname() {
        return nickname;
    }

    StackPane getLogOutButtonPane() {
        return logOutButtonPane;
    }

    StackPane getNicknameTextFieldHBox() {
        return nicknameTextFieldHBox;
    }

    ImageView getNicknameConfirmButton() {
        return nicknameConfirmButton;
    }

    Label getAmountOfPoints(){
        return amountOfPoints;
    }
}

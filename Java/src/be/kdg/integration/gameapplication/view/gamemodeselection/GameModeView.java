package be.kdg.integration.gameapplication.view.gamemodeselection;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.List;

public class GameModeView extends BorderPane {

    private Button startCasual;
    private Button startCompetitive;

    private ToggleButton easy;
    private ToggleButton hard;
    private ToggleButton you;
    private ToggleButton cpu;

    private Spinner<Integer> minutes1;
    private Spinner<Integer> minutes2;
    private Spinner<Integer> seconds1;
    private Spinner<Integer> seconds2;

    private ImageView goBackButton;

    private Label amountGames;
    private Label amountGamesWon;
    private Label amountGamesLost;

    public GameModeView() {
        initializeNodes();
        layoutNodes();
    }

    private void initializeNodes(){
        this.easy = new ToggleButton("EASY");
        this.hard = new ToggleButton("HARD");
        this.you = new ToggleButton("YOU");
        this.cpu = new ToggleButton("CPU");

        this.startCasual = new Button("START CASUAL");
        this.startCompetitive = new Button("START COMPETITIVE");

        this.amountGames = new Label();
        this.amountGamesLost = new Label();
        this.amountGamesWon = new Label();

        goBackButton = new ImageView(new Image("/icons/exit_button.png"));

        minutes1 = new Spinner<>(0, 9, 0);
        minutes2 = new Spinner<>(0, 9, 5);
        seconds1 = new Spinner<>(0, 5, 0);
        seconds2 = new Spinner<>(0, 9, 0);
    }

    private void layoutNodes(){
        // SETTINGS
        goBackButton.setFitWidth(60);
        goBackButton.setFitHeight(60);
        goBackButton.setTranslateX(20);
        goBackButton.setTranslateY(20);
        setLeft(goBackButton);

        Region rightBalance = new Region();
        rightBalance.setMinWidth(40);
        setRight(rightBalance);

        // TIME LIMIT
        for (Spinner<Integer> s : List.of(minutes1, minutes2, seconds1, seconds2)) {
            s.setPrefWidth(60);
            s.setPrefHeight(40);
            s.setEditable(false);
        }

        Label colon = new Label(":");
        colon.setFont(Font.font(20));
        HBox timeBox = new HBox(5, minutes1, minutes2, colon, seconds1, seconds2);
        timeBox.setAlignment(Pos.CENTER); // center the whole timeBox

        // DIFFICULTY
        ToggleGroup difficulty = new ToggleGroup();
        easy.setToggleGroup(difficulty);
        hard.setToggleGroup(difficulty);

        easy.setPrefWidth(100);
        hard.setPrefWidth(100);

        // MOVE
        ToggleGroup moveGroup = new ToggleGroup();
        you.setToggleGroup(moveGroup);
        cpu.setToggleGroup(moveGroup);

        you.setPrefWidth(100);
        cpu.setPrefWidth(100);

        Label labelCasual = new Label("CASUAL");
        Label labelComp = new Label("COMPETITIVE");
        Label firstMove = new Label("FIRST MOVE");
        Label timeLabel = new Label("TIME LIMIT");
        Label diff = new Label("DIFFICULTY");
        Label yourStatsLabel = new Label("YOUR STATS");
        Label matchRulesLabel = new Label("MATCH RULES");

        labelCasual.setFont(Font.font(50));
        labelComp.setFont(Font.font(50));
        firstMove.setFont(Font.font(20));
        firstMove.getStyleClass().add("casual-section-label");
        diff.setFont(Font.font(20));
        diff.getStyleClass().add("casual-section-label");
        timeLabel.setFont(Font.font(20));
        timeLabel.getStyleClass().add("casual-section-label");
        yourStatsLabel.setFont(Font.font(20));
        matchRulesLabel.setFont(Font.font(20));


        // CASUAL VBOX
        VBox casualMiddleContent = new VBox(48);
        casualMiddleContent.getStyleClass().add("casual-controls-box");
        VBox timeLimitVBox = new VBox(16, timeLabel, timeBox);
        timeLimitVBox.setAlignment(Pos.TOP_CENTER);
        VBox difficultyVBox = new VBox(16, diff,easy,hard);
        difficultyVBox.setAlignment(Pos.TOP_CENTER);
        VBox firstMoveVBox = new VBox(16, firstMove, you, cpu);
        firstMoveVBox.setAlignment(Pos.TOP_CENTER);
        casualMiddleContent.getChildren().addAll(timeLimitVBox,difficultyVBox,firstMoveVBox);
        casualMiddleContent.setAlignment(Pos.TOP_CENTER);

        // COMP VBOX
        VBox compMiddleContent = new VBox(20);
        compMiddleContent.setAlignment(Pos.TOP_CENTER);
        compMiddleContent.setSpacing(15);

        VBox gamesPlayedCard = new VBox();
        Label playedLabel = new Label("PLAYED");
        playedLabel.setAlignment(Pos.CENTER);

        amountGames.setFont(Font.font(25));
        gamesPlayedCard.setSpacing(10);
        gamesPlayedCard.getChildren().addAll(amountGames, playedLabel);
        gamesPlayedCard.setAlignment(Pos.CENTER);

        VBox gamesWonCard = new VBox();
        Label gamesWonLabel = new Label("WON");
        amountGamesWon.setFont(Font.font(25));
        gamesWonCard.setSpacing(10);
        gamesWonCard.getChildren().addAll(amountGamesWon, gamesWonLabel);
        gamesWonCard.setAlignment(Pos.CENTER);
        gamesWonLabel.setAlignment(Pos.CENTER);

        VBox gamesLostCard = new VBox();
        Label gamesLostLabel = new Label("LOST");
        amountGamesLost.setFont(Font.font(25));
        gamesLostCard.setSpacing(10);
        gamesLostCard.getChildren().addAll(amountGamesLost, gamesLostLabel);
        gamesLostCard.setAlignment(Pos.CENTER);
        gamesLostLabel.setAlignment(Pos.CENTER);

        HBox statCards = new HBox();
        statCards.getChildren().addAll(gamesPlayedCard, gamesWonCard, gamesLostCard);
        statCards.setAlignment(Pos.CENTER);
        statCards.setSpacing(20);

        compMiddleContent.getStyleClass().add("compMiddleContent-vbox-comp");
        gamesPlayedCard.getStyleClass().add("games-played-card");
        gamesLostCard.getStyleClass().add("games-lost-card");
        gamesWonCard.getStyleClass().add("games-won-card");
        statCards.getStyleClass().add("stat-cards-box");


        // gamematch rules inside comp vbox
        HBox rulesBox = new HBox(10);
        VBox rulesLabelColumn = new VBox();
        VBox rulesColumn = new VBox();
        Label timeLimitText = new Label("05:00");
        Label difficultyRuleText = new Label("HARD");
        Label firstMoveRuleText = new Label("CPU");
        Label timeLabel2 = new Label("Time Limit");
        Label difficultyRuleLabel = new Label("Difficulty");
        Label firstMoveRuleLabel = new Label("First Move");

        rulesBox.getStyleClass().add("rules-box");
        rulesLabelColumn.getStyleClass().add("rules-label-column");
        rulesColumn.getStyleClass().add("rules-value-column");
        timeLabel2.getStyleClass().add("rules-label");
        difficultyRuleLabel.getStyleClass().add("rules-label");
        firstMoveRuleLabel.getStyleClass().add("rules-label");
        timeLimitText.getStyleClass().add("rules-value");
        difficultyRuleText.getStyleClass().addAll("rules-value", "difficulty-hard");
        firstMoveRuleText.getStyleClass().add("rules-value");

        rulesLabelColumn.getChildren().addAll(timeLabel2, difficultyRuleLabel, firstMoveRuleLabel);
        rulesColumn.getChildren().addAll(timeLimitText, difficultyRuleText, firstMoveRuleText);
        rulesBox.getChildren().addAll(rulesLabelColumn, rulesColumn);
        rulesBox.setAlignment(Pos.CENTER);
        rulesBox.setSpacing(30);
        rulesLabelColumn.setSpacing(20);
        rulesColumn.setSpacing(20);

        Label competitiveInfoLabel = new Label("Information");
        competitiveInfoLabel.setFont(Font.font(20));
        TextArea competitiveInfo = new TextArea("This competitive game match will count towards your record.");
        competitiveInfo.setEditable(false);
        competitiveInfo.setWrapText(false);
        competitiveInfo.setFocusTraversable(false);
        competitiveInfo.setPrefRowCount(1);
        competitiveInfo.setPrefWidth(350);
        competitiveInfo.setMaxWidth(400);
        competitiveInfo.getStyleClass().add("competitive-info");



        compMiddleContent.getChildren().addAll(yourStatsLabel, statCards, matchRulesLabel, rulesBox, competitiveInfoLabel, competitiveInfo);

        // positioning both vbox
        Region casualTopSpace = new Region();
        Region compTopSpace = new Region();
        Region casualBottomSpace = new Region();
        Region compBottomSpace = new Region();

        VBox.setVgrow(casualTopSpace, Priority.ALWAYS);
        VBox.setVgrow(compTopSpace, Priority.ALWAYS);
        VBox.setVgrow(casualBottomSpace, Priority.ALWAYS);
        VBox.setVgrow(compBottomSpace, Priority.ALWAYS);

        VBox left = new VBox();
        left.getChildren().addAll(labelCasual, casualTopSpace, casualMiddleContent, casualBottomSpace, startCasual);
        left.setAlignment(Pos.TOP_CENTER);
        left.setPadding(new Insets(40, 20, 20, 20));

        VBox right = new VBox();
        right.getChildren().addAll(labelComp, compTopSpace, compMiddleContent, compBottomSpace, startCompetitive);
        right.setAlignment(Pos.TOP_CENTER);
        right.setPadding(new Insets(40, 20, 20, 20));

        Separator separator = new Separator(Orientation.VERTICAL);
        separator.setMaxHeight(500);

        HBox splitScreen = new HBox();
        splitScreen.getChildren().addAll(left, separator , right);
        setCenter(splitScreen);

        // filling the screen
        splitScreen.setAlignment(Pos.CENTER);
        HBox.setHgrow(left, Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);

        startCasual.setFont(Font.font(15));
        startCasual.setPrefWidth(200);
        startCompetitive.setFont(Font.font(15));
        startCompetitive.setPrefWidth(200);


        setCenter(splitScreen);

        setPadding(new Insets(25));

        labelCasual.getStyleClass().add("label-mode-selection");
        labelComp.getStyleClass().add("label-mode-selection");
        firstMove.getStyleClass().add("label-mode-selection");
        timeLabel.getStyleClass().add("label-mode-selection");
        diff.getStyleClass().add("label-mode-selection");
        yourStatsLabel.getStyleClass().add("label-mode-selection");
        amountGames.getStyleClass().add("label-mode-selection");
        amountGamesWon.getStyleClass().add("label-mode-selection");
        amountGamesLost.getStyleClass().add("label-mode-selection");
        matchRulesLabel.getStyleClass().add("label-mode-selection");
        competitiveInfoLabel.getStyleClass().add("label-mode-selection");

    }

    Spinner<Integer> getMinutes1(){
        return minutes1;
    }

    Spinner<Integer> getMinutes2(){
        return minutes2;
    }

    Spinner<Integer> getSeconds1(){
        return seconds1;
    }

    Spinner<Integer> getSeconds2(){
        return seconds2;
    }

    Button getStartCasual() {
        return startCasual;
    }

    Button getStartCompetitive(){
        return startCompetitive;
    }

    ImageView getGoBack(){
        return goBackButton;
    }

    ToggleButton getEasy(){
        return easy;
    }
    ToggleButton getHard(){
        return hard;
    }

    ToggleButton getYouToggleButton(){
        return you;
    }

    ToggleButton getCpuToggleButton(){
        return cpu;
    }

    Label getAmountGames() {
        return amountGames;
    }
    Label getAmountGamesWon () {
        return amountGamesWon;
    }
    Label getAmountGamesLost(){
        return amountGamesLost;
    }
}

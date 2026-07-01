package be.kdg.integration.gameapplication.view.finalscreen;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FinalScreenView extends BorderPane {
    private Label gameResultLabel;
    private Label thanksLabel;
    private Label movesValueLabel;
    private Label avgPerMoveLabel;
    private Button returnToMenuButton;
    private Button playAgainButton;
    private VBox movesBox;
    private VBox avgBox;

    public FinalScreenView(){
        initializeNodes();
        layoutNodes();
    }

    private void initializeNodes(){
        this.gameResultLabel    = new Label();
        this.thanksLabel        = new Label();
        this.movesValueLabel    = new Label("0");
        this.avgPerMoveLabel    = new Label("0.0s");
        this.playAgainButton    = new Button("Play Again");
        this.returnToMenuButton = new Button("Return to Menu");
    }

    private void layoutNodes(){
        this.getStyleClass().add("final-screen");
        gameResultLabel.getStyleClass().add("game-result-label");
        thanksLabel.getStyleClass().add("thanks-label");
        playAgainButton.getStyleClass().add("btn-secondary");
        returnToMenuButton.getStyleClass().add("btn-primary");

        Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("game-over-label");

        Label congratsLabel = new Label();
        congratsLabel.getStyleClass().add("congrats-label");

        // stat boxes
        movesBox = createStatBox(movesValueLabel, "MOVES",         "stat-val-default");
        avgBox   = createStatBox(avgPerMoveLabel,  "AVG. PER MOVE", "stat-val-time");

        HBox statsRow = new HBox(12, movesBox, avgBox);
        statsRow.setAlignment(Pos.CENTER);

        // seperator
        Separator separator = new Separator(Orientation.HORIZONTAL);
        separator.setPrefWidth(60);
        separator.getStyleClass().add("divider");

        // header
        VBox header = new VBox(10, gameOverLabel, gameResultLabel, congratsLabel);
        header.setAlignment(Pos.CENTER);

        // buttons
        HBox buttons = new HBox(14, playAgainButton, returnToMenuButton);
        buttons.setAlignment(Pos.CENTER);

        // main
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(header, separator, statsRow, thanksLabel, buttons);
        thanksLabel.setAlignment(Pos.CENTER);
        VBox.setMargin(separator,   new Insets(16, 0, 20, 0));
        VBox.setMargin(statsRow,    new Insets(0,  0, 16, 0));
        VBox.setMargin(thanksLabel, new Insets(0,  0, 24, 0));

        setCenter(root);
        setPadding(new Insets(45, 30, 45, 30));
    }

    private VBox createStatBox(Label valueLabel, String labelText, String valueStyleClass) {
        valueLabel.getStyleClass().addAll("stat-val", valueStyleClass);
        Label caption = new Label(labelText);
        caption.getStyleClass().add("stat-label");
        VBox box = new VBox(4, valueLabel, caption);
        box.setAlignment(Pos.CENTER);
        box.getStyleClass().add("stat-box");
        box.setPadding(new Insets(15, 30, 15, 30));
        return box;
    }


    VBox getAvgBox(){
        return avgBox;
    }
    VBox getMovesBox(){
        return movesBox;
    }

    Label getGameResultLabel()   { return gameResultLabel; }
    Label getThanksLabel()       { return thanksLabel; }
    Button getPlayAgainButton()  { return playAgainButton; }
    Button getReturnToMenuButton() { return returnToMenuButton; }
    Label getMovesValueLabel() { return movesValueLabel; }
    Label getAvgPerMoveLabel() { return avgPerMoveLabel; }
}

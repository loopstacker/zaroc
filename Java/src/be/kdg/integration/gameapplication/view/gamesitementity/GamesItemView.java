package be.kdg.integration.gameapplication.view.gamesitementity;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GamesItemView extends HBox {
    private Label gameIdLabel;
    private Circle statusIndicator;
    private Label dateLabel;
    private Button continueButton;
    private Button replayButton;
    private Button statsButton;


    public GamesItemView() {
        initializeNodes();
        layoutNodes();
    }

    private void initializeNodes() {
        gameIdLabel = new Label();
//        gameIdLabel.setPrefWidth(60);
        statusIndicator = new Circle(8);
        dateLabel = new Label();
        continueButton = new Button("Continue");
        replayButton = new Button("Replay");
        statsButton = new Button("Stats");
    }

    private void layoutNodes() {
        gameIdLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        dateLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 14px;");
        setStyle("-fx-background-color: #3c3c3c; -fx-background-radius: 5;");
        setPadding(new Insets(10));
        setSpacing(15);

        continueButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 5 10;");
        replayButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-padding: 5 10;");
        statsButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-padding: 5 10;");

        VBox infoBox = new VBox(5);
        infoBox.getChildren().addAll(gameIdLabel, dateLabel);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(continueButton, replayButton, statsButton);

        HBox.setHgrow(infoBox, Priority.ALWAYS);

        getChildren().addAll(statusIndicator, infoBox, buttonBox);
        setAlignment(Pos.CENTER_LEFT);
    }

    Circle getStatusIndicator(){return statusIndicator;}
    Button getContinueButton() { return continueButton; }
    Button getReplayButton() { return replayButton; }
    Button getStatsButton() { return statsButton; }
    Label getGameIdLabel(){return gameIdLabel;}
    Label getDateLabel(){return dateLabel;}
}


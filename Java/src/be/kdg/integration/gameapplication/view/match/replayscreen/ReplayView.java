package be.kdg.integration.gameapplication.view.match.replayscreen;

import be.kdg.integration.gameapplication.view.match.AbstractMatchView;
import be.kdg.integration.gameapplication.view.match.BoardLayout;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

public class ReplayView extends AbstractMatchView {
    private Button previousMoveButton;
    private Button nextMoveButton;
    private Button startCustomGameButton;

    public ReplayView(){
        initializeNodes();
        layoutNodes();
    }

    private void initializeNodes(){
        this.boardLayout = new BoardLayout();
        this.previousMoveButton = new Button("PREVIOUS MOVE");
        this.nextMoveButton = new Button("NEXT MOVE");
        this.startCustomGameButton = new Button("START GAME FROM HERE");
        this.exitButton = new Button("EXIT");
        this.movesHistoryScroll = new ScrollPane();
        this.movesHistoryContent = new VBox();
        this.playerIndicator = new Circle(10);
        this.enemyIndicator = new Circle(10);
        this.timer = new Label("00:00");
    }

    private void layoutNodes(){
        timer.setStyle("-fx-font-size: 50px");
        getChildren().addAll(timer);

        getStyleClass().add("game-screen-background");

        movesHistoryScroll.setContent(movesHistoryContent);
        movesHistoryContent.setSpacing(5);
        movesHistoryContent.setAlignment(Pos.TOP_CENTER);

        VBox vBoxForMoveHistory = new VBox();
        movesHistoryScroll.setFitToHeight(true);
        movesHistoryScroll.setFitToWidth(true);
        Label madeMovesLabel = new Label("MOVES");
        madeMovesLabel.getStyleClass().addAll("made-moves-label");
        vBoxForMoveHistory.setAlignment(Pos.TOP_CENTER);
        vBoxForMoveHistory.getChildren().addAll(madeMovesLabel,movesHistoryScroll);
        vBoxForMoveHistory.setPrefSize(300, 400);
        vBoxForMoveHistory.setMaxHeight(400);

        HBox hBoxGameFieldAndMoveHistory = new HBox();

        movesHistoryScroll.getStyleClass().add("history-scroll-styling");
        movesHistoryContent.getStyleClass().add("history-content");
        vBoxForMoveHistory.getStyleClass().add("board-color");

        VBox fieldAndTimerAndControls = new VBox();
        setTop(exitButton);
        BorderPane.setMargin(exitButton, new Insets(20));
        HBox controlButtons = new HBox();

        HBox.setMargin(previousMoveButton, new Insets(10));
        HBox.setMargin(nextMoveButton, new Insets(10));
        HBox.setMargin(startCustomGameButton, new Insets(10));

        controlButtons.getChildren().addAll(previousMoveButton, nextMoveButton, startCustomGameButton);
        setMargin(controlButtons, new Insets(20));
        controlButtons.setAlignment(Pos.CENTER);
        BorderPane.setMargin(timer, new Insets(20));
        fieldAndTimerAndControls.setAlignment(Pos.CENTER);
        fieldAndTimerAndControls.getChildren().addAll(timer, boardLayout, controlButtons);

        Region space = new Region();
        space.setMaxWidth(Double.MAX_VALUE);
        space.setMaxHeight(Double.MAX_VALUE);
        hBoxGameFieldAndMoveHistory.getChildren().addAll(fieldAndTimerAndControls, space, vBoxForMoveHistory);
        HBox.setMargin(space, new Insets(10));

        hBoxGameFieldAndMoveHistory.setAlignment(Pos.CENTER);
        hBoxGameFieldAndMoveHistory.setFillHeight(false);

        setCenter(hBoxGameFieldAndMoveHistory);
    }

    Button getPreviousMoveButton(){
        return previousMoveButton;
    }

    protected Button getExitButton(){
        return super.getExitButton();
    }

    protected Label getTimer(){
        return super.getTimer();
    }

    Button getNextMoveButton(){ return nextMoveButton; }

    Button getStartCustomGameButton() {
        return startCustomGameButton;
    }
}
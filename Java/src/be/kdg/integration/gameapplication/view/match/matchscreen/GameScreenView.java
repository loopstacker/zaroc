package be.kdg.integration.gameapplication.view.match.matchscreen;

import be.kdg.integration.gameapplication.view.match.AbstractMatchView;
import be.kdg.integration.gameapplication.view.match.BoardLayout;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GameScreenView extends AbstractMatchView  {
    private Button undoButton;
    private Button pauseButton;
    private ImageView pauseSign;
    private Button tutorialButton;

    public GameScreenView(){
        initializeNodes();
        layoutNodes();
    }

    private void initializeNodes(){
        this.boardLayout = new BoardLayout();
        this.undoButton = new Button();
        this.exitButton = new Button();
        this.pauseButton = new Button();
        this.pauseSign = new ImageView(new Image("icons/player-pause.png"));
        this.movesHistoryScroll = new ScrollPane();
        this.movesHistoryContent = new VBox();
        this.playerIndicator = new Circle(10);
        this.enemyIndicator = new Circle(10);
        this.timer = new Label("00:00");
        this.tutorialButton = new Button();
    }

    private void layoutNodes(){
        ImageView questionMark = new ImageView(new Image("/icons/question-mark.png"));
        ImageView undoSign = new ImageView(new Image("icons/arrow-back-up.png"));
        ImageView exitSign = new ImageView(new Image("icons/logout-2.png"));

        questionMark.setFitHeight(25);
        questionMark.setFitWidth(25);
        undoSign.setFitWidth(25);
        undoSign.setFitHeight(25);
        pauseSign.setFitHeight(25);
        pauseSign.setFitWidth(25);
        exitSign.setFitWidth(25);
        exitSign.setFitHeight(25);

        tutorialButton.setGraphic(questionMark);
        pauseButton.setGraphic(pauseSign);
        exitButton.setGraphic(exitSign);
        undoButton.setGraphic(undoSign);


        //Extra touch.....
        Tooltip.install(tutorialButton, new Tooltip("Tutorial"));
        Tooltip.install(pauseButton, new Tooltip("Pause/Play"));
        Tooltip.install(exitButton, new Tooltip("Exit game"));
        Tooltip.install(undoButton, new Tooltip("Undo move"));


        timer.setStyle("-fx-font-size: 50px");
        getChildren().addAll(timer);

        HBox controlButtons = new HBox();
        controlButtons.getChildren().addAll(undoButton, exitButton, pauseButton);

        BorderPane topBorderPane = new BorderPane();
        topBorderPane.setLeft(controlButtons);
        topBorderPane.setRight(tutorialButton);
        BorderPane.setMargin(tutorialButton, new Insets(10));
     /*   Border bordToTest = new Border(new BorderStroke(
                Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2)
        ));*/

        setMargin(controlButtons, new Insets(20));
        setTop(topBorderPane);


        movesHistoryScroll.setContent(movesHistoryContent);
        movesHistoryContent.setSpacing(5);
        movesHistoryContent.setAlignment(Pos.TOP_CENTER);

        VBox vBoxForMoveHistory = new VBox();
        movesHistoryScroll.setFitToHeight(true);
        movesHistoryScroll.setFitToWidth(true);
        Label madeMovesLabel = new Label("MOVES");
        madeMovesLabel.getStyleClass().addAll("made-moves-label");
        vBoxForMoveHistory.setAlignment(Pos.TOP_CENTER);
        vBoxForMoveHistory.getChildren().addAll(madeMovesLabel, movesHistoryScroll);
        vBoxForMoveHistory.setPrefSize(300, 400);
        vBoxForMoveHistory.setMaxHeight(400);

        HBox hBoxGameFieldAndMoveHistory = new HBox();


        /*Border border = new Border(new BorderStroke(BoardLayout.BORDER_COLOR, BorderStrokeStyle.SOLID, new CornerRadii(15), new BorderWidths(10)));
*/

        movesHistoryScroll.getStyleClass().add("history-scroll-styling");
        movesHistoryContent.getStyleClass().add("history-content");
        vBoxForMoveHistory.getStyleClass().add("board-color");
       /* vBoxForMoveHistory.setBorder(border);
*/

        // -----------------TIMER----------------//
        VBox fieldAndTimer = new VBox();
//        timer.setStyle("-fx-font-size: 50px");

        BorderPane.setMargin(timer, new Insets(20));
//        timer.setText("XX:XX");
        fieldAndTimer.setAlignment(Pos.CENTER);
        fieldAndTimer.getChildren().addAll(timer, boardLayout);

        Region space = new Region();
        space.setMaxWidth(Double.MAX_VALUE);
        space.setMaxHeight(Double.MAX_VALUE);
        hBoxGameFieldAndMoveHistory.getChildren().addAll(fieldAndTimer,space, vBoxForMoveHistory);
        HBox.setMargin(space, new Insets(10));

        hBoxGameFieldAndMoveHistory.setAlignment(Pos.CENTER);
        hBoxGameFieldAndMoveHistory.setFillHeight(false);

        setCenter(hBoxGameFieldAndMoveHistory);
    }

    Button getUndoButton(){
        return undoButton;
    }

    Button getPauseButton(){return pauseButton;}

    protected Label getTimer(){
        return super.getTimer();
    }

    protected Button getExitButton(){
        return super.getExitButton();
    }

    protected BoardLayout getBoardLayout(){
        return super.getBoardLayout();
    }

    protected Button getTutorialButton(){return tutorialButton;}

    protected ImageView getPauseSign() {
        return pauseSign;
    }
}


package be.kdg.integration.gameapplication.view.match.matchscreen;

import be.kdg.integration.gameapplication.model.alertproperties.AlertSetting;
import be.kdg.integration.gameapplication.model.authservices.AuthentificationManager;
import be.kdg.integration.gameapplication.model.gamematch.GameController;
import be.kdg.integration.gameapplication.model.gamematch.match.*;
import be.kdg.integration.gameapplication.model.gamematch.match.board.*;
import be.kdg.integration.gameapplication.model.settings.GameSettings;
import be.kdg.integration.gameapplication.model.settings.GameSoundFX;
import be.kdg.integration.gameapplication.view.UIHandler;
import be.kdg.integration.gameapplication.view.customalert.CustomAlertCall;
import be.kdg.integration.gameapplication.view.finalscreen.FinalScreenPresenter;
import be.kdg.integration.gameapplication.view.finalscreen.FinalScreenView;
import be.kdg.integration.gameapplication.view.gamesound.GameSoundPresenter;
import be.kdg.integration.gameapplication.view.match.BoardLayout;
import be.kdg.integration.gameapplication.view.match.AbstractMatchPresenter;
import be.kdg.integration.gameapplication.view.tutorial.TutorialPresenter;
import be.kdg.integration.gameapplication.view.tutorial.TutorialView;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class GameScreenPresenter extends AbstractMatchPresenter{
    private GameController gameController;
    private GameScreenView view;

    private Match match;
    private TimerModel moveTimer;
    private Timeline timelineUpdater;

    private Circle hoveredCircle;
    private Circle selectedCircle;
    private Circle circleToMove;

    private ArrayList<Circle> invalidCircles = new ArrayList<>();
    private ArrayList<Circle> validCirclesToMove = new ArrayList<>();
    private ArrayList<Timeline> invalidAnimations = new ArrayList<>();
    private ArrayList<Position> positionsOfValidatedCircles = new ArrayList<>();
    private AuthentificationManager authentificationManager;
    private GameSoundPresenter soundPresenter;

    //--------------------- FOR GAME ----------------------------//
    public GameScreenPresenter(Match matchModel, GameScreenView view, GameSoundPresenter gameSoundPresenter, GameSettings gameSettings, AuthentificationManager authentificationManager, UIHandler uiHandler){
        super(view, uiHandler);
        this.view = view;
        this.match = matchModel;
        this.boardModel = match.getBoardModel();
        this.moveTimer = match.getTimerModel();
        this.gameController = new GameController();
        this.gameSettings = gameSettings;
        this.authentificationManager = authentificationManager;
        this.soundPresenter = gameSoundPresenter;
        initializeCells();
        initializeTimer();
        addEventHandlers();
        checkAiMove();
        updateView();
        updatePieceView();
    }

    //--------------------- FOR REPLAY ----------------------------//
    public GameScreenPresenter(Match matchModel, GameScreenView view, GameSettings gameSettings, AuthentificationManager authentificationManager, UIHandler uiHandler, GameSoundPresenter soundPresenter){
        super(view, uiHandler);
        this.view = view;
        this.match = matchModel;
        this.boardModel = match.getBoardModel();
        this.moveTimer = match.getTimerModel();
        this.gameController = new GameController();
        this.gameSettings = gameSettings;
        this.authentificationManager = authentificationManager;
        this.soundPresenter = soundPresenter;
        initializeCells();
        initializeTimer();
        addEventHandlers();
        checkAiMove();
        updateView();
        updatePieceView();
    }

    private void addEventHandlers(){
        view.getUndoButton().setOnAction(actionEvent -> {
            try{
                gameController.handleUndoRequest(match);
                validCirclesToMove.clear();
                circleToMove = null;
                selectedCircle = null;
                updateMoveHistoryView();
                updatePieceView();
                updateView();
            }catch (NoUndoLeftException e) {
                new CustomAlertCall(AlertType.INFORMATION, new AlertSetting(2, "No undo's left", e.getMessage()), view);
            }
        });

        view.getExitButton().setOnAction(event -> {
            interruptGame();
        });

        for(Circle circle : circlesField.keySet()){ //setting the same click events for each of circles

            circle.setOnMouseMoved(mouseEvent -> {
                if(invalidCircles.contains(circle)){
                    hoveredCircle = null;
                }else{
                    hoveredCircle = circle;
                }
                updateView();
            });

            circle.setOnMouseClicked(mouseEvent -> {
                if(match.getCurrentMoveOwner() != MoveOwner.PLAYER){
                    return;
                }

                if(selectedCircle != null && circle != selectedCircle && validCirclesToMove.contains(circle)){
                    circleToMove = circle;
                    gameController.handleMoveRequest(match, circlesField.get(circle));
                    soundPresenter.playSoundEffect(GameSoundFX.PIECE_MOVE);
                    validCirclesToMove.clear();
                    circleToMove = null;
                    selectedCircle = null;
                    checkAiMove();
                    updateMoveHistoryView();
                    updatePieceView(); //placed separately in order to optimise it and not rerender pieces
                    //each time we move around the board
                    updateView();
                    return;
                }

                if(selectedCircle == null || circle != selectedCircle){
                    selectedCircle = circle;
                    Position position = circlesField.get(selectedCircle);
                    try{
                        positionsOfValidatedCircles = gameController.handleSetSelectedCellRequest(match, position);
                        showValidCircles(positionsOfValidatedCircles);
                        soundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
                    }catch (EmptyPegException e) {
                        colorInvalidCircle(circle); //that one is a kind of in-game alert
                        //which will make invalidly selected cell blinking for a couple of seconds
                        soundPresenter.playSoundEffect(GameSoundFX.ERROR);
                        selectedCircle = null;
                        return;
                    }
                }
                updateView();
            });

            circle.setOnMouseExited(mouseEvent -> {
                hoveredCircle = null;
                updateView();
            });
        }

        view.getPauseButton().setOnAction(event -> {
            moveTimer.switchOnOff();
            setGameClicksEnabled(moveTimer.isRunning());
            updateView();
        });

        view.getTutorialButton().setOnAction(event -> {
            boolean isRunning = moveTimer.isRunning();
            if (isRunning){
                moveTimer.switchOnOff();
                setGameClicksEnabled(false);
            }
            openTutorialPopUp();
            if (isRunning){
                moveTimer.switchOnOff();
                setGameClicksEnabled(true);
            }
        });
    }

    private void openTutorialPopUp(){
        Stage tutorialPopUp = new Stage();
        tutorialPopUp.initModality(Modality.APPLICATION_MODAL);
        TutorialView tutorialView = new TutorialView();
        new TutorialPresenter(tutorialView, gameSettings, soundPresenter ,authentificationManager, uiHandler);
        Scene scene = new Scene(tutorialView, 700, 500);
        scene.getStylesheets().add(uiHandler.getColorVariables());
        scene.getStylesheets().add(uiHandler.getTutorialPageStyle());
        tutorialPopUp.setScene(scene);
        tutorialPopUp.showAndWait();
    }

    private void updateView(){
        if (moveTimer.isRunning()){
            view.getPauseButton().setText("PAUSE");
            view.getPauseSign().setImage(new Image("icons/player-pause.png"));
        } else {
            view.getPauseButton().setText("PLAY");
            view.getPauseSign().setImage(new Image("icons/player-play.png"));
        }
        view.getTimer().setText(moveTimer.getFormattedTime());
        applyTurnChanges();
        super.clearHover(invalidCircles);
        super.clearSelect();

        if(selectedCircle != null) setSelectedCell(selectedCircle);

        if(!validCirclesToMove.isEmpty()){
            for(Circle circle : validCirclesToMove){
                setValidCircleToMove(circle);
            }
        }
        if(hoveredCircle != null && !invalidCircles.contains(hoveredCircle)){
            setHoveredCircle(hoveredCircle);
        }
    }

    private void setGameClicksEnabled(boolean enabled){
        view.getBoardLayout().setMouseTransparent(!enabled);
        view.getUndoButton().setDisable(!enabled);
    }

    private void initializeTimer(){
        timelineUpdater = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            moveTimer.decreaseEachSecond();
            view.getTimer().setText(moveTimer.getFormattedTime());
            if(moveTimer.isFinished() || match.getBoardModel().isGameReadyToBeFinished()){
                finishTheGame();
            }
        }));
        timelineUpdater.setCycleCount(Timeline.INDEFINITE);
        timelineUpdater.play();

        //This method constantly updates the time view and checks if the game is finished//
    }

    private void interruptGame(){
        try{
            match.leaveTheGame();
            stopTimer();
        }catch (SQLException e) {
            new CustomAlertCall(AlertType.ERROR, new AlertSetting("Game interrupter", e.getMessage()), view);
        }
        returnToMainMenu();
    }

    private void finishTheGame(){
        try{
            match.finishTheGame();
            showFinishScreen();
            stopTimer();
        }catch (SQLException e) {
            new CustomAlertCall(AlertType.ERROR, new AlertSetting("Game finish", e.getMessage()), view);
        }
    }

    private void showFinishScreen(){
        Platform.runLater(() -> {
            FinalScreenView finalScreenView = new FinalScreenView();
            if(match instanceof CustomMatch m){
                new FinalScreenPresenter(finalScreenView, m, soundPresenter,gameSettings, authentificationManager, uiHandler);
            }
            if(match instanceof CompetitiveMatch m){
                new FinalScreenPresenter(finalScreenView, m, soundPresenter,gameSettings, authentificationManager, m.getGameFlowLogger(), uiHandler);
            }
            Stage stage = (Stage) view.getScene().getWindow();
            stage.getScene().setRoot(finalScreenView);
        });
    }

    private void showValidCircles(ArrayList<Position> validPositions){
        validCirclesToMove.clear();
        for(Map.Entry<Circle, Position> entry : circlesField.entrySet()){
            Position position = entry.getValue();
            if(validPositions.contains(position)){
                Circle circle = entry.getKey();
                validCirclesToMove.add(circle);
            }
        }
    }



    private void colorInvalidCircle(Circle circle){
        Timeline timeline = new Timeline();
        invalidAnimations.add(timeline);
        invalidCircles.add(circle);

        KeyFrame animation = new KeyFrame(Duration.seconds(0.1), actionEvent -> {
            if(circle.getFill() == BoardLayout.ON_HOVER || circle.getFill() == BoardLayout.DEFAULT_CIRCLE_COLOR){
                circle.setFill(Color.RED);
            }else{
                circle.setFill(BoardLayout.DEFAULT_CIRCLE_COLOR);
            }
        });
        timeline.getKeyFrames().addAll(
                animation
        );
        timeline.setCycleCount(5);
        timeline.setOnFinished(e -> {
            invalidCircles.remove(circle);
            invalidAnimations.remove(timeline);
        });
        timeline.play();

    }

    private void checkAiMove(){
        if(match.getCurrentMoveOwner() != MoveOwner.ENEMY){
            return;
        }
        Timeline wait = new Timeline();
        wait.getKeyFrames().add(new KeyFrame(Duration.seconds(match.getAiThinkingDelay())));
        wait.play();
        wait.setOnFinished(e -> {
            Move move = match.startAiMoveIfApplicable();
            soundPresenter.playSoundEffect(GameSoundFX.PIECE_MOVE);
            updatePieceView();
            Circle aiCircleTo = findCircle(move.getPositionTo());
            Circle aiCircleFrom = findCircle(move.getPositionFrom());
            aiCircleFrom.setFill(Color.GREEN);
            aiCircleTo.setFill(Color.BLUE);

            KeyFrame green = new KeyFrame(Duration.seconds(0.5), new KeyValue(
                    aiCircleFrom.fillProperty(), BoardLayout.DEFAULT_CIRCLE_COLOR
            ));
            KeyFrame blue = new KeyFrame(Duration.seconds(0.5), new KeyValue(
                    aiCircleTo.fillProperty(), BoardLayout.DEFAULT_CIRCLE_COLOR
            ));
            Timeline anim = new Timeline();
            anim.setCycleCount(1);
            anim.getKeyFrames().addAll(blue, green);
            anim.setOnFinished(ev -> {
                checkAiMove();
            });
            anim.play();
            updateMoveHistoryView();
        });
    }

    private void setValidCircleToMove(Circle circleToMove){
        circleToMove.setFill(BoardLayout.CIRCLE_AVAILABLE_FOR_MOVE);
    }

    private void setHoveredCircle(Circle circle){
        circle.setFill(BoardLayout.ON_HOVER);
    }

    private void setSelectedCell(Circle circle){
        circle.setStrokeType(StrokeType.OUTSIDE);
        circle.setStrokeWidth(2);
        circle.setStroke(BoardLayout.SELECTED_CIRCLE);
    }

    private void stopTimer(){
        if (timelineUpdater != null){
            timelineUpdater.stop();
            timelineUpdater = null;
        }
    }

    @Override
    protected void returnToMainMenu(){
        stopTimer();
        view.getScene().setRoot(previousWindow);
    }
}

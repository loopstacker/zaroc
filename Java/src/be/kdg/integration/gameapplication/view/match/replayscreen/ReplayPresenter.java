package be.kdg.integration.gameapplication.view.match.replayscreen;

import be.kdg.integration.gameapplication.model.authservices.AuthentificationManager;
import be.kdg.integration.gameapplication.model.gamematch.match.CustomMatch;
import be.kdg.integration.gameapplication.model.gamematch.match.ai.EnemyMode;
import be.kdg.integration.gameapplication.model.gamematch.match.board.*;
import be.kdg.integration.gameapplication.model.replay.Replay;
import be.kdg.integration.gameapplication.model.settings.GameSettings;
import be.kdg.integration.gameapplication.model.settings.GameSoundFX;
import be.kdg.integration.gameapplication.view.UIHandler;
import be.kdg.integration.gameapplication.view.gamesound.GameSoundPresenter;
import be.kdg.integration.gameapplication.view.match.BoardLayout;
import be.kdg.integration.gameapplication.view.match.AbstractMatchPresenter;
import be.kdg.integration.gameapplication.view.match.matchscreen.GameScreenPresenter;
import be.kdg.integration.gameapplication.view.match.matchscreen.GameScreenView;
import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class ReplayPresenter extends AbstractMatchPresenter{
    private Replay model;
    private ReplayView view;
    private AuthentificationManager authentificationManager;
    private GameSoundPresenter gameSoundPresenter;

    public ReplayPresenter(Replay model, ReplayView view, GameSettings gameSettings, AuthentificationManager authentificationManager, UIHandler uiHandler, GameSoundPresenter gameSoundPresenter){
        super(view, uiHandler);
        this.view = view;
        this.model = model;
        this.boardModel = model.getBoardModel();
        this.gameSettings = gameSettings;
        this.authentificationManager = authentificationManager;
        this.gameSoundPresenter = gameSoundPresenter;
        initializeCells();
        addEventHandlers();
        updateView();
        updatePieceView();
    }

    private void addEventHandlers(){
        view.getPreviousMoveButton().setOnAction(actionEvent -> {
            model.goBack();
            updateView();
            updateMoveHistoryView();
            updatePieceView();
        });

        view.getPreviousMoveButton().setOnMousePressed(event -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
        });

        view.getNextMoveButton().setOnAction(actionEvent -> {
            model.goFurther();
            updateView();
            updateMoveHistoryView();
            animateMove();
            updatePieceView();
        });

        view.getNextMoveButton().setOnMousePressed(event -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
        });

        view.getStartCustomGameButton().setOnAction(actionEvent -> {
            startCustomMatch();
        });

        view.getStartCustomGameButton().setOnMousePressed(event -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
        });

        view.getStartCustomGameButton().disableProperty().bind(
                Bindings.createBooleanBinding(
                () -> !(model.getBoardModel().getMoves().isEmpty() || model.getCurrentMove() == 0)));

        view.getExitButton().setOnAction(event -> {
            returnToMainMenu();
        });

        view.getExitButton().setOnMousePressed(event -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
        });
    }

    private void updateView(){
        applyTurnChanges();
        view.getTimer().setText(model.getFormattedTimeLeft());
        if(model.isFirstMove()){
            view.getPreviousMoveButton().setDisable(true);
        }else if(model.isLastMove()){
            view.getNextMoveButton().setDisable(true);
        }else{
            view.getPreviousMoveButton().setDisable(false);
            view.getNextMoveButton().setDisable(false);
        }
    }

    private void animateMove(){
        Move move = model.getLastAppliedMove();
        if(move == null){
            return;
        }
        updatePieceView();
        Circle aiCircleTo = findCircle(move.getPositionTo());
        Circle aiCircleFrom = findCircle(move.getPositionFrom());
        aiCircleFrom.setFill(Color.GREEN);
        aiCircleTo.setFill(Color.BLUE);

        KeyFrame green = new KeyFrame(Duration.seconds(0.3), new KeyValue(
                aiCircleFrom.fillProperty(), BoardLayout.DEFAULT_CIRCLE_COLOR
        ));

        KeyFrame blue = new KeyFrame(Duration.seconds(1), new KeyValue(
                aiCircleTo.fillProperty(), BoardLayout.DEFAULT_CIRCLE_COLOR
        ));
        Timeline anim = new Timeline();
        anim.setCycleCount(1);
        anim.getKeyFrames().addAll(blue, green);
        anim.play();
    }

    private void startCustomMatch(){
        GameScreenView gameScreenView = new GameScreenView();
        CustomMatch match = new CustomMatch(new BoardModel(boardModel));
        GameScreenPresenter gameScreenPresenter = new GameScreenPresenter(match, gameScreenView, gameSettings, authentificationManager, uiHandler, gameSoundPresenter);
        gameScreenPresenter.setPreviousWindow(view);
        view.getScene().setRoot(gameScreenView);
    }

    @Override
    protected void returnToMainMenu(){
        view.getScene().setRoot(previousWindow);
    }
}

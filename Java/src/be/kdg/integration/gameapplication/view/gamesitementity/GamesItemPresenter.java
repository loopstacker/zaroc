package be.kdg.integration.gameapplication.view.gamesitementity;

import be.kdg.integration.gameapplication.model.alertproperties.AlertSetting;
import be.kdg.integration.gameapplication.model.authservices.AuthentificationManager;
import be.kdg.integration.gameapplication.model.gamematch.match.CompetitiveMatch;
import be.kdg.integration.gameapplication.model.replay.Replay;
import be.kdg.integration.gameapplication.model.settings.GameSettings;
import be.kdg.integration.gameapplication.model.settings.GameSoundFX;
import be.kdg.integration.gameapplication.model.stats.AllGamesManager;
import be.kdg.integration.gameapplication.model.stats.GameData;
import be.kdg.integration.gameapplication.model.stats.GameStatus;
import be.kdg.integration.gameapplication.view.UIHandler;
import be.kdg.integration.gameapplication.view.allgamesscreen.GamesEntitiesPresenter;
import be.kdg.integration.gameapplication.view.allgamesscreen.GamesEntitiesView;
import be.kdg.integration.gameapplication.view.customalert.CustomAlertCall;
import be.kdg.integration.gameapplication.view.gamesound.GameSoundPresenter;
import be.kdg.integration.gameapplication.view.match.matchscreen.GameScreenPresenter;
import be.kdg.integration.gameapplication.view.match.matchscreen.GameScreenView;
import be.kdg.integration.gameapplication.view.match.replayscreen.ReplayPresenter;
import be.kdg.integration.gameapplication.view.match.replayscreen.ReplayView;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import java.sql.SQLException;


public class GamesItemPresenter{
    private GamesItemView view;
    private GameData gameData;
    private AllGamesManager allGamesManager;
    private GameSettings gameSettings;
    private AuthentificationManager authentificationManager;
    private UIHandler uiHandler;
    private GameSoundPresenter gameSoundPresenter;

    public GamesItemPresenter(GamesItemView view, GameData gameData, AllGamesManager allGamesManage, GameSettings gameSettings, AuthentificationManager authentificationManager, UIHandler uiHandler, GameSoundPresenter gameSoundPresenter){
        this.view = view;
        this.gameData = gameData;
        this.allGamesManager = allGamesManage;
        this.gameSettings = gameSettings;
        this.authentificationManager = authentificationManager;
        this.uiHandler = uiHandler;
        this.gameSoundPresenter = gameSoundPresenter;

        addEventHandlers();
        updateView();
    }

    private void addEventHandlers(){
        view.getContinueButton().setOnAction(e -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
            continueGame();
        });
        view.getReplayButton().setOnAction(e -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
            replayGame();
        });
        view.getStatsButton().setOnAction(e -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
            showStats();
        });
    }

    private void continueGame(){
        GameScreenView gameScreenView = new GameScreenView();
        try{
            CompetitiveMatch match = allGamesManager.continueGame(gameData.getGameId());
            GameScreenPresenter gameScreenPresenter = new GameScreenPresenter(match, gameScreenView, gameSettings, authentificationManager, uiHandler, gameSoundPresenter);
            GamesEntitiesView gamesEntitiesView = new GamesEntitiesView();
            new GamesEntitiesPresenter(gamesEntitiesView, allGamesManager, gameSettings, authentificationManager, uiHandler, gameSoundPresenter);
            gameScreenPresenter.setPreviousWindow(gamesEntitiesView);
            uiHandler.getScene().setRoot(gameScreenView);
        }catch (SQLException | IllegalStateException e) {
            new CustomAlertCall(Alert.AlertType.ERROR, new AlertSetting("Continue game", e.getMessage()), gameScreenView);
        }
    }

    private void replayGame(){
        ReplayView replayView = new ReplayView();
        replayView.getStylesheets().clear();
        try{
            Replay replay = allGamesManager.getReplayOfTheGame(gameData.getGameId());
            ReplayPresenter replayPresenter = new ReplayPresenter(replay, replayView, gameSettings, authentificationManager, uiHandler, gameSoundPresenter);
            GamesEntitiesView gamesEntitiesView = new GamesEntitiesView();
            new GamesEntitiesPresenter(gamesEntitiesView, allGamesManager, gameSettings, authentificationManager, uiHandler, gameSoundPresenter);
            replayPresenter.setPreviousWindow(gamesEntitiesView);
            uiHandler.getScene().setRoot(replayView);
        }catch (SQLException e) {
            new CustomAlertCall(Alert.AlertType.ERROR, new AlertSetting("Replay error", e.getMessage()), replayView);
        }
    }

    private void showStats(){
        int gameId = gameData.getGameId();

        GameData gameData = allGamesManager.getGameStats(gameId);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Stats");
        alert.setHeaderText("Game: " + gameId);
        alert.setContentText(
                "Status: " + gameData.getGameStatus() + "\n" +
                        "Date: " + gameData.getFormattedDate() + "\n" +
                        "Previous Points (non-authoritative): " + gameData.getPlayerPointsBeforeGame() + "\n" +
                        "Reward: " + gameData.getGameReward() + "\n"
        );
        alert.showAndWait();
    }

    private void updateView(){
        view.getGameIdLabel().setText("Game ID: " + gameData.getGameId());
        view.getDateLabel().setText(gameData.getFormattedDate() + "  |  Undos left: " + gameData.getUndoLeft());// points before game are not authoritative, as a player can have multiple paused games at the same time



        if(gameData.isGameGoing()){
            setStatusForGoing();
        }else if(gameData.getGameStatus() == GameStatus.WIN){
            setStatusForWin();
        }else{
            setStatusForLoss();
        }
    }

    void setStatusForGoing(){

        view.getStatusIndicator().setFill(Color.ORANGE);
        view.getContinueButton().setDisable(false);
        view.getReplayButton().setDisable(true);
        view.getStatsButton().setDisable(true);
        view.getReplayButton().setStyle("-fx-background-color: #666; -fx-text-fill: #999; -fx-padding: 5 10;");
        view.getStatsButton().setStyle("-fx-background-color: #666; -fx-text-fill: #999; -fx-padding: 5 10;");
    }

    private void setStatusForWin(){

        view.getStatusIndicator().setFill(Color.GREEN);
        view.getContinueButton().setDisable(true);
        view.getReplayButton().setDisable(false);
        view.getStatsButton().setDisable(false);
        view.getContinueButton().setStyle("-fx-background-color: #666; -fx-text-fill: #999; -fx-padding: 5 10;");
        view.getReplayButton().setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-padding: 5 10;");
        view.getStatsButton().setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-padding: 5 10;");

    }

    private void setStatusForLoss(){

        view.getStatusIndicator().setFill(Color.RED);
        view.getContinueButton().setDisable(true);
        view.getReplayButton().setDisable(false);
        view.getStatsButton().setDisable(false);
        view.getContinueButton().setStyle("-fx-background-color: #666; -fx-text-fill: #999; -fx-padding: 5 10;");
        view.getReplayButton().setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-padding: 5 10;");
        view.getStatsButton().setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-padding: 5 10;");
    }
}


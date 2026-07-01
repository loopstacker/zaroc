package be.kdg.integration.gameapplication.view.finalscreen;

import be.kdg.integration.gameapplication.model.authservices.AuthentificationManager;
import be.kdg.integration.gameapplication.model.databaseaccess.GameFlowLogger;
import be.kdg.integration.gameapplication.model.gamematch.match.CompetitiveMatch;
import be.kdg.integration.gameapplication.model.gamematch.match.CustomMatch;
import be.kdg.integration.gameapplication.model.gamematch.match.Match;
import be.kdg.integration.gameapplication.model.settings.GameSettings;
import be.kdg.integration.gameapplication.model.settings.GameSoundFX;
import be.kdg.integration.gameapplication.model.stats.GameData;
import be.kdg.integration.gameapplication.model.stats.GameStatus;
import be.kdg.integration.gameapplication.model.user.Player;
import be.kdg.integration.gameapplication.model.user.User;
import be.kdg.integration.gameapplication.view.UIHandler;
import be.kdg.integration.gameapplication.view.gamesound.GameSoundPresenter;
import be.kdg.integration.gameapplication.view.match.matchscreen.GameScreenPresenter;
import be.kdg.integration.gameapplication.view.match.matchscreen.GameScreenView;
import be.kdg.integration.gameapplication.view.start.StartPresenter;
import be.kdg.integration.gameapplication.view.start.StartView;
import javafx.scene.control.Alert;

import java.sql.SQLException;

public class FinalScreenPresenter{
    private FinalScreenView finalScreenView;
    private GameSettings gameSettings;
    private GameData gameDataModel;
    private AuthentificationManager authentificationManager;
    private GameSoundPresenter gameSoundPresenter;
    private Match matchModel;
    private GameFlowLogger gameFlowLogger;
    private UIHandler uiHandler;

    public FinalScreenPresenter(FinalScreenView finalScreenView, CompetitiveMatch gameDataModel, GameSoundPresenter gameSoundPresenter, GameSettings gameSettings, AuthentificationManager authentificationManager, GameFlowLogger gameFlowLogger, UIHandler uiHandler){
        this.finalScreenView = finalScreenView;
        this.authentificationManager = authentificationManager;
        this.matchModel = gameDataModel;
        this.gameDataModel = new GameData(gameDataModel);
        this.gameSettings = gameSettings;
        this.gameSoundPresenter = gameSoundPresenter;
        this.gameFlowLogger = gameFlowLogger;
        this.uiHandler = uiHandler;
        addEventHandlers();
        updateView();
    }

    public FinalScreenPresenter(FinalScreenView finalScreenView, CustomMatch gameDataModel, GameSoundPresenter gameSoundPresenter, GameSettings gameSettings, AuthentificationManager authentificationManager, UIHandler uiHandler){
        this.finalScreenView = finalScreenView;
        this.authentificationManager = authentificationManager;
        this.matchModel = gameDataModel;
        this.gameDataModel = new GameData(gameDataModel);
        this.gameSettings = gameSettings;
        this.gameSoundPresenter = gameSoundPresenter;
        this.uiHandler = uiHandler;
        addEventHandlers();
        updateView();
    }


    private void addEventHandlers(){
        finalScreenView.getReturnToMenuButton().setOnAction(e -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
            returnToMainMenu();
        });
        finalScreenView.getPlayAgainButton().setOnAction(e -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
            startNewGame();
        });
    }

    private void updateView(){
        GameSoundFX soundFX = gameDataModel.getGameStatus() == GameStatus.WIN ? GameSoundFX.WIN_SOUND : GameSoundFX.LOSS_SOUND ;
        gameSoundPresenter.playSoundEffect(soundFX);

        finalScreenView.getGameResultLabel().setText(gameDataModel.getGameStatus().name());
        finalScreenView.getThanksLabel().setText("Thank You for playing!");
        if(gameFlowLogger != null){
            //for competitive
            try{
                int moveCount = gameFlowLogger.getMoveCountForGame(gameDataModel.getGameId());
                finalScreenView.getMovesValueLabel().setText(String.valueOf(moveCount));

                float avgDuration = gameFlowLogger.getAvgMoveDurationPerGame(gameDataModel.getGameId());
                finalScreenView.getAvgPerMoveLabel().setText(String.format("%.1fs", avgDuration));
            }catch (SQLException e) {
                finalScreenView.getMovesValueLabel().setText("N/A");
                finalScreenView.getAvgPerMoveLabel().setText("N/A");
            }
        }else{
            //for custom
            hideStats();
            finalScreenView.getThanksLabel().setText("More statistics in Competitive Match. Thank You for the Game!");
        }
    }

    private void hideStats() {
        finalScreenView.getMovesBox().setVisible(false);
        finalScreenView.getMovesBox().setManaged(false);
        finalScreenView.getAvgBox().setVisible(false);
        finalScreenView.getAvgBox().setManaged(false);
    }

    private void startNewGame(){
        try{
            Match match = null;
            User user = gameSettings.getUser();

            if(matchModel instanceof CompetitiveMatch && user instanceof Player player){
                match = new CompetitiveMatch(gameFlowLogger, player);
            }

            if(matchModel instanceof CustomMatch matchOld){
                match = new CustomMatch(matchOld.getCustomGameSettings());
            }

            GameScreenView gameScreenView = new GameScreenView();
            new GameScreenPresenter(match, gameScreenView, gameSoundPresenter, gameSettings, authentificationManager, uiHandler);
            finalScreenView.getScene().setRoot(gameScreenView);
        }catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Unable to start the game: " + e.getMessage()).showAndWait();
        }
    }

    private void returnToMainMenu(){
        StartView startView = new StartView();
        new StartPresenter(startView, authentificationManager, gameSettings, gameSoundPresenter, uiHandler);
        finalScreenView.getScene().setRoot(startView);
    }
}

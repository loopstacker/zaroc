package be.kdg.integration.gameapplication.view.gamemodeselection;


import be.kdg.integration.gameapplication.model.alertproperties.AlertSetting;
import be.kdg.integration.gameapplication.model.authservices.AuthentificationManager;
import be.kdg.integration.gameapplication.model.gamematch.match.CustomGameSettings;
import be.kdg.integration.gameapplication.model.settings.GameSoundFX;
import be.kdg.integration.gameapplication.model.stats.AllGamesManager;
import be.kdg.integration.gameapplication.model.databaseaccess.DataBaseConnection;
import be.kdg.integration.gameapplication.model.gamematch.match.ai.EnemyMode;
import be.kdg.integration.gameapplication.model.gamematch.match.CompetitiveMatch;
import be.kdg.integration.gameapplication.model.gamematch.match.CustomMatch;
import be.kdg.integration.gameapplication.model.databaseaccess.GameFlowLogger;
import be.kdg.integration.gameapplication.model.gamematch.match.Match;
import be.kdg.integration.gameapplication.model.gamematch.match.board.MoveOwner;
import be.kdg.integration.gameapplication.model.settings.GameSettings;
import be.kdg.integration.gameapplication.model.user.Guest;
import be.kdg.integration.gameapplication.model.user.Player;
import be.kdg.integration.gameapplication.model.user.User;
import be.kdg.integration.gameapplication.view.UIHandler;
import be.kdg.integration.gameapplication.view.customalert.CustomAlertCall;
import be.kdg.integration.gameapplication.view.gamesound.GameSoundPresenter;
import be.kdg.integration.gameapplication.view.match.matchscreen.GameScreenPresenter;
import be.kdg.integration.gameapplication.view.match.matchscreen.GameScreenView;
import be.kdg.integration.gameapplication.view.start.StartPresenter;
import be.kdg.integration.gameapplication.view.start.StartView;
import javafx.scene.control.Alert;


public class GameModePresenter {
    private DataBaseConnection dataBaseConnection;
    private EnemyMode enemyMode;
    private MoveOwner moveOwner;
    private Match match;
    private User user;
    private GameSettings gameSettings;

    private GameModeView gameModeView;
    private long timeLimit;

    private AllGamesManager allGamesManager;
    private AuthentificationManager authentificationManager;
    private GameSoundPresenter gameSoundPresenter;

    private UIHandler uiHandler;


    public GameModePresenter(User user, GameModeView gameModeView, GameSoundPresenter gameSoundPresenter, AuthentificationManager authentificationManager, GameSettings gameSettings, AllGamesManager allGamesManager, UIHandler uiHandler){
        this.gameModeView = gameModeView;
        this.user = user;
        this.authentificationManager = authentificationManager;
        this.dataBaseConnection = authentificationManager.getDataBaseConnection();
        this.gameSettings = gameSettings;
        this.allGamesManager = allGamesManager;
        this.gameSoundPresenter = gameSoundPresenter;
        this.uiHandler = uiHandler;
        addEventHandlers();
        updateView();
    }

    private void addEventHandlers(){
        gameModeView.getStartCasual().setOnAction(e -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
                startCustomMatch();
        });

        gameModeView.getStartCompetitive().setOnAction(e -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
                startCompetitiveMatch();
            });

        gameModeView.getEasy().setOnAction(e ->{
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
            this.enemyMode = EnemyMode.RANDOM;
        });

        gameModeView.getHard().setOnAction(e ->{
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
            this.enemyMode = EnemyMode.SMART;
        });

        gameModeView.getCpuToggleButton().setOnAction(e ->{
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
            this.moveOwner = MoveOwner.ENEMY;
        });

        gameModeView.getYouToggleButton().setOnAction(e ->{
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
            this.moveOwner = MoveOwner.PLAYER;
        });

        gameModeView.getGoBack().setOnMouseClicked(mouseEvent -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
            returnToMenu();
        });

        gameModeView.getEasy().setSelected(true);
        enemyMode = EnemyMode.RANDOM;
        gameModeView.getCpuToggleButton().setSelected(true);
        moveOwner = MoveOwner.ENEMY;
    }

    private void updateView(){
        try{
            if (user instanceof Player){
                allGamesManager.refreshGames((Player) user);
            }
        }catch(Exception e){
            handleException(e);
        }
        if(user instanceof Guest){
            gameModeView.getStartCompetitive().setDisable(true);
        }
        setStats();
    }

    private void startCustomMatch(){
        int minutes = Integer.parseInt( gameModeView.getMinutes1().getValue() +""+
                gameModeView.getMinutes2().getValue());

        int seconds = Integer.parseInt( gameModeView.getSeconds1().getValue() + ""+
                gameModeView.getSeconds2().getValue());
        timeLimit = minutes * 60L + seconds;

        GameScreenView gameScreenView = new GameScreenView();
        /*gameScreenView.getStylesheets().addAll(
                getClass().getResource("/css-default/color-variables.css").toExternalForm(),
                getClass().getResource("/css-default/game-screen-style.css").toExternalForm());*/
        CustomGameSettings customGameSettings = new CustomGameSettings(enemyMode, moveOwner, timeLimit);
        match = new CustomMatch(customGameSettings);
        GameScreenPresenter gameScreenPresenter = new GameScreenPresenter(match, gameScreenView, gameSoundPresenter,gameSettings, authentificationManager, uiHandler);
        gameScreenPresenter.setPreviousWindow(gameModeView);
        gameModeView.getScene().setRoot(gameScreenView);
    }

    private void startCompetitiveMatch(){
        try{
            match = new CompetitiveMatch(new GameFlowLogger(dataBaseConnection), (Player) user);
            GameScreenView gameScreenView = new GameScreenView();
            GameScreenPresenter gameScreenPresenter = new GameScreenPresenter(match, gameScreenView, gameSoundPresenter, gameSettings, authentificationManager, uiHandler);
            gameScreenPresenter.setPreviousWindow(gameModeView);
            gameModeView.getScene().setRoot(gameScreenView);
        }catch (Exception e) {
            new CustomAlertCall(Alert.AlertType.ERROR, new AlertSetting(2, "Unable to start the competitive game"), gameModeView);
        }
    }

    private void setStats() {
        try {
            if (user instanceof Guest || allGamesManager.getTotalGamesOfUser() < 0){
                gameModeView.getAmountGames().setText("N/A");
                gameModeView.getAmountGamesLost().setText("N/A");
                gameModeView.getAmountGamesWon().setText("N/A");
            } else {
                gameModeView.getAmountGames().setText(String.valueOf(allGamesManager.getTotalGamesOfUser()));
                gameModeView.getAmountGamesWon().setText(String.valueOf(allGamesManager.getTotalWinsOfUser()));
                gameModeView.getAmountGamesLost().setText(String.valueOf(allGamesManager.getTotalLossesOfUser()));
            }
        } catch (Exception e) {
            new CustomAlertCall(Alert.AlertType.ERROR, new AlertSetting(e.getMessage()), gameModeView);
        }
    }

    private void returnToMenu(){
        StartView startView = new StartView();
        new StartPresenter(startView, authentificationManager, gameSettings, gameSoundPresenter, uiHandler);
        gameModeView.getScene().setRoot(startView);
    }

    private void handleException(Exception e){
        new CustomAlertCall(Alert.AlertType.ERROR, new AlertSetting(e.getMessage()), gameModeView);
    }
}

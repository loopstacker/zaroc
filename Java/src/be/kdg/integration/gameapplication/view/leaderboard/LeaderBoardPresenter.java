package be.kdg.integration.gameapplication.view.leaderboard;

import be.kdg.integration.gameapplication.model.authservices.AuthentificationManager;
import be.kdg.integration.gameapplication.model.leaderboard.LeaderBoardEntry;
import be.kdg.integration.gameapplication.model.leaderboard.LeaderBoardManager;
import be.kdg.integration.gameapplication.model.settings.GameSettings;
import be.kdg.integration.gameapplication.model.settings.GameSoundFX;
import be.kdg.integration.gameapplication.view.UIHandler;
import be.kdg.integration.gameapplication.view.gamesound.GameSoundPresenter;
import be.kdg.integration.gameapplication.view.start.StartPresenter;
import be.kdg.integration.gameapplication.view.start.StartView;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoardPresenter{
    private LeaderBoardManager leaderBoardManager;
    private LeaderBoardView view;
    private AuthentificationManager authentificationManager;
    private GameSettings gameSettings;
    private GameSoundPresenter gameSoundPresenter;
    private UIHandler uiHandler;

    public LeaderBoardPresenter(LeaderBoardView view, LeaderBoardManager leaderBoardManager, AuthentificationManager authentificationManager, GameSettings gameSettings, GameSoundPresenter gameSoundPresenter, UIHandler uiHandler){
        this.view = view;
        this.leaderBoardManager = leaderBoardManager;
        this.authentificationManager = authentificationManager;
        this.gameSettings = gameSettings;
        this.gameSoundPresenter = gameSoundPresenter;
        this.uiHandler = uiHandler;
        addEventHandlers();
        updateView();
    }

    private void addEventHandlers(){
        view.getReturn().setOnMouseClicked(mouseEvent -> {
            goBack();
        });

        view.getReturn().setOnMousePressed(e -> {
            gameSoundPresenter.playSoundEffect(GameSoundFX.CLICK_SOUND);
        });
    }

    private void updateView(){
        assignLeaderboard();
    }

    private void assignLeaderboard(){
        List<LeaderBoardEntry> entries = leaderBoardManager.getAllEntries();
        List<String[]> rows = new ArrayList<>();

        for(int i = 0; i < entries.size(); i++){
            LeaderBoardEntry entry = entries.get(i);
            if(entry.getTotalGames() == 0) continue;
            rows.add(new String[]{
                    String.valueOf(i + 1),                              // #
                    String.valueOf(entry.getRank()),                     // RANK
                    entry.getPlayerName(),                               // NAME
                    String.valueOf(entry.getScore()),                    // SCORE
                    String.valueOf(entry.getTotalMinutesPlayed()) + " MINUTES ",         // M PLAYED
                    entry.getWins() + " / " + entry.getLoses() + " / " +
                            entry.getTotalGames() + " TOTAL GAMES  ",    // WINS/LOSSES/TOTAL GAMES
                    String.format("%.2f", entry.getWinRate()) + "%",             // WIN RATE
                    String.valueOf(entry.getAverageNumberOfTurns()),     // AVG(mvs)/game
                    String.valueOf(entry.getAverageDurationPerTurn()) //AVG(d)/turn
            });
        }
        setData(rows);
    }

    private void goBack(){
        StartView startView = new StartView();
        new StartPresenter(startView, authentificationManager, gameSettings, gameSoundPresenter, uiHandler);
        view.getScene().setRoot(startView);
    }

    private void setData(List<String[]> rows){
        view.getLeaderboardTable().getItems().setAll(rows);
    }
}

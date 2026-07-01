package be.kdg.integration.gameapplication.model.stats;

import be.kdg.integration.gameapplication.model.databaseaccess.DataBaseConnection;
import be.kdg.integration.gameapplication.model.databaseaccess.GameFlowLogger;
import be.kdg.integration.gameapplication.model.databaseaccess.GameStatsDatabase;
import be.kdg.integration.gameapplication.model.gamematch.match.CompetitiveMatch;
import be.kdg.integration.gameapplication.model.gamematch.match.board.Move;
import be.kdg.integration.gameapplication.model.replay.Replay;
import be.kdg.integration.gameapplication.model.user.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class AllGamesManager{
    private GameStatsDatabase gameStatsDatabase;
    private GameFlowLogger gameFlowLogger;
    private Map<Integer, GameData> gamesMetadata;

    public AllGamesManager(DataBaseConnection dataBaseConnection) throws SQLException{
        this.gameStatsDatabase = new GameStatsDatabase(dataBaseConnection);
        this.gameFlowLogger = new GameFlowLogger(dataBaseConnection);
        this.gamesMetadata = new HashMap<>();
    }

    public void refreshGames(Player player) throws SQLException{
        try{
            requestDataForAllGames(player);
        }catch (SQLException e) {
            throw e;
        }
    }


    private void requestDataForAllGames(Player player) throws SQLException{
        try{
            ArrayList<GameData> data = gameStatsDatabase.getGameData(player.getPlayerID());
            gamesMetadata.clear();
            for(GameData gd : data){
                gamesMetadata.put(gd.getGameId(), gd);
            }
        }catch (SQLException e) {
            throw e;
        }
    }

    public CompetitiveMatch continueGame(int gameId) throws SQLException, IllegalStateException{
        try{
            if(!isGameGoing(gameId)) throw new IllegalStateException("Cannot continue: game " + gameId + " is not in progress");
            //exception above usually doesn't going to happen.
            Stack<Move> moves = gameStatsDatabase.loadAllMovesForGame(gameId);
            return new CompetitiveMatch(gamesMetadata.get(gameId), gameFlowLogger, moves);
        }catch (SQLException | IllegalStateException e) {
            throw e;
        }
    }

    public Map<Integer, GameData> getAllGamesMetadata(){
        return gamesMetadata;
    }

    private boolean isGameGoing(int gameId){
        GameData gd = gamesMetadata.get(gameId);
        return gd != null && gd.isGameGoing();
    }


    public GameData getGameStats(int gameId){
        return gamesMetadata.get(gameId);
    }

    public Replay getReplayOfTheGame(int gameId) throws SQLException{
        try{
            Stack<Move> moves = gameStatsDatabase.loadAllMovesForGame(gameId);
            return new Replay(moves);
        }catch (SQLException e) {
            throw e;
        }
    }

    public int getTotalGamesOfUser(){
        return gamesMetadata.size();
    }

    public int getTotalWinsOfUser(){
        int counter = 0;
        for(GameData gamesMetadata : gamesMetadata.values()){
            if(gamesMetadata.getGameStatus() == GameStatus.WIN){
                counter++;
            }
        }
        return counter;
    }

    public int getTotalLossesOfUser(){
        int counter = 0;
        for(GameData gamesMetadata : gamesMetadata.values()){
            if(gamesMetadata.getGameStatus() == GameStatus.LOSS){
                counter++;
            }
        }
        return counter;
    }

    public int getPlayersPoints(){
        int points = 0;
        for(GameData gamesMetadata : gamesMetadata.values()){
            if(gamesMetadata.getGameStatus() != GameStatus.GOING){
                points += gamesMetadata.getGameReward();
            }
        }
        return points;
    }
}

package be.kdg.integration.gameapplication.model.stats;

import be.kdg.integration.gameapplication.model.gamematch.match.CompetitiveMatch;
import be.kdg.integration.gameapplication.model.gamematch.match.CustomMatch;
import be.kdg.integration.gameapplication.model.gamematch.match.Match;


public class GameData{
    private int gameId;
    private String gameDate;
    private GameStatus gameStatus;
    private int gameReward;
    private int undoLeft;
    private int playerPointBeforeGame;

    public GameData(int gameId, String gameDate, GameStatus gameStatus, int gameReward, int undoLeft, int playerPointBeforeGame){
        this.gameId = gameId;
        this.gameDate = gameDate;
        this.gameStatus = gameStatus;
        this.gameReward = gameReward;
        this.undoLeft = undoLeft;
        this.playerPointBeforeGame = playerPointBeforeGame;
    }

    public GameData(Match match){
        this.gameStatus = match.isWin() ? GameStatus.WIN : GameStatus.LOSS;
        if(match instanceof CompetitiveMatch matched){
            this.gameId = matched.getGameId();
            this.gameDate = matched.getStartDate();
            this.gameReward = matched.getReward();
            this.undoLeft = matched.getUndoLeft();
        }
        if(match instanceof CustomMatch){
            this.gameId = -1;
            this.gameDate = "Now";
            this.gameReward = 0;
            this.undoLeft = -1;
        }
    }

    public int getGameId(){
        return gameId;
    }

    public GameStatus getGameStatus(){
        return gameStatus;
    }

    public int getUndoLeft(){
        return undoLeft;
    }

    public int getGameReward(){
        return gameReward;
    }

    public String getFormattedDate(){
        return gameDate;
    }

    public boolean isGameGoing(){
        return gameStatus == GameStatus.GOING;
    }

    public int getPlayerPointsBeforeGame(){return playerPointBeforeGame;}
}

package be.kdg.integration.gameapplication.model.gamematch.match;

import be.kdg.integration.gameapplication.model.databaseaccess.GameFlowLogger;
import be.kdg.integration.gameapplication.model.gamematch.match.ai.DirectionDeterminant;
import be.kdg.integration.gameapplication.model.gamematch.match.ai.EnemyMode;
import be.kdg.integration.gameapplication.model.gamematch.match.board.*;
import be.kdg.integration.gameapplication.model.stats.GameData;
import be.kdg.integration.gameapplication.model.user.Player;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

/**
 * Represents a competitive match where progress is persisted and
 * player scores are affected by the outcome.
 */

public class CompetitiveMatch extends Match{
    private GameFlowLogger gameFlowLogger;
    private int gameId;
    private int reward;
    private String startDate;
    private int undoLeft;

    /**
     * Constructs a new competitive match for the given player,
     * logging the game creation to the database.
     *
     * @param gameFlowLogger follows the game state and outcomes
     * @param player that is starting the match
     * @throws SQLException if the database can't be reached
     */
    //------------ FOR NEW GAME ------------//
    public CompetitiveMatch(GameFlowLogger gameFlowLogger, Player player) throws SQLException{
        super();
        super.firstMoveOwner = MoveOwner.ENEMY;
        this.undoLeft = 3;
        this.gameFlowLogger = gameFlowLogger;
        this.startDate = getCurrentDate();
        try{
            this.gameId = gameFlowLogger.newGame(player);
        }catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Reconstructs a competitive match from a previously saved state,
     * replaying all recorded moves to restore the board.
     *
     * @param gameData the saved game metadata to restore from
     * @param gameFlowLogger follows the game state and outcomes
     * @param madeMoves the sequence of moves already played in the saved game
     */

    //------------ TO CONTINUE EXISTING GAME ------------//
    public CompetitiveMatch(GameData gameData, GameFlowLogger gameFlowLogger, Collection<Move> madeMoves){
        super();
        super.firstMoveOwner = MoveOwner.ENEMY;

        float timeOfAllPreviousMoves = 0;
        for(Move move : madeMoves){
            super.getBoardModel().applyMoveForced(move);
            timeOfAllPreviousMoves  += move.getTimePerMove();
        }

        float timeToMove = TimerModel.START_AMOUNT_FOR_COMPETITIVE_MATCH - timeOfAllPreviousMoves;
        super.timerModel = new TimerModel(timeToMove);
        this.gameFlowLogger = gameFlowLogger;
        this.startDate = gameData.getFormattedDate();
        this.gameId = gameData.getGameId();
        this.undoLeft = gameData.getUndoLeft();
    }


    private String getCurrentDate(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy mm:HH");
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.format(dateTimeFormatter);
    }

    private void saveProgress() throws SQLException{
        try{
            gameFlowLogger.saveGameProgress(this);
        }catch (SQLException e) {
            throw e;
        }
    }

    public int getUndoLeft(){
        return undoLeft;
    }

    /**
     * Decreases the number of remaining undo actions by one.
     */
    public void decreaseUndoLeft(){
        undoLeft--;
    }


    /**
     * Executes the AI's move by selecting a smart direction and applying it to the board.
     *
     * @return the move made by the AI
     */
    @Override
    public Move startAiMoveIfApplicable(){
        DirectionDeterminant dt = getEnemy().getEnemyMovements().get(EnemyMode.SMART);
        dt.findTheMove();
        Position from = dt.getPosFrom();
        Position to = dt.getPosTo();
        Move move = new Move(from, to, MoveOwner.ENEMY, super.getAiThinkingDelay());
        super.getBoardModel().applyMove(move);
        return move;
    }

    /**
     * Saves the current game progress and exits the match.
     *
     * @throws SQLException if the progress cannot be saved to the database
     */
    @Override
    public void leaveTheGame() throws SQLException{
        try{
            saveProgress();
        }catch (SQLException e) {
            throw e;
        }
    }


    /**
     * Finalizes the match by determining the outcome, calculating the score reward,
     * and saving the result to the database.
     *
     * @throws SQLException if the result cannot be saved to the database
     */
    @Override
    public void finishTheGame() throws SQLException{
        int rewardRatio = super.defineGameOutcome();
        calculateReward(rewardRatio);
        try{
            saveProgress();
            gameFlowLogger.onGameFinish(this);
        }catch (SQLException e) {
            throw e;
        }
    }

    private void calculateReward(int rewardRatio){
        this.reward = switch(rewardRatio){
            case 0 -> -20;
            case 1 -> -10;
            case 2 -> -5;
            case 3 -> 5;
            case 4 -> 10;
            case 5 -> 20;
            default -> 0;
        };
    }

    public GameFlowLogger getGameFlowLogger() {
        return gameFlowLogger;
    }

    public String getStartDate(){
        return startDate;
    }

    public int getReward(){
        return reward;
    }

    public int getGameId(){
        return gameId;
    }

    public boolean isWin(){
        return super.isWin();
    }
}

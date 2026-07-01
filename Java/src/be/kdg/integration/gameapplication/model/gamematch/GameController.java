package be.kdg.integration.gameapplication.model.gamematch;

import be.kdg.integration.gameapplication.model.gamematch.match.CompetitiveMatch;
import be.kdg.integration.gameapplication.model.gamematch.match.Match;
import be.kdg.integration.gameapplication.model.gamematch.match.NoUndoLeftException;
import be.kdg.integration.gameapplication.model.gamematch.match.TimerModel;
import be.kdg.integration.gameapplication.model.gamematch.match.board.*;

import java.util.ArrayList;

/**
 * Handles player input requests during an active match,
 * delegating board and timer operations accordingly.
 */
public class GameController{

    /**
     * Selects the given position on the board and returns all valid moves from it.
     *
     * @param match    the active match
     * @param position the position the player wants to select
     * @return a list of valid target positions from the selected position
     * @throws EmptyPegException if the selected position contains no peg
     */
    public ArrayList<Position> handleSetSelectedCellRequest(Match match, Position position) throws EmptyPegException{
        try{
            BoardModel boardModel = match.getBoardModel();
            boardModel.setSelectedPosition(position);
            return boardModel.findValidMoves();
        }catch (EmptyPegException e) {
            throw e;
        }
    }

    /**
     * Undoes the last move made in the match.
     * In a competitive match, consumes one undo charge.
     *
     * @param match the active match
     * @throws NoUndoLeftException if the match is competitive and no undos remain
     */
    public void handleUndoRequest(Match match) throws NoUndoLeftException{
        if(match instanceof CompetitiveMatch that){
            if(that.getUndoLeft() <= 0){
                throw new NoUndoLeftException();
            }
            that.decreaseUndoLeft();
        }
        match.getBoardModel().undoMove();
    }

    /**
     * Applies the player's move to the given position,
     * recording the time taken for the move.
     *
     * @param match    the active match
     * @param position the target position of the player's move
     */
    public void handleMoveRequest(Match match, Position position){
        BoardModel boardModel = match.getBoardModel();
        TimerModel timerModel = match.getTimerModel();

        float timeForAllPreviousMoves = 0;
        for(Move move : boardModel.getMoves()){
            timeForAllPreviousMoves  += move.getTimePerMove();
        }

        float timeForMove = timerModel.getTimeLimit() - timerModel.getSecondsLeft() - timeForAllPreviousMoves;

        Move move = boardModel.createMove(position, MoveOwner.PLAYER, timeForMove);
        boardModel.applyMove(move);
    }

}

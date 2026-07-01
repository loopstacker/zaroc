package be.kdg.integration.gameapplication.model.replay;

import be.kdg.integration.gameapplication.model.gamematch.match.TimerModel;
import be.kdg.integration.gameapplication.model.gamematch.match.board.BoardModel;
import be.kdg.integration.gameapplication.model.gamematch.match.board.Move;

import java.util.List;

public class Replay{
    private BoardModel boardModel;
    private List<Move> allMoves;
    private int currentMove = 0;
    private float timeLeft;

    public Replay(List<Move> allMoves) {
        this.boardModel = new BoardModel();
        this.allMoves = allMoves;
        this.timeLeft = TimerModel.START_AMOUNT_FOR_COMPETITIVE_MATCH;
    }

    public void goFurther(){
        if(!allMoves.isEmpty() && !isLastMove()){
            Move current = allMoves.get(currentMove);
            boardModel.applyMoveForced(current);
            timeLeft -= current.getTimePerMove();
            currentMove += 1;
        }
    }

    public void goBack(){
        if(!allMoves.isEmpty() && !isFirstMove()){
            currentMove -= 1;
            Move current = allMoves.get(currentMove);
            boardModel.reverseAndApplyMove(current);
            timeLeft += current.getTimePerMove();
        }
    }

    public String getFormattedTimeLeft() {
        long minutes = (long) timeLeft/60;
        long seconds = (long) timeLeft%60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public BoardModel getBoardModel(){
        return boardModel;
    }

    public Move getLastAppliedMove(){
        return allMoves.get(currentMove-1);
    }

    public boolean isFirstMove(){
        return currentMove == 0;
    }

    public boolean isLastMove(){
        return currentMove == allMoves.size();
    }

    public int getCurrentMove() {
        return currentMove;
    }
}

package be.kdg.integration.gameapplication.model.gamematch.match;

import be.kdg.integration.gameapplication.model.gamematch.match.ai.Enemy;
import be.kdg.integration.gameapplication.model.gamematch.match.board.*;

import java.sql.SQLException;

public abstract class Match{
    private final float aiThinkingDelay = 0.1F;
    protected TimerModel timerModel;
    protected BoardModel boardModel;
    protected MoveOwner firstMoveOwner;
    private Enemy enemy;
    private boolean isWin;

    protected Match(){
        this.boardModel = new BoardModel();
        this.enemy = new Enemy(boardModel);
        this.timerModel = new TimerModel(TimerModel.START_AMOUNT_FOR_COMPETITIVE_MATCH);
    }

    protected Match(BoardModel boardModel){
        this.boardModel = boardModel;
        this.enemy = new Enemy(boardModel);
    }

    public float getAiThinkingDelay(){
        return aiThinkingDelay;
    }

    public abstract Move startAiMoveIfApplicable();

    public abstract void leaveTheGame() throws SQLException;

    public abstract void finishTheGame() throws SQLException;

    public BoardModel getBoardModel(){
        return boardModel;
    }

    public TimerModel getTimerModel(){
        return timerModel;
    }

    protected Enemy getEnemy(){
        return this.enemy;
    }

    protected int defineGameOutcome(){
        int rewardRatio = 0;
        for(int i = 1; i <= 5; i++){
            PegStack peg = getBoardModel().getGameBoard().get(new Position(i, 1));
            Piece piece = peg.pop();
            if(piece != null && piece.getPieceOwner() == PieceOwner.PLAYER) rewardRatio++;
        }
        this.isWin = rewardRatio >= 3;
        return rewardRatio;
    }

    public MoveOwner getCurrentMoveOwner(){
        int moves = boardModel.getMoves().size();
        if((moves / 2) % 2 == 0){
            return firstMoveOwner;
        }else{
            return firstMoveOwner == MoveOwner.PLAYER ? MoveOwner.ENEMY : MoveOwner.PLAYER;
        }
    }

    public boolean isWin(){
        return isWin;
    }
}

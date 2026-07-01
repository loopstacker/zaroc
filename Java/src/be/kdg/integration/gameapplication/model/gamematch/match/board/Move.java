package be.kdg.integration.gameapplication.model.gamematch.match.board;

public class Move{
    private Position positionTo;
    private Position positionFrom;
    private MoveOwner moveOwner;
    private float timePerMove;

    public Move(Position positionFrom, Position positionTo, MoveOwner moveOwner, float timePerMove){
        this.positionFrom = positionFrom;
        this.positionTo = positionTo;
        this.moveOwner = moveOwner;
        this.timePerMove = timePerMove;
    }

    public MoveOwner getMoveOwner(){
        return moveOwner;
    }

    public Position getPositionFrom(){
        return positionFrom;
    }

    public Position getPositionTo(){
        return positionTo;
    }

    public float getTimePerMove(){
        return timePerMove;
    }

    @Override
    public String toString() {
        return String.format("Move From(%s) To(%s) By(%s) Time(%d)", positionFrom, positionTo, moveOwner, timePerMove);
    }
}

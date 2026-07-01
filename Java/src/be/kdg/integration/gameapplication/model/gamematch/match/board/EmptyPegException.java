package be.kdg.integration.gameapplication.model.gamematch.match.board;

public class EmptyPegException extends RuntimeException{
    public EmptyPegException(){
        super("Empty peg cannot be selected");
    }
}

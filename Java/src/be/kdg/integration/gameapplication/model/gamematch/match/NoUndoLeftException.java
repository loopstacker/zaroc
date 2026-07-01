package be.kdg.integration.gameapplication.model.gamematch.match;

public class NoUndoLeftException extends RuntimeException{
    public NoUndoLeftException(){
        super("No undo's left!");
    }
}

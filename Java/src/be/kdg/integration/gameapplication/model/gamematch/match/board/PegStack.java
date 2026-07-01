package be.kdg.integration.gameapplication.model.gamematch.match.board;

import java.util.LinkedList;
import java.util.List;

public class PegStack{
    private int length;
    private List<Piece> stack = new LinkedList<>();

    public PegStack(int length){
        this.length = length;
    }

    public PegStack (PegStack old) {
        this.length = old.length;
        this.stack.addAll(old.getStack());
    }

    public boolean hasPieceOnTop(){
        return length() == size();
    }

    public boolean isEmpty(){
        return stack.isEmpty();
    }

    public void push(Piece piece){
        stack.addLast(piece);
    }

    public Piece pop(){
        if(!stack.isEmpty()){
            return stack.removeLast();
        }
        return null;
    }

    public int length(){
        return this.length;
    }


    public int size(){
        return stack.size();
    }

    public List<Piece> getStack(){
        return stack;
    }
}

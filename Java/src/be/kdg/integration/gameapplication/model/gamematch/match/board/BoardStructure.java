package be.kdg.integration.gameapplication.model.gamematch.match.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public interface BoardStructure{
    HashMap<Position, PegStack> getGameBoard();
    ArrayList<Position> findValidMoves(Position position);
    Stack<Move> getMoves();
}

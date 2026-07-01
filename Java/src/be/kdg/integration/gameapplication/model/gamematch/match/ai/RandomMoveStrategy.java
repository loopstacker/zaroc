package be.kdg.integration.gameapplication.model.gamematch.match.ai;

import be.kdg.integration.gameapplication.model.gamematch.match.board.BoardStructure;
import be.kdg.integration.gameapplication.model.gamematch.match.board.Position;

import java.util.ArrayList;
import java.util.Random;

/**
 * One of the enemy strategies
 *
 * <p>
 *    Makes random decision to move
 * </p>
 */

public class RandomMoveStrategy extends MoveStrategyOrganizer{
    private Random random;

    public RandomMoveStrategy(BoardStructure boardStructure){
        super(boardStructure);
        this.random = new Random();
    }

    /**
     * Randomly generates moves and remembers one of them, once it valid to apply
     */
    @Override
    public void findTheMove(){
        super.foundPosFrom = null;
        super.foundPosTo = null;
        ArrayList<Position> valid;
        do{
            int y = random.nextInt(1, 5);
            int x = y <= 2 ? random.nextInt(1, 6) : random.nextInt(1, 5);
            super.foundPosFrom = new Position(x, y);
            valid = getBoardStructure().findValidMoves(foundPosFrom);
        }while(valid.isEmpty());

        super.foundPosTo = valid.get(random.nextInt(0, valid.size()));
    }
}

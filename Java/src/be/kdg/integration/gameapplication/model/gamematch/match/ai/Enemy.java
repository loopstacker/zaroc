package be.kdg.integration.gameapplication.model.gamematch.match.ai;

import be.kdg.integration.gameapplication.model.gamematch.match.board.BoardStructure;

import java.util.HashMap;

/**
 *Game AI class
 *
 *<p>
 *     Knows a board structure and knows all strategies to make a move
 *</p>
 *
 * @see EnemyMode
 * @see DirectionDeterminant
 * @see BoardStructure
 */

public class Enemy implements EnemyMovementModeCollection{
    private HashMap<EnemyMode, DirectionDeterminant> allStrategies = new HashMap();
    /**
     * Creates an enemy Class
     *  <p>
     *      Initializes all its strategies
     *  </p>
     *
     * @param boardStructure to pass it to the strategies
     */
    public Enemy(BoardStructure boardStructure){
        allStrategies.put(EnemyMode.RANDOM, new RandomMoveStrategy(boardStructure));
        allStrategies.put(EnemyMode.SMART, new SmartMoveStrategy(boardStructure));
    }

    @Override
    public HashMap<EnemyMode, DirectionDeterminant> getEnemyMovements(){
        return allStrategies;
    }
}

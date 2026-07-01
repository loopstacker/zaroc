package be.kdg.integration.gameapplication.model.gamematch.match.ai;

import java.util.HashMap;

/**
 * Defines contract, that implementing class must be able to return HashMap of its movement strategies
 */
public interface EnemyMovementModeCollection{
    HashMap<EnemyMode, DirectionDeterminant> getEnemyMovements();
}

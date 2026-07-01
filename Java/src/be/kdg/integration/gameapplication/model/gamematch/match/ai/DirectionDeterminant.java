package be.kdg.integration.gameapplication.model.gamematch.match.ai;

import be.kdg.integration.gameapplication.model.gamematch.match.board.Position;

/**
 * Defines the contract that the class implementing that interface must be able
 * to find the move and be able to return the starting and ending positions
 */
public interface DirectionDeterminant{
    /**
     * Starts algorithm that fill assign positionFrom and positionTo variable
     */

    void findTheMove();
    Position getPosTo();
    Position getPosFrom();
}

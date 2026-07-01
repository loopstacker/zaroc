package be.kdg.integration.gameapplication.model.gamematch.match.ai;

import be.kdg.integration.gameapplication.model.gamematch.match.board.BoardStructure;
import be.kdg.integration.gameapplication.model.gamematch.match.board.Position;

/**
 * Parent class for all move strategies
 *
 * <p>
 *     Initializes all basic dependencies for other move strategies for AI
 *     Implements {@link DirectionDeterminant}
 * </p>
 *
 * @see RandomMoveStrategy
 * @see SmartMoveStrategy
 */
public abstract class MoveStrategyOrganizer implements DirectionDeterminant{
    private BoardStructure boardStructure;
    protected Position foundPosTo;
    protected Position foundPosFrom;

    public MoveStrategyOrganizer(BoardStructure boardStructure){
        this.boardStructure = boardStructure;
    }

    protected BoardStructure getBoardStructure(){
        return boardStructure;
    }

    public Position getPosTo(){
        return foundPosTo;
    }

    public Position getPosFrom(){
        return foundPosFrom;
    }


}

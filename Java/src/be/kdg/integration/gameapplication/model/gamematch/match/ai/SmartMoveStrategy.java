package be.kdg.integration.gameapplication.model.gamematch.match.ai;

import be.kdg.integration.gameapplication.model.gamematch.match.board.*;

import java.util.ArrayList;
import java.util.List;

/**
 * One of the enemy strategies
 *
 * <p>
 * Smart move strategy, that is based on evaluation of moves
 * </p>
 */

//
public class SmartMoveStrategy extends MoveStrategyOrganizer{

    public SmartMoveStrategy(BoardStructure boardStructure){
        super(boardStructure);
    }

    /**
     * Finds the move to make
     * <p>
     * Gets all possible valid moves and based on that generates
     * and evaluate all possible outcomes from them.
     * During evaluation all moves are sorted by priority
     * and the top one is defined as the smartest one
     * </p>
     */
    @Override
    public void findTheMove(){
        super.foundPosFrom = null;
        super.foundPosTo = null;


        List<Move> allMoves = getAllValidMoves();

        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        for(Move move : allMoves){
            int score = evaluateMove(move);
            if(score > bestScore){
                bestMove = move;
                bestScore = score;
            }
        }

        super.foundPosFrom = bestMove.getPositionFrom();
        super.foundPosTo = bestMove.getPositionTo();
    }

    private int evaluateMove(Move move){
        int score = 0;
        int moveSafety = evaluateMoveSafety(move);

        if(checkIfMyPieceCanFinish(move)){
            return 10000;
        }
        if(checkIfMyPieceCanFinishNextMove(move)){ // next move of the same turn
            return 8000;
        }
        if(checkIfBlocksPlayerFromFinish(move)){
            score += 600;
        }
        if(checkIfMyPieceCanFinishNextTurn(move) && moveSafety > 0){ // first move of next turn
            score += 1000;
        }
        if(checkIfMyPieceCanMoveForward(move)){ // into the next row on y-axis
            score += (5 - move.getPositionTo().getY()) * 100;
        }
        if(checkIfMyPieceCanMoveForwardOnSecondMove(move)){ // next move of the same turn
            score += (5 - move.getPositionTo().getY()) * 80;
        }
        if(checkIfMyPieceCanMoveForwardNextTurn(move)){ // first move of next turn
            score += 100;
        }
        score += getPlayerPushDownValue(move); // get additional score for moving the player down the pegs


        if(moveSafety == 0){
            score -= 5000;
        }else if(moveSafety == 1){
            score -= 100;
        }

        return score;
    }


    private List<Move> getAllValidMoves(){
        ArrayList<Move> validMoves = new ArrayList<>();
        // gather all the valid moves
        for(int y = 1; y < 3; y++){
            for(int x = 1; x < 6; x++){
                for(Position validPositionTo : getBoardStructure().findValidMoves(new Position(x, y))){
                    validMoves.add(new Move(new Position(x, y), validPositionTo, MoveOwner.ENEMY, 0));
                }
            }
        }
        for(int y = 3; y < 5; y++){
            for(int x = 1; x < 5; x++){
                for(Position validPositionTo : getBoardStructure().findValidMoves(new Position(x, y))){
                    validMoves.add(new Move(new Position(x, y), validPositionTo, MoveOwner.ENEMY, 0));
                }
            }
        }
        return validMoves;
    }

    private List<Move> getValidMovesForPositionInSimulatedBoard(BoardStructure boardStructure, Position position){
        ArrayList<Move> validMoves = new ArrayList<>();
        for(Position validPositionTo : boardStructure.findValidMoves(position)){
            validMoves.add(new Move(position, validPositionTo, MoveOwner.ENEMY, 0));
        }
        return validMoves;
    }

    private boolean checkIfMyPieceCanFinish(Move validMove){
        return checkIfMyPieceCanFinishInSimulatedBoard(super.getBoardStructure(), validMove);
    }

    private boolean checkIfMyPieceCanFinishInSimulatedBoard(BoardStructure boardStructure, Move validMove){
        PieceOwner currentPieceOwner = getTopPieceOwner(boardStructure, validMove.getPositionFrom());
        return validMove.getPositionTo().getY() == 1 && currentPieceOwner == PieceOwner.ENEMY;
    }

    private boolean checkIfPlayerPieceCanFinishInSimulatedBoard(BoardStructure boardStructure, Move validMove){
        PieceOwner currentPieceOwner = getTopPieceOwner(boardStructure, validMove.getPositionFrom());
        return validMove.getPositionTo().getY() == 1 && currentPieceOwner == PieceOwner.PLAYER;
    }

    private boolean checkIfBlocksPlayerFromFinish(Move move){
        PieceOwner currentPieceOwner = getTopPieceOwner(super.getBoardStructure(), move.getPositionFrom());
        PegStack pegStackToMoveFrom = super.getBoardStructure().getGameBoard().get(move.getPositionFrom());
        PegStack pegStackToMoveTo = super.getBoardStructure().getGameBoard().get(move.getPositionTo());
        if((move.getPositionFrom().getY() == 2 && currentPieceOwner == PieceOwner.PLAYER)){
            List<Move> possiblePlayerMoves = getValidMovesForPositionInSimulatedBoard(super.getBoardStructure(), move.getPositionFrom());
            boolean isWinningMove = false;
            int i = 0;
            while (!isWinningMove && i < possiblePlayerMoves.size()){
                if(possiblePlayerMoves.get(i).getPositionTo().getY() == 1){
                    isWinningMove = true;
                }
                i++;
            }

            if(isWinningMove && pegStackToMoveFrom.size() > pegStackToMoveTo.size()){
                return true;
            }
        }
        return false;
    }

    private boolean checkIfMyPieceCanMoveForward(Move validMove){
        return checkIfMyPieceCanMoveForwardInSimulatedBoard(super.getBoardStructure(), validMove);
    }

    private boolean checkIfMyPieceCanMoveForwardInSimulatedBoard(BoardStructure boardStructure, Move validMove){
        PieceOwner currentPieceOwner = getTopPieceOwner(boardStructure, validMove.getPositionFrom());
        return currentPieceOwner == PieceOwner.ENEMY && validMove.getPositionTo().getY() < validMove.getPositionFrom().getY();
    }

    private Move checkIfPlayerCanMoveForwardInSimulatedBoard(BoardStructure boardStructure, Move validMove){
        PieceOwner currentPieceOwner = getTopPieceOwner(boardStructure, validMove.getPositionFrom());
        if(currentPieceOwner == PieceOwner.PLAYER && validMove.getPositionTo().getY() < validMove.getPositionFrom().getY()){
            return validMove;
        }
        return null;
    }

    private int getPlayerPushDownValue(Move move){
        PieceOwner currentPieceOwner = getTopPieceOwner(super.getBoardStructure(), move.getPositionFrom());
        if(currentPieceOwner != PieceOwner.PLAYER || move.getPositionTo().getY() != move.getPositionFrom().getY())
            return 0;

        PegStack stackFrom = super.getBoardStructure().getGameBoard().get(move.getPositionFrom());
        PegStack stackTo = super.getBoardStructure().getGameBoard().get(move.getPositionTo());

        int pushDepth = stackFrom.size() - stackTo.size();
        int fromSize = stackFrom.size();
        return pushDepth * fromSize * 10;
    }

    // ------- SECOND MOVES --------

    private boolean checkIfMyPieceCanFinishNextMove(Move move){
        if(isFirstMoveInTurn()){
            BoardModel simulatedBoardModel = new BoardModel((BoardModel) super.getBoardStructure()); // make a copy of BoardModel to simulate the game multiple moves into the future
            simulatedBoardModel.applyMoveForced(move);
            for(Move validSecondMove : getValidMovesForPositionInSimulatedBoard(simulatedBoardModel, move.getPositionTo())){
                if(checkIfMyPieceCanFinishInSimulatedBoard(simulatedBoardModel, validSecondMove)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkIfMyPieceCanMoveForwardOnSecondMove(Move move){
        if(isFirstMoveInTurn()){
            BoardModel simulatedBoardModel = new BoardModel((BoardModel) super.getBoardStructure()); // make a copy of BoardModel to simulate the game multiple moves into the future
            simulatedBoardModel.applyMoveForced(move);
            for(Move validSecondMove : getValidMovesForPositionInSimulatedBoard(simulatedBoardModel, move.getPositionTo())){
                if(checkIfMyPieceCanMoveForwardInSimulatedBoard(simulatedBoardModel, validSecondMove)){
                    return true;
                }
            }
        }
        return false;
    }

    // ----- SECOND TURNS ------

    private boolean checkIfMyPieceCanFinishNextTurn(Move move){
        if(!isFirstMoveInTurn()){
            BoardModel simulated = new BoardModel((BoardModel) super.getBoardStructure());
            simulated.applyMoveForced(move);
            for(Move nextMove : getValidMovesForPositionInSimulatedBoard(simulated, move.getPositionTo())){
                if(checkIfMyPieceCanFinishInSimulatedBoard(simulated, nextMove)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkIfMyPieceCanMoveForwardNextTurn(Move move){
        if(!isFirstMoveInTurn()){
            BoardModel simulated = new BoardModel((BoardModel) super.getBoardStructure());
            simulated.applyMoveForced(move);
            for(Move nextMove : getValidMovesForPositionInSimulatedBoard(simulated, move.getPositionTo())){
                if(checkIfMyPieceCanMoveForwardInSimulatedBoard(simulated, nextMove)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isFirstMoveInTurn(){
        return super.getBoardStructure().getMoves().size() % 2 == 0;
    }

    private int evaluateMoveSafety(Move validMove){ // if your move will allow the player to finish the game, then return 0. If you will help player move to the next row, return 1. If you will not help the player, return 10
        BoardModel simulatedBoardModel = new BoardModel((BoardModel) super.getBoardStructure()); // make a copy of BoardModel to simulate the game multiple moves into the future
        simulatedBoardModel.applyMoveForced(validMove);
        for(Move validSecondMove : getValidMovesForPositionInSimulatedBoard(simulatedBoardModel, validMove.getPositionTo())){
            if(checkIfPlayerPieceCanFinishInSimulatedBoard(simulatedBoardModel, validSecondMove)){
                return 0;
            }
            if(checkIfPlayerCanMoveForwardInSimulatedBoard(simulatedBoardModel, validSecondMove) != null){
                return 1;
            }
        }
        return 10;
    }

    private PieceOwner getTopPieceOwner(BoardStructure board, Position pos){
        return board.getGameBoard().get(pos).getStack().getLast().getPieceOwner();
    }
}

package be.kdg.integration.gameapplication.model.gamematch.match.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class BoardModel implements BoardStructure{
    private Position selectedPosition = null;
    private HashMap<Position, PegStack> gameBoard = new HashMap();
    private Stack<Move> madeMoves = new Stack<>();
    private boolean isPlayersTurn = false;
    private ArrayList<Position> validPositions = new ArrayList<>();

    public BoardModel(){
        initializeBoard();
        refillPieces();
    }

    public BoardModel (BoardModel modelToBaseOn) {
        for (Map.Entry<Position, PegStack> entry : modelToBaseOn.gameBoard.entrySet()) {
            this.gameBoard.put(entry.getKey(), new PegStack(entry.getValue()));
        }

        this.selectedPosition = modelToBaseOn.selectedPosition;
        this.madeMoves.addAll(modelToBaseOn.madeMoves);
        this.isPlayersTurn = modelToBaseOn.isPlayersTurn;
        this.validPositions.addAll(modelToBaseOn.validPositions);
    }

    private void refillPieces(){
        for(int x = 1; x <= 4; x++){
            PegStack peg = gameBoard.get(new Position(x, 4));
            for(int i = 0; i < peg.length(); i++){
                PieceOwner pieceOwner = ((i + x) % 2 == 0) ? PieceOwner.ENEMY : PieceOwner.PLAYER;
                peg.push(new Piece(pieceOwner));
            }
        }
    }

    public void undoMove(){
        if(madeMoves.isEmpty()) return;
        if(madeMoves.size() == 2 && madeMoves.firstElement().getMoveOwner() == MoveOwner.ENEMY) return; // in case the bot makes the two moves first, you cannot undo those moves
        if(madeMoves.peek().getMoveOwner() == MoveOwner.PLAYER){
            reverseAndApplyMove(madeMoves.pop()); //passes the last move to reverse method
            return;
        }
        while(!madeMoves.isEmpty() && madeMoves.peek().getMoveOwner() == MoveOwner.ENEMY){
            reverseAndApplyMove(madeMoves.pop()); //passes the last move to reverse method
        }
        if(!madeMoves.isEmpty()) reverseAndApplyMove(madeMoves.pop()); //passes the last move to reverse method
    }

    public void reverseAndApplyMove(Move move){
        Position posFrom = move.getPositionFrom();
        Position posTo = move.getPositionTo();
        PegStack pegFrom = this.gameBoard.get(posFrom);
        PegStack pegTo = this.gameBoard.get(posTo);
        if(pegFrom == null || pegTo == null) return;
        pegFrom.push(pegTo.pop()); //takes the last move which is passed, and then apply it reversely on pegs
    }

    private void initializeBoard(){
        for(int y = 1; y <= 4; y++){
            if(y <= 2){
                for(int x = 1; x <= 5; x++){
                    gameBoard.put(new Position(x, y), new PegStack(y));
                }
            }else{
                for(int x = 1; x <= 4; x++){
                    gameBoard.put(new Position(x, y), new PegStack(y));
                }
            }
        }
    }

    public void setSelectedPosition(Position position) throws EmptyPegException{
        if(position != null){
            if(gameBoard.get(position).isEmpty()){
                throw new EmptyPegException();
            }
            this.selectedPosition = position;
        }
    }

    public Move createMove(Position posTo, MoveOwner moveOwner, float timePerMove){
        int x = selectedPosition.getX();
        int y = selectedPosition.getY();
        Position posFrom = new Position(x, y);
        return new Move(posFrom, posTo, moveOwner, timePerMove);
    }

    public void applyMoveForced(Move move){   //for the replay and undo
        Position posFrom = move.getPositionFrom();
        Position posTo = move.getPositionTo();
        PegStack pegFrom = this.gameBoard.get(posFrom);
        PegStack pegTo = this.gameBoard.get(posTo);
        if(pegFrom == null || pegTo == null) return;
        pegTo.push(pegFrom.pop());
        madeMoves.add(move);
    }

    public void applyMove(Move move){
        Position posFrom = move.getPositionFrom();
        Position posTo = move.getPositionTo();
        PegStack pegFrom = this.gameBoard.get(posFrom);
        PegStack pegTo = this.gameBoard.get(posTo);
        if(pegFrom == null || pegTo == null) return;
        if(validPositions.contains(posTo)){
            pegTo.push(pegFrom.pop());
            madeMoves.add(move);
        }else if(move.getMoveOwner() == MoveOwner.ENEMY){
            pegTo.push(pegFrom.pop());
            madeMoves.add(move);
        }
    }

        //The same method but just for ai
    public ArrayList<Position> findValidMoves(Position selected){
        PegStack selectedPeg = this.gameBoard.get(selected);
        ArrayList<Position> validOnes =  new ArrayList<>();

        if(selectedPeg == null || selectedPeg.isEmpty() || selected.getY() == 1){
            return validOnes; //or could be null
        }


        if(selected.getY() == 3){
            int[][] shifts = {{-1, 0}, {1, 0}, {1, -1}, {0, -1}};

            for(int[] shift : shifts){
                int xS = shift[0];
                int yS = shift[1];
                int xShifted = selected.getX() + xS;
                int yShifted = selected.getY() + yS;

                Position positionToCheck = new Position(xShifted, yShifted);
                PegStack pegToCheck = this.gameBoard.get(positionToCheck);

                if(pegToCheck != null && yS != 0 && selectedPeg.hasPieceOnTop() && !pegToCheck.hasPieceOnTop()){
                    validOnes.add(positionToCheck);
                }else if(pegToCheck != null && yS == 0 && !pegToCheck.hasPieceOnTop()){
                    validOnes.add(positionToCheck);
                }
            }
        }else{
            for(int xShift = -1; xShift <= 1; xShift++){
                int yShift = xShift != 0 ? 0 : -1;
                Position position = new Position(selected.getX() + xShift, selected.getY() + yShift);
                PegStack pegToCheck = this.gameBoard.get(position);
                if(pegToCheck != null && yShift != 0 && selectedPeg.hasPieceOnTop() && !pegToCheck.hasPieceOnTop()){
                    validOnes.add(position);
                }else if(pegToCheck != null && yShift == 0 && !pegToCheck.hasPieceOnTop()){
                    validOnes.add(position);
                }
            }
        }
        return validOnes;
    }


    //show valid moves could be saved locally in the presenter and then be passed as an attribute to the applyMove
    public ArrayList<Position> findValidMoves(){
        //click will create new Position objects by the presenter. He will highlight valid ones.
        //if person would click on an any peg it will check if that position of that peg is
        //on the list of valid positions. If yes than applyMove gonna be performed. If not
        //then it will show the error or smth like that.

        PegStack selectedPeg = this.gameBoard.get(selectedPosition);
        this.validPositions = new ArrayList<>();


        if(selectedPeg == null || selectedPeg.isEmpty() || selectedPosition.getY()==1){
            return validPositions; //or could be null
        }

        if(selectedPosition.getY() == 3){
            // 0 0 0 0 0
            // 0 0 0 0 0
            //  0 P 0 0
            //  0 0 0 0
            // So in order to move from position P,
            //   I need  [x-1,y], [x+1, y],[x+1, y-1][x-1,y-1], [x,y-1]

            int[][] shifts = {{-1, 0}, {1, 0}, {1, -1}, {0, -1}};

            for(int[] shift : shifts){
                int xS = shift[0];
                int yS = shift[1];
                int xShifted = selectedPosition.getX() + xS;
                int yShifted = selectedPosition.getY() + yS;
                Position position;
                position = new Position(xShifted, yShifted);
                PegStack pegToCheck = this.gameBoard.get(position);


                if(pegToCheck != null && yS != 0 && selectedPeg.hasPieceOnTop() && !pegToCheck.hasPieceOnTop()){
                    validPositions.add(position);
                }else if(pegToCheck != null && yS == 0 && !pegToCheck.hasPieceOnTop()){
                    validPositions.add(position);
                }

            }
        }else{
            for(int xShift = -1; xShift <= 1; xShift++){
                //                                  xShift = -1 means that we look to the left
                //                                  xShift = +1 means that we look to the right
                Position position;
                int yShift = xShift != 0 ? 0 : -1;   //xShift = 0 but getY() - 1 means that we look forward
                position = new Position(selectedPosition.getX() + xShift, selectedPosition.getY() + yShift);
                PegStack pegToCheck = this.gameBoard.get(position);

                //short-circuit check prevents it from null pointer exception
                //                                after it will check if there is a piece no piece on top of the peg
                if(pegToCheck != null && yShift != 0 && selectedPeg.hasPieceOnTop() && !pegToCheck.hasPieceOnTop()){
                    validPositions.add(position);
                }else if(pegToCheck != null && yShift == 0 && !pegToCheck.hasPieceOnTop()){
                    validPositions.add(position);
                }
            }
        }
        return validPositions;
    }

    public boolean isGameReadyToBeFinished(){
        for(int i = 1; i <= 5; i++){
            if(gameBoard.get(new Position(i, 1)).isEmpty()){
                return false;
            }
        }
        return true;
    }

    public boolean getPlayerIndicatorStatus(){
        return isPlayersTurn;
    }

    @Override
    public HashMap<Position, PegStack> getGameBoard(){
        return gameBoard;
    }

    @Override
    public Stack<Move> getMoves(){
        return this.madeMoves;
    }

    public MoveOwner getFirstMoveOwner(){
        if (!madeMoves.isEmpty()){
            return madeMoves.getFirst().getMoveOwner();
        }
        return null;
    }
}

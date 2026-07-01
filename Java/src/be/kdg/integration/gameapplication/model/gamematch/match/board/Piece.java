package be.kdg.integration.gameapplication.model.gamematch.match.board;

public class Piece{
    private PieceOwner pieceOwner;

    public Piece(PieceOwner pieceOwner){
        this.pieceOwner = pieceOwner;
    }

    public PieceOwner getPieceOwner(){
        return pieceOwner;
    }

}

package games.draughts.piece;

import enums.Side;
import model.game.gamefield.Gamefield;
import model.game.piece.Piece;
import model.game.piece.Pieces;
import model.game.position.Position;

import java.util.ArrayList;
import java.util.List;

public class DraughtsPieces extends Pieces {

    public DraughtsPieces(Gamefield gamefield) {
        super(gamefield);
    }

    public DraughtsPieces(Gamefield gamefield, Pieces inputPieces) {
        super(gamefield, inputPieces);
        gamefield.setPieces(this);
    }

    protected void initializePieces(Gamefield gamefield) {
        pieces = new ArrayList<Piece>();
        int size = gamefield.getSize();
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                if ((row + column) % 2 != 0) {
                    if (row < 3) {
                        pieces.add(new Man(new Position(row, column), Side.BLACK, gamefield));
                    }

                    if (row > 4) {
                        pieces.add(new Man(new Position(row, column), Side.WHITE, gamefield));
                    }
                }
            }
        }
        addPiecesToGameField(gamefield);
    }

    protected void clonePiecesToGamefield(Gamefield gamefield, Pieces inputPieces) {
        pieces = new ArrayList<Piece>();
        for (Piece piece : inputPieces.getPieces()) {
            if (piece instanceof Man) {
                pieces.add(new Man(piece.getPosition(), piece.getSide(), gamefield));
            } else {
                pieces.add(new King(piece.getPosition(), piece.getSide(), gamefield));
            }
        }
        addPiecesToGameField(gamefield);
    }

    public List<DraughtsPiece> getPiecesAbleToCapture(Side side) {
        List<DraughtsPiece> pieceList = new ArrayList<DraughtsPiece>();
        for (Piece piece : pieces) {
            DraughtsPiece draughtsPiece = (DraughtsPiece) piece;
            if (draughtsPiece.getSide() == side && draughtsPiece.isAbleToCapture()) {
                pieceList.add(draughtsPiece);
            }
        }
        return pieceList;
    }

    public List<DraughtsPiece> getPiecesAbleToMove(Side side) {
        List<DraughtsPiece> pieceList = new ArrayList<DraughtsPiece>();
        for (Piece piece : pieces) {
            DraughtsPiece draughtsPiece = (DraughtsPiece) piece;
            if (draughtsPiece.getSide() == side && draughtsPiece.isAbleToMove()) {
                pieceList.add(draughtsPiece);
            }
        }
        return pieceList;
    }
}

package model.game.piece;

import model.game.gamefield.Gamefield;
import model.game.gamefield.ModelCell;

import java.util.List;

public abstract class Pieces {

    protected List<Piece> pieces;

    public Pieces(Gamefield gamefield) {
        initializePieces(gamefield);
    }

    protected abstract void initializePieces(Gamefield gamefield);

    protected void addPiecesToGameField(Gamefield gamefield) {
        for (Piece piece : pieces) {
            ModelCell cell = gamefield.getCell(piece.getPosition());
            cell.setPiece(piece);
        }
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public void add(Piece piece) {
        pieces.add(piece);
    }

    public void remove(Piece piece) {
        pieces.remove(piece);
    }

}

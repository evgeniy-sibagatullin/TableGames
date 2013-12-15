package model.game.piece;

import model.game.gamefield.Gamefield;
import model.game.gamefield.ModelCell;
import model.game.position.Position;

import java.util.List;

public abstract class PieceSet {

    protected List<Piece> pieces;
    protected Gamefield gamefield;

    public PieceSet(Gamefield gamefield) {
        setGamefield(gamefield);
        initializePieces();
    }

    public PieceSet(PieceSet pieceSet) {
        setGamefield(pieceSet.getGamefield());
        gamefield.removePieces();
        clonePiecesToGamefield(pieceSet);
        gamefield.setPieceSet(this);
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

    public void setGamefield(Gamefield gamefield) {
        this.gamefield = gamefield;
    }

    public Gamefield getGamefield() {
        return gamefield;
    }

    public void applyPiecesToGamefield() {
        gamefield.removePieces();
        addPiecesToGameField();
        gamefield.setPieceSet(this);
    }

    public Piece getPieceByPosition(Position position) {
        for (Piece piece : pieces) {
            if (piece.getPosition().equals(position)) {
                return piece;
            }
        }
        return null;
    }

    protected abstract void initializePieces();

    protected void clonePiecesToGamefield(PieceSet pieceSet) {
    }

    protected void addPiecesToGameField() {
        for (Piece piece : pieces) {
            ModelCell cell = gamefield.getCell(piece.getPosition());
            cell.setPiece(piece);
        }
    }
}

package org.javatablegames.core.model.game.piece;

import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.game.gamefield.ModelCell;
import org.javatablegames.core.model.position.Position;

import java.util.List;

public abstract class PieceSet {

    protected List<Piece> pieces;
    protected Gamefield gamefield;

    protected PieceSet(Gamefield gamefield) {
        setGamefield(gamefield);
        initializePieces();
        gamefield.setPieceSet(this);
    }

    protected PieceSet(PieceSet pieceSet) {
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

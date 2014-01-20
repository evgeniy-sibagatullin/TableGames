package org.javatablegames.core.model.game.piece;

import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.game.gamefield.ModelCell;
import org.javatablegames.core.model.position.Position;

import java.util.Set;

public abstract class PieceSet<T extends Piece> {

    protected Set<T> pieces;
    protected Gamefield<T> gamefield;

    protected PieceSet(Gamefield<T> gamefield) {
        setGamefield(gamefield);
        initializePieces();
        gamefield.setPieceSet(this);
    }

    protected PieceSet(PieceSet<T> pieceSet) {
        setGamefield(pieceSet.getGamefield());
        gamefield.removePieces();

        try {
            clonePiecesToGamefield(pieceSet);
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }

        gamefield.setPieceSet(this);
    }

    public Set<T> getPieces() {
        return pieces;
    }

    public void add(T piece) {
        pieces.add(piece);
    }

    public void remove(T piece) {
        pieces.remove(piece);
    }

    public Gamefield<T> getGamefield() {
        return gamefield;
    }

    public void setGamefield(Gamefield<T> gamefield) {
        this.gamefield = gamefield;
    }

    public void applyPiecesToGamefield() {
        gamefield.removePieces();
        addPiecesToGameField();
        gamefield.setPieceSet(this);
    }

    public T getPieceByPosition(Position position) {
        for (T piece : pieces) {
            if (piece.getPosition().equals(position)) {
                return piece;
            }
        }

        return null;
    }

    protected abstract void initializePieces();

    protected abstract void clonePiecesToGamefield(PieceSet<T> pieceSet) throws CloneNotSupportedException;

    protected void addPiecesToGameField() {
        for (T piece : pieces) {
            ModelCell cell = gamefield.getCell(piece.getPosition());
            cell.setPiece(piece);
        }
    }

}

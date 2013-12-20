package org.javatablegames.core.model.game.gamefield;

import org.javatablegames.core.model.game.piece.Piece;
import org.javatablegames.core.model.game.piece.PieceSet;
import org.javatablegames.core.model.position.Position;

public abstract class Gamefield {

    protected ModelCell[][] field;
    protected int size;
    protected PieceSet pieceSet;

    public Gamefield() {
        initializeGamefield();
    }

    protected abstract void initializeGamefield();

    protected ModelCell[][] getField() {
        return field;
    }

    public int getSize() {
        return size;
    }

    protected void setCell(ModelCell modelCell, Position position) {
        field[position.getRow()][position.getColumn()] = modelCell;
    }

    public ModelCell getCell(Position position) {
        return field[position.getRow()][position.getColumn()];
    }

    public Piece getPiece(Position position) {
        return field[position.getRow()][position.getColumn()].getPiece();
    }

    public void setPieceSet(PieceSet pieceSet) {
        this.pieceSet = pieceSet;
    }

    public void removePieces() {
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                field[row][column].setPiece(null);
            }
        }
    }

}

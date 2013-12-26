package org.javatablegames.core.model.game.gamefield;

import org.javatablegames.core.enums.CellState;
import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.game.piece.Piece;
import org.javatablegames.core.model.game.piece.PieceSet;
import org.javatablegames.core.model.position.Position;

import java.util.List;

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

    public void setTotalCellStateDefault() {
        for (ModelCell[] gameFieldRow : getField()) {
            for (int column = 0; column < getSize(); column++) {
                gameFieldRow[column].updateCellState(CellState.DEFAULT);
            }
        }
    }

    public boolean isCellEmpty(Position position) {
        return position.isValid(size) && getCell(position).getPiece() == null;
    }

    public boolean isCellOpponent(Position position, Side side) {
        if (position.isValid(getSize())) {
            Piece gamePiece = getPiece(position);
            return (gamePiece != null && gamePiece.getSide() != side);
        }
        return false;
    }

    public void updatePiecesReadyToMove(List<? extends Piece> pieceList) {
        for (Piece piece : pieceList) {
            ModelCell modelCell = getCell(piece.getPosition());
            if (modelCell.getCellState() != CellState.SELECTED) {
                modelCell.updateCellState(CellState.ALLOWED_PIECE);
            }
        }
    }

}

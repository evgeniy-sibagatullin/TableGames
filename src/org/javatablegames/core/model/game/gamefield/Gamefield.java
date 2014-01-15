package org.javatablegames.core.model.game.gamefield;

import org.javatablegames.core.enums.CellState;
import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.game.piece.Piece;
import org.javatablegames.core.model.game.piece.PieceSet;
import org.javatablegames.core.model.position.Position;

import java.util.List;

public abstract class Gamefield<T extends Piece> {

    protected ModelCell[][] field;
    protected int size;
    protected PieceSet<T> pieceSet;
    protected ModelCell selectedCell;

    public Gamefield() {
        initializeGamefield();
    }

    protected abstract void initializeGamefield();

    protected ModelCell[][] getField() {
        return field;
    }

    protected void setCell(ModelCell modelCell, Position position) {
        field[position.getRow()][position.getColumn()] = modelCell;
    }

    public int getSize() {
        return size;
    }

    public ModelCell getCell(Position position) {
        return field[position.getRow()][position.getColumn()];
    }

    public T getPiece(Position position) {
        return (T) field[position.getRow()][position.getColumn()].getPiece();
    }

    public T getSelectedPiece() {
        Position position = selectedCell.getPosition();
        return (T) field[position.getRow()][position.getColumn()].getPiece();
    }

    public void setPieceSet(PieceSet<T> pieceSet) {
        this.pieceSet = pieceSet;
    }

    public ModelCell getSelectedCell() {
        return selectedCell;
    }

    public void setSelectedCellByPosition(Position position) {
        selectedCell = getCell(position);
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
            Piece piece = getPiece(position);
            return (piece != null && piece.getSide() != side);
        }
        return false;
    }

    public boolean isCellUnderAttack(Position position, Side side) {
        return position == null;
    }

    public void updatePiecesReadyToMove(List<T> pieceList) {
        for (T piece : pieceList) {
            ModelCell modelCell = getCell(piece.getPosition());
            if (modelCell.getCellState() != CellState.SELECTED) {
                modelCell.updateCellState(CellState.ALLOWED_PIECE);
            }
        }
    }

}

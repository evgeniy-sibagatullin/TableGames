package model.game.gamefield;

import model.game.piece.Piece;
import model.game.piece.PieceSet;
import model.game.position.Position;

public abstract class Gamefield {

    protected ModelCell[][] field;
    protected int size;
    protected PieceSet pieceSet;

    public Gamefield() {
        initializeGamefield();
    }

    protected abstract void initializeGamefield();

    public ModelCell[][] getField() {
        return field;
    }

    public int getSize() {
        return size;
    }

    public void setCell(ModelCell modelCell, Position position) {
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

package model.game.gamefield;

import model.game.piece.Piece;
import model.game.piece.Pieces;
import model.game.position.Position;

public abstract class Gamefield {

    protected ModelCell[][] field;
    protected int size;
    protected Pieces pieces;

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

    public void setPieces(Pieces pieces) {
        this.pieces = pieces;
    }
}

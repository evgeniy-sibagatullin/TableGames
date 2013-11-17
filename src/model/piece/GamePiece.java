package model.piece;

import enums.Side;
import model.ModelCell;

public abstract class GamePiece implements Piece {

    private int row;
    private int column;
    private Side side;
    protected ModelCell[][] gameField;

    public GamePiece(int row, int column, Side side, ModelCell[][] gameField) {
        setRow(row);
        setColumn(column);
        setSide(side);
        setGameField(gameField);
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public Side getSide() {
        return side;
    }

    @Override
    public void setSide(Side side) {
        this.side = side;
    }

    @Override
    public void setGameField(ModelCell[][] gameField) {
        this.gameField = gameField;
    }

    public abstract String getImagePath();

}

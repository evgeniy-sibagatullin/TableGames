package model.piece;

import enums.Side;

public abstract class GamePiece implements Piece {

    private int row;
    private int column;
    private Side side;

    public GamePiece(int row, int column, Side side) {
        setRow(row);
        setColumn(column);
        setSide(side);
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

    public abstract String getImagePath();

}

package model;

import enums.CellState;
import model.piece.Piece;

public class ModelCell {

    private CellState cellState = CellState.DEFAULT;
    private int row;
    private int column;
    private String backgroundImage;
    private int power;
    private boolean changed;
    private Piece piece;

    public ModelCell(int row, int column, String color, Piece piece) {
        this(row, column, 0, color, piece, CellState.DEFAULT);
    }

    public ModelCell(int row, int column, int power, String color,
                     Piece piece, CellState cellState) {
        setRow(row);
        setColumn(column);
        setPower(power);
        setBackgroundImage(color);
        setPiece(piece);
        setCellState(cellState);
        setChanged(true);
    }

    public CellState getCellState() {
        return cellState;
    }

    public void setCellState(CellState cellState) {
        this.cellState = cellState;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public boolean getChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}

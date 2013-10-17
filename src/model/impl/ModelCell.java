package model.impl;

import enums.CellState;

public class ModelCell {

    private CellState cellState = CellState.DEFAULT;
    private int row;
    private int column;
    private String backgroundImage;
    private String pieceImage;
    private int power;
    private boolean changed;

    public ModelCell(int row, int column, String color, String piece) {
        this(row, column, 0, color, piece, CellState.DEFAULT);
    }

    public ModelCell(int row, int column, int power, String color,
                     String piece, CellState cellState) {
        setRow(row);
        setColumn(column);
        setPower(power);
        setBackgroundImage(color);
        setPieceImage(piece);
        setCellState(cellState);
        setChanged(false);
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

    public String getPieceImage() {
        return pieceImage;
    }

    public void setPieceImage(String pieceImage) {
        this.pieceImage = pieceImage;
    }

    public boolean getChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}

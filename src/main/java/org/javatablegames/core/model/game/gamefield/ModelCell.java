package org.javatablegames.core.model.game.gamefield;

import org.javatablegames.core.enums.CellState;
import org.javatablegames.core.model.game.piece.Piece;
import org.javatablegames.core.model.position.Position;

public class ModelCell {

    private CellState cellState = CellState.DEFAULT;
    private Position position;
    private String backgroundImage;
    private int power;
    private boolean changed;
    private Piece piece;

    public ModelCell(Position position, int power, String color,
                     CellState cellState) {
        setPosition(position);
        setPower(power);
        setBackgroundImage(color);
        setPiece(null);
        setCellState(cellState);
        setChanged(true);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    protected void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public CellState getCellState() {
        return cellState;
    }

    public void setCellState(CellState cellState) {
        this.cellState = cellState;
    }

    public boolean getChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public void updateCellState(CellState cellState) {
        setCellState(cellState);
        setChanged(true);
    }

}

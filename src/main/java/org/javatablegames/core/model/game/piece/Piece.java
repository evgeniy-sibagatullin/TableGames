package org.javatablegames.core.model.game.piece;

import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.position.Position;

public abstract class Piece implements Cloneable {

    protected Position position;
    protected Side side;
    protected Gamefield gamefield;
    protected int power;

    protected Piece(Position position, Side side, int power, Gamefield gameField) {
        setPosition(position);
        setSide(side);
        setPower(power);
        setGameField(gameField);
    }

    public abstract String getImagePath();

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public int getPower() {
        return power;
    }

    @Override
    public Piece clone() throws CloneNotSupportedException {
        return (Piece) super.clone();
    }

    protected void setPower(int power) {
        this.power = power;
    }

    private void setGameField(Gamefield gameField) {
        this.gamefield = gameField;
    }

}

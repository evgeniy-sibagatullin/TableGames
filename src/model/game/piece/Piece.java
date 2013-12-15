package model.game.piece;

import enums.Side;
import model.game.gamefield.Gamefield;
import model.game.position.Position;

public abstract class Piece {

    protected Position position;
    protected Side side;
    protected Gamefield gamefield;
    protected int power;

    public Piece(Position position, Side side, Gamefield gameField) {
        this(position, side, 0, gameField);
    }

    public Piece(Position position, Side side, int power, Gamefield gameField) {
        setPosition(position);
        setSide(side);
        setPower(power);
        setGameField(gameField);
    }

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

    public void setGameField(Gamefield gameField) {
        this.gamefield = gameField;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public abstract String getImagePath();
}

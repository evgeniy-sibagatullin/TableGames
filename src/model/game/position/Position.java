package model.game.position;

import enums.Direction;

public class Position {

    private int row;
    private int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Position(Position position) {
        this.row = position.row;
        this.column = position.column;
    }

    public void setPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public void setPosition(Position position) {
        this.row = position.row;
        this.column = position.column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void moveInDirection(Direction direction) {
        this.row += direction.getDeltaY();
        this.column += direction.getDeltaX();
    }

    public boolean isValid(int gamefieldSize) {
        return (row >= 0 && row < gamefieldSize && column >= 0 && column < gamefieldSize);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof Position)) {
            return false;
        } else {
            Position position = (Position) object;
            return (position.getRow() == this.row && position.getColumn() == this.column);
        }
    }
}

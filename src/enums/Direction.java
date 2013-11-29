package enums;

public enum Direction {
    NORTHWEST(-1, -1),
    NORTH(-1, 0),
    NORTHEAST(-1, 1),
    WEST(0, -1),
    EAST(0, 1),
    SOUTHWEST(1, -1),
    SOUTH(1, 0),
    SOUTHEAST(1, 1);

    private final int deltaY;
    private final int deltaX;

    private Direction(int deltaRow, int deltaColumn) {
        this.deltaY = deltaRow;
        this.deltaX = deltaColumn;
    }

    public int getDeltaY() {
        return this.deltaY;
    }

    public int getDeltaX() {
        return this.deltaX;
    }
}
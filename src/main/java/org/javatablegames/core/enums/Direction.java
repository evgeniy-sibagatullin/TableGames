package org.javatablegames.core.enums;

public enum Direction {

    NORTHWEST(-1, -1),
    NORTH(-1, 0),
    NORTHEAST(-1, 1),
    WEST(0, -1),
    EAST(0, 1),
    SOUTHWEST(1, -1),
    SOUTH(1, 0),
    SOUTHEAST(1, 1);

    public static final Direction[] directions =
            {NORTHWEST, NORTH, NORTHEAST, WEST, EAST, SOUTHWEST, SOUTH, SOUTHEAST};

    private final int deltaY;
    private final int deltaX;

    private Direction(int deltaY, int deltaX) {
        this.deltaY = deltaY;
        this.deltaX = deltaX;
    }

    public int getDeltaY() {
        return this.deltaY;
    }

    public int getDeltaX() {
        return this.deltaX;
    }

    public static Direction getDirection(int deltaY, int deltaX) {
        for (Direction direction : directions) {
            if (direction.deltaY == Math.signum(deltaY) && direction.deltaX == Math.signum(deltaX)) {
                return direction;
            }
        }
        return null;
    }

}
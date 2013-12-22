package org.javatablegames.core.enums;

public enum Side {

    WHITE, BLACK;

    public static Side oppositeSide(Side side) {
        return (side.equals(WHITE)) ? BLACK : WHITE;
    }
}

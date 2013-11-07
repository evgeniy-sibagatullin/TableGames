package games.chess.piece;

import enums.Side;

public class Knight extends ChessPiece {

    public Knight(int row, int column, Side side) {
        super(row, column, side);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/chess/Chess-KnightW.png" :
                "img/chess/Chess-KnightB.png";
    }

    @Override
    public int getPower() {
        return 2;
    }
}

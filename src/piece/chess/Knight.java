package piece.chess;

import enums.Side;
import piece.GamePiece;

public class Knight extends GamePiece {

    public Knight(int row, int column, Side side) {
        super(row, column, side);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/Chess-KnightW.png" :
                "img/Chess-KnightB.png";
    }

}

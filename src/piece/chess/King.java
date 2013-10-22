package piece.chess;

import enums.Side;
import piece.GamePiece;

public class King extends GamePiece {

    public King(int row, int column, Side side) {
        super(row, column, side);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/Chess-KingW.png" :
                "img/Chess-KingB.png";
    }

}

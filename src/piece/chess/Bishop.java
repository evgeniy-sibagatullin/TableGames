package piece.chess;

import enums.Side;
import piece.GamePiece;

public class Bishop extends GamePiece {

    public Bishop(int row, int column, Side side) {
        super(row, column, side);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/Chess-BishopW.png" :
                "img/Chess-BishopB.png";
    }

}

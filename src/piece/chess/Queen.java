package piece.chess;

import enums.Side;
import piece.GamePiece;

public class Queen extends GamePiece {

    public Queen(int row, int column, Side side) {
        super(row, column, side);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/Chess-QueenW.png" :
                "img/Chess-QueenB.png";
    }

}

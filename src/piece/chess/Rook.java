package piece.chess;

import enums.Side;
import piece.GamePiece;

public class Rook extends GamePiece {

    public Rook(int row, int column, Side side) {
        super(row, column, side);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/Chess-RookW.png" :
                "img/Chess-RookB.png";
    }

}

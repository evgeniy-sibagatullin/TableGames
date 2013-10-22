package piece.chess;

import enums.Side;
import piece.GamePiece;

public class Pawn extends GamePiece {

    public Pawn(int row, int column, Side side) {
        super(row, column, side);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/Chess-PawnW.png" :
                "img/Chess-PawnB.png";
    }

}

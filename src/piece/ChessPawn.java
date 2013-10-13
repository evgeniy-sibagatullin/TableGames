package piece;

import enums.Side;

public class ChessPawn extends GamePiece {

    public ChessPawn(int row, int column, Side side) {
        super(row, column, side);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/Chess-PawnW.png" :
                "img/Chess-PawnB.png";
    }

}

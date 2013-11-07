package games.chess.piece;

import enums.Side;

public class King extends ChessPiece {

    public King(int row, int column, Side side) {
        super(row, column, side);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/chess/Chess-KingW.png" :
                "img/chess/Chess-KingB.png";
    }

    @Override
    public int getPower() {
        return 6;
    }

}

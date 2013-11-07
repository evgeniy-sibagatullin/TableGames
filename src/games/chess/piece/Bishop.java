package games.chess.piece;

import enums.Side;

public class Bishop extends ChessPiece {

    public Bishop(int row, int column, Side side) {
        super(row, column, side);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/chess/Chess-BishopW.png" :
                "img/chess/Chess-BishopB.png";
    }

    @Override
    public int getPower() {
        return 3;
    }

}

package games.chess.piece;

import enums.Side;

public class Pawn extends ChessPiece {

    public Pawn(int row, int column, Side side) {
        super(row, column, side);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/chess/Chess-PawnW.png" :
                "img/chess/Chess-PawnB.png";
    }

    @Override
    public int getPower() {
        return 1;
    }

}

package games.chess.piece;

import enums.Side;

public class Queen extends ChessPiece {

    public Queen(int row, int column, Side side) {
        super(row, column, side);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/chess/Chess-QueenW.png" :
                "img/chess/Chess-QueenB.png";
    }

    @Override
    public int getPower() {
        return 5;
    }
}

package games.chess.piece;

import enums.Side;

public class Rook extends ChessPiece {

    public Rook(int row, int column, Side side) {
        super(row, column, side);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/chess/Chess-RookW.png" :
                "img/chess/Chess-RookB.png";
    }

    @Override
    public int getPower() {
        return 4;
    }
}

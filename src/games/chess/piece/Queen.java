package games.chess.piece;

import enums.Side;
import model.piece.GamePiece;

public class Queen extends GamePiece {

    public Queen(int row, int column, Side side) {
        super(row, column, side);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/chess/Chess-QueenW.png" :
                "img/chess/Chess-QueenB.png";
    }

}

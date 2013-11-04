package games.chess.piece;

import enums.Side;
import model.piece.GamePiece;

public class Knight extends GamePiece {

    public Knight(int row, int column, Side side) {
        super(row, column, side);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/chess/Chess-KnightW.png" :
                "img/chess/Chess-KnightB.png";
    }

}

package games.chess.piece;

import enums.Side;
import model.piece.GamePiece;

public class King extends GamePiece {

    public King(int row, int column, Side side) {
        super(row, column, side);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/chess/Chess-KingW.png" :
                "img/chess/Chess-KingB.png";
    }

}

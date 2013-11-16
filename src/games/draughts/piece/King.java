package games.draughts.piece;

import enums.Side;
import model.piece.GamePiece;

public class King extends GamePiece {

    public King(int row, int column, Side side) {
        super(row, column, side);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/draughts/Draughts-KingB.png" :
                "img/draughts/Draughts-KingR.png";
    }
}

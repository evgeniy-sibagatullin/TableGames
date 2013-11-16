package games.draughts.piece;

import enums.Side;
import model.piece.GamePiece;

public class Man extends GamePiece {

    public Man(int row, int column, Side side) {
        super(row, column, side);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/draughts/Draughts-ManB.png" :
                "img/draughts/Draughts-ManR.png";
    }
}

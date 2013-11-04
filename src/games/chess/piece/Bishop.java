package games.chess.piece;

import enums.Side;
import model.piece.GamePiece;

public class Bishop extends GamePiece {

    public Bishop(int row, int column, Side side) {
        super(row, column, side);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/chess/Chess-BishopW.png" :
                "img/chess/Chess-BishopB.png";
    }

}

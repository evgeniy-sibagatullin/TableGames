package games.chess.piece;

import enums.Side;
import model.piece.GamePiece;

public class Pawn extends GamePiece {

    public Pawn(int row, int column, Side side) {
        super(row, column, side);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/chess/Chess-PawnW.png" :
                "img/chess/Chess-PawnB.png";
    }

}

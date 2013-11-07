package games.chess.piece;

import enums.Side;
import model.piece.GamePiece;

public abstract class ChessPiece extends GamePiece {

    public ChessPiece(int row, int column, Side side) {
        super(row, column, side);
    }

    public abstract int getPower();

}

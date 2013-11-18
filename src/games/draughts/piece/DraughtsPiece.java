package games.draughts.piece;

import enums.Side;
import model.ModelCell;
import model.piece.GamePiece;

public abstract class DraughtsPiece extends GamePiece {

    public DraughtsPiece(int row, int column, Side side, ModelCell[][] gameField) {
        super(row, column, side, gameField);
    }

    public abstract boolean isAbleToCapture(Side side);

    public abstract boolean isAbleToMove(Side side);
}

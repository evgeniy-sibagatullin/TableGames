package games.draughts.piece;

import enums.Side;
import model.ModelCell;
import model.piece.GamePiece;

import java.util.List;

public abstract class DraughtsPiece extends GamePiece {

    public DraughtsPiece(int row, int column, Side side, ModelCell[][] gameField) {
        super(row, column, side, gameField);
    }

    protected boolean isValidPosition(int row, int column) {
        return (row >= 0 && row < gameField.length && column >= 0 && column < gameField.length);
    }

    public abstract boolean isAbleToCapture();

    public abstract boolean isAbleToMove();

    public abstract List<ModelCell> getCellsAllowedToMoveIn();
}

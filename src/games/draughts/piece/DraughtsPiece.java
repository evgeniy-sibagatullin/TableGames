package games.draughts.piece;

import enums.Side;
import model.ModelCell;
import model.piece.GamePiece;
import model.piece.Piece;

import java.util.List;

public abstract class DraughtsPiece extends GamePiece {

    protected List<ModelCell> cellsAllowedToMoveIn;
    protected List<ModelCell> cellsAllowedToCaptureIn;

    public DraughtsPiece(int row, int column, Side side, ModelCell[][] gameField) {
        super(row, column, side, gameField);
    }

    protected boolean isValidPosition(int row, int column) {
        return (row >= 0 && row < gameField.length && column >= 0 && column < gameField.length);
    }

    protected boolean isCellEmpty(int row, int column) {
        return (isValidPosition(row, column) && gameField[row][column].getPiece() == null);
    }

    protected boolean isCellOpponent(int row, int column) {
        if (isValidPosition(row, column)) {
            Piece gamePiece = gameField[row][column].getPiece();
            return (gamePiece != null && gamePiece.getSide() != side);
        }
        return false;
    }

    public abstract boolean isAbleToCapture();

    public abstract List<ModelCell> getCellsAllowedToCapture();

    public abstract boolean isAbleToMove();

    public abstract List<ModelCell> getCellsAllowedToMoveIn();

    public boolean isAbleToBecomeKing() {
        return (this instanceof Man && ((side == Side.WHITE && row == 0) || (side == Side.BLACK && row == 7)));
    }
}

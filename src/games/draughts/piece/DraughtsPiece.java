package games.draughts.piece;

import enums.Direction;
import enums.Side;
import model.ModelCell;
import model.piece.GamePiece;
import model.piece.Piece;

import java.util.ArrayList;
import java.util.List;

public abstract class DraughtsPiece extends GamePiece {

    protected List<ModelCell> cellsAllowedToMoveIn;
    protected List<ModelCell> cellsAllowedToCaptureIn;

    private int deltaX;
    private int deltaY;
    private int checkRow;
    private int checkColumn;

    public DraughtsPiece(int row, int column, Side side, ModelCell[][] gameField) {
        super(row, column, side, gameField);
    }

    public boolean isAbleToBecomeKing() {
        return (this instanceof Man && ((side == Side.WHITE && row == 0) || (side == Side.BLACK && row == 7)));
    }

    public List<ModelCell> getCellsAllowedToCapture() {
        return cellsAllowedToCaptureIn;
    }

    public List<ModelCell> getCellsAllowedToMoveIn() {
        return cellsAllowedToMoveIn;
    }

    public abstract boolean isAbleToCapture();

    public abstract boolean isAbleToMove();

    protected void searchCellsAllowedToCapture(Direction[] directions, int moveLength) {
        cellsAllowedToCaptureIn = new ArrayList<ModelCell>();

        for (Direction direction : directions) {
            prepareDeltaAndCheckVars(direction);

            for (int moveIndex = 1; moveIndex <= moveLength; moveIndex++) {
                updateCheckVarsByDeltas();

                if (isCellOpponent(checkRow, checkColumn)) {
                    do {
                        updateCheckVarsByDeltas();

                        if (isCellEmpty(checkRow, checkColumn)) {
                            cellsAllowedToCaptureIn.add(gameField[checkRow][checkColumn]);
                        } else {
                            break;
                        }

                        moveIndex++;
                    } while (moveIndex <= moveLength);

                    break;
                } else if (!isCellEmpty(checkRow, checkColumn)) {
                    break;
                }
            }
        }
    }

    protected void searchCellsAllowedToMoveIn(Direction[] directions, int moveLength) {
        cellsAllowedToMoveIn = new ArrayList<ModelCell>();

        for (Direction direction : directions) {
            prepareDeltaAndCheckVars(direction);

            for (int moveIndex = 1; moveIndex <= moveLength; moveIndex++) {
                updateCheckVarsByDeltas();

                if (isCellEmpty(checkRow, checkColumn)) {
                    cellsAllowedToMoveIn.add(gameField[checkRow][checkColumn]);
                } else {
                    break;
                }
            }
        }
    }

    private void prepareDeltaAndCheckVars(Direction direction) {
        deltaX = direction.getDeltaX();
        deltaY = direction.getDeltaY();
        checkRow = row;
        checkColumn = column;
    }

    private void updateCheckVarsByDeltas() {
        checkRow += deltaY;
        checkColumn += deltaX;
    }

    private boolean isCellEmpty(int row, int column) {
        return (isValidPosition(row, column) && gameField[row][column].getPiece() == null);
    }

    private boolean isCellOpponent(int row, int column) {
        if (isValidPosition(row, column)) {
            Piece gamePiece = gameField[row][column].getPiece();
            return (gamePiece != null && gamePiece.getSide() != side);
        }
        return false;
    }

    private boolean isValidPosition(int row, int column) {
        return (row >= 0 && row < gameField.length && column >= 0 && column < gameField.length);
    }

}

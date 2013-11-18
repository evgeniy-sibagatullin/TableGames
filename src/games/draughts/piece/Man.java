package games.draughts.piece;

import enums.Side;
import model.ModelCell;

public class Man extends DraughtsPiece {

    public Man(int row, int column, Side side, ModelCell[][] gameField) {
        super(row, column, side, gameField);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/draughts/Draughts-ManB.png" :
                "img/draughts/Draughts-ManR.png";
    }

    @Override
    public boolean isAbleToCapture(Side side) {
        return (side == null);
    }

    @Override
    public boolean isAbleToMove(Side side) {
        int deltaY = (side == Side.WHITE) ? -1 : 1;
        for (int deltaX = -1; deltaX <= 1; deltaX += 2) {
            int checkRow = getRow() + deltaY;
            int checkColumn = getColumn() + deltaX;
            if (isValidPosition(checkRow, checkColumn) && gameField[checkRow][checkColumn].getPiece() == null) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidPosition(int row, int column) {
        return (row >= 0 && row < gameField.length && column >= 0 && column < gameField.length);
    }
}

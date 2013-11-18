package games.draughts.piece;

import enums.Side;
import model.ModelCell;

import java.util.ArrayList;
import java.util.List;

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
    public boolean isAbleToCapture() {
        return false;
    }

    @Override
    public boolean isAbleToMove() {
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

    @Override
    public List<ModelCell> getCellsAllowedToMoveIn() {
        List<ModelCell> cellList = new ArrayList<ModelCell>();
        int deltaY = (side == Side.WHITE) ? -1 : 1;

        for (int deltaX = -1; deltaX <= 1; deltaX += 2) {
            int checkRow = getRow() + deltaY;
            int checkColumn = getColumn() + deltaX;
            if (isValidPosition(checkRow, checkColumn) && gameField[checkRow][checkColumn].getPiece() == null) {
                cellList.add(gameField[checkRow][checkColumn]);
            }
        }

        return cellList;
    }
}

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
        cellsAllowedToCaptureIn = new ArrayList<ModelCell>();

        for (int deltaY = -1; deltaY <= 1; deltaY += 2) {
            for (int deltaX = -1; deltaX <= 1; deltaX += 2) {
                int checkRow = row + deltaY;
                int checkColumn = column + deltaX;

                if (isCellOpponent(checkRow, checkColumn)) {
                    checkRow += deltaY;
                    checkColumn += deltaX;

                    if (isCellEmpty(checkRow, checkColumn)) {
                        cellsAllowedToCaptureIn.add(gameField[checkRow][checkColumn]);
                    }
                }
            }
        }

        return (!cellsAllowedToCaptureIn.isEmpty());
    }

    @Override
    public List<ModelCell> getCellsAllowedToCapture() {
        return cellsAllowedToCaptureIn;
    }

    @Override
    public boolean isAbleToMove() {
        cellsAllowedToMoveIn = new ArrayList<ModelCell>();
        int deltaY = (side == Side.WHITE) ? -1 : 1;

        for (int deltaX = -1; deltaX <= 1; deltaX += 2) {
            int checkRow = row + deltaY;
            int checkColumn = column + deltaX;

            if (isCellEmpty(checkRow, checkColumn)) {
                cellsAllowedToMoveIn.add(gameField[checkRow][checkColumn]);
            }
        }

        return (!cellsAllowedToMoveIn.isEmpty());
    }

    @Override
    public List<ModelCell> getCellsAllowedToMoveIn() {
        return cellsAllowedToMoveIn;
    }
}

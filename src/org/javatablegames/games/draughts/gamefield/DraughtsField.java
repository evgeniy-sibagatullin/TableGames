package org.javatablegames.games.draughts.gamefield;

import org.javatablegames.core.enums.CellState;
import org.javatablegames.core.enums.Direction;
import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.game.gamefield.ModelCell;
import org.javatablegames.core.model.position.Position;
import org.javatablegames.games.draughts.piece.DraughtsPiece;
import org.javatablegames.games.draughts.piece.King;

import java.util.List;

public class DraughtsField extends Gamefield<DraughtsPiece> {

    private static final String BLACK_CELL = "src/org/javatablegames/games/draughts/img/black-cell.png";
    private static final String WHITE_CELL = "src/org/javatablegames/games/draughts/img/white-cell.png";

    @Override
    protected void initializeGamefield() {
        size = 8;
        field = new ModelCell[size][size];

        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                String color = ((row + column) % 2 == 0) ? WHITE_CELL
                        : BLACK_CELL;
                Position position = new Position(row, column);
                setCell(new ModelCell<DraughtsPiece>(position, 0, color, CellState.DEFAULT), position);
            }
        }
    }

    public void selectCell(ModelCell<DraughtsPiece> modelCell) {
        modelCell.updateCellState(CellState.SELECTED);
        selectedCell = modelCell;

        if (isAbleToCapture(modelCell)) {
            setCellStateToAllowedCells(CellState.ATTACKED);
        } else {
            setCellStateToAllowedCells(CellState.ALLOWED_MOVE);
        }
    }

    public void reselectCell(ModelCell<DraughtsPiece> modelCell) {
        selectedCell.updateCellState(CellState.ALLOWED_PIECE);
        setCellStateToAllowedCells(CellState.DEFAULT);
        selectCell(modelCell);
    }

    public void moveToCell(ModelCell<DraughtsPiece> modelCell) {
        DraughtsPiece piece = selectedCell.getPiece();
        piece.setPosition(modelCell.getPosition());

        if (piece.isAbleToBecomeKing()) {
            promoteToKing(piece);
        }

        modelCell.setPiece(selectedCell.getPiece());
        selectedCell.setPiece(null);
        selectedCell = null;
    }

    public boolean isAbleToCapture(ModelCell<DraughtsPiece> modelCell) {
        return (modelCell.getPiece()).isAbleToCapture();
    }

    public void captureToCell(ModelCell<DraughtsPiece> modelCell) {
        Position targetPosition = new Position(modelCell.getPosition());
        Position selectedPosition = new Position(selectedCell.getPosition());
        Direction captureDirection = Direction.getDirection(
                targetPosition.getRow() - selectedPosition.getRow(),
                targetPosition.getColumn() - selectedPosition.getColumn());

        ModelCell<DraughtsPiece> checkCell;
        do {
            selectedPosition.moveInDirection(captureDirection);
            checkCell = getCell(selectedPosition);
        } while (checkCell.getPiece() == null);

        pieceSet.remove(checkCell.getPiece());
        checkCell.setPiece(null);

        moveToCell(modelCell);
    }

    private void setCellStateToAllowedCells(CellState cellState) {
        DraughtsPiece piece = selectedCell.getPiece();
        List<ModelCell<DraughtsPiece>> cellList = piece.getCellsAllowedToCapture();

        if (cellList.isEmpty()) {
            cellList = piece.getCellsAllowedToMoveIn();
        }

        for (ModelCell modelCell : cellList) {
            modelCell.updateCellState(cellState);
        }
    }

    private void promoteToKing(DraughtsPiece piece) {
        pieceSet.remove(piece);
        piece = new King(piece.getPosition(), piece.getSide(), this);
        pieceSet.add(piece);
        selectedCell.setPiece(piece);
    }

}

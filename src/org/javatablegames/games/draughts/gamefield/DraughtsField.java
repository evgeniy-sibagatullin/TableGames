package org.javatablegames.games.draughts.gamefield;

import org.javatablegames.core.enums.CellState;
import org.javatablegames.core.enums.Direction;
import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.game.gamefield.ModelCell;
import org.javatablegames.core.model.game.piece.Piece;
import org.javatablegames.core.model.position.Position;
import org.javatablegames.games.draughts.piece.DraughtsPiece;
import org.javatablegames.games.draughts.piece.King;

import java.util.List;

public class DraughtsField extends Gamefield {

    private static final String BLACK_CELL = "src/org/javatablegames/games/draughts/img/black-cell.png";
    private static final String WHITE_CELL = "src/org/javatablegames/games/draughts/img/white-cell.png";
    private ModelCell selectedCell = null;

    public void setSelectedCellByPiece(Piece piece) {
        selectedCell = getCell(piece.getPosition());
    }

    public ModelCell getSelectedCell() {
        return selectedCell;
    }

    @Override
    protected void initializeGamefield() {
        size = 8;
        field = new ModelCell[size][size];

        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                String color = ((row + column) % 2 == 0) ? WHITE_CELL
                        : BLACK_CELL;
                Position position = new Position(row, column);
                setCell(new ModelCell(position, 0, color, CellState.DEFAULT), position);
            }
        }
    }

    public void selectCell(ModelCell modelCell) {
        modelCell.updateCellState(CellState.SELECTED);
        selectedCell = modelCell;

        if (isAbleToCapture(modelCell)) {
            setCellStateToAllowedCells(CellState.ATTACKED);
        } else {
            setCellStateToAllowedCells(CellState.ALLOWED_MOVE);
        }
    }

    public void reselectCell(ModelCell modelCell) {
        selectedCell.updateCellState(CellState.ALLOWED_PIECE);
        setCellStateToAllowedCells(CellState.DEFAULT);
        selectCell(modelCell);
    }

    public void moveToCell(ModelCell modelCell) {
        DraughtsPiece piece = (DraughtsPiece) selectedCell.getPiece();
        piece.setPosition(modelCell.getPosition());

        if (piece.isAbleToBecomeKing()) {
            promoteToKing(piece);
        }

        modelCell.setPiece(selectedCell.getPiece());
        selectedCell.setPiece(null);
        selectedCell = null;
    }

    public boolean isAbleToCapture(ModelCell modelCell) {
        return ((DraughtsPiece) modelCell.getPiece()).isAbleToCapture();
    }

    public void captureToCell(ModelCell modelCell) {
        Position targetPosition = new Position(modelCell.getPosition());
        Position selectedPosition = new Position(selectedCell.getPosition());
        Direction captureDirection = Direction.getDirection(
                targetPosition.getRow() - selectedPosition.getRow(),
                targetPosition.getColumn() - selectedPosition.getColumn());

        ModelCell checkCell;
        do {
            selectedPosition.moveInDirection(captureDirection);
            checkCell = getCell(selectedPosition);
        } while (checkCell.getPiece() == null);

        pieceSet.remove(checkCell.getPiece());
        checkCell.setPiece(null);

        moveToCell(modelCell);
    }

    private void setCellStateToAllowedCells(CellState cellState) {
        DraughtsPiece piece = (DraughtsPiece) selectedCell.getPiece();
        List<ModelCell> cellList = piece.getCellsAllowedToCapture();

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

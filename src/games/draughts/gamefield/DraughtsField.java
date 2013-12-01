package games.draughts.gamefield;

import enums.CellState;
import games.draughts.piece.DraughtsPiece;
import games.draughts.piece.King;
import model.game.gamefield.Gamefield;
import model.game.gamefield.ModelCell;
import model.game.piece.Piece;
import model.game.position.Position;

import java.util.List;

public class DraughtsField extends Gamefield {

    private static final String BLACK_CELL = "img/chess/black-cell.png";
    private static final String WHITE_CELL = "img/chess/white-cell.png";

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

    public void totalGameFieldCleanUp() {
        for (ModelCell[] gameFieldRow : getField()) {
            for (int column = 0; column < getSize(); column++) {
                ModelCell modelCell = gameFieldRow[column];
                updateCellState(modelCell, CellState.DEFAULT);
            }
        }
    }

    public void updatePiecesReadyToMove(List<DraughtsPiece> pieceList) {
        for (DraughtsPiece draughtsPiece : pieceList) {
            ModelCell modelCell = getCell(draughtsPiece.getPosition());
            if (modelCell.getCellState() != CellState.SELECTED) {
                updateCellState(modelCell, CellState.ALLOWED_PIECE);
            }
        }
    }

    public void selectCell(ModelCell modelCell) {
        updateCellState(modelCell, CellState.SELECTED);
        selectedCell = modelCell;

        if (isAbleToCapture(modelCell)) {
            updateCellsAllowedToMoveIn(CellState.ATTACKED);
        } else {
            updateCellsAllowedToMoveIn(CellState.ALLOWED_MOVE);
        }
    }

    public void reselectCell(ModelCell modelCell) {
        updateCellState(selectedCell, CellState.ALLOWED_PIECE);
        updateCellsAllowedToMoveIn(CellState.DEFAULT);
        selectCell(modelCell);
    }


    private void updateCellsAllowedToMoveIn(CellState cellState) {
        DraughtsPiece piece = (DraughtsPiece) selectedCell.getPiece();
        List<ModelCell> cellList = piece.getCellsAllowedToCapture();

        if (cellList.isEmpty()) {
            cellList = piece.getCellsAllowedToMoveIn();
        }

        for (ModelCell modelCell : cellList) {
            updateCellState(modelCell, cellState);
        }
    }

    public boolean isAbleToCapture(ModelCell modelCell) {
        return ((DraughtsPiece) modelCell.getPiece()).isAbleToCapture();
    }

    public void captureToCell(ModelCell modelCell) {
        int checkRow = selectedCell.getPosition().getRow();
        int checkColumn = selectedCell.getPosition().getColumn();

        Position checkPosition = new Position(checkRow, checkColumn);

        int deltaY = (modelCell.getPosition().getRow() > checkRow) ? 1 : -1;
        int deltaX = (modelCell.getPosition().getColumn() > checkColumn) ? 1 : -1;
        ModelCell checkCell;
        do {
            checkPosition.setPosition(checkPosition.getRow() + deltaY, checkPosition.getColumn() + deltaX);
            checkCell = getCell(checkPosition);
        } while (checkCell.getPiece() == null);

        pieces.remove(checkCell.getPiece());
        checkCell.setPiece(null);

        moveToCell(modelCell);
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

    private void promoteToKing(DraughtsPiece piece) {
        pieces.remove(piece);
        piece = new King(piece.getPosition(), piece.getSide(), this);
        pieces.add(piece);
        selectedCell.setPiece(piece);
    }


    private void updateCellState(ModelCell modelCell, CellState cellState) {
        modelCell.setCellState(cellState);
        modelCell.setChanged(true);
    }
}

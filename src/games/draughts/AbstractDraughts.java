package games.draughts;

import enums.CellState;
import enums.Side;
import games.draughts.gamefield.DraughtsField;
import games.draughts.piece.DraughtsPiece;
import games.draughts.piece.DraughtsPieces;
import games.draughts.piece.King;
import model.Model;
import model.game.Game;
import model.game.gamefield.ModelCell;
import model.game.piece.Piece;
import model.game.position.Position;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDraughts extends Game<DraughtsField> {

    protected Side sidePlayer = Side.WHITE;
    protected boolean isPlayerMove = true;
    protected boolean needToPrepareFieldForPlayer = true;
    protected String checkWinConditionsResult = "";
    protected ModelCell selectedCell = null;

    public AbstractDraughts(Model model) {
        super(model);
        gamefield = new DraughtsField();
        pieces = new DraughtsPieces(gamefield);
    }

    @Override
    public String checkWinConditions() {
        return checkWinConditionsResult;
    }

    protected void updateGameFieldForPlayer() {
        List<DraughtsPiece> ableToCaptureList = getPiecesAbleToCapture(sidePlayer);
        List<DraughtsPiece> ableToMoveList = getPiecesAbleToMove(sidePlayer);

        if (!ableToCaptureList.isEmpty()) {
            updatePiecesReadyToMove(ableToCaptureList);
        } else if (!ableToMoveList.isEmpty()) {
            updatePiecesReadyToMove(ableToMoveList);
        } else {
            checkWinConditionsResult = "You have lost this game.";
        }

        needToPrepareFieldForPlayer = false;
        model.setChanged(true);
    }

    protected List<DraughtsPiece> getPiecesAbleToCapture(Side side) {
        List<DraughtsPiece> pieceList = new ArrayList<DraughtsPiece>();
        for (Piece piece : pieces.getPieces()) {
            DraughtsPiece draughtsPiece = (DraughtsPiece) piece;
            if (draughtsPiece.getSide() == side && draughtsPiece.isAbleToCapture()) {
                pieceList.add(draughtsPiece);
            }
        }
        return pieceList;
    }

    protected List<DraughtsPiece> getPiecesAbleToMove(Side side) {
        List<DraughtsPiece> pieceList = new ArrayList<DraughtsPiece>();
        for (Piece piece : pieces.getPieces()) {
            DraughtsPiece draughtsPiece = (DraughtsPiece) piece;
            if (draughtsPiece.getSide() == side && draughtsPiece.isAbleToMove()) {
                pieceList.add(draughtsPiece);
            }
        }
        return pieceList;
    }

    private void updatePiecesReadyToMove(List<DraughtsPiece> pieceList) {
        for (DraughtsPiece draughtsPiece : pieceList) {
            ModelCell modelCell = gamefield.getCell(draughtsPiece.getPosition());
            if (modelCell.getCellState() != CellState.SELECTED) {
                updateCellState(modelCell, CellState.ALLOWED_PIECE);
            }
        }
    }

    private void updateCellState(ModelCell modelCell, CellState cellState) {
        modelCell.setCellState(cellState);
        modelCell.setChanged(true);
    }

    @Override
    public void clickCell(Position position) {
        if (isPlayerMove) {
            boolean isModelChanged = false;
            ModelCell modelCell = gamefield.getCell(position);
            if (selectedCell != null) {
                if (modelCell.getCellState() == CellState.ATTACKED) {
                    capturePlayer(modelCell);
                    isModelChanged = true;
                } else if (modelCell.getCellState() == CellState.ALLOWED_MOVE) {
                    movePlayer(modelCell);
                    isModelChanged = true;
                } else if (modelCell.getCellState() == CellState.ALLOWED_PIECE) {
                    reselectPlayer(modelCell);
                    isModelChanged = true;
                }
            } else {
                if (modelCell.getCellState() == CellState.ALLOWED_PIECE) {
                    selectPlayer(modelCell);
                    isModelChanged = true;
                }
            }
            model.setChanged(isModelChanged);
        }
    }

    private void capturePlayer(ModelCell modelCell) {
        captureToCell(modelCell);
        boolean isAbleToCaptureAgain = isAbleToCapture(modelCell);
        if (isAbleToCaptureAgain) {
            totalGameFieldCleanUp();
            selectPlayer(modelCell);
        }
        isPlayerMove = isAbleToCaptureAgain;
    }

    private void movePlayer(ModelCell modelCell) {
        moveToCell(modelCell);
        isPlayerMove = false;
    }

    private void reselectPlayer(ModelCell modelCell) {
        updateCellState(selectedCell, CellState.ALLOWED_PIECE);
        updateCellsAllowedToMoveIn(CellState.DEFAULT);
        selectPlayer(modelCell);
    }

    private void selectPlayer(ModelCell modelCell) {
        updateCellState(modelCell, CellState.SELECTED);
        selectedCell = modelCell;

        if (isAbleToCapture(modelCell)) {
            updateCellsAllowedToMoveIn(CellState.ATTACKED);
        } else {
            updateCellsAllowedToMoveIn(CellState.ALLOWED_MOVE);
        }
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

    private boolean isAbleToCapture(ModelCell modelCell) {
        return ((DraughtsPiece) modelCell.getPiece()).isAbleToCapture();
    }

    protected void captureToCell(ModelCell modelCell) {
        int checkRow = selectedCell.getPosition().getRow();
        int checkColumn = selectedCell.getPosition().getColumn();

        Position checkPosition = new Position(checkRow, checkColumn);

        int deltaY = (modelCell.getPosition().getRow() > checkRow) ? 1 : -1;
        int deltaX = (modelCell.getPosition().getColumn() > checkColumn) ? 1 : -1;
        ModelCell checkCell;
        do {
            checkPosition.setPosition(checkPosition.getRow() + deltaY, checkPosition.getColumn() + deltaX);
            checkCell = gamefield.getCell(checkPosition);
        } while (checkCell.getPiece() == null);

        pieces.remove(checkCell.getPiece());
        checkCell.setPiece(null);

        moveToCell(modelCell);
    }

    protected void moveToCell(ModelCell modelCell) {
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
        piece = new King(piece.getPosition(), piece.getSide(), gamefield);
        pieces.add(piece);
        selectedCell.setPiece(piece);
    }

    protected void totalGameFieldCleanUp() {
        for (ModelCell[] gameFieldRow : gamefield.getField()) {
            for (int column = 0; column < gamefield.getSize(); column++) {
                ModelCell modelCell = gameFieldRow[column];
                updateCellState(modelCell, CellState.DEFAULT);
            }
        }
    }

    protected void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

}
package games.draughts;

import enums.CellState;
import enums.Side;
import games.draughts.piece.DraughtsPiece;
import games.draughts.piece.King;
import games.draughts.piece.Man;
import model.Model;
import model.ModelCell;
import model.game.AbstractGame;
import model.piece.Piece;

import java.util.ArrayList;
import java.util.List;

public abstract class ClassicDraughts extends AbstractGame implements Draughts {

    private static final int FIELD_SIZE = 8;

    protected Side sidePlayer = Side.WHITE;
    protected boolean isPlayerMove = true;
    protected boolean needToPrepareFieldForPlayer = true;
    protected String checkWinConditionsResult = "";
    protected ModelCell selectedCell = null;

    public ClassicDraughts(Model model) {
        super(model);
        initGameField();
        initPieces();
        addPiecesToGameField();
    }

    @Override
    public int getFieldSize() {
        return FIELD_SIZE;
    }

    @Override
    public String checkWinConditions() {
        return checkWinConditionsResult;
    }

    private void initGameField() {
        gameField = new ModelCell[FIELD_SIZE][FIELD_SIZE];
        for (int row = 0; row < FIELD_SIZE; row++) {
            for (int column = 0; column < FIELD_SIZE; column++) {
                String color = ((row + column) % 2 == 0) ? WHITE_CELL
                        : BLACK_CELL;
                gameField[row][column] = new ModelCell(row, column, 0, color, null, CellState.DEFAULT);
            }
        }
    }

    private void initPieces() {
        pieces = new ArrayList<Piece>();

        for (int row = 0; row < FIELD_SIZE; row++) {
            for (int column = 0; column < FIELD_SIZE; column++) {
                if ((row + column) % 2 != 0) {
                    if (row < 3) {
                        pieces.add(new Man(row, column, Side.BLACK, gameField));
                    }

                    if (row > 4) {
                        pieces.add(new Man(row, column, Side.WHITE, gameField));
                    }
                }
            }
        }
    }

    private void addPiecesToGameField() {
        for (Piece piece : pieces) {
            gameField[piece.getRow()][piece.getColumn()].setPiece(piece);
        }
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
        for (Piece piece : pieces) {
            DraughtsPiece draughtsPiece = (DraughtsPiece) piece;
            if (draughtsPiece.getSide() == side && draughtsPiece.isAbleToCapture()) {
                pieceList.add(draughtsPiece);
            }
        }
        return pieceList;
    }

    protected List<DraughtsPiece> getPiecesAbleToMove(Side side) {
        List<DraughtsPiece> pieceList = new ArrayList<DraughtsPiece>();
        for (Piece piece : pieces) {
            DraughtsPiece draughtsPiece = (DraughtsPiece) piece;
            if (draughtsPiece.getSide() == side && draughtsPiece.isAbleToMove()) {
                pieceList.add(draughtsPiece);
            }
        }
        return pieceList;
    }

    private void updatePiecesReadyToMove(List<DraughtsPiece> pieceList) {
        for (DraughtsPiece draughtsPiece : pieceList) {
            ModelCell modelCell = gameField[draughtsPiece.getRow()][draughtsPiece.getColumn()];
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
    public void clickCell(int row, int column) {
        if (isPlayerMove) {
            boolean isModelChanged = false;
            ModelCell modelCell = gameField[row][column];
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
        int deltaY = (modelCell.getRow() > selectedCell.getRow()) ? 1 : -1;
        int deltaX = (modelCell.getColumn() > selectedCell.getColumn()) ? 1 : -1;

        int checkRow = selectedCell.getRow();
        int checkColumn = selectedCell.getColumn();
        ModelCell checkCell;
        do {
            checkRow += deltaY;
            checkColumn += deltaX;
            checkCell = gameField[checkRow][checkColumn];
        } while (checkCell.getPiece() == null);

        pieces.remove(checkCell.getPiece());
        checkCell.setPiece(null);

        moveToCell(modelCell);
    }

    protected void moveToCell(ModelCell modelCell) {
        DraughtsPiece piece = (DraughtsPiece) selectedCell.getPiece();
        piece.setRow(modelCell.getRow());
        piece.setColumn(modelCell.getColumn());

        if (piece.isAbleToBecomeKing()) {
            promoteToKing(piece);
        }

        modelCell.setPiece(selectedCell.getPiece());
        selectedCell.setPiece(null);
        selectedCell = null;
    }

    private void promoteToKing(DraughtsPiece piece) {
        pieces.remove(piece);
        piece = new King(piece.getRow(), piece.getColumn(), piece.getSide(), gameField);
        pieces.add(piece);
        selectedCell.setPiece(piece);
    }

    protected void totalGameFieldCleanUp() {
        for (ModelCell[] gameFieldRow : gameField) {
            for (int column = 0; column < gameField.length; column++) {
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
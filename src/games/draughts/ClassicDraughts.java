package games.draughts;

import enums.CellState;
import enums.GameType;
import enums.Side;
import games.draughts.piece.DraughtsPiece;
import games.draughts.piece.Man;
import model.Model;
import model.ModelCell;
import model.game.AbstractGame;
import model.piece.Piece;

import java.util.ArrayList;
import java.util.List;

public class ClassicDraughts extends AbstractGame implements Draughts {

    private static final int FIELD_SIZE = 8;

    private boolean isPlayerMove = true;
    private Side sidePlayer = Side.WHITE;
    private Side sideAI = Side.BLACK;
    private boolean needToPrepareFieldForPlayer = true;

    private ModelCell selectedCell = null;
    private String checkWinConditionsResult = "";

    public ClassicDraughts(Model model, GameType gameType) {
        super(model, gameType);
        initGameField();
        initPieces();
        addPiecesToGameField();
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

    @Override
    public int getFieldSize() {
        return FIELD_SIZE;
    }

    @Override
    public String checkWinConditions() {
        return checkWinConditionsResult;
    }

    @Override
    public void run() {
        while (isThreadNeeded) {
            if (needToPrepareFieldForPlayer) {
                updateGameFieldForPlayer();
            }
            if (!isPlayerMove) {
                performMoveAI();
            }
            delay(50);
        }
    }

    private void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    private void updateGameFieldForPlayer() {
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

    private List<DraughtsPiece> getPiecesAbleToCapture(Side side) {
        List<DraughtsPiece> pieceList = new ArrayList<DraughtsPiece>();
        for (Piece piece : pieces) {
            DraughtsPiece draughtsPiece = (DraughtsPiece) piece;
            if (draughtsPiece.getSide() == side && draughtsPiece.isAbleToCapture()) {
                pieceList.add(draughtsPiece);
            }
        }
        return pieceList;
    }

    private List<DraughtsPiece> getPiecesAbleToMove(Side side) {
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

    private void captureToCell(ModelCell modelCell) {
        int deltaY = (modelCell.getRow() > selectedCell.getRow()) ? 1 : -1;
        int deltaX = (modelCell.getColumn() > selectedCell.getColumn()) ? 1 : -1;

        int checkRow = selectedCell.getRow() + deltaY;
        int checkColumn = selectedCell.getColumn() + deltaX;
        ModelCell checkCell = gameField[checkRow][checkColumn];
        while (checkCell.getPiece() == null) {
            checkRow += deltaY;
            checkColumn += deltaX;
        }
        pieces.remove(checkCell.getPiece());
        checkCell.setPiece(null);

        moveToCell(modelCell);
    }

    private void moveToCell(ModelCell modelCell) {
        Piece piece = selectedCell.getPiece();
        piece.setRow(modelCell.getRow());
        piece.setColumn(modelCell.getColumn());

        modelCell.setPiece(selectedCell.getPiece());
        selectedCell.setPiece(null);
        selectedCell = null;
    }

    private void performMoveAI() {
        totalGameFieldCleanUp();

        List<DraughtsPiece> ableToCaptureList = getPiecesAbleToCapture(sideAI);
        if (!ableToCaptureList.isEmpty()) {
            capturePieceAI(ableToCaptureList.get(0));
        } else {
            List<DraughtsPiece> ableToMoveList = getPiecesAbleToMove(sideAI);
            if (!ableToMoveList.isEmpty()) {
                movePieceAI(ableToMoveList.get(0));
            } else {
                checkWinConditionsResult = "Congratulations! You have win this game!";
            }
        }
        isPlayerMove = true;
        needToPrepareFieldForPlayer = true;
    }

    private void capturePieceAI(DraughtsPiece piece) {
        while (piece.isAbleToCapture()) {
            selectedCell = gameField[piece.getRow()][piece.getColumn()];
            captureToCell(piece.getCellsAllowedToCapture().get(0));
        }
    }

    private void movePieceAI(DraughtsPiece piece) {
        selectedCell = gameField[piece.getRow()][piece.getColumn()];
        moveToCell(piece.getCellsAllowedToMoveIn().get(0));
    }

    private void totalGameFieldCleanUp() {
        for (ModelCell[] gameFieldRow : gameField) {
            for (int column = 0; column < gameField.length; column++) {
                ModelCell modelCell = gameFieldRow[column];
                updateCellState(modelCell, CellState.DEFAULT);
            }
        }
    }
}
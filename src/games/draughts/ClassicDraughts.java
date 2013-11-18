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
    private Side playerSide = Side.WHITE;
    private Side AIside = Side.BLACK;
    private boolean needToPrepareFieldForPlayer = true;

    private ModelCell selectedCell = null;
    private boolean isPieceSelected = false;
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
        List<Piece> pieces = new ArrayList<Piece>();

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

        setPieces(pieces);
    }

    private void addPiecesToGameField() {
        for (Piece piece : getPieces()) {
            ModelCell cell = gameField[piece.getRow()][piece.getColumn()];
            cell.setPiece(piece);
        }
    }

    @Override
    public int getFieldSize() {
        return FIELD_SIZE;
    }

    @Override
    public void run() {
        while (isThreadNeeded) {
            if (needToPrepareFieldForPlayer) {
                updateGameFieldForPlayer();
            }
            if (!isPlayerMove) {
                performAIMove();
            }
            delay(50);
        }
    }

    @Override
    public void clickCell(int row, int column) {
        if (isPlayerMove) {
            boolean isModelChanged = false;
            ModelCell modelCell = gameField[row][column];
            if (isPieceSelected) {
                if (modelCell.getCellState() == CellState.ALLOWED_MOVE) {
                    movePiece(modelCell);
                    isModelChanged = true;
                    isPlayerMove = false;
                }
                /*else if (modelCell.getCellState() == CellState.ATTACKED) {
                    capturePiece(modelCell);
                }*/
                else if (modelCell.getCellState() == CellState.ALLOWED_PIECE) {
                    reselectPiece(modelCell);
                    isModelChanged = true;
                }
            } else {
                if (modelCell.getCellState() == CellState.ALLOWED_PIECE) {
                    selectPiece(modelCell);
                    isModelChanged = true;
                }
            }
            model.setChanged(isModelChanged);
        }
    }


    private void updateGameFieldForPlayer() {
        List<DraughtsPiece> ableToCaptureList = getPiecesAbleToCapture(playerSide);
        List<DraughtsPiece> ableToMoveList = getPiecesAbleToMove(playerSide);

        if (!ableToCaptureList.isEmpty()) {
            updatePiecesReadyToCapture(ableToCaptureList);
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
        for (Piece piece : getPieces()) {
            DraughtsPiece draughtsPiece = (DraughtsPiece) piece;
            if (draughtsPiece.getSide() == side && draughtsPiece.isAbleToCapture()) {
                pieceList.add(draughtsPiece);
            }
        }
        return pieceList;
    }

    private List<DraughtsPiece> getPiecesAbleToMove(Side side) {
        List<DraughtsPiece> pieceList = new ArrayList<DraughtsPiece>();
        for (Piece piece : getPieces()) {
            DraughtsPiece draughtsPiece = (DraughtsPiece) piece;
            if (draughtsPiece.getSide() == side && draughtsPiece.isAbleToMove()) {
                pieceList.add(draughtsPiece);
            }
        }
        return pieceList;
    }


    private void updatePiecesReadyToCapture(List<DraughtsPiece> pieceList) {
        pieceList.isEmpty();
    }


    private void updatePiecesReadyToMove(List<DraughtsPiece> pieceList) {
        for (DraughtsPiece draughtsPiece : pieceList) {
            ModelCell modelCell = gameField[draughtsPiece.getRow()][draughtsPiece.getColumn()];
            if (modelCell.getCellState() != CellState.SELECTED) {
                updateCellState(modelCell, CellState.ALLOWED_PIECE);
            }
        }
    }


    private void selectPiece(ModelCell modelCell) {
        updateCellState(modelCell, CellState.SELECTED);
        selectedCell = modelCell;
        updateCellsAllowedToMoveIn(CellState.ALLOWED_MOVE);
        isPieceSelected = true;
    }

    private void updateCellsAllowedToMoveIn(CellState cellState) {
        int deltaY = -1;
        for (int deltaX = -1; deltaX <= 1; deltaX += 2) {
            int updateRow = selectedCell.getRow() + deltaY;
            int updateColumn = selectedCell.getColumn() + deltaX;
            if (isValidPosition(updateRow, updateColumn)) {
                ModelCell updateCell = gameField[updateRow][updateColumn];
                if (updateCell.getPiece() == null) {
                    updateCellState(updateCell, cellState);
                }
            }
        }
    }

    private void reselectPiece(ModelCell modelCell) {
        updateCellState(selectedCell, CellState.ALLOWED_PIECE);
        updateCellsAllowedToMoveIn(CellState.DEFAULT);
        selectPiece(modelCell);
    }

    private void movePiece(ModelCell modelCell) {
        Piece piece = selectedCell.getPiece();
        piece.setRow(modelCell.getRow());
        piece.setColumn(modelCell.getColumn());

        modelCell.setPiece(selectedCell.getPiece());
        selectedCell.setPiece(null);
        selectedCell = null;

        isPieceSelected = false;
    }


    private void performAIMove() {
        totalCleanUp();

        List<DraughtsPiece> ableToCaptureList = getPiecesAbleToCapture(AIside);
        List<DraughtsPiece> ableToMoveList = getPiecesAbleToMove(AIside);

        if (!ableToCaptureList.isEmpty()) {
            updatePiecesReadyToCapture(ableToCaptureList);
        } else if (!ableToMoveList.isEmpty()) {
            DraughtsPiece piece = ableToMoveList.get(0);
            List<ModelCell> cellsAllowedToMoveIn = piece.getCellsAllowedToMoveIn();

            selectedCell = gameField[piece.getRow()][piece.getColumn()];
            movePiece(cellsAllowedToMoveIn.get(0));
        } else {
            checkWinConditionsResult = "Congratulations! You have win this game!";
        }

        isPlayerMove = true;
        needToPrepareFieldForPlayer = true;
    }

    private void totalCleanUp() {
        for (ModelCell[] gameFieldRow : gameField) {
            for (int column = 0; column < gameField.length; column++) {
                ModelCell modelCell = gameFieldRow[column];
                updateCellState(modelCell, CellState.DEFAULT);
            }
        }
    }


    private void updateCellState(ModelCell modelCell, CellState cellState) {
        modelCell.setCellState(cellState);
        modelCell.setChanged(true);
    }

    private boolean isValidPosition(int row, int column) {
        return (row >= 0 && row < gameField.length && column >= 0 && column < gameField.length);
    }

    private void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    @Override
    public String checkWinConditions() {
        return checkWinConditionsResult;
    }
}
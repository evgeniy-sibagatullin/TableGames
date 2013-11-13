package games.chess;

import enums.CellState;
import enums.Direction;
import enums.GameType;
import enums.Side;
import games.chess.piece.*;
import model.Model;
import model.ModelCell;
import model.game.AbstractGame;
import model.piece.Piece;

import java.util.ArrayList;
import java.util.List;

public class ClassicChess extends AbstractGame implements Chess {

    private static final int FIELD_SIZE = 8;
    private Side activeSide;
    private ModelCell selectedCell;

    public ClassicChess(Model model) {
        super(model);
        activeSide = Side.WHITE;
        setGameType(GameType.CHESS);
        initGameField();
        initPieces();
        addPiecesToGameField();
        updateFieldBeforeNewMove();
    }

    @Override
    public int getFieldSize() {
        return FIELD_SIZE;
    }

    private void initGameField() {
        ModelCell[][] gameField = new ModelCell[FIELD_SIZE][FIELD_SIZE];
        for (int row = 0; row < FIELD_SIZE; row++) {
            for (int column = 0; column < FIELD_SIZE; column++) {
                String color = ((row + column) % 2 == 0) ? WHITE_CELL
                        : BLACK_CELL;
                gameField[row][column] = new ModelCell(row, column, 0, color, null, CellState.DEFAULT);
            }
        }

        setGameField(gameField);
    }

    private void initPieces() {
        List<Piece> pieces = new ArrayList<Piece>();

        for (int i = 0; i < FIELD_SIZE; i++) {
            pieces.add(new Pawn(6, i, Side.WHITE));
            pieces.add(new Pawn(1, i, Side.BLACK));
        }

        pieces.add(new Rook(0, 0, Side.BLACK));
        pieces.add(new Rook(0, 7, Side.BLACK));
        pieces.add(new Rook(7, 0, Side.WHITE));
        pieces.add(new Rook(7, 7, Side.WHITE));

        pieces.add(new Knight(0, 1, Side.BLACK));
        pieces.add(new Knight(0, 6, Side.BLACK));
        pieces.add(new Knight(7, 1, Side.WHITE));
        pieces.add(new Knight(7, 6, Side.WHITE));

        pieces.add(new Bishop(0, 2, Side.BLACK));
        pieces.add(new Bishop(0, 5, Side.BLACK));
        pieces.add(new Bishop(7, 2, Side.WHITE));
        pieces.add(new Bishop(7, 5, Side.WHITE));

        pieces.add(new Queen(0, 3, Side.BLACK));
        pieces.add(new Queen(7, 3, Side.WHITE));
        pieces.add(new King(0, 4, Side.BLACK));
        pieces.add(new King(7, 4, Side.WHITE));

        setPieces(pieces);
    }

    private void addPiecesToGameField() {
        for (Piece piece : getPieces()) {
            ModelCell cell = getGameField()[piece.getRow()][piece.getColumn()];
            cell.setPiece(piece);
        }
    }

    @Override
    public void clickCell(int row, int column) {
        ModelCell cell = getGameField()[row][column];
        if (CellState.ATTACKED == cell.getCellState() || CellState.ALLOWED_MOVE == cell.getCellState()) {
            movePiece(row, column);
            model.modelChangedEvent();
        } else if (CellState.ALLOWED_PIECE == cell.getCellState()) {
            updateFieldBeforeNewMove();

            updateAllowedCells(cell);
            selectedCell = getGameField()[row][column];
            selectedCell.setCellState(CellState.SELECTED);
            selectedCell.setChanged(true);
            model.modelChangedEvent();
        }
    }

    private void movePiece(int row, int column) {
        updatePiecesOnMove(row, column);
        getGameField()[row][column].setPiece(selectedCell.getPiece());
        getGameField()[row][column].setPower(selectedCell.getPower());
        selectedCell.setPiece(null);
        selectedCell.setPower(0);
        activeSide = (activeSide == Side.WHITE) ? Side.BLACK : Side.WHITE;
        updateFieldBeforeNewMove();
    }

    private void updatePiecesOnMove(int row, int column) {
        Piece pieceToBeRemoved = null;
        for (Piece piece : getPieces()) {
            if (piece.getRow() == row && piece.getColumn() == column) {
                pieceToBeRemoved = piece;
            } else if (piece.getRow() == selectedCell.getRow() && piece.getColumn() == selectedCell.getColumn()) {
                piece.setRow(row);
                piece.setColumn(column);
            }
        }
        if (pieceToBeRemoved != null) {
            getPieces().remove(pieceToBeRemoved);
        }
    }

    private void updateFieldBeforeNewMove() {
        clearPowerFromEmptyCells();
        setPowerToCellsOfGameField();
        setPiecesOfActiveSideAllowed();
    }

    private void clearPowerFromEmptyCells() {
        for (int row = 0; row < FIELD_SIZE; row++) {
            for (int column = 0; column < FIELD_SIZE; column++) {
                ModelCell cell = getGameField()[row][column];
                if (cell.getPiece() == null && cell.getPower() != 0) {
                    cell.setPower(0);
                    cell.setChanged(true);
                }
            }
        }
    }

    private void setPowerToCellsOfGameField() {
        for (Piece piece : getPieces()) {
            if (piece.getSide() != activeSide) {
                getGameField()[piece.getRow()][piece.getColumn()].setPower(((ChessPiece) piece).getPower());
                updateAllowedCells(getGameField()[piece.getRow()][piece.getColumn()]);
            }
        }
    }

    private void setPiecesOfActiveSideAllowed() {
        for (int row = 0; row < FIELD_SIZE; row++) {
            for (int column = 0; column < FIELD_SIZE; column++) {
                ModelCell cell = getGameField()[row][column];
                CellState cellState = (cell.getPiece() == null) ?
                        CellState.DEFAULT :
                        (cell.getPiece().getSide() == activeSide) ?
                                CellState.ALLOWED_PIECE :
                                CellState.DEFAULT;
                if (cell.getCellState() != cellState) {
                    cell.setCellState(cellState);
                    cell.setChanged(true);
                }
            }
        }
    }

    private void updateAllowedCells(ModelCell cell) {
        Piece piece = cell.getPiece();
        if (piece instanceof Pawn) {
            updateByPawnRules(cell);
        } else if (piece instanceof Knight) {
            updateByKnightRules(cell);
        } else if (piece instanceof Bishop) {
            updateByBishopRules(cell);
        } else if (piece instanceof Rook) {
            updateByRookRules(cell);
        } else if (piece instanceof Queen) {
            updateByQueenRules(cell);
        } else if (piece instanceof King) {
            updateByKingRules(cell);
        }
    }

    private void setCellStatePowerAndStatusChanged(ModelCell cell, CellState cellState, int power) {
        cell.setCellState(cellState);
        cell.setPower(power);
        cell.setChanged(true);
    }

    private void updateByPawnRules(ModelCell cell) {
        Piece piece = cell.getPiece();
        Direction moveDirection = (piece.getSide() == Side.WHITE) ?
                Direction.NORTH :
                Direction.SOUTH;
        int moveLength = 1;
        if ((moveDirection == Direction.NORTH && cell.getRow() == 6)
                || (moveDirection == Direction.SOUTH && cell.getRow() == 1)) {
            moveLength = 2;
        }
        int deltaY = moveDirection.getDeltaY();
        int row = cell.getRow();
        int column = cell.getColumn();
        for (int move = 0; move < moveLength; move++) {
            row += deltaY;
            if (row >= 0 && row < FIELD_SIZE && column >= 0 && column < FIELD_SIZE) {
                if (getGameField()[row][column].getPiece() == null) {
                    setCellStatePowerAndStatusChanged(getGameField()[row][column], CellState.ALLOWED_MOVE, getGameField()[row][column].getPower());
                }
                if (move == 0) {
                    if (column < FIELD_SIZE - 1) {
                        updateAttackedByPawnCells(row, column + 1);
                    }
                    if (column > 0) {
                        updateAttackedByPawnCells(row, column - 1);
                    }
                }
            }
        }
    }

    private void updateAttackedByPawnCells(int row, int column) {
        ModelCell cellToBeCaptured = getGameField()[row][column];
        if (cellToBeCaptured.getPiece() != null) {
            if (cellToBeCaptured.getPiece().getSide() != activeSide) {
                setCellStatePowerAndStatusChanged(cellToBeCaptured, CellState.ATTACKED, cellToBeCaptured.getPower());
            }
        } else {
            setCellStatePowerAndStatusChanged(cellToBeCaptured, CellState.DEFAULT, 1);
        }
    }

    private void updateByKnightRules(ModelCell cell) {
        for (int deltaX = -2; deltaX <= 2; deltaX++) {
            for (int deltaY = -2; deltaY <= 2; deltaY++) {
                int row = cell.getRow() + deltaY;
                int column = cell.getColumn() + deltaX;
                if (isValidPosition(row, column) && ((Math.abs(deltaX) + Math.abs(deltaY)) == 3)) {
                    ModelCell presentCell = getGameField()[row][column];

                    int power;
                    if (presentCell.getPower() == 0) {
                        power = cell.getPower();
                    } else {
                        power = (presentCell.getPower() > cell.getPower()) ? cell.getPower() : presentCell.getPower();
                    }

                    if (presentCell.getPiece() == null) {
                        setCellStatePowerAndStatusChanged(presentCell, CellState.ALLOWED_MOVE, power);
                    } else if (presentCell.getPiece().getSide() != cell.getPiece().getSide()) {
                        setCellStatePowerAndStatusChanged(presentCell, CellState.ATTACKED, power);
                    }
                }
            }
        }
    }

    private void updateByBishopRules(ModelCell cell) {
        Direction[] directions = new Direction[]{Direction.SOUTHEAST, Direction.SOUTHWEST, Direction.NORTHEAST, Direction.NORTHWEST};
        updateAllowedCellsToMove(cell, directions, FIELD_SIZE);
    }

    private void updateByRookRules(ModelCell cell) {
        Direction[] directions = new Direction[]{Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.EAST};
        updateAllowedCellsToMove(cell, directions, FIELD_SIZE);
    }

    private void updateByQueenRules(ModelCell cell) {
        Direction[] directions = new Direction[]{Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.EAST,
                Direction.SOUTHEAST, Direction.SOUTHWEST, Direction.NORTHEAST, Direction.NORTHWEST};
        updateAllowedCellsToMove(cell, directions, FIELD_SIZE);
    }

    private void updateByKingRules(ModelCell cell) {
        Direction[] directions = new Direction[]{Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.EAST,
                Direction.SOUTHEAST, Direction.SOUTHWEST, Direction.NORTHEAST, Direction.NORTHWEST};
        updateAllowedCellsToMove(cell, directions, 1);
    }

    private void updateAllowedCellsToMove(ModelCell cell, Direction[] directions, int moveLength) {
        for (Direction direction : directions) {
            int deltaX = direction.getDeltaX();
            int deltaY = direction.getDeltaY();
            int row = cell.getRow();
            int column = cell.getColumn();

            for (int move = 0; move < moveLength; move++) {
                row += deltaY;
                column += deltaX;
                if (isValidPosition(row, column)) {
                    ModelCell presentCell = getGameField()[row][column];

                    int power;
                    if (presentCell.getPower() == 0) {
                        power = cell.getPower();
                    } else {
                        power = (presentCell.getPower() > cell.getPower()) ? ((ChessPiece) cell.getPiece()).getPower() : presentCell.getPower();
                    }

                    if (presentCell.getPiece() == null) {
                        setCellStatePowerAndStatusChanged(presentCell, CellState.ALLOWED_MOVE, power);
                    } else {
                        if (presentCell.getPiece().getSide() != cell.getPiece().getSide()) {
                            setCellStatePowerAndStatusChanged(presentCell, CellState.ATTACKED, power);
                        }
                        break;
                    }
                }
            }
        }
    }

    private boolean isValidPosition(int row, int column) {
        return (row >= 0 && row < FIELD_SIZE && column >= 0 && column < FIELD_SIZE);
    }

}

package org.javatablegames.games.chess;

import org.javatablegames.core.enums.CellState;
import org.javatablegames.core.enums.Direction;
import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.Model;
import org.javatablegames.core.model.game.Game;
import org.javatablegames.core.model.game.gamefield.ModelCell;
import org.javatablegames.core.model.game.piece.Piece;
import org.javatablegames.core.model.position.Position;
import org.javatablegames.games.chess.gamefield.ChessField;
import org.javatablegames.games.chess.piece.*;

public class Chess extends Game<ChessField, ChessPieceSet> {

    private static final int FIELD_SIZE = 8;

    private Side activeSide;
    private ModelCell selectedCell;

    public Chess(Model model) {
        super(model);
        activeSide = Side.WHITE;
        gamefield = new ChessField();
        pieceSet = new ChessPieceSet(gamefield);
        updateFieldBeforeNewMove();
    }

    @Override
    public void clickCell(Position position) {
        ModelCell cell = gamefield.getCell(position);
        boolean isChanged = false;
        if (CellState.ATTACKED == cell.getCellState() || CellState.ALLOWED_MOVE == cell.getCellState()) {
            movePiece(position);
            isChanged = true;
        } else if (CellState.ALLOWED_PIECE == cell.getCellState()) {
            updateFieldBeforeNewMove();

            updateAllowedCells(cell);
            selectedCell = gamefield.getCell(position);
            selectedCell.setCellState(CellState.SELECTED);
            selectedCell.setChanged(true);
            isChanged = true;
        }
        model.setChanged(isChanged);
    }

    private void movePiece(Position position) {
        updatePiecesOnMove(position);
        gamefield.getCell(position).setPiece(selectedCell.getPiece());
        gamefield.getCell(position).setPower(selectedCell.getPower());
        selectedCell.setPiece(null);
        selectedCell.setPower(0);
        activeSide = (activeSide == Side.WHITE) ? Side.BLACK : Side.WHITE;
        updateFieldBeforeNewMove();
    }

    private void updatePiecesOnMove(Position position) {
        Piece pieceToBeRemoved = null;
        for (Piece piece : pieceSet.getPieces()) {
            if (piece.getPosition().equals(position)) {
                pieceToBeRemoved = piece;
            } else if (piece.getPosition().equals(selectedCell.getPosition())) {
                piece.setPosition(position);
            }
        }
        if (pieceToBeRemoved != null) {
            pieceSet.remove(pieceToBeRemoved);
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
                ModelCell cell = gamefield.getCell(new Position(row, column));
                if (cell.getPiece() == null && cell.getPower() != 0) {
                    cell.setPower(0);
                    cell.setChanged(true);
                }
            }
        }
    }

    private void setPowerToCellsOfGameField() {
        for (Piece piece : pieceSet.getPieces()) {
            if (piece.getSide() != activeSide) {
                gamefield.getCell(piece.getPosition()).setPower(piece.getPower());
                updateAllowedCells(gamefield.getCell(piece.getPosition()));
            }
        }
    }

    private void setPiecesOfActiveSideAllowed() {
        for (int row = 0; row < FIELD_SIZE; row++) {
            for (int column = 0; column < FIELD_SIZE; column++) {
                ModelCell cell = gamefield.getCell(new Position(row, column));
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
        if ((moveDirection == Direction.NORTH && cell.getPosition().getRow() == 6)
                || (moveDirection == Direction.SOUTH && cell.getPosition().getRow() == 1)) {
            moveLength = 2;
        }
        int deltaY = moveDirection.getDeltaY();
        int row = cell.getPosition().getRow();
        int column = cell.getPosition().getColumn();
        for (int move = 0; move < moveLength; move++) {
            row += deltaY;
            if (row >= 0 && row < FIELD_SIZE && column >= 0 && column < FIELD_SIZE) {
                ModelCell modelCell = gamefield.getCell(new Position(row, column));
                if (modelCell.getPiece() == null) {
                    setCellStatePowerAndStatusChanged(modelCell, CellState.ALLOWED_MOVE, modelCell.getPower());
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
        ModelCell cellToBeCaptured = gamefield.getCell(new Position(row, column));
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
                int row = cell.getPosition().getRow() + deltaY;
                int column = cell.getPosition().getColumn() + deltaX;
                if (isValidPosition(row, column) && ((Math.abs(deltaX) + Math.abs(deltaY)) == 3)) {
                    ModelCell presentCell = gamefield.getCell(new Position(row, column));

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
            int row = cell.getPosition().getRow();
            int column = cell.getPosition().getColumn();

            for (int move = 0; move < moveLength; move++) {
                row += deltaY;
                column += deltaX;
                if (isValidPosition(row, column)) {
                    ModelCell presentCell = gamefield.getCell(new Position(row, column));

                    int power;
                    if (presentCell.getPower() == 0) {
                        power = cell.getPower();
                    } else {
                        power = (presentCell.getPower() > cell.getPower()) ? cell.getPiece().getPower() : presentCell.getPower();
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

    @Override
    public void run() {
    }

    @Override
    public String checkWinConditions() {
        return "";
    }

}

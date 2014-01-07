package org.javatablegames.games.chess.gamefield;

import org.javatablegames.core.enums.CellState;
import org.javatablegames.core.enums.Direction;
import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.game.gamefield.ModelCell;
import org.javatablegames.core.model.game.piece.Piece;
import org.javatablegames.core.model.position.Position;
import org.javatablegames.games.chess.piece.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ChessField extends Gamefield {

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
                setCell(new ModelCell(position, 0, color, CellState.DEFAULT), position);
            }
        }
    }

    public void selectCell(ModelCell modelCell) {
        modelCell.updateCellState(CellState.SELECTED);
        selectedCell = modelCell;
        setCellStateToAllowedCells();
    }

    public void reselectCell(ModelCell modelCell) {
        selectedCell.updateCellState(CellState.ALLOWED_PIECE);
        setDefaultCellStateToAllowedCells();
        selectCell(modelCell);
    }

    public void moveToCell(ModelCell modelCell) {
        ChessPiece piece = (ChessPiece) selectedCell.getPiece();
        piece.setPosition(modelCell.getPosition());
        piece.setMoved();

        if (piece.isAbleToPromote()) {
            promoteToQueen(piece);
        }

        if (piece.isJumpedPawn(selectedCell)) {
            ((Pawn) piece).setJumped();
        }

        modelCell.setPiece(selectedCell.getPiece());

        if (isCastlingMove(modelCell)) {
            moveRookOnCastling(modelCell);
        }

        selectedCell.setPiece(null);
        selectedCell = null;
        setTotalCellStateDefault();
    }

    public void captureToCell(ModelCell modelCell) {
        Piece piece = getPiece(modelCell.getPosition());

        if (piece == null) { //elPassant case
            piece = getPiece(new Position(selectedCell.getPosition().getRow(), modelCell.getPosition().getColumn()));
            getCell(piece.getPosition()).setPiece(null);
        }

        pieceSet.remove(piece);
        moveToCell(modelCell);
        setTotalCellStateDefault();
    }

    public void setSelectedCellByPosition(Position position) {
        selectedCell = getCell(position);
    }

    public boolean isCellUnderAttack(Position position, Side side) {
        if (isCellUnderKnightAttack(position, side)) {
            return true;
        }

        for (Direction direction : Direction.directions) {
            Position checkPosition = new Position(position);
            int distance = 0;

            while (checkPosition.isValid(size)) {
                checkPosition.moveInDirection(direction);
                distance++;

                if (isCellOpponent(checkPosition, side)) {
                    ChessPiece piece = (ChessPiece) getPiece(checkPosition);

                    if ((piece instanceof Pawn && distance > 1)
                            || (piece instanceof King && distance > 1)
                            || piece instanceof Knight) {
                        break;
                    }

                    if (Arrays.asList(piece.getCaptureDirections(side)).contains(direction)) {
                        return true;
                    } else {
                        break;
                    }
                } else if (!isCellEmpty(checkPosition)) {
                    break;
                }
            }
        }

        return false;
    }

    private boolean isCellUnderKnightAttack(Position position, Side side) {
        for (int deltaX = -2; deltaX <= 2; deltaX++) {
            for (int deltaY = -2; deltaY <= 2; deltaY++) {
                int row = position.getRow() + deltaY;
                int column = position.getColumn() + deltaX;
                Position checkPosition = new Position(row, column);

                if (((Math.abs(deltaX) + Math.abs(deltaY)) == 3) && checkPosition.isValid(size)
                        && isCellOpponent(checkPosition, side)) {
                    return getPiece(checkPosition) instanceof Knight;
                }
            }
        }

        return false;
    }

    private void setCellStateToAllowedCells() {
        ChessPiece piece = (ChessPiece) selectedCell.getPiece();
        List<ModelCell> cellList = piece.getCellsAllowedToMoveIn();

        for (ModelCell modelCell : cellList) {
            CellState cellState = (modelCell.getPiece() == null) ? CellState.ALLOWED_MOVE : CellState.ATTACKED;
            modelCell.updateCellState(cellState);
        }

        setCellStateIfElPassant(piece);
    }

    private void setCellStateIfElPassant(ChessPiece piece) {
        if (piece instanceof Pawn) {
            Set<ModelCell> elPassantCells = ((Pawn) piece).getElPassantCells();

            if (!elPassantCells.isEmpty()) {
                for (ModelCell modelCell : elPassantCells) {
                    modelCell.updateCellState(CellState.ATTACKED);
                }
            }
        }
    }

    private void setDefaultCellStateToAllowedCells() {
        ChessPiece piece = (ChessPiece) selectedCell.getPiece();
        List<ModelCell> cellList = piece.getCellsAllowedToMoveIn();

        for (ModelCell modelCell : cellList) {
            modelCell.updateCellState(CellState.DEFAULT);
        }
    }

    private void promoteToQueen(ChessPiece piece) {
        pieceSet.remove(piece);
        piece = new Queen(piece.getPosition(), piece.getSide(), this);
        pieceSet.add(piece);
        selectedCell.setPiece(piece);
    }

    private boolean isCastlingMove(ModelCell modelCell) {
        return (selectedCell.getPiece() instanceof King &&
                (Math.abs(selectedCell.getPosition().getColumn() - modelCell.getPosition().getColumn()) > 1));
    }

    private void moveRookOnCastling(ModelCell modelCell) {
        int kingRow = modelCell.getPosition().getRow();
        int kingColumn = modelCell.getPosition().getColumn();
        int rookColumn = (kingColumn == 6) ? 7 : 0;
        int newRookColumn = (kingColumn == 6) ? 5 : 3;

        Position rookPosition = new Position(kingRow, rookColumn);
        Position newRookPosition = new Position(kingRow, newRookColumn);
        getPiece(rookPosition).setPosition(newRookPosition);

        getCell(newRookPosition).setPiece(getPiece(rookPosition));
        getCell(rookPosition).setPiece(null);

        getCell(newRookPosition).setChanged(true);
        getCell(rookPosition).setChanged(true);
    }

}

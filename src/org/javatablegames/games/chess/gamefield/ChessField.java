package org.javatablegames.games.chess.gamefield;

import org.javatablegames.core.enums.CellState;
import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.game.gamefield.ModelCell;
import org.javatablegames.core.model.position.Position;
import org.javatablegames.games.chess.piece.ChessPiece;
import org.javatablegames.games.chess.piece.King;
import org.javatablegames.games.chess.piece.Pawn;
import org.javatablegames.games.chess.piece.Queen;

import java.util.List;

public class ChessField extends Gamefield {

    private static final String BLACK_CELL = "src/org/javatablegames/games/draughts/img/black-cell.png";
    private static final String WHITE_CELL = "src/org/javatablegames/games/draughts/img/white-cell.png";
    private ModelCell selectedCell = null;

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

        if (piece.isJumpedPawn(modelCell)) {
            ((Pawn) piece).setJumped();
        }

        modelCell.setPiece(selectedCell.getPiece());

        if (isCastlingMove(modelCell)) {
            moveRookOnCastling(modelCell);
        }

        selectedCell.setPiece(null);
        selectedCell = null;
    }

    public void captureToCell(ModelCell modelCell) {
        pieceSet.remove(getPiece(modelCell.getPosition()));
        moveToCell(modelCell);
    }

    private void setCellStateToAllowedCells() {
        ChessPiece piece = (ChessPiece) selectedCell.getPiece();
        List<ModelCell> cellList = piece.getCellsAllowedToMoveIn();

        for (ModelCell modelCell : cellList) {
            CellState cellState = (modelCell.getPiece() == null) ? CellState.ALLOWED_MOVE : CellState.ATTACKED;
            modelCell.updateCellState(cellState);
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

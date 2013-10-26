package game.set;

import enums.CellState;
import enums.Direction;
import enums.GameType;
import enums.Side;
import game.AbstractGame;
import model.impl.ModelCell;
import piece.Piece;
import piece.chess.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassicChess extends AbstractGame implements Chess {

    private static final int FIELD_SIZE = 8;
    private Side activeSide;
    private boolean isPieceSelected;
    private ModelCell selectedCell;

    public ClassicChess() {
        isPieceSelected = false;
        activeSide = Side.WHITE;
        setGameType(GameType.CHESS);
        initGameField();
        initPieces();
        addPiecesToGameField();
        giveMoveToOtherSide();
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
            addPieceImageToCell(piece);
        }
    }

    private void addPieceImageToCell(Piece piece) {
        ModelCell[][] gameField = getGameField();
        ModelCell fieldCell = gameField[piece.getRow()][piece.getColumn()];
        fieldCell.setPiece(piece);
    }

    @Override
    public int getFieldSize() {
        return FIELD_SIZE;
    }

    @Override
    public void clickCell(int row, int column) {
        ModelCell cell = getGameField()[row][column];
        if (CellState.ALLOWED == cell.getCellState()) {
            if (isPieceSelected && cell.getPiece() == null) {
                movePiece(row, column);
                activeSide = (activeSide == Side.WHITE) ? Side.BLACK : Side.WHITE;
                giveMoveToOtherSide();
                isPieceSelected = false;

            } else {
                if (isPieceSelected) {
                    activeSide = (activeSide == Side.WHITE) ? Side.WHITE : Side.BLACK;
                    giveMoveToOtherSide();
                } else {
                    isPieceSelected = true;
                }
                updateAllowedCells(cell);
                selectedCell = getGameField()[row][column];
                selectedCell.setCellState(CellState.CHOOSE);
                selectedCell.setChanged(true);
            }
        }
    }

    private void movePiece(int row, int column) {
        getGameField()[row][column].setPiece(selectedCell.getPiece());
        selectedCell.setPiece(null);
    }

    private void giveMoveToOtherSide() {
        for (int row = 0; row < FIELD_SIZE; row++) {
            for (int column = 0; column < FIELD_SIZE; column++) {
                CellState cellState = (getGameField()[row][column].getPiece() == null) ?
                        CellState.DEFAULT :
                        (getGameField()[row][column].getPiece().getSide() == activeSide) ?
                                CellState.ALLOWED :
                                CellState.DEFAULT;
                if (getGameField()[row][column].getCellState() != cellState) {
                    getGameField()[row][column].setCellState(cellState);
                    getGameField()[row][column].setChanged(true);
                }
            }
        }
    }

    private void updateAllowedCells(ModelCell cell) {
        Piece piece = cell.getPiece();
        if (piece instanceof Pawn) {
            Direction direction = (piece.getSide() == Side.WHITE) ?
                    Direction.NORTH :
                    Direction.SOUTH;
            int moveLength = 1;
            if ((direction == Direction.NORTH && cell.getRow() == 6)
                    || (direction == Direction.SOUTH && cell.getRow() == 1)) {
                moveLength = 2;
            }
            updateAllowedCellsToMove(cell, new Direction[]{direction}, moveLength);
        }
    }

    private void updateAllowedCellsToMove(ModelCell cell, Direction[] directions, int moveLength) {
        for (Direction direction : directions) {
            int deltaX = defineDeltaX(direction);
            int deltaY = defineDeltaY(direction);
            int row = cell.getRow();
            int column = cell.getColumn();

            for (int move = 0; move < moveLength; move++) {
                row += deltaY;
                column += deltaX;
                if (row < FIELD_SIZE && column < FIELD_SIZE) {
                    if (getGameField()[row][column].getPiece() == null) {
                        getGameField()[row][column].setCellState(CellState.ALLOWED);
                        getGameField()[row][column].setChanged(true);
                    } else {
                        break;
                    }
                }
            }
        }
    }

    private int defineDeltaX(Direction direction) {
        Direction[] east = new Direction[]{Direction.NORTHEAST, Direction.EAST, Direction.SOUTHEAST};
        Direction[] west = new Direction[]{Direction.NORTHWEST, Direction.WEST, Direction.SOUTHWEST};
        int delta = 0;
        if (Arrays.asList(east).contains(direction)) {
            delta++;
        } else if (Arrays.asList(west).contains(direction)) {
            delta--;
        }
        return delta;
    }

    private int defineDeltaY(Direction direction) {
        Direction[] south = new Direction[]{Direction.SOUTHWEST, Direction.SOUTH, Direction.SOUTHEAST};
        Direction[] north = new Direction[]{Direction.NORTHWEST, Direction.NORTH, Direction.NORTHEAST};
        int delta = 0;
        if (Arrays.asList(south).contains(direction)) {
            delta++;
        } else if (Arrays.asList(north).contains(direction)) {
            delta--;
        }
        return delta;
    }
}

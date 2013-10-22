package game.set;

import enums.CellState;
import enums.GameType;
import enums.Side;
import game.AbstractGame;
import model.impl.ModelCell;
import piece.Piece;
import piece.chess.*;

import java.util.ArrayList;
import java.util.List;

public class ClassicChess extends AbstractGame implements Chess {

    private static final int FIELD_SIZE = 8;
    private Side activeSide;
    private boolean isPieceSelected;
    private int selectedPieceRow;
    private int selectedPieceColumn;

    public ClassicChess() {
        setGameType(GameType.CHESS);
        initGameField();
        initPieces();
        addPiecesToGameField();
        isPieceSelected = false;
        activeSide = Side.WHITE;
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
        if (isPieceSelected) {
            if (null == getGameField()[row][column].getPiece()) {
                getGameField()[selectedPieceRow][selectedPieceColumn].setCellState(CellState.DEFAULT);
                getGameField()[selectedPieceRow][selectedPieceColumn].setChanged(true);
                isPieceSelected = false;
            }
        } else {
            if (null != getGameField()[row][column].getPiece()) {
                if (activeSide == getGameField()[row][column].getPiece().getSide()) {
                    getGameField()[row][column].setCellState(CellState.CHOOSE);
                    getGameField()[row][column].setChanged(true);
                    isPieceSelected = true;
                    selectedPieceRow = row;
                    selectedPieceColumn = column;
                    activeSide = (activeSide == Side.WHITE) ? Side.BLACK : Side.WHITE;
                }
            }
        }
    }
}

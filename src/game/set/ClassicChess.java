package game.set;

import enums.CellState;
import enums.GameType;
import enums.Side;
import game.AbstractGame;
import model.impl.ModelCell;
import piece.ChessPawn;
import piece.Piece;

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
                gameField[row][column] = new ModelCell(row, column, row
                        + column, color, null, CellState.DEFAULT);
            }
        }

        setGameField(gameField);
    }

    private void initPieces() {
        List<Piece> pieces = new ArrayList<Piece>();
        for (int i = 0; i < FIELD_SIZE; i++) {
            pieces.add(new ChessPawn(6, i, Side.WHITE));
            pieces.add(new ChessPawn(1, i, Side.BLACK));
        }

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

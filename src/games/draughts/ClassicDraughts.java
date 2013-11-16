package games.draughts;

import enums.CellState;
import enums.GameType;
import enums.Side;
import games.draughts.piece.Man;
import model.Model;
import model.ModelCell;
import model.game.AbstractGame;
import model.piece.Piece;

import java.util.ArrayList;
import java.util.List;

public class ClassicDraughts extends AbstractGame implements Draughts {

    private static final int FIELD_SIZE = 8;

    public ClassicDraughts(Model model, GameType gameType) {
        super(model, gameType);
        initGameField();
        initPieces();
        addPiecesToGameField();
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

        for (int row = 0; row < FIELD_SIZE; row++) {
            for (int column = 0; column < FIELD_SIZE; column++) {
                if ((row + column) % 2 != 0) {
                    if (row < 3) {
                        pieces.add(new Man(row, column, Side.BLACK));
                    } else if (row > 4) {
                        pieces.add(new Man(row, column, Side.WHITE));
                    }
                }
            }
        }

        setPieces(pieces);
    }

    private void addPiecesToGameField() {
        for (Piece piece : getPieces()) {
            ModelCell cell = getGameField()[piece.getRow()][piece.getColumn()];
            cell.setPiece(piece);
        }
    }

    @Override
    public int getFieldSize() {
        return FIELD_SIZE;
    }

    @Override
    public void clickCell(int row, int column) {
    }

}

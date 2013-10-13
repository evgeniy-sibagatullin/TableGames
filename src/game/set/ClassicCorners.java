package game.set;

import enums.GameType;
import game.AbstractGame;
import model.impl.ModelCell;
import piece.Piece;

import java.util.List;

public class ClassicCorners extends AbstractGame implements Corners {

    private static final int FIELD_SIZE = 10;

    public ClassicCorners() {
        setGameType(GameType.CORNERS);
        setScore(0);
        initGameField();
    }

    private void initGameField() {
        ModelCell[][] gameField = new ModelCell[FIELD_SIZE][FIELD_SIZE];
        for (int row = 0; row < FIELD_SIZE; row++) {
            for (int column = 0; column < FIELD_SIZE; column++) {
                String color = BLACK_CELL;
                gameField[row][column] = new ModelCell(row, column, color, null);
            }
        }

        setGameField(gameField);
    }

    @Override
    public int getFieldSize() {
        return FIELD_SIZE;
    }

    @Override
    public List<Piece> getPieces() {
        return null;
    }

    @Override
    public void setPieces(List<Piece> pieces) {
    }

    @Override
    public void clickCell(int row, int column) {
    }

}

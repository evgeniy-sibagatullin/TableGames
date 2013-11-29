package games.draughts.gamefield;

import enums.CellState;
import model.game.gamefield.Gamefield;
import model.game.gamefield.ModelCell;
import model.game.position.Position;

public class DraughtsField extends Gamefield {

    private static final String BLACK_CELL = "img/chess/black-cell.png";
    private static final String WHITE_CELL = "img/chess/white-cell.png";

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

}

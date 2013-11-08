package games.barleyBreak;

import enums.CellState;
import enums.GameType;
import model.ModelCell;
import model.game.AbstractGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClassicBarleyBreak extends AbstractGame implements BarleyBreak {

    private static final int FIELD_SIZE = 4;
    private ModelCell emptyCell = new ModelCell(FIELD_SIZE - 1, FIELD_SIZE - 1, 0,
            null, null, CellState.SELECTED);

    private static final int PREPARATION_RANDOM_MOVES_NEEDED = 50;
    private boolean isFieldPrepared = false;
    private int preparationRandomMovesCount = 0;
    private Random random = new Random();

    public ClassicBarleyBreak() {
        setGameType(GameType.BARLEY_BREAK);
        initGameField();
        prepareField();
    }

    @Override
    public int getFieldSize() {
        return FIELD_SIZE;
    }

    @Override
    public boolean clickCell(int row, int column) {
        boolean isMoveReal = false;
        ModelCell selectedCell = getGameField()[row][column];
        if (selectedCell.getCellState() == CellState.ALLOWED_PIECE) {
            swapCells(selectedCell);
            isMoveReal = true;
        }
        return isMoveReal;
    }

    private void initGameField() {
        List<Integer> numbers = new ArrayList<Integer>();
        for (int index = 0; index < FIELD_SIZE * FIELD_SIZE - 1; index++) {
            numbers.add(index + 1);
        }
        numbers.add(0);

        ModelCell[][] gameField = new ModelCell[FIELD_SIZE][FIELD_SIZE];

        for (int row = 0; row < FIELD_SIZE; row++) {
            for (int column = 0; column < FIELD_SIZE; column++) {

                int power = numbers.get(row * FIELD_SIZE + column);
                gameField[row][column] = new ModelCell(row, column, power,
                        COLOR_CELL, null, CellState.DEFAULT);
            }
        }
        gameField[FIELD_SIZE - 1][FIELD_SIZE - 1] = emptyCell;
        setGameField(gameField);
        updateNeighbourCellStates(FIELD_SIZE - 1, FIELD_SIZE - 1, CellState.ALLOWED_PIECE);
    }

    private void prepareField() {
        while (!isFieldPrepared) {
            performRandomMove();
            preparationRandomMovesCount++;
            if (preparationRandomMovesCount > PREPARATION_RANDOM_MOVES_NEEDED) {
                isFieldPrepared = true;
            }
        }
    }

    private void performRandomMove() {
        boolean moveHappened = false;
        while (!moveHappened) {
            int row = emptyCell.getRow();
            int column = emptyCell.getColumn();
            int delta = (random.nextDouble() > 0.5) ?
                    -1 : 1;
            if (preparationRandomMovesCount % 2 == 0) {
                row += delta;
            } else {
                column += delta;
            }
            if (row > -1 && row < FIELD_SIZE && column > -1 && column < FIELD_SIZE) {
                moveHappened = clickCell(row, column);
            }
        }
    }

    private void swapCells(ModelCell selectedCell) {
        getGameField()[emptyCell.getRow()][emptyCell.getColumn()] =
                new ModelCell(emptyCell.getRow(), emptyCell.getColumn(), selectedCell.getPower(),
                        COLOR_CELL, null, CellState.DEFAULT);

        updateNeighbourCellStates(emptyCell.getRow(), emptyCell.getColumn(), CellState.DEFAULT);
        updateNeighbourCellStates(selectedCell.getRow(), selectedCell.getColumn(), CellState.ALLOWED_PIECE);

        emptyCell.setRow(selectedCell.getRow());
        emptyCell.setColumn(selectedCell.getColumn());

        getGameField()[selectedCell.getRow()][selectedCell.getColumn()] = emptyCell;
        getGameField()[selectedCell.getRow()][selectedCell.getColumn()].setChanged(true);

    }

    private void updateNeighbourCellStates(int row, int column, CellState cellState) {
        if (row > 0) {
            getGameField()[row - 1][column].setCellState(cellState);
            getGameField()[row - 1][column].setChanged(true);
        }
        if (row < FIELD_SIZE - 1) {
            getGameField()[row + 1][column].setCellState(cellState);
            getGameField()[row + 1][column].setChanged(true);
        }
        if (column > 0) {
            getGameField()[row][column - 1].setCellState(cellState);
            getGameField()[row][column - 1].setChanged(true);
        }
        if (column < FIELD_SIZE - 1) {
            getGameField()[row][column + 1].setCellState(cellState);
            getGameField()[row][column + 1].setChanged(true);
        }
    }


    public boolean checkWinConditions() {
        for (int row = 0; row < FIELD_SIZE; row++) {
            for (int column = 0; column < FIELD_SIZE; column++) {
                int power = getGameField()[row][column].getPower();
                if (row * FIELD_SIZE + column + 1 != power) {
                    if ((row != FIELD_SIZE - 1 || column != FIELD_SIZE - 1))
                        return false;
                }
            }
        }
        return true;
    }

}

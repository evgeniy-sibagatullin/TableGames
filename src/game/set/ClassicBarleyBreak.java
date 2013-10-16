package game.set;

import enums.CellState;
import enums.Direction;
import enums.GameType;
import game.AbstractGame;
import model.impl.ModelCell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class
        ClassicBarleyBreak extends AbstractGame implements BarleyBreak {

    private static final int FIELD_SIZE = 4;

    public ClassicBarleyBreak() {
        setGameType(GameType.BARLEY_BREAK);
        initGameField();
    }

    private void initGameField() {
        List<Integer> numbers = new ArrayList<Integer>();
        for (int index = 0; index < FIELD_SIZE * FIELD_SIZE - 1; index++) {
            numbers.add(index + 1);
        }
        Collections.shuffle(numbers);
        numbers.add(0);

        ModelCell[][] gameField = new ModelCell[FIELD_SIZE][FIELD_SIZE];

        for (int row = 0; row < FIELD_SIZE; row++) {
            for (int column = 0; column < FIELD_SIZE; column++) {

                int power = numbers.get(row * FIELD_SIZE + column);
                gameField[row][column] = new ModelCell(row, column, power,
                        COLOR_CELL, null, CellState.DEFAULT);
            }
        }
        gameField[FIELD_SIZE - 1][FIELD_SIZE - 1].setBackgroundImage(null);

        setGameField(gameField);
    }

    @Override
    public int getFieldSize() {
        return FIELD_SIZE;
    }

    @Override
    public void clickCell(int row, int column) {
        Direction moveDirection = moveDirection(row, column);
        if (moveDirection != Direction.NONE) {
            performMove(row, column, moveDirection);
        }
    }

    private Direction moveDirection(int row, int column) {
        if (row > 0 && getGameField()[row - 1][column].getPower() == 0) {
            return Direction.NORTH;
        }
        if (row < FIELD_SIZE - 1 && getGameField()[row + 1][column].getPower() == 0) {
            return Direction.SOUTH;
        }
        if (column > 0 && getGameField()[row][column - 1].getPower() == 0) {
            return Direction.WEST;
        }
        if (column < FIELD_SIZE - 1 && getGameField()[row][column + 1].getPower() == 0) {
            return Direction.EAST;
        }
        return Direction.NONE;
    }

    private void performMove(int row, int column, Direction direction) {
        int swRow = row;
        int swColumn = column;
        if (direction == Direction.NORTH) {
            swRow--;
        } else if (direction == Direction.SOUTH) {
            swRow++;
        } else if (direction == Direction.EAST) {
            swColumn++;
        } else {
            swColumn--;
        }
        swapCells(row, column, swRow, swColumn);
    }

    private void swapCells(int row, int column, int swRow, int swColumn) {
        getGameField()[swRow][swColumn].setPower(getGameField()[row][column].getPower());
        getGameField()[swRow][swColumn].setBackgroundImage(COLOR_CELL);
        getGameField()[row][column].setPower(0);
        getGameField()[row][column].setBackgroundImage(null);
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

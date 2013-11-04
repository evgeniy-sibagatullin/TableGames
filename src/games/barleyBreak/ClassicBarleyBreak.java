package games.barleyBreak;

import enums.CellState;
import enums.Direction;
import enums.GameType;
import model.ModelCell;
import model.game.AbstractGame;

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
        gameField[FIELD_SIZE - 1][FIELD_SIZE - 1].setCellState(CellState.CHOOSE);
        setGameField(gameField);
        updateNeighbourCellStates(FIELD_SIZE - 1, FIELD_SIZE - 1, CellState.ALLOWED);
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
        getGameField()[swRow][swColumn].setChanged(true);
        updateNeighbourCellStates(swRow, swColumn, CellState.DEFAULT);
        getGameField()[row][column].setPower(0);
        getGameField()[row][column].setBackgroundImage(null);
        getGameField()[row][column].setChanged(true);
        updateNeighbourCellStates(row, column, CellState.ALLOWED);
        getGameField()[row][column].setCellState(CellState.CHOOSE);
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

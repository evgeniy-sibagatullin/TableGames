package org.javatablegames.games.barleyBreak.gamefield;

import org.javatablegames.core.enums.CellState;
import org.javatablegames.core.enums.Direction;
import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.game.gamefield.ModelCell;
import org.javatablegames.core.model.position.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BarleyBreakField extends Gamefield {

    private final Random random = new Random();

    private static final String COLOR_CELL = "img/barleyBreak/SolidGreen.bmp";
    private ModelCell emptyCell;

    @Override
    public void initializeGamefield() {
        size = 4;
        field = new ModelCell[size][size];

        List<Integer> numbers = new ArrayList<Integer>();
        for (int index = 0; index < size * size - 1; index++) {
            numbers.add(index + 1);
        }
        numbers.add(0);

        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                Position position = new Position(row, column);
                int power = numbers.get(row * size + column);

                setCell(new ModelCell(position, power, COLOR_CELL,
                        CellState.DEFAULT), position);
            }
        }

        addEmptyCell();
    }

    private void addEmptyCell() {
        Position emptyPosition = new Position(size - 1, size - 1);
        emptyCell = new ModelCell(emptyPosition, 0, null,
                CellState.SELECTED);
        setCell(emptyCell, emptyPosition);
        updateNeighbourCellStates(emptyPosition, CellState.ALLOWED_PIECE);
    }

    private void updateNeighbourCellStates(Position position,
                                           CellState cellState) {
        int row = position.getRow();
        int column = position.getColumn();

        if (row > 0) {
            updateCellState(new Position(row - 1, column), cellState);
        }
        if (row < size - 1) {
            updateCellState(new Position(row + 1, column), cellState);
        }
        if (column > 0) {
            updateCellState(new Position(row, column - 1), cellState);
        }
        if (column < size - 1) {
            updateCellState(new Position(row, column + 1), cellState);
        }
    }

    private void updateCellState(Position position, CellState cellState) {
        getCell(position).setCellState(cellState);
        getCell(position).setChanged(true);
    }

    public void moveEmptyCellToPosition(Position position) {
        ModelCell selectedCell = getCell(position);
        setCell(new ModelCell(emptyCell.getPosition(), selectedCell.getPower(),
                COLOR_CELL, CellState.DEFAULT), emptyCell.getPosition());

        updateNeighbourCellStates(emptyCell.getPosition(), CellState.DEFAULT);
        updateNeighbourCellStates(selectedCell.getPosition(),
                CellState.ALLOWED_PIECE);

        emptyCell.setPosition(selectedCell.getPosition());

        setCell(emptyCell, selectedCell.getPosition());
        getCell(selectedCell.getPosition()).setChanged(true);
    }

    public void performRandomMove(int randomMovesCount) {
        boolean moveHappened = false;

        while (!moveHappened) {
            Position emptyCellPosition = new Position(emptyCell.getPosition());

            Direction randomVertical = (random.nextDouble() > 0.5) ? Direction.NORTH
                    : Direction.SOUTH;
            Direction randomHorizontal = (random.nextDouble() > 0.5) ? Direction.EAST
                    : Direction.WEST;

            if (randomMovesCount % 2 == 0) {
                emptyCellPosition.moveInDirection(randomVertical);
            } else {
                emptyCellPosition.moveInDirection(randomHorizontal);
            }

            if (emptyCellPosition.isValid(size)) {
                moveEmptyCellToPosition(emptyCellPosition);
                moveHappened = true;
            }
        }
    }

}

package org.javatablegames.games.chess.piece;

import org.javatablegames.core.enums.Direction;
import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.position.Position;

public class SuperQueen extends Queen {

    public static final int SUPER_POWER = 700;

    public SuperQueen(Position position, Side side, int power, Gamefield gameField) {
        super(position, side, power, gameField);
    }

    @Override
    public boolean isAbleToMove() {
        searchCellsAllowedToMoveIn(Direction.directions, MOVE_LENGTH);
        searchCellsAllowedToMoveInAsKnight();
        return !cellsAllowedToMoveIn.isEmpty();
    }

    private void searchCellsAllowedToMoveInAsKnight() {
        for (int deltaX = -2; deltaX <= 2; deltaX++) {
            for (int deltaY = -2; deltaY <= 2; deltaY++) {
                int row = getPosition().getRow() + deltaY;
                int column = getPosition().getColumn() + deltaX;
                checkPosition = new Position(row, column);

                if (((Math.abs(deltaX) + Math.abs(deltaY)) == 3)
                        && checkPosition.isValid(gamefield.getSize())
                        && (gamefield.isCellEmpty(checkPosition) || gamefield.isCellOpponent(checkPosition, side))
                        ) {
                    cellsAllowedToMoveIn.add(gamefield.getCell(checkPosition));
                }
            }
        }
    }
}

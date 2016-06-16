package org.javatablegames.games.chess.piece;

import org.javatablegames.core.enums.Direction;
import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.position.Position;

public class Bishop extends ChessPiece {

    private static final Direction[] MOVE_DIRECTIONS =
            {Direction.NORTHEAST, Direction.NORTHWEST, Direction.SOUTHEAST, Direction.SOUTHWEST};
    private static final int MOVE_LENGTH = 7;
    public static final int POWER = 25;

    public Bishop(Position position, Side side, int power, Gamefield gameField) {
        super(position, side, power, gameField);
    }

    @Override
    public String getImagePath() {
        return PIECE_IMAGE_PATH + ((getSide() == Side.WHITE) ?
                "Chess-BishopW.png" :
                "Chess-BishopB.png");
    }

    @Override
    public Direction[] getCaptureDirections(Side side) {
        return MOVE_DIRECTIONS;
    }

    @Override
    public boolean isAbleToMove() {
        searchCellsAllowedToMoveIn(MOVE_DIRECTIONS, MOVE_LENGTH);
        return !cellsAllowedToMoveIn.isEmpty();
    }

}

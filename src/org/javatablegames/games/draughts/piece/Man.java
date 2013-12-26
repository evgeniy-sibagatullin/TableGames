package org.javatablegames.games.draughts.piece;

import org.javatablegames.core.enums.Direction;
import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.position.Position;

public class Man extends DraughtsPiece {

    private static final Direction[] WHITE_MAN_MOVE_DIRECTIONS =
            {Direction.NORTHEAST,
                    Direction.NORTHWEST};

    private static final Direction[] BLACK_MAN_MOVE_DIRECTIONS =
            {Direction.SOUTHEAST,
                    Direction.SOUTHWEST};

    private static final Direction[] MAN_CAPTURE_DIRECTIONS =
            {Direction.NORTHEAST,
                    Direction.NORTHWEST,
                    Direction.SOUTHEAST,
                    Direction.SOUTHWEST};

    private static final int MOVE_LENGTH = 1;

    public Man(Position position, Side side, Gamefield gameField) {
        super(position, side, 10, gameField);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "src/org/javatablegames/games/draughts/img/Draughts-ManB.png" :
                "src/org/javatablegames/games/draughts/img/Draughts-ManR.png";
    }

    @Override
    public boolean isAbleToCapture() {
        searchCellsAllowedToCapture(MAN_CAPTURE_DIRECTIONS, MOVE_LENGTH);
        return !cellsAllowedToCaptureIn.isEmpty();
    }

    @Override
    public boolean isAbleToMove() {
        Direction[] directions = (side == Side.WHITE) ?
                WHITE_MAN_MOVE_DIRECTIONS :
                BLACK_MAN_MOVE_DIRECTIONS;
        searchCellsAllowedToMoveIn(directions, MOVE_LENGTH);
        return !cellsAllowedToMoveIn.isEmpty();
    }

}

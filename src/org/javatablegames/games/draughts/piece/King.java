package org.javatablegames.games.draughts.piece;

import org.javatablegames.core.enums.Direction;
import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.position.Position;

public class King extends DraughtsPiece {

    private static final Direction[] KING_MOVE_DIRECTIONS =
            {Direction.NORTHEAST,
                    Direction.NORTHWEST,
                    Direction.SOUTHEAST,
                    Direction.SOUTHWEST};

    private static final int MOVE_LENGTH = 7;

    public King(Position position, Side side, Gamefield gameField) {
        super(position, side, 25, gameField);
    }

    @Override
    public String getImagePath() {
        return (side == Side.WHITE) ?
                "img/draughts/Draughts-KingB.png" :
                "img/draughts/Draughts-KingR.png";
    }

    @Override
    public boolean isAbleToCapture() {
        searchCellsAllowedToCapture(KING_MOVE_DIRECTIONS, MOVE_LENGTH);
        return !cellsAllowedToCaptureIn.isEmpty();
    }

    @Override
    public boolean isAbleToMove() {
        searchCellsAllowedToMoveIn(KING_MOVE_DIRECTIONS, MOVE_LENGTH);
        return !cellsAllowedToMoveIn.isEmpty();
    }

}

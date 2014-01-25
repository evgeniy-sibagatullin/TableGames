package org.javatablegames.games.chess.piece;

import org.javatablegames.core.enums.Direction;
import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.position.Position;

public class Queen extends ChessPiece {

    private static final int MOVE_LENGTH = 7;

    public Queen(Position position, Side side, Gamefield gameField) {
        super(position, side, 70, gameField);
    }

    @Override
    public String getImagePath() {
        return PIECE_IMAGE_PATH + ((getSide() == Side.WHITE) ?
                "Chess-QueenW.png" :
                "Chess-QueenB.png");
    }

    @Override
    public boolean isAbleToMove() {
        searchCellsAllowedToMoveIn(Direction.directions, MOVE_LENGTH);
        return !cellsAllowedToMoveIn.isEmpty();
    }

}

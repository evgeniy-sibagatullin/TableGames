package games.draughts.piece;

import enums.Direction;
import enums.Side;
import model.ModelCell;

public class King extends DraughtsPiece {

    private static final Direction[] KING_MOVE_DIRECTIONS =
            {Direction.NORTHEAST,
                    Direction.NORTHWEST,
                    Direction.SOUTHEAST,
                    Direction.SOUTHWEST};

    private static final int MOVE_LENGTH = 7;

    public King(int row, int column, Side side, ModelCell[][] gameField) {
        super(row, column, side, gameField);
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

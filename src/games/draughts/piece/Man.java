package games.draughts.piece;

import enums.Direction;
import enums.Side;
import model.ModelCell;

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

    public Man(int row, int column, Side side, ModelCell[][] gameField) {
        super(row, column, side, gameField);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/draughts/Draughts-ManB.png" :
                "img/draughts/Draughts-ManR.png";
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

package org.javatablegames.games.chess.piece;

import org.javatablegames.core.enums.Direction;
import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.game.gamefield.ModelCell;
import org.javatablegames.core.model.game.piece.Piece;
import org.javatablegames.core.model.position.Position;

import java.util.ArrayList;

public class Pawn extends ChessPiece {

    private static final Direction WHITE_PAWN_MOVE_DIRECTION = Direction.NORTH;
    private static final Direction[] WHITE_PAWN_CAPTURE_DIRECTIONS = {Direction.NORTHEAST, Direction.NORTHWEST};
    private static final Direction BLACK_PAWN_MOVE_DIRECTION = Direction.SOUTH;
    private static final Direction[] BLACK_PAWN_CAPTURE_DIRECTIONS = {Direction.SOUTHEAST, Direction.SOUTHWEST};
    private boolean isJumped;

    public Pawn(Position position, Side side, Gamefield gameField) {
        super(position, side, gameField);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "src/org/javatablegames/games/chess/img/Chess-PawnW.png" :
                "src/org/javatablegames/games/chess/img/Chess-PawnB.png";
    }

    @Override
    public boolean isAbleToMove() {
        isJumped = false;
        searchCellsAllowedToMoveIn();
        return !cellsAllowedToMoveIn.isEmpty();
    }

    public boolean isJumped() {
        return isJumped;
    }

    public void setJumped() {
        this.isJumped = true;
    }

    private void searchCellsAllowedToMoveIn() {
        cellsAllowedToMoveIn = new ArrayList<ModelCell>();

        Direction moveDirection = (side.equals(Side.WHITE)) ?
                WHITE_PAWN_MOVE_DIRECTION : BLACK_PAWN_MOVE_DIRECTION;
        checkPosition = new Position(position);

        checkNextMoveInDirection(moveDirection);

        if (!cellsAllowedToMoveIn.isEmpty() &&
                ((side.equals(Side.WHITE) && getPosition().getRow() == 6)
                        || (side.equals(Side.BLACK) && getPosition().getRow() == 1))) {
            checkNextMoveInDirection(moveDirection);
        }

        Direction[] captureDirections = (side.equals(Side.WHITE)) ?
                WHITE_PAWN_CAPTURE_DIRECTIONS : BLACK_PAWN_CAPTURE_DIRECTIONS;

        for (Direction direction : captureDirections) {
            checkPosition = new Position(position);
            checkPosition.moveInDirection(direction);
            if (gamefield.isCellOpponent(checkPosition, side)) {
                cellsAllowedToMoveIn.add(gamefield.getCell(checkPosition));
            }
        }

        //elPassant
        checkPosition = new Position(position);
        checkPosition.moveInDirection(Direction.EAST);
        if (gamefield.isCellOpponent(checkPosition, side)) {
            ChessPiece chessPiece = (ChessPiece) gamefield.getPiece(checkPosition);
            if (chessPiece instanceof Pawn && ((Pawn) chessPiece).isJumped) {
                checkPosition.moveInDirection(moveDirection);
                cellsAllowedToMoveIn.add(gamefield.getCell(checkPosition));
            }
        }

        checkPosition = new Position(position);
        checkPosition.moveInDirection(Direction.WEST);
        if (gamefield.isCellOpponent(checkPosition, side)) {
            ChessPiece chessPiece = (ChessPiece) gamefield.getPiece(checkPosition);
            if (chessPiece instanceof Pawn && ((Pawn) chessPiece).isJumped) {
                checkPosition.moveInDirection(moveDirection);
                cellsAllowedToMoveIn.add(gamefield.getCell(checkPosition));
            }
        }

    }

    private void checkNextMoveInDirection(Direction moveDirection) {
        checkPosition.moveInDirection(moveDirection);
        if (gamefield.isCellEmpty(checkPosition)) {
            cellsAllowedToMoveIn.add(gamefield.getCell(checkPosition));
        }
    }

}

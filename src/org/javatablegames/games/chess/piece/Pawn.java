package org.javatablegames.games.chess.piece;

import org.javatablegames.core.enums.Direction;
import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.game.gamefield.ModelCell;
import org.javatablegames.core.model.position.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Pawn extends ChessPiece {

    private static final Direction WHITE_PAWN_MOVE_DIRECTION = Direction.NORTH;
    private static final Direction[] WHITE_PAWN_CAPTURE_DIRECTIONS = {Direction.NORTHEAST, Direction.NORTHWEST};
    private static final Direction BLACK_PAWN_MOVE_DIRECTION = Direction.SOUTH;
    private static final Direction[] BLACK_PAWN_CAPTURE_DIRECTIONS = {Direction.SOUTHEAST, Direction.SOUTHWEST};
    private boolean isJumped;
    private Set<ModelCell> elPassantCells;

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
    public Direction[] getCaptureDirections(Side side) {
        return (side.equals(Side.WHITE)) ?
                WHITE_PAWN_CAPTURE_DIRECTIONS : BLACK_PAWN_CAPTURE_DIRECTIONS;
    }

    @Override
    public boolean isAbleToMove() {
        isJumped = false;
        elPassantCells = new HashSet<ModelCell>();

        searchCellsAllowedToMoveIn();
        return !cellsAllowedToMoveIn.isEmpty();
    }

    public void setJumped() {
        this.isJumped = true;
    }

    public Set<ModelCell> getElPassantCells() {
        return elPassantCells;
    }

    private void searchCellsAllowedToMoveIn() {
        cellsAllowedToMoveIn = new ArrayList<ModelCell>();
        Direction moveDirection = (side.equals(Side.WHITE)) ?
                WHITE_PAWN_MOVE_DIRECTION : BLACK_PAWN_MOVE_DIRECTION;

        checkMoves(moveDirection);
        checkElPassant(moveDirection);
        checkElPassant(moveDirection);
        checkCaptures();
    }

    private void checkMoves(Direction moveDirection) {
        checkPosition = new Position(position);
        checkNextMoveInDirection(moveDirection);

        if (!cellsAllowedToMoveIn.isEmpty() && isStartPosition()) {
            checkNextMoveInDirection(moveDirection);
        }
    }

    private void checkNextMoveInDirection(Direction moveDirection) {
        checkPosition.moveInDirection(moveDirection);
        if (gamefield.isCellEmpty(checkPosition)) {
            cellsAllowedToMoveIn.add(gamefield.getCell(checkPosition));
        }
    }

    private boolean isStartPosition() {
        return (side.equals(Side.WHITE) && getPosition().getRow() == 6)
                || (side.equals(Side.BLACK) && getPosition().getRow() == 1);
    }

    private void checkElPassant(Direction moveDirection) {
        Direction[] horShiftDirections = {Direction.EAST, Direction.WEST};

        for (Direction horShiftDirection : horShiftDirections) {
            checkPosition = new Position(position);
            checkPosition.moveInDirection(horShiftDirection);

            if (isCheckJumpedPawn()) {
                checkPosition.moveInDirection(moveDirection);
                cellsAllowedToMoveIn.add(gamefield.getCell(checkPosition));
                elPassantCells.add(gamefield.getCell(checkPosition));
            }
        }
    }

    private boolean isCheckJumpedPawn() {
        if (gamefield.isCellOpponent(checkPosition, side)) {
            ChessPiece chessPiece = (ChessPiece) gamefield.getPiece(checkPosition);
            return (chessPiece instanceof Pawn && ((Pawn) chessPiece).isJumped);
        } else {
            return false;
        }
    }

    private void checkCaptures() {
        Direction[] captureDirections = getCaptureDirections(side);

        for (Direction direction : captureDirections) {
            checkPosition = new Position(position);
            checkPosition.moveInDirection(direction);
            if (gamefield.isCellOpponent(checkPosition, side)) {
                cellsAllowedToMoveIn.add(gamefield.getCell(checkPosition));
            }
        }
    }

}

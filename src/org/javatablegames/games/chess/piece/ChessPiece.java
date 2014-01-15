package org.javatablegames.games.chess.piece;

import org.javatablegames.core.enums.Direction;
import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.game.gamefield.ModelCell;
import org.javatablegames.core.model.game.piece.Piece;
import org.javatablegames.core.model.position.Position;

import java.util.ArrayList;
import java.util.List;

public abstract class ChessPiece extends Piece {

    protected List<ModelCell> cellsAllowedToMoveIn;
    protected Position checkPosition;
    protected boolean everMoved = false;

    protected ChessPiece(Position position, Side side, int power, Gamefield gameField) {
        super(position, side, power, gameField);
    }

    public List<ModelCell> getCellsAllowedToMoveIn() {
        return cellsAllowedToMoveIn;
    }

    public abstract boolean isAbleToMove();

    public Direction[] getCaptureDirections(Side side) {
        return Direction.directions;
    }

    protected void searchCellsAllowedToMoveIn(Direction[] directions, int moveLength) {
        cellsAllowedToMoveIn = new ArrayList<ModelCell>();

        for (Direction direction : directions) {
            checkPosition = new Position(position);

            for (int moveIndex = 0; moveIndex < moveLength; moveIndex++) {
                checkPosition.moveInDirection(direction);

                if (gamefield.isCellEmpty(checkPosition)) {
                    cellsAllowedToMoveIn.add(gamefield.getCell(checkPosition));
                } else {
                    if (gamefield.isCellOpponent(checkPosition, side)) {
                        cellsAllowedToMoveIn.add(gamefield.getCell(checkPosition));
                    }
                    break;
                }
            }
        }
    }

    public boolean isAbleToPromote() {
        return (this instanceof Pawn &&
                ((side == Side.WHITE && position.getRow() == 0) || (side == Side.BLACK && position.getRow() == 7)));
    }

    public boolean isJumpedPawn(ModelCell modelCell) {
        return (this instanceof Pawn && Math.abs(modelCell.getPosition().getRow() - getPosition().getRow()) > 1);
    }

    public boolean isEverMoved() {
        return everMoved;
    }

    public void setMoved() {
        this.everMoved = true;
    }

}

package org.javatablegames.games.draughts.piece;

import org.javatablegames.core.enums.Direction;
import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.game.gamefield.ModelCell;
import org.javatablegames.core.model.game.piece.Piece;
import org.javatablegames.core.model.position.Position;

import java.util.ArrayList;
import java.util.List;

public abstract class DraughtsPiece extends Piece {

    protected List<ModelCell<DraughtsPiece>> cellsAllowedToMoveIn;
    protected List<ModelCell<DraughtsPiece>> cellsAllowedToCaptureIn;
    private Position checkPosition;

    public DraughtsPiece(Position position, Side side, int power, Gamefield gameField) {
        super(position, side, power, gameField);
    }

    public boolean isAbleToBecomeKing() {
        return (this instanceof Man &&
                ((side == Side.WHITE && position.getRow() == 0) || (side == Side.BLACK && position.getRow() == 7)));
    }

    public List<ModelCell<DraughtsPiece>> getCellsAllowedToCapture() {
        return cellsAllowedToCaptureIn;
    }

    public List<ModelCell<DraughtsPiece>> getCellsAllowedToMoveIn() {
        return cellsAllowedToMoveIn;
    }

    public abstract boolean isAbleToCapture();

    public abstract boolean isAbleToMove();

    protected void searchCellsAllowedToCapture(Direction[] directions, int moveLength) {
        cellsAllowedToCaptureIn = new ArrayList<ModelCell<DraughtsPiece>>();

        for (Direction direction : directions) {
            checkPosition = new Position(position);

            for (int moveIndex = 0; moveIndex < moveLength; moveIndex++) {
                checkPosition.moveInDirection(direction);

                if (gamefield.isCellOpponent(checkPosition, side)) {
                    do {
                        checkPosition.moveInDirection(direction);

                        if (gamefield.isCellEmpty(checkPosition)) {
                            cellsAllowedToCaptureIn.add(gamefield.getCell(checkPosition));
                        } else {
                            break;
                        }

                        moveIndex++;
                    } while (moveIndex < moveLength);

                    break;
                } else if (!gamefield.isCellEmpty(checkPosition)) {
                    break;
                }
            }
        }
    }

    protected void searchCellsAllowedToMoveIn(Direction[] directions, int moveLength) {
        cellsAllowedToMoveIn = new ArrayList<ModelCell<DraughtsPiece>>();

        for (Direction direction : directions) {
            checkPosition = new Position(position);

            for (int moveIndex = 1; moveIndex <= moveLength; moveIndex++) {
                checkPosition.moveInDirection(direction);

                if (gamefield.isCellEmpty(checkPosition)) {
                    cellsAllowedToMoveIn.add(gamefield.getCell(checkPosition));
                } else {
                    break;
                }
            }
        }
    }

}

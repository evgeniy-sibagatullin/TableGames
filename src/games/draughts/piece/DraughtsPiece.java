package games.draughts.piece;

import enums.Direction;
import enums.Side;
import model.game.gamefield.Gamefield;
import model.game.gamefield.ModelCell;
import model.game.piece.Piece;
import model.game.position.Position;

import java.util.ArrayList;
import java.util.List;

public abstract class DraughtsPiece extends Piece {

    protected List<ModelCell> cellsAllowedToMoveIn;
    protected List<ModelCell> cellsAllowedToCaptureIn;

    private Position checkPosition;

    public DraughtsPiece(Position position, Side side, Gamefield gameField) {
        super(position, side, gameField);
    }

    public boolean isAbleToBecomeKing() {
        return (this instanceof Man &&
                ((side == Side.WHITE && position.getRow() == 0) || (side == Side.BLACK && position.getRow() == 7)));
    }

    public List<ModelCell> getCellsAllowedToCapture() {
        return cellsAllowedToCaptureIn;
    }

    public List<ModelCell> getCellsAllowedToMoveIn() {
        return cellsAllowedToMoveIn;
    }

    public abstract boolean isAbleToCapture();

    public abstract boolean isAbleToMove();

    protected void searchCellsAllowedToCapture(Direction[] directions, int moveLength) {
        cellsAllowedToCaptureIn = new ArrayList<ModelCell>();

        for (Direction direction : directions) {
            checkPosition = new Position(position);

            for (int moveIndex = 1; moveIndex <= moveLength; moveIndex++) {
                checkPosition.moveInDirection(direction);

                if (isCellOpponent(checkPosition)) {
                    do {
                        checkPosition.moveInDirection(direction);

                        if (isCellEmpty(checkPosition)) {
                            cellsAllowedToCaptureIn.add(gamefield.getCell(checkPosition));
                        } else {
                            break;
                        }

                        moveIndex++;
                    } while (moveIndex <= moveLength);

                    break;
                } else if (!isCellEmpty(checkPosition)) {
                    break;
                }
            }
        }
    }

    protected void searchCellsAllowedToMoveIn(Direction[] directions, int moveLength) {
        cellsAllowedToMoveIn = new ArrayList<ModelCell>();

        for (Direction direction : directions) {
            checkPosition = new Position(position);

            for (int moveIndex = 1; moveIndex <= moveLength; moveIndex++) {
                checkPosition.moveInDirection(direction);

                if (isCellEmpty(checkPosition)) {
                    cellsAllowedToMoveIn.add(gamefield.getCell(checkPosition));
                } else {
                    break;
                }
            }
        }
    }

    private boolean isCellEmpty(Position position) {
        return position.isValid(gamefield.getSize()) && gamefield.getCell(position).getPiece() == null;
    }

    private boolean isCellOpponent(Position position) {
        if (position.isValid(gamefield.getSize())) {
            Piece gamePiece = gamefield.getPiece(position);
            return (gamePiece != null && gamePiece.getSide() != side);
        }
        return false;
    }
}

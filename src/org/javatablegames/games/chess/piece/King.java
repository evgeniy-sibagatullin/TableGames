package org.javatablegames.games.chess.piece;

import org.javatablegames.core.enums.Direction;
import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.game.gamefield.ModelCell;
import org.javatablegames.core.model.position.Position;

import java.util.ArrayList;
import java.util.List;

public class King extends ChessPiece {

    private static final int MOVE_LENGTH = 1;
    private List<ModelCell> cellsAllowedToCastling;

    public King(Position position, Side side, Gamefield gameField) {
        super(position, side, gameField);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "src/org/javatablegames/games/chess/img/Chess-KingW.png" :
                "src/org/javatablegames/games/chess/img/Chess-KingB.png";
    }

    @Override
    public boolean isAbleToMove() {
        searchCellsAllowedToMoveIn(Direction.directions, MOVE_LENGTH);
        if (!isEverMoved()) {
            if (isCastlingPossible()) {
                cellsAllowedToMoveIn.addAll(cellsAllowedToCastling);
            }
        }
        return !cellsAllowedToMoveIn.isEmpty();
    }

    private boolean isCastlingPossible() {
        cellsAllowedToCastling = new ArrayList<ModelCell>();
        checkCastlingSide(0);
        checkCastlingSide(7);
        return !cellsAllowedToCastling.isEmpty();
    }

    private void checkCastlingSide(int column) {
        int row = getPosition().getRow();
        ChessPiece chessPiece = (ChessPiece) gamefield.getPiece(new Position(row, column));

        if (chessPiece instanceof Rook && !chessPiece.isEverMoved()) {
            checkPosition = new Position(position);
            Direction direction = Direction.getDirection(0, column - 4);
            checkPosition.moveInDirection(direction);
            boolean isCastlingPossible = true;

            do {
                if (!gamefield.isCellEmpty(checkPosition)) {
                    isCastlingPossible = false;
                    break;
                }

                checkPosition.moveInDirection(direction);
            } while (checkPosition.getColumn() != column);

            if (isCastlingPossible) {
                int castlingColumn = (column == 0) ? 2 : 6;
                cellsAllowedToCastling.add(gamefield.getCell(new Position(row, castlingColumn)));
            }
        }
    }

}

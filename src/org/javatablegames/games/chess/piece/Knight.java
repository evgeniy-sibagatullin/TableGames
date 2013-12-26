package org.javatablegames.games.chess.piece;

import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.game.gamefield.ModelCell;
import org.javatablegames.core.model.position.Position;

import java.util.ArrayList;

public class Knight extends ChessPiece {

    public Knight(Position position, Side side, Gamefield gameField) {
        super(position, side, gameField);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "src/org/javatablegames/games/chess/img/Chess-KnightW.png" :
                "src/org/javatablegames/games/chess/img/Chess-KnightB.png";
    }

    @Override
    public boolean isAbleToMove() {
        searchCellsAllowedToMoveIn();
        return !cellsAllowedToMoveIn.isEmpty();
    }

    private void searchCellsAllowedToMoveIn() {
        cellsAllowedToMoveIn = new ArrayList<ModelCell>();

        for (int deltaX = -2; deltaX <= 2; deltaX++) {
            for (int deltaY = -2; deltaY <= 2; deltaY++) {
                int row = getPosition().getRow() + deltaY;
                int column = getPosition().getColumn() + deltaX;
                checkPosition = new Position(row, column);

                if (((Math.abs(deltaX) + Math.abs(deltaY)) == 3)
                        && checkPosition.isValid(gamefield.getSize())
                        && (gamefield.isCellEmpty(checkPosition) || gamefield.isCellOpponent(checkPosition, side))
                        ) {
                    cellsAllowedToMoveIn.add(gamefield.getCell(checkPosition));
                }
            }
        }
    }

}

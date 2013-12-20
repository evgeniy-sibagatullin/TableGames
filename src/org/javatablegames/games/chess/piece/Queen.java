package org.javatablegames.games.chess.piece;

import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.position.Position;

public class Queen extends ChessPiece {

    public Queen(Position position, Side side, Gamefield gameField) {
        super(position, side, gameField);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/chess/Chess-QueenW.png" :
                "img/chess/Chess-QueenB.png";
    }

    @Override
    public int getPower() {
        return 5;
    }

}

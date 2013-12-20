package org.javatablegames.games.chess.piece;

import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.position.Position;

public class Bishop extends ChessPiece {

    public Bishop(Position position, Side side, Gamefield gameField) {
        super(position, side, gameField);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/chess/Chess-BishopW.png" :
                "img/chess/Chess-BishopB.png";
    }

    @Override
    public int getPower() {
        return 3;
    }

}

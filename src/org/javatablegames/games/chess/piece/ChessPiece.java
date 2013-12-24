package org.javatablegames.games.chess.piece;

import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.game.piece.Piece;
import org.javatablegames.core.model.position.Position;

public abstract class ChessPiece extends Piece {

    protected ChessPiece(Position position, Side side, Gamefield gameField) {
        super(position, side, gameField);
    }

}

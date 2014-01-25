package org.javatablegames.games.template.piece;

import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.game.piece.Piece;
import org.javatablegames.core.model.position.Position;

public class TemplatePiece extends Piece {

    public TemplatePiece(Position position, Side side, int power, Gamefield gameField) {
        super(position, side, power, gameField);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "src/org/javatablegames/games/template/img/template-blue.png" :
                "src/org/javatablegames/games/template/img/template-red.png";
    }

}

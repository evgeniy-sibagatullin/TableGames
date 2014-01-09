package org.javatablegames.games.template.piece;

import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.game.piece.Piece;
import org.javatablegames.core.model.game.piece.PieceSet;

import java.util.HashSet;

public class TemplatePieceSet extends PieceSet {

    public TemplatePieceSet(Gamefield gamefield) {
        super(gamefield);
    }

    protected void initializePieces() {
        pieces = new HashSet<Piece>();
        addPiecesToGameField();
    }

}

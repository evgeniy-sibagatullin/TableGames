package org.javatablegames.games.template.piece;

import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.game.piece.PieceSet;

import java.util.HashSet;

public class TemplatePieceSet extends PieceSet<TemplatePiece> {

    public TemplatePieceSet(Gamefield<TemplatePiece> gamefield) {
        super(gamefield);
    }

    protected void initializePieces() {
        pieces = new HashSet<TemplatePiece>();
        addPiecesToGameField();
    }

    @Override
    protected void clonePiecesToGamefield(PieceSet<TemplatePiece> pieceSet) {

    }

}

package org.javatablegames.games.template;

import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.Model;
import org.javatablegames.core.model.game.Game;
import org.javatablegames.core.model.position.Position;
import org.javatablegames.games.template.gamefield.TemplateField;
import org.javatablegames.games.template.piece.TemplatePieceSet;

public abstract class AbstractTemplate extends Game<TemplateField, TemplatePieceSet> {

    protected Side sidePlayer;
    protected boolean isPlayerMove;
    protected String checkWinConditionsResult = "";

    public AbstractTemplate(Model model) {
        super(model);
        gamefield = new TemplateField();
        pieceSet = new TemplatePieceSet(gamefield);
    }

    @Override
    public String checkWinConditions() {
        return checkWinConditionsResult;
    }

    @Override
    public void clickCell(Position position) {

    }

}

package org.javatablegames.games.template.gamefield;

import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.game.gamefield.ModelCell;

public class TemplateField extends Gamefield {

    @Override
    protected void initializeGamefield() {
        size = 8;
        field = new ModelCell[size][size];
    }

}

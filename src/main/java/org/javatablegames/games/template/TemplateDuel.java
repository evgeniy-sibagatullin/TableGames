package org.javatablegames.games.template;

import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.Model;

public class TemplateDuel extends AbstractTemplate {

    public TemplateDuel(Model model) {
        super(model);
        sidePlayer = Side.WHITE;
        isPlayerMove = true;
    }

    @Override
    public void run() {

    }

}


package org.javatablegames.games.template;

import org.javatablegames.core.enums.Side;

public class TemplateDuel extends AbstractTemplate {

    public TemplateDuel() {
        sidePlayer = Side.WHITE;
        isPlayerMove = true;
    }

    @Override
    public void run() {
    }

}


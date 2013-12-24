package org.javatablegames.games.draughts;

import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.Model;

public class DraughtsDuel extends AbstractDraughts {

    public DraughtsDuel(Model model) {
        super(model);
        sidePlayer = Side.WHITE;
        isPlayerMove = true;
    }

    @Override
    public void run() {
        giveMoveToPlayer();

        while (isThreadNeeded) {
            if (!isPlayerMove) {
                sidePlayer = Side.oppositeSide(sidePlayer);
                giveMoveToPlayer();
            }
            delay(50);
        }
    }

}

package org.javatablegames.games.chess;

import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.Model;

public class ChessDuel extends AbstractChess {

    public ChessDuel(Model model) {
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
            delay(DELAY_PERIOD);
        }
    }

}


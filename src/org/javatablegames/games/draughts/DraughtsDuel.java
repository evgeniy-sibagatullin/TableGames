package org.javatablegames.games.draughts;

import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.Model;

public class DraughtsDuel extends AbstractDraughts {

    public DraughtsDuel(Model model) {
        super(model);
        sidePlayer = Side.WHITE;
        isPlayerMove = true;
        needToPrepareFieldForPlayer = true;
    }

    @Override
    public void run() {
        while (isThreadNeeded) {
            if (needToPrepareFieldForPlayer) {
                updateGameFieldForPlayer();
            }
            if (!isPlayerMove) {
                switchPlayerSide();
            }
            delay(50);
        }
    }

    private void switchPlayerSide() {
        gamefield.totalGameFieldCleanUp();
        sidePlayer = (sidePlayer == Side.WHITE) ? Side.BLACK : Side.WHITE;
        isPlayerMove = true;
        needToPrepareFieldForPlayer = true;
    }

}

package games.draughts;

import enums.GameType;
import enums.Side;
import model.Model;

public class DraughtsDuel extends AbstractDraughts {

    public DraughtsDuel(Model model) {
        super(model);
        gameType = GameType.DRAUGHTS_DUEL;
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

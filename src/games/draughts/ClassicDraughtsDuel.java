package games.draughts;

import enums.GameType;
import enums.Side;
import model.Model;

public class ClassicDraughtsDuel extends ClassicDraughts {

    public ClassicDraughtsDuel(Model model) {
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
        totalGameFieldCleanUp();
        sidePlayer = (sidePlayer == Side.WHITE) ? Side.BLACK : Side.WHITE;
        isPlayerMove = true;
        needToPrepareFieldForPlayer = true;
    }
}

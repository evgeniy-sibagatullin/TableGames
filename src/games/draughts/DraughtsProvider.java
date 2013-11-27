package games.draughts;

import enums.GameType;
import model.Model;
import model.game.Game;
import model.provider.Provider;

public class DraughtsProvider implements Provider {

    @Override
    public Game newGame(Model model, GameType gameType) {
        if (gameType == GameType.DRAUGHTS_DUEL) {
            return new ClassicDraughtsDuel(model);
        } else if (gameType == GameType.DRAUGHTS_VS_AI) {
            return new ClassicDraughtsVsAi(model);
        } else {
            return null;
        }
    }

}

package games.barleyBreak;

import enums.GameType;
import model.Model;
import model.game.Game;
import model.provider.Provider;

public class BarleyBreakProvider implements Provider {

    @Override
    public Game<?, ?> newGame(Model model, GameType gameType) {
        if (gameType == GameType.BARLEY_BREAK) {
            return new BarleyBreak(model);
        } else {
            return null;
        }
    }

}

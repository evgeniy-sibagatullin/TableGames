package games.barleyBreak;

import model.game.Game;
import model.provider.Provider;

public class BarleyBreakProvider implements Provider {

    @Override
    public Game newGame() {
        return new ClassicBarleyBreak();
    }

}

package games.barleyBreak;

import model.Model;
import model.game.Game;
import model.provider.Provider;

public class BarleyBreakProvider implements Provider {

    @Override
    public Game newGame(Model model) {
        return new ClassicBarleyBreak(model);
    }

}

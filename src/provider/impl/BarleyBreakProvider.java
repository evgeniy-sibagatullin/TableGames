package provider.impl;

import game.Game;
import game.set.ClassicBarleyBreak;
import provider.Provider;

public class BarleyBreakProvider implements Provider {

    @Override
    public Game newGame() {
        return new ClassicBarleyBreak();
    }

}

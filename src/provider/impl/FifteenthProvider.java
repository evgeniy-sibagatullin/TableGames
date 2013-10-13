package provider.impl;

import game.Game;
import game.set.ClassicFifteenth;
import provider.Provider;

public class FifteenthProvider implements Provider {

    @Override
    public Game newGame() {
        return new ClassicFifteenth();
    }

}

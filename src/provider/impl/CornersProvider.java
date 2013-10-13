package provider.impl;

import game.Game;
import game.set.ClassicCorners;
import provider.Provider;

public class CornersProvider implements Provider {

    @Override
    public Game newGame() {
        return new ClassicCorners();
    }

}

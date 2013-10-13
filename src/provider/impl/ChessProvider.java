package provider.impl;

import game.Game;
import game.set.ClassicChess;
import provider.Provider;

public class ChessProvider implements Provider {

    @Override
    public Game newGame() {
        return new ClassicChess();
    }

}

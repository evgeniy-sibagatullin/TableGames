package games.chess;

import model.game.Game;
import model.provider.Provider;

public class ChessProvider implements Provider {

    @Override
    public Game newGame() {
        return new ClassicChess();
    }

}

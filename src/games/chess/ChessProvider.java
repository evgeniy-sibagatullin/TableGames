package games.chess;

import model.Model;
import model.game.Game;
import model.provider.Provider;

public class ChessProvider implements Provider {

    @Override
    public Game newGame(Model model) {
        return new ClassicChess(model);
    }

}

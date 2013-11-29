package games.chess;

import enums.GameType;
import model.Model;
import model.game.Game;
import model.provider.Provider;

public class ChessProvider implements Provider {

    @Override
    public Game<?> newGame(Model model, GameType gameType) {
        if (gameType == GameType.CHESS) {
            return new Chess(model);
        } else {
            return null;
        }
    }

}

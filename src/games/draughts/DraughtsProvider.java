package games.draughts;

import enums.GameType;
import model.Model;
import model.game.Game;
import model.provider.Provider;

public class DraughtsProvider implements Provider {

    @Override
    public Game newGame(Model model, GameType gameType) {
        return new ClassicDraughts(model, gameType);
    }

}

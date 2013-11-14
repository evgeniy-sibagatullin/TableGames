package games.corners;

import enums.GameType;
import model.Model;
import model.game.Game;
import model.provider.Provider;

public class CornersProvider implements Provider {

    @Override
    public Game newGame(Model model, GameType gameType) {
        return new ClassicCorners(model, gameType);
    }

}

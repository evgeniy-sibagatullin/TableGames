package games.corners;

import model.Model;
import model.game.Game;
import model.provider.Provider;

public class CornersProvider implements Provider {

    @Override
    public Game newGame(Model model) {
        return new ClassicCorners(model);
    }

}

package games.corners;

import model.game.Game;
import model.provider.Provider;

public class CornersProvider implements Provider {

    @Override
    public Game newGame() {
        return new ClassicCorners();
    }

}

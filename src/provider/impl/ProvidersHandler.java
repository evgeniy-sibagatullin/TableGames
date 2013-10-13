package provider.impl;

import enums.GameType;
import game.Game;
import provider.Provider;

import java.util.HashMap;
import java.util.Map;

public class ProvidersHandler {

    private ProvidersHandler() {
    }

    private static final Map<GameType, Provider> providers = new HashMap<GameType, Provider>();

    public static final GameType DEFAULT_GAMETYPE = GameType.BARLEY_BREAK;

    public static void registerProviders() {
        registerProvider(GameType.BARLEY_BREAK, new BarleyBreakProvider());
        registerProvider(GameType.CHESS, new ChessProvider());
        registerProvider(GameType.CORNERS, new CornersProvider());
    }

    public static void registerProvider(GameType gametype, Provider p) {
        providers.put(gametype, p);
    }

    public static Game newInstance() {
        return newInstance(DEFAULT_GAMETYPE);
    }

    public static Game newInstance(GameType gametype) {

        Provider p = providers.get(gametype);
        if (p == null)
            throw new IllegalArgumentException(
                    "No provider registered with name: " + gametype.toString());
        return p.newGame();
    }

}

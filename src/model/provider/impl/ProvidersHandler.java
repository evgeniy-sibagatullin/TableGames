package model.provider.impl;

import enums.GameType;
import games.barleyBreak.BarleyBreakProvider;
import games.chess.ChessProvider;
import games.draughts.DraughtsProvider;
import model.Model;
import model.game.Game;
import model.provider.Provider;

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
        registerProvider(GameType.DRAUGHTS_DUEL, new DraughtsProvider());
        registerProvider(GameType.DRAUGHTS_VS_AI, new DraughtsProvider());
    }

    public static void registerProvider(GameType gametype, Provider p) {
        providers.put(gametype, p);
    }

    public static Game newInstance(GameType gametype, Model model) {

        Provider p = providers.get(gametype);
        if (p == null)
            throw new IllegalArgumentException(
                    "No model.provider registered with name: " + gametype.toString());
        return p.newGame(model, gametype);
    }

}

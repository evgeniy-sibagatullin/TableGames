package model;

import enums.GameType;
import model.game.Game;
import model.game.position.Position;
import model.provider.impl.ProvidersHandler;

public class GameModel implements Model {

    private Game<?> game;
    private boolean isChanged;

    @Override
    public Game<?> getGame() {
        return game;
    }

    private void setGame(Game<?> game) {
        this.game = game;
    }

	/* Logic */

    @Override
    public void initializeModel() {
        System.out.println("Hi, folks!");
        ProvidersHandler.registerProviders();
    }

    @Override
    public void startGame(GameType gameType) {
        if (game != null) {
            game.terminateThread();
        }

        System.out.println("Game " + gameType + " started.");
        setGame(ProvidersHandler.newInstance(gameType, this));
        setChanged(true);
        Thread thread = new Thread(game);
        thread.start();
    }

    @Override
    public void restartGame() {
        System.out.println("Game " + game.getGameType() + " restarted.");
        startGame(game.getGameType());
    }

    @Override
    public void stopGame() {
        System.out.println("Game " + game.getGameType() + " finished.");
        startGame(ProvidersHandler.DEFAULT_GAMETYPE);
    }

    @Override
    public void clickCell(Position position) {
        game.clickCell(position);
    }

    @Override
    public void setChanged(boolean changed) {
        isChanged = changed;
    }

    @Override
    public boolean isChanged() {
        return isChanged;
    }

    @Override
    public String checkWinConditions() {
        return game.checkWinConditions();
    }
}

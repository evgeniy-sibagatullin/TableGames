package model;

import enums.GameType;
import model.game.Game;
import model.provider.impl.ProvidersHandler;

public class GameModel implements Model {

    private Game game;

    @Override
    public Game getGame() {
        return game;
    }

    private void setGame(Game game) {
        this.game = game;
    }

	/* Logic */

    @Override
    public void initializeModel() {
        System.out.println("Hi, folks!");
        ProvidersHandler.registerProviders();
        startGame(ProvidersHandler.DEFAULT_GAMETYPE);
    }

    @Override
    public void startGame(GameType gameType) {
        System.out.println("Game " + gameType + " started.");
        setGame(ProvidersHandler.newInstance(gameType));
    }

    @Override
    public void restartGame() {
        System.out.println("Game " + game.getGameType() + " restarted.");
        startGame(game.getGameType());
    }

    @Override
    public void stopGame() {
        System.out.println("Game " + game.getGameType() + " finished.");
        setGame(ProvidersHandler.newInstance());
    }

    @Override
    public boolean clickCell(int row, int column) {
        return game.clickCell(row, column);
    }

    @Override
    public boolean checkWinConditions() {
        return game.checkWinConditions();
    }

    @Override
    public void viewUpdateComplete() {
        game.viewUpdateComplete();
    }
}

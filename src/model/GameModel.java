package model;

import enums.GameType;
import model.game.Game;
import model.provider.impl.ProvidersHandler;
import view.GameObserver;

import java.util.ArrayList;
import java.util.List;

public class GameModel implements Model {

    private Game game;
    private List<GameObserver> gameObservers = new ArrayList<GameObserver>();

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
        setGame(ProvidersHandler.newInstance(gameType, this));
        modelChangedEvent();
    }

    @Override
    public void restartGame() {
        System.out.println("Game " + game.getGameType() + " restarted.");
        startGame(game.getGameType());
    }

    @Override
    public void stopGame() {
        System.out.println("Game " + game.getGameType() + " finished.");
        setGame(ProvidersHandler.newInstance(this));
        modelChangedEvent();
    }

    @Override
    public void clickCell(int row, int column) {
        game.clickCell(row, column);
    }

    @Override
    public boolean checkWinConditions() {
        return game.checkWinConditions();
    }

    @Override
    public void registerObserver(GameObserver gameObserver) {
        gameObservers.add(gameObserver);
    }

    @Override
    public void removeObserver(GameObserver gameObserver) {
        if (gameObservers.contains(gameObserver)) {
            gameObservers.remove(gameObserver);
        }
    }

    @Override
    public void modelChangedEvent() {
        notifyGameObservers();
    }

    private void notifyGameObservers() {
        for (GameObserver gameObserver : gameObservers) {
            gameObserver.updateView();
        }
    }
}

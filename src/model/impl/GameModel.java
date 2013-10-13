package model.impl;

import enums.GameType;
import game.Game;
import model.GameObserver;
import model.Model;
import provider.impl.ProvidersHandler;

import java.util.ArrayList;

public class GameModel implements Model {

    private ArrayList<GameObserver> gameObservers;
    private Game game;

    @Override
    public void registerObserver(GameObserver gameObserver) {
        gameObservers.add(gameObserver);
    }

    @Override
    public void removeObserver(GameObserver gameObserver) {
        int index = gameObservers.indexOf(gameObserver);
        if (index >= 0) {
            gameObservers.remove(index);
        }
    }

    @Override
    public void notifyObservers() {
        for (GameObserver gameObserver : gameObservers) {
            gameObserver.update();
        }
    }

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
        System.out.println("Hi, man!");
        System.out.println("This game out of scope");
        ProvidersHandler.registerProviders();
        gameObservers = new ArrayList<GameObserver>();
        startGame(ProvidersHandler.DEFAULT_GAMETYPE);
    }

    @Override
    public void startGame(GameType gameType) {
        System.out.println("Game " + gameType + " started.");
        setGame(ProvidersHandler.newInstance(gameType));
        notifyObservers();
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
        notifyObservers();
    }

    @Override
    public void clickCell(int row, int column) {
        game.clickCell(row, column);
        notifyObservers();
    }

    @Override
    public boolean checkWinConditions() {
        return game.checkWinConditions();
    }


}

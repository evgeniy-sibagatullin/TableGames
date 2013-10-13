package model;

import enums.GameType;
import game.Game;

public interface Model {

    void registerObserver(GameObserver gameObserver);

    void removeObserver(GameObserver gameObserver);

    void notifyObservers();

    Game getGame();

    /**
     * Initializes model during application start.
     */
    void initializeModel();

    /**
     * Starts new game defined by input parameter;
     */
    void startGame(GameType gameType);

    /**
     * Starts new one round of the same game.
     */
    void restartGame();

    /**
     * Stops current game
     */
    void stopGame();

    /**
     * Performs check of clicked cell in defined coordinates
     */
    void clickCell(int row, int column);

}

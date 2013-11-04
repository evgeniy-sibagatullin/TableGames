package controller;

import enums.GameType;
import model.Model;

public interface MenuController {

    /**
     * Initializes controller with defined model during application start.
     */
    void initializeController(Model model);

    /**
     * Performs control of new game start(update model and view)
     */
    void startGame(GameType gameType);

    /**
     * Performs control of game restart(update model and view)
     */
    void restartGame();

    /**
     * Performs control of game reselect
     */
    void reselectGame();
}

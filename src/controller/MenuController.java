package controller;

import enums.GameType;
import model.Model;

public interface MenuController {

    /**
     * Initializes controller with defined model during application start.
     */
    void initializeController(Model model);

    /**
     * Performs control of new model.game start(update model and view)
     */
    void startGame(GameType gameType);

    /**
     * Performs control of model.game restart(update model and view)
     */
    void restartGame();

    /**
     * Performs control of model.game reselect
     */
    void reselectGame();
}

package controller;

import enums.GameType;
import model.Model;

public interface Controller {

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

    /**
     * Performs check of clicked cell in defined coordinates
     */
    void clickCell(int row, int column);

    /**
     * Checks if win conditions satisfied
     */
    void checkWinConditions();
}

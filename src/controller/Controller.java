package controller;

import enums.GameType;

public interface Controller {

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

    /**
     * Performs check of clicked cell in defined coordinates
     */
    void clickCell(int row, int column);
}

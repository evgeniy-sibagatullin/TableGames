package controller;

import enums.GameType;

public interface Controller {

    void startDefaultGame();

    void startGame(GameType gameType);

    void restartGame();

    void reselectGame();

    void clickCell(int row, int column);
}

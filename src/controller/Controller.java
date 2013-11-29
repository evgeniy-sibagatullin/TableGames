package controller;

import enums.GameType;
import model.game.position.Position;

public interface Controller {
    void startDefaultGame();

    void startGame(GameType gameType);

    void restartGame();

    void reselectGame();

    void clickCell(Position position);

    void checkWinConditions();
}

package org.javatablegames.core.controller;

import org.javatablegames.core.model.position.Position;

public interface Controller {

    void initialize();

    void startGame(String gameClassName);

    void restartGame();

    void startDefaultGame();

    void clickCell(Position position);

    void checkWinConditions();

    void showMessage(String message);

}

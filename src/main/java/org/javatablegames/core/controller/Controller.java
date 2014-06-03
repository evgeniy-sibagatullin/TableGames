package org.javatablegames.core.controller;

import org.javatablegames.core.model.position.Position;

public interface Controller {

    void initialize();

    void startGame(String gameClassName);

    void restartGame();

    void startDefaultGame();

    void undoMove();

    void redoMove();

    void clickCell(Position position);

    void checkWinConditions();

}

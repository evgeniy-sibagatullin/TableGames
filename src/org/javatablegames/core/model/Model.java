package org.javatablegames.core.model;

import org.javatablegames.core.model.game.Game;
import org.javatablegames.core.model.position.Position;

public interface Model {

    Game<?, ?> getGame();

    void startGame(String gameClassName);

    void restartGame();

    void undoMove();

    void redoMove();

    void clickCell(Position position);

    void setChanged(boolean changed);

    boolean isChanged();

    String checkWinConditions();

}

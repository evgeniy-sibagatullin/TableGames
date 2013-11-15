package model;

import enums.GameType;
import model.game.Game;

public interface Model {

    Game getGame();

    /**
     * Initializes model during application start.
     */
    void initializeModel();

    /**
     * Starts new model.game defined by input parameter;
     */
    void startGame(GameType gameType);

    /**
     * Starts new one round of the same model.game.
     */
    void restartGame();

    /**
     * Stops current model.game
     */
    void stopGame();

    void clickCell(int row, int column);

    void setChanged(boolean changed);

    boolean isChanged();

    boolean checkWinConditions();
}

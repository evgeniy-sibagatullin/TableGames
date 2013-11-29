package model;

import enums.GameType;
import model.game.Game;
import model.game.position.Position;

public interface Model {

    Game<?> getGame();

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

    void clickCell(Position position);

    void setChanged(boolean changed);

    boolean isChanged();

    String checkWinConditions();
}

package model.game;

import enums.GameType;
import model.ModelCell;

public interface Game extends Runnable {

    GameType getGameType();

    int getFieldSize();

    ModelCell[][] getGameField();

    void clickCell(int row, int column);

    String checkWinConditions();

    void terminateThread();
}
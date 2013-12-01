package model.game;

import enums.GameType;
import model.Model;
import model.game.gamefield.Gamefield;
import model.game.piece.Pieces;
import model.game.position.Position;

public abstract class Game<TF extends Gamefield, TP extends Pieces> implements Runnable {

    protected final Model model;

    protected boolean isThreadNeeded;
    protected GameType gameType;
    protected TF gamefield;
    protected TP pieces;

    public Game(Model model) {
        this.model = model;
        isThreadNeeded = true;
    }

    public GameType getGameType() {
        return gameType;
    }

    public TF getGameField() {
        return gamefield;
    }

    public void terminateThread() {
        isThreadNeeded = false;
    }

    public int getFieldSize() {
        return gamefield.getSize();
    }

    public abstract String checkWinConditions();

    public abstract void clickCell(Position position);
}

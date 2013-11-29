package model.game;

import enums.GameType;
import model.Model;
import model.game.gamefield.Gamefield;
import model.game.piece.Piece;
import model.game.position.Position;

import java.util.List;

public abstract class Game<T extends Gamefield> implements Runnable {

    protected final Model model;

    protected boolean isThreadNeeded;
    protected GameType gameType;
    protected T gamefield;
    protected List<Piece> pieces;

    public Game(Model model) {
        this.model = model;
        isThreadNeeded = true;
    }

    public GameType getGameType() {
        return gameType;
    }

    public T getGameField() {
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

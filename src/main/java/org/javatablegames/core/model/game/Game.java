package org.javatablegames.core.model.game;

import org.javatablegames.core.model.Model;
import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.game.piece.PieceSet;
import org.javatablegames.core.model.position.Position;

import java.util.HashMap;

public abstract class Game<TF extends Gamefield, TP extends PieceSet> implements Runnable {

    protected final Model model;

    protected boolean isThreadNeeded;
    protected TF gamefield;
    protected TP pieceSet;
    protected HashMap<Integer, TP> moveHistory = new HashMap<Integer, TP>();

    protected Game(Model model) {
        this.model = model;
        isThreadNeeded = true;
    }

    public String getGameClassName() {
        return this.getClass().getName();
    }

    public TF getGameField() {
        return gamefield;
    }

    public int getFieldSize() {
        return gamefield.getSize();
    }

    public void terminateThread() {
        isThreadNeeded = false;
    }

    public abstract String checkWinConditions();

    public void undoMove() {
    }

    public void redoMove() {
    }

    public abstract void clickCell(Position position);

    protected void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

}

package org.javatablegames.core.model.game;

import org.javatablegames.core.model.Model;
import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.game.piece.PieceSet;
import org.javatablegames.core.model.position.Position;

public abstract class Game<TF extends Gamefield, TP extends PieceSet> implements Runnable {

    protected final Model model;

    protected boolean isThreadNeeded;
    protected TF gamefield;
    protected TP pieceSet;

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

    public void terminateThread() {
        isThreadNeeded = false;
    }

    public int getFieldSize() {
        return gamefield.getSize();
    }

    public abstract String checkWinConditions();

    public abstract void clickCell(Position position);

}

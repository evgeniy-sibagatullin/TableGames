package org.javatablegames.core.model;

import org.javatablegames.core.model.game.Game;
import org.javatablegames.core.model.position.Position;

import java.lang.reflect.Constructor;

public enum Model {

    INSTANCE;

    private Game<?, ?> game;
    private boolean isChanged;

    public Game<?, ?> getGame() {
        return game;
    }

    private void setGame(Game<?, ?> game) {
        this.game = game;
    }

    public void startGame(String gameClassName) {
        if (game != null) {
            game.terminateThread();
        }

        try {
            Class gameClass = Class.forName(gameClassName);
            Constructor constructor = gameClass.getDeclaredConstructor();
            game = (Game<?, ?>) constructor.newInstance();
            setGame(game);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setChanged(true);
        Thread thread = new Thread(game);
        thread.start();
    }

    public void restartGame() {
        startGame(game.getGameClassName());
    }

    public void undoMove() {
        game.undoMove();
    }

    public void redoMove() {
        game.redoMove();
    }

    public void clickCell(Position position) {
        game.clickCell(position);
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public String checkWinConditions() {
        return game.checkWinConditions();
    }

}

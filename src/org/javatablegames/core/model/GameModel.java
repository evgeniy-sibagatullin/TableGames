package org.javatablegames.core.model;

import org.javatablegames.core.model.game.Game;
import org.javatablegames.core.model.position.Position;

import java.lang.reflect.Constructor;

public class GameModel implements Model {

    private Game<?, ?> game;
    private boolean isChanged;

    @Override
    public Game<?, ?> getGame() {
        return game;
    }

    private void setGame(Game<?, ?> game) {
        this.game = game;
    }

    @Override
    public void startGame(String gameClassName) {
        if (game != null) {
            game.terminateThread();
        }

        try {
            Class gameClass = Class.forName(gameClassName);
            Constructor constructor = gameClass.getDeclaredConstructor(Model.class);
            game = (Game<?, ?>) constructor.newInstance(this);
            setGame(game);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setChanged(true);
        Thread thread = new Thread(game);
        thread.start();
    }

    @Override
    public void restartGame() {
        startGame(game.getGameClassName());
    }

    @Override
    public void clickCell(Position position) {
        game.clickCell(position);
    }

    @Override
    public void setChanged(boolean changed) {
        isChanged = changed;
    }

    @Override
    public boolean isChanged() {
        return isChanged;
    }

    @Override
    public String checkWinConditions() {
        return game.checkWinConditions();
    }

}

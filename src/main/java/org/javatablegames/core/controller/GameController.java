package org.javatablegames.core.controller;

import org.javatablegames.core.model.Model;
import org.javatablegames.core.model.position.Position;
import org.javatablegames.core.view.GameView;
import org.javatablegames.core.view.View;

public class GameController implements Controller {

    public final static String DEFAULT_GAME_CLASS = "org.javatablegames.games.barleyBreak.BarleyBreak";

    private final View view;

    public GameController() {
        view = new GameView(this);
    }

    @Override
    public void initialize() {
        startDefaultGame();
        view.initializeView();
    }

    @Override
    public void startGame(String gameClassName) {
        Model.INSTANCE.startGame(gameClassName);
        view.enableManageGameMenu();
        view.disableSelectGameMenu();
    }

    @Override
    public void restartGame() {
        Model.INSTANCE.restartGame();
    }

    @Override
    public void startDefaultGame() {
        view.enableSelectGameMenu();
        view.disableManageGameMenu();
        Model.INSTANCE.startGame(DEFAULT_GAME_CLASS);
    }

    @Override
    public void undoMove() {
        Model.INSTANCE.undoMove();
    }

    @Override
    public void redoMove() {
        Model.INSTANCE.redoMove();
    }

    @Override
    public void clickCell(Position position) {
        Model.INSTANCE.clickCell(position);
    }

    @Override
    public void checkWinConditions() {
        String checkWinConditionsResult = Model.INSTANCE.checkWinConditions();
        if (!checkWinConditionsResult.isEmpty()) {
            view.showMessage(checkWinConditionsResult);
            Model.INSTANCE.restartGame();
        }
    }

}
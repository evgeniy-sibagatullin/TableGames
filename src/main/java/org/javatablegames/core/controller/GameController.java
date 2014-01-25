package org.javatablegames.core.controller;

import org.javatablegames.core.model.Model;
import org.javatablegames.core.model.position.Position;
import org.javatablegames.core.view.GameView;
import org.javatablegames.core.view.View;

public class GameController implements Controller {

    public final static String DEFAULT_GAME_CLASS = "org.javatablegames.games.barleyBreak.BarleyBreak";

    private final Model model;
    private final View view;

    public GameController(Model model) {
        this.model = model;
        view = new GameView(this, model);
    }

    @Override
    public void initialize() {
        startDefaultGame();
        view.initializeView();
    }

    @Override
    public void startGame(String gameClassName) {
        model.startGame(gameClassName);
        view.enableManageGameMenu();
        view.disableSelectGameMenu();
    }

    @Override
    public void restartGame() {
        model.restartGame();
    }

    @Override
    public void startDefaultGame() {
        view.enableSelectGameMenu();
        view.disableManageGameMenu();
        model.startGame(DEFAULT_GAME_CLASS);
    }

    @Override
    public void undoMove() {
        model.undoMove();
    }

    @Override
    public void redoMove() {
        model.redoMove();
    }

    @Override
    public void clickCell(Position position) {
        model.clickCell(position);
    }

    @Override
    public void checkWinConditions() {
        String checkWinConditionsResult = model.checkWinConditions();
        if (!checkWinConditionsResult.isEmpty()) {
            showMessage(checkWinConditionsResult);
            model.restartGame();
        }
    }

    @Override
    public void showMessage(String message) {
        view.showMessage(message);
    }

}
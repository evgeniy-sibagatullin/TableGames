package controller;

import enums.GameType;
import model.GameObserver;
import model.Model;
import view.View;
import view.impl.GameView;

public class GameController implements Controller, GameObserver {

    private Model model;
    private View view;

    @Override
    public void initializeController(Model model) {
        this.model = model;
        model.initializeModel();
        model.registerObserver(this);
        view = new GameView(this, model);
        view.initializeView();
    }

    @Override
    public void startGame(GameType gameType) {
        model.startGame(gameType);
        view.enableManageGameMenu();
        view.disableSelectGameMenu();
    }

    @Override
    public void restartGame() {
        model.restartGame();
        view.enableManageGameMenu();
        view.disableSelectGameMenu();
    }

    @Override
    public void reselectGame() {
        model.stopGame();
        view.enableSelectGameMenu();
        view.disableManageGameMenu();
    }

    @Override
    public void update() {
        view.constructGameField();
    }

    @Override
    public void clickCell(int row, int column) {
        model.clickCell(row, column);
        checkWinConditions();
    }

    @Override
    public void checkWinConditions() {
        if (model.checkWinConditions()) {
            view.showWinPopup();
            model.restartGame();
        }
    }

}
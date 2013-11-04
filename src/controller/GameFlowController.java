package controller;

import model.GameObserver;
import model.Model;
import view.View;

public class GameFlowController implements FlowController, GameObserver {

    private Model model;
    private View view;

    public GameFlowController(Model model, View view) {
        this.model = model;
        this.view = view;
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

    @Override
    public void viewUpdateComplete() {
        model.viewUpdateComplete();
    }

    @Override
    public void update() {
        view.updateAfterClick();
    }
}

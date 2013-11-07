package controller;

import model.Model;
import view.View;

public class GameFlowController implements FlowController {

    private Model model;
    private View view;

    public GameFlowController(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void clickCell(int row, int column) {
        if (model.clickCell(row, column)) {
            checkWinConditions();
            view.updateGameField();
        }
    }

    @Override
    public void checkWinConditions() {
        if (model.checkWinConditions()) {
            view.showMessage("Congratulations. You win!");
            model.restartGame();
        }
    }

    @Override
    public void viewUpdateComplete() {
        model.viewUpdateComplete();
    }
}

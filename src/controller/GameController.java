package controller;

import enums.GameType;
import model.Model;
import view.View;
import view.impl.GameView;

public class GameController implements Controller {

    private Model model;
    private View view;

    public GameController(Model model) {
        this.model = model;
        view = new GameView(this, model);

        model.initializeModel();
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
    public void clickCell(int row, int column) {
        model.clickCell(row, column);
        checkWinConditions();
    }

    private void checkWinConditions() {
        if (model.checkWinConditions()) {
            view.showMessage("Congratulations. You win!");
            model.restartGame();
        }
    }
}
package controller;

import enums.GameType;
import model.Model;
import model.provider.impl.ProvidersHandler;
import view.View;
import view.impl.GameView;

public class GameController implements Controller {

    private Model model;
    private View view;

    public GameController(Model model) {
        this.model = model;
        model.initializeModel();
        view = new GameView(this, model);
        startDefaultGame();
        view.initializeView();
    }

    @Override
    public void startDefaultGame() {
        view.enableSelectGameMenu();
        view.disableManageGameMenu();
        model.startGame(ProvidersHandler.DEFAULT_GAMETYPE);
    }

    @Override
    public void startGame(GameType gameType) {
        view.enableManageGameMenu();
        view.disableSelectGameMenu();
        model.startGame(gameType);
    }

    @Override
    public void restartGame() {
        model.restartGame();
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
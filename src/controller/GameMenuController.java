package controller;

import enums.GameType;
import model.Model;
import view.View;
import view.impl.GameView;

public class GameMenuController implements MenuController {

    private Model model;
    private View view;

    @Override
    public void initializeController(Model model) {
        this.model = model;
        model.initializeModel();
        view = new GameView(this, model);
        GameFlowController flowController = new GameFlowController(model, view);
        view.setFlowController(flowController);
        view.initializeView();
    }

    @Override
    public void startGame(GameType gameType) {
        model.startGame(gameType);
        view.enableManageGameMenu();
        view.disableSelectGameMenu();
        view.updateGameField();
    }

    @Override
    public void restartGame() {
        model.restartGame();
        view.enableManageGameMenu();
        view.disableSelectGameMenu();
        view.updateGameField();
    }

    @Override
    public void reselectGame() {
        model.stopGame();
        view.enableSelectGameMenu();
        view.disableManageGameMenu();
        view.updateGameField();
    }
}
package controller;

import enums.GameType;
import model.Model;
import model.game.position.Position;
import model.provider.impl.ProvidersHandler;
import view.View;
import view.impl.GameView;

public class GameController implements Controller {

    private final Model model;
    private final View view;

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
    public void clickCell(Position position) {
        model.clickCell(position);
    }

    @Override
    public void checkWinConditions() {
        String checkWinConditionsResult = model.checkWinConditions();
        if (!checkWinConditionsResult.isEmpty()) {
            view.showMessage(checkWinConditionsResult);
            model.restartGame();
        }
    }
}
package starter;

import controller.GameMenuController;
import controller.MenuController;
import model.GameObserver;
import model.Model;
import model.impl.GameModel;

public class Starter {

    /**
     * Starts new table game application
     */
    public static void main(String[] args) {
        Model model = new GameModel();
        MenuController menuController = new GameMenuController();
        try {
            menuController.initializeController(model);
        } finally {
            model.removeObserver((GameObserver) menuController);
            System.out.println("Game " + model.getGame().getGameType() + " finished.");
            System.out.println("See you!");
        }
    }
}

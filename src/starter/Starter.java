package starter;

import controller.Controller;
import controller.GameController;
import model.GameObserver;
import model.Model;
import model.impl.GameModel;

public class Starter {

    /**
     * Starts new table game application
     */
    public static void main(String[] args) {
        Model model = new GameModel();
        Controller controller = new GameController();
        try {
            controller.initializeController(model);
        } finally {
            model.removeObserver((GameObserver) controller);
            System.out.println("Game " + model.getGame().getGameType() + " finished.");
            System.out.println("See you!");
        }
    }

}

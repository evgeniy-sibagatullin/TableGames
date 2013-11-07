import controller.GameMenuController;
import controller.MenuController;
import model.GameModel;
import model.Model;

public class Starter {

    /**
     * Starts new table model.game application
     */
    public static void main(String[] args) {
        Model model = new GameModel();
        MenuController menuController = new GameMenuController();
        try {
            menuController.initializeController(model);
        } finally {
            System.out.println("Game " + model.getGame().getGameType() + " finished.");
            System.out.println("See you!");
        }
    }
}

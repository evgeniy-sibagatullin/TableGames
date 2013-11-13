import controller.Controller;
import controller.GameController;
import model.GameModel;
import model.Model;

public class Starter {

    /**
     * Starts new table model.game application
     */
    public static void main(String[] args) {
        Model model = new GameModel();
        try {
            Controller menuController = new GameController(model);
            menuController.toString();
        } finally {
            System.out.println("Game " + model.getGame().getGameType() + " finished.");
            System.out.println("See you!");
        }
    }
}

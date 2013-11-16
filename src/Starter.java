import controller.Controller;
import controller.GameController;
import model.GameModel;
import model.Model;

public class Starter {

    public static void main(String[] args) {
        Model model = new GameModel();
        try {
            Controller menuController = new GameController(model);
            menuController.toString();
        } finally {
            model.getGame().terminateThread();
            System.out.println("Game " + model.getGame().getGameType() + " aborted.");
            System.out.println("See you!");
        }
    }
}

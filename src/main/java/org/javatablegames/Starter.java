package org.javatablegames;

import org.javatablegames.core.controller.Controller;
import org.javatablegames.core.controller.GameController;
import org.javatablegames.core.model.GameModel;
import org.javatablegames.core.model.Model;

public class Starter {

    public static void main(String[] args) {
        Model model = new GameModel();
        try {
            Controller controller = new GameController(model);
            controller.initialize();
        } finally {
            model.getGame().terminateThread();
        }
    }

}

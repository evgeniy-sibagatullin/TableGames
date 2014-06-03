package org.javatablegames;

import org.javatablegames.core.controller.Controller;
import org.javatablegames.core.controller.GameController;
import org.javatablegames.core.model.Model;

public class Starter {

    public static void main(String[] args) {
        try {
            Controller controller = new GameController();
            controller.initialize();
        } finally {
            Model.INSTANCE.getGame().terminateThread();
        }
    }

}

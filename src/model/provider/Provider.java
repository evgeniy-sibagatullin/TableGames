package model.provider;

import model.Model;
import model.game.Game;

public interface Provider {

    public Game newGame(Model model);

}

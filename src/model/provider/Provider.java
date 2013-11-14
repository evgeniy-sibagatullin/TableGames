package model.provider;

import enums.GameType;
import model.Model;
import model.game.Game;

public interface Provider {

    public Game newGame(Model model, GameType gameType);

}

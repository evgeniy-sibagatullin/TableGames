package model.game;

import enums.GameType;
import model.Model;
import model.ModelCell;
import model.piece.Piece;

import java.util.List;

public abstract class AbstractGame implements Game {

    protected Model model;
    protected boolean isThreadNeeded = true;
    private GameType gameType;
    protected ModelCell[][] gameField;
    protected List<Piece> pieces;

    public AbstractGame(Model model, GameType gameType) {
        this.model = model;
        this.gameType = gameType;
    }

    @Override
    public GameType getGameType() {
        return gameType;
    }

    @Override
    public ModelCell[][] getGameField() {
        return gameField;
    }

    @Override
    public String checkWinConditions() {
        return "";
    }

    @Override
    public void run() {
    }

    @Override
    public void terminateThread() {
        isThreadNeeded = false;
    }
}

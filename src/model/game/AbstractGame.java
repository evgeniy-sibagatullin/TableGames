package model.game;

import enums.GameType;
import model.Model;
import model.ModelCell;
import model.piece.Piece;

import java.util.List;

public abstract class AbstractGame implements Game {

    protected Model model;
    private GameType gameType;
    private ModelCell[][] gameField;
    private List<Piece> pieces;

    public AbstractGame(Model model) {
        this.model = model;
    }

    @Override
    public GameType getGameType() {
        return gameType;
    }

    @Override
    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    @Override
    public ModelCell[][] getGameField() {
        return gameField;
    }

    @Override
    public void setGameField(ModelCell[][] gameField) {
        this.gameField = gameField;
    }

    @Override
    public List<Piece> getPieces() {
        return pieces;
    }

    @Override
    public void setPieces(List<Piece> pieces) {
        this.pieces = pieces;
    }

    @Override
    public boolean checkWinConditions() {
        return false;
    }
}

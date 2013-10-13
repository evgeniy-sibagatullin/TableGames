package game;

import enums.GameType;
import model.impl.ModelCell;
import piece.Piece;

import java.util.List;

public abstract class AbstractGame implements Game {

    private GameType gameType;
    private ModelCell[][] gameField;
    private List<Piece> pieces;
    private int score;

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
    public int getScore() {
        return score;
    }

    @Override
    public void setScore(int score) {
        this.score = score;
    }

}

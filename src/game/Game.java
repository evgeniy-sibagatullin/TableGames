package game;

import enums.GameType;
import model.impl.ModelCell;
import piece.Piece;

import java.util.List;

public interface Game {

    GameType getGameType();

    void setGameType(GameType gameType);

    int getFieldSize();

    ModelCell[][] getGameField();

    void setGameField(ModelCell[][] gameField);

    List<Piece> getPieces();

    void setPieces(List<Piece> pieces);

    int getScore();

    void setScore(int score);

    void clickCell(int row, int column);

}
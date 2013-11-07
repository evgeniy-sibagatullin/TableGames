package model.game;

import enums.GameType;
import model.ModelCell;
import model.piece.Piece;

import java.util.List;

public interface Game {

    GameType getGameType();

    void setGameType(GameType gameType);

    int getFieldSize();

    ModelCell[][] getGameField();

    void setGameField(ModelCell[][] gameField);

    List<Piece> getPieces();

    void setPieces(List<Piece> pieces);

    boolean clickCell(int row, int column);

    boolean checkWinConditions();

    void viewUpdateComplete();
}
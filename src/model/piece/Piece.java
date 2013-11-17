package model.piece;

import enums.Side;
import model.ModelCell;

public interface Piece {

    int getRow();

    void setRow(int row);

    int getColumn();

    void setColumn(int column);

    Side getSide();

    void setSide(Side side);

    void setGameField(ModelCell[][] gameField);

    String getImagePath();

}

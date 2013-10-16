package piece;

import enums.Side;

public interface Piece {

    int getRow();

    void setRow(int row);

    int getColumn();

    void setColumn(int column);

    Side getSide();

    void setSide(Side side);

    String getImagePath();

}

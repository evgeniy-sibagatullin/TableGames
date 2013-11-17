package games.chess.piece;

import enums.Side;
import model.ModelCell;

public class Bishop extends ChessPiece {

    public Bishop(int row, int column, Side side, ModelCell[][] gameField) {
        super(row, column, side, gameField);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/chess/Chess-BishopW.png" :
                "img/chess/Chess-BishopB.png";
    }

    @Override
    public int getPower() {
        return 3;
    }

}

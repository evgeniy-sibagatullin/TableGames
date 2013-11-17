package games.chess.piece;

import enums.Side;
import model.ModelCell;

public class King extends ChessPiece {

    public King(int row, int column, Side side, ModelCell[][] gameField) {
        super(row, column, side, gameField);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/chess/Chess-KingW.png" :
                "img/chess/Chess-KingB.png";
    }

    @Override
    public int getPower() {
        return 6;
    }

}

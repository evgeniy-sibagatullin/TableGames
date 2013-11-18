package games.draughts.piece;

import enums.Side;
import model.ModelCell;

public class King extends DraughtsPiece {

    public King(int row, int column, Side side, ModelCell[][] gameField) {
        super(row, column, side, gameField);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/draughts/Draughts-KingB.png" :
                "img/draughts/Draughts-KingR.png";
    }

    @Override
    public boolean isAbleToCapture(Side side) {
        return side == null;
    }

    @Override
    public boolean isAbleToMove(Side side) {
        return side == null;
    }
}

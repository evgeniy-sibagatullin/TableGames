package games.draughts.piece;

import enums.Side;
import model.ModelCell;

import java.util.List;

public class King extends DraughtsPiece {

    public King(int row, int column, Side side, ModelCell[][] gameField) {
        super(row, column, side, gameField);
    }

    @Override
    public String getImagePath() {
        return (side == Side.WHITE) ?
                "img/draughts/Draughts-KingB.png" :
                "img/draughts/Draughts-KingR.png";
    }

    @Override
    public boolean isAbleToCapture() {
        return false;
    }

    @Override
    public boolean isAbleToMove() {
        return false;
    }

    @Override
    public List<ModelCell> getCellsAllowedToCapture() {
        return cellsAllowedToCaptureIn;
    }

    @Override
    public List<ModelCell> getCellsAllowedToMoveIn() {
        return cellsAllowedToMoveIn;
    }
}

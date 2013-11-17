package games.chess.piece;

import enums.Side;
import model.ModelCell;

public class Queen extends ChessPiece {

    public Queen(int row, int column, Side side, ModelCell[][] gameField) {
        super(row, column, side, gameField);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/chess/Chess-QueenW.png" :
                "img/chess/Chess-QueenB.png";
    }

    @Override
    public int getPower() {
        return 5;
    }
}

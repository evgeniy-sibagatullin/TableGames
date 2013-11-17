package games.chess.piece;

import enums.Side;
import model.ModelCell;

public class Rook extends ChessPiece {

    public Rook(int row, int column, Side side, ModelCell[][] gameField) {
        super(row, column, side, gameField);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/chess/Chess-RookW.png" :
                "img/chess/Chess-RookB.png";
    }

    @Override
    public int getPower() {
        return 4;
    }
}

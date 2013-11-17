package games.chess.piece;

import enums.Side;
import model.ModelCell;

public class Pawn extends ChessPiece {

    public Pawn(int row, int column, Side side, ModelCell[][] gameField) {
        super(row, column, side, gameField);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/chess/Chess-PawnW.png" :
                "img/chess/Chess-PawnB.png";
    }

    @Override
    public int getPower() {
        return 1;
    }

}

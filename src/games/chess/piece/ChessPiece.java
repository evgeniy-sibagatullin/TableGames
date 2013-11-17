package games.chess.piece;

import enums.Side;
import model.ModelCell;
import model.piece.GamePiece;

public abstract class ChessPiece extends GamePiece {

    public ChessPiece(int row, int column, Side side, ModelCell[][] gameField) {
        super(row, column, side, gameField);
    }

    public abstract int getPower();

}

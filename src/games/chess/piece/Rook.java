package games.chess.piece;

import enums.Side;
import model.piece.GamePiece;

public class Rook extends GamePiece {

    public Rook(int row, int column, Side side) {
        super(row, column, side);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/chess/Chess-RookW.png" :
                "img/chess/Chess-RookB.png";
    }

}

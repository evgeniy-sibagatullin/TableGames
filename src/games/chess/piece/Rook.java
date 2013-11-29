package games.chess.piece;

import enums.Side;
import model.game.gamefield.Gamefield;
import model.game.position.Position;

public class Rook extends ChessPiece {

    public Rook(Position position, Side side, Gamefield gameField) {
        super(position, side, gameField);
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

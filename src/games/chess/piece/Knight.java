package games.chess.piece;

import enums.Side;
import model.game.gamefield.Gamefield;
import model.game.position.Position;

public class Knight extends ChessPiece {

    public Knight(Position position, Side side, Gamefield gameField) {
        super(position, side, gameField);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/chess/Chess-KnightW.png" :
                "img/chess/Chess-KnightB.png";
    }

    @Override
    public int getPower() {
        return 2;
    }
}

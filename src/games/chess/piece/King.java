package games.chess.piece;

import enums.Side;
import model.game.gamefield.Gamefield;
import model.game.position.Position;

public class King extends ChessPiece {

    public King(Position position, Side side, Gamefield gameField) {
        super(position, side, gameField);
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

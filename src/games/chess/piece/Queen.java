package games.chess.piece;

import enums.Side;
import model.game.gamefield.Gamefield;
import model.game.position.Position;

public class Queen extends ChessPiece {

    public Queen(Position position, Side side, Gamefield gameField) {
        super(position, side, gameField);
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

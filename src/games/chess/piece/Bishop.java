package games.chess.piece;

import enums.Side;
import model.game.gamefield.Gamefield;
import model.game.position.Position;

public class Bishop extends ChessPiece {

    public Bishop(Position position, Side side, Gamefield gameField) {
        super(position, side, gameField);
    }

    @Override
    public String getImagePath() {
        return (getSide() == Side.WHITE) ?
                "img/chess/Chess-BishopW.png" :
                "img/chess/Chess-BishopB.png";
    }

    @Override
    public int getPower() {
        return 3;
    }

}

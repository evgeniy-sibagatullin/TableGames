package games.chess.piece;

import enums.Side;
import model.game.gamefield.Gamefield;
import model.game.position.Position;

public class Pawn extends ChessPiece {

    public Pawn(Position position, Side side, Gamefield gameField) {
        super(position, side, gameField);
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

package games.chess.piece;

import enums.Side;
import model.game.gamefield.Gamefield;
import model.game.piece.Piece;
import model.game.position.Position;

public abstract class ChessPiece extends Piece {

    public ChessPiece(Position position, Side side, Gamefield gameField) {
        super(position, side, gameField);
    }

    public abstract int getPower();
}

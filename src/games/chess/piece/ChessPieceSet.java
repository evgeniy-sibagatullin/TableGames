package games.chess.piece;

import enums.Side;
import model.game.gamefield.Gamefield;
import model.game.piece.Piece;
import model.game.piece.PieceSet;
import model.game.position.Position;

import java.util.ArrayList;

public class ChessPieceSet extends PieceSet {

    public ChessPieceSet(Gamefield gamefield) {
        super(gamefield);
    }

    @Override
    protected void initializePieces() {
        pieces = new ArrayList<Piece>();

        for (int i = 0; i < gamefield.getSize(); i++) {
            pieces.add(new Pawn(new Position(6, i), Side.WHITE, gamefield));
            pieces.add(new Pawn(new Position(1, i), Side.BLACK, gamefield));
        }

        pieces.add(new Rook(new Position(0, 0), Side.BLACK, gamefield));
        pieces.add(new Rook(new Position(0, 7), Side.BLACK, gamefield));
        pieces.add(new Rook(new Position(7, 0), Side.WHITE, gamefield));
        pieces.add(new Rook(new Position(7, 7), Side.WHITE, gamefield));

        pieces.add(new Knight(new Position(0, 1), Side.BLACK, gamefield));
        pieces.add(new Knight(new Position(0, 6), Side.BLACK, gamefield));
        pieces.add(new Knight(new Position(7, 1), Side.WHITE, gamefield));
        pieces.add(new Knight(new Position(7, 6), Side.WHITE, gamefield));

        pieces.add(new Bishop(new Position(0, 2), Side.BLACK, gamefield));
        pieces.add(new Bishop(new Position(0, 5), Side.BLACK, gamefield));
        pieces.add(new Bishop(new Position(7, 2), Side.WHITE, gamefield));
        pieces.add(new Bishop(new Position(7, 5), Side.WHITE, gamefield));

        pieces.add(new Queen(new Position(0, 3), Side.BLACK, gamefield));
        pieces.add(new Queen(new Position(7, 3), Side.WHITE, gamefield));
        pieces.add(new King(new Position(0, 4), Side.BLACK, gamefield));
        pieces.add(new King(new Position(7, 4), Side.WHITE, gamefield));

        addPiecesToGameField();
    }
}

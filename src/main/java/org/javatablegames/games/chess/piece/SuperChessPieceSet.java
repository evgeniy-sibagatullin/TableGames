package org.javatablegames.games.chess.piece;

import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.position.Position;
import org.javatablegames.games.chess.gamefield.ChessField;

import java.util.HashSet;

public class SuperChessPieceSet extends ChessPieceSet{

    public SuperChessPieceSet(ChessField gamefield) {
        super(gamefield);
        gamefield.removePieces();
    }

    @Override
    protected void initializePieces() {
        pieces = new HashSet<>();

        blackKing = new King(new Position(0, 4), Side.BLACK, King.POWER, gamefield);
        pieces.add(blackKing);

        for (int i = 0; i < gamefield.getSize(); i++) {
            pieces.add(new Pawn(new Position(1, i), Side.BLACK, Pawn.POWER, gamefield));
        }

        pieces.add(new Rook(new Position(0, 0), Side.BLACK, Rook.POWER, gamefield));
        pieces.add(new Rook(new Position(0, 7), Side.BLACK, Rook.POWER, gamefield));

        pieces.add(new Knight(new Position(0, 1), Side.BLACK, Knight.POWER, gamefield));
        pieces.add(new Knight(new Position(0, 6), Side.BLACK, Knight.POWER, gamefield));

        pieces.add(new Bishop(new Position(0, 2), Side.BLACK, Bishop.POWER, gamefield));
        pieces.add(new Bishop(new Position(0, 5), Side.BLACK, Bishop.POWER, gamefield));

        pieces.add(new Queen(new Position(0, 3), Side.BLACK, Queen.POWER, gamefield));
        pieces.add(new SuperQueen(new Position(7, 3), Side.WHITE, SuperQueen.SUPER_POWER, gamefield));

        addPiecesToGameField();
    }


}

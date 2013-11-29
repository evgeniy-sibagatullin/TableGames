package games.chess.piece;

import enums.Side;
import model.game.gamefield.Gamefield;
import model.game.gamefield.ModelCell;
import model.game.piece.Piece;
import model.game.position.Position;

import java.util.ArrayList;
import java.util.List;

public class ChessPieceInit {

    private final List<Piece> pieces = new ArrayList<Piece>();

    public List<Piece> initPieces(Gamefield gamefield) {
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

        addPiecesToGameField(gamefield);

        return pieces;
    }

    private void addPiecesToGameField(Gamefield gamefield) {
        for (Piece piece : pieces) {
            ModelCell cell = gamefield.getCell(piece.getPosition());
            cell.setPiece(piece);
        }
    }

}

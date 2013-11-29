package games.draughts.piece;

import enums.Side;
import model.game.gamefield.Gamefield;
import model.game.gamefield.ModelCell;
import model.game.piece.Piece;
import model.game.position.Position;

import java.util.ArrayList;
import java.util.List;

public class DraughtsPieceInit {

    private final List<Piece> pieces = new ArrayList<Piece>();

    public List<Piece> initPieces(Gamefield gamefield) {
        int size = gamefield.getSize();
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                if ((row + column) % 2 != 0) {
                    if (row < 3) {
                        pieces.add(new Man(new Position(row, column), Side.BLACK, gamefield));
                    }

                    if (row > 4) {
                        pieces.add(new Man(new Position(row, column), Side.WHITE, gamefield));
                    }
                }
            }
        }

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

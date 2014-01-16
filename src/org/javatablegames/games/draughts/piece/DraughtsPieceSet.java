package org.javatablegames.games.draughts.piece;

import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.game.piece.PieceSet;
import org.javatablegames.core.model.position.Position;
import org.javatablegames.games.draughts.gamefield.DraughtsField;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DraughtsPieceSet extends PieceSet<DraughtsPiece> {

    public DraughtsPieceSet(DraughtsField gamefield) {
        super(gamefield);
    }

    public DraughtsPieceSet(DraughtsPieceSet pieceSet) {
        super(pieceSet);
    }

    protected void initializePieces() {
        pieces = new HashSet<DraughtsPiece>();
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

        addPiecesToGameField();
    }

    protected void clonePiecesToGamefield(PieceSet<DraughtsPiece> inputPieces) throws CloneNotSupportedException {
        pieces = new HashSet<DraughtsPiece>();

        for (DraughtsPiece inputPiece : inputPieces.getPieces()) {
            pieces.add((DraughtsPiece) inputPiece.clone());
        }

        addPiecesToGameField();
    }

    public List<DraughtsPiece> getPiecesAbleToCapture(Side side) {
        List<DraughtsPiece> pieceList = new ArrayList<DraughtsPiece>();

        for (DraughtsPiece piece : pieces) {
            if (piece.getSide() == side && piece.isAbleToCapture()) {
                pieceList.add(piece);
            }
        }

        return pieceList;
    }

    public List<DraughtsPiece> getPiecesAbleToMove(Side side) {
        List<DraughtsPiece> pieceList = new ArrayList<DraughtsPiece>();

        for (DraughtsPiece piece : pieces) {
            if (piece.getSide() == side && piece.isAbleToMove()) {
                pieceList.add(piece);
            }
        }

        return pieceList;
    }

}

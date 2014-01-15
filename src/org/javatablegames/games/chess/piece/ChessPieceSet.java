package org.javatablegames.games.chess.piece;

import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.game.gamefield.ModelCell;
import org.javatablegames.core.model.game.piece.PieceSet;
import org.javatablegames.core.model.position.Position;
import org.javatablegames.games.chess.gamefield.ChessField;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ChessPieceSet extends PieceSet<ChessPiece> {

    private King whiteKing;
    private King blackKing;

    public ChessPieceSet(ChessField gamefield) {
        super(gamefield);
    }

    public ChessPieceSet(ChessPieceSet pieceSet) {
        super(pieceSet);
    }

    @Override
    protected void initializePieces() {
        pieces = new HashSet<ChessPiece>();

        blackKing = new King(new Position(0, 4), Side.BLACK, gamefield);
        whiteKing = new King(new Position(7, 4), Side.WHITE, gamefield);
        pieces.add(blackKing);
        pieces.add(whiteKing);

        for (int i = 0; i < gamefield.getSize(); i++) {
            pieces.add(new Pawn(new Position(1, i), Side.BLACK, gamefield));
            pieces.add(new Pawn(new Position(6, i), Side.WHITE, gamefield));
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

        addPiecesToGameField();
    }

    @Override
    protected void clonePiecesToGamefield(PieceSet<ChessPiece> inputPieces) {
        pieces = new HashSet<ChessPiece>();

        for (ChessPiece inputPiece : inputPieces.getPieces()) {
            ChessPiece chessPiece;

            if (inputPiece instanceof Pawn) {
                chessPiece = new Pawn(inputPiece.getPosition(), inputPiece.getSide(), gamefield);
            } else if (inputPiece instanceof Knight) {
                chessPiece = new Knight(inputPiece.getPosition(), inputPiece.getSide(), gamefield);
            } else if (inputPiece instanceof Bishop) {
                chessPiece = new Bishop(inputPiece.getPosition(), inputPiece.getSide(), gamefield);
            } else if (inputPiece instanceof Rook) {
                chessPiece = new Rook(inputPiece.getPosition(), inputPiece.getSide(), gamefield);
            } else if (inputPiece instanceof Queen) {
                chessPiece = new Queen(inputPiece.getPosition(), inputPiece.getSide(), gamefield);
            } else {
                if (inputPiece.getSide().equals(Side.WHITE)) {
                    whiteKing = new King(inputPiece.getPosition(), inputPiece.getSide(), gamefield);
                    chessPiece = whiteKing;
                } else {
                    blackKing = new King(inputPiece.getPosition(), inputPiece.getSide(), gamefield);
                    chessPiece = blackKing;
                }
            }

            if (inputPiece.isEverMoved()) {
                chessPiece.setMoved();
            }

            pieces.add(chessPiece);
        }

        addPiecesToGameField();
    }

    public List<ChessPiece> getPiecesAbleToMove(Side side) {
        List<ChessPiece> pieceList = new ArrayList<ChessPiece>();

        for (ChessPiece piece : pieces) {

            if (piece.getSide() == side && piece.isAbleToMove()) {
                removeMovesCauseCheck(piece);

                if (!piece.getCellsAllowedToMoveIn().isEmpty()) {
                    pieceList.add(piece);
                }
            }
        }

        return pieceList;
    }

    public boolean isKingUnderAttack(Side side) {
        ChessPiece king = getKingBySide(side);
        return king != null && gamefield.isCellUnderAttack(king.getPosition(), side);
    }

    private King getKingBySide(Side side) {
        return (side.equals(Side.WHITE)) ? whiteKing : blackKing;
    }

    private void removeMovesCauseCheck(ChessPiece chessPiece) {
        List<ModelCell<ChessPiece>> cellsAllowedToMoveIn = chessPiece.getCellsAllowedToMoveIn();
        List<ModelCell> cellsToRemove = new ArrayList<ModelCell>();

        for (ModelCell<ChessPiece> modelCell : cellsAllowedToMoveIn) {
            ChessPieceSet nextPieceSet = new ChessPieceSet(this);
            gamefield.setSelectedCellByPosition(chessPiece.getPosition());

            Position position = modelCell.getPosition();
            if (gamefield.isCellEmpty(position)) {
                ((ChessField) gamefield).moveToCell(modelCell);
            } else {
                ((ChessField) gamefield).captureToCell(modelCell);
            }

            if (nextPieceSet.isKingUnderAttack(chessPiece.getSide())) {
                cellsToRemove.add(modelCell);
            }
        }

        cellsAllowedToMoveIn.removeAll(cellsToRemove);
        this.applyPiecesToGamefield();
    }

}

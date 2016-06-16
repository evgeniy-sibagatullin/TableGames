package org.javatablegames.games.chess;

import org.javatablegames.core.enums.CellState;
import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.Model;
import org.javatablegames.core.model.game.Game;
import org.javatablegames.core.model.game.gamefield.ModelCell;
import org.javatablegames.core.model.position.Position;
import org.javatablegames.games.chess.gamefield.ChessField;
import org.javatablegames.games.chess.piece.ChessPiece;
import org.javatablegames.games.chess.piece.ChessPieceSet;

import java.util.List;

public abstract class AbstractChess extends Game<ChessField, ChessPieceSet> {

    protected static final int DELAY_PERIOD = 500;

    protected Side sidePlayer;
    protected boolean isPlayerMove;
    protected String checkWinConditionsResult = "";
    protected List<ChessPiece> ableToMoveList;
    protected Integer moveIndex = 1;

    public AbstractChess() {
        gamefield = new ChessField();
        pieceSet = new ChessPieceSet(gamefield);
    }

    @Override
    public String checkWinConditions() {
        return checkWinConditionsResult;
    }

    @Override
    public void clickCell(Position position) {
        if (isPlayerMove) {
            ModelCell modelCell = gamefield.getCell(position);

            if (gamefield.getSelectedCell() == null) {
                if (modelCell.getCellState() == CellState.ALLOWED_PIECE) {
                    gamefield.selectCell(modelCell);
                }
            } else {
                if (modelCell.getCellState() == CellState.ALLOWED_PIECE) {
                    gamefield.reselectCell(modelCell);
                } else if (modelCell.getCellState() == CellState.ALLOWED_MOVE) {
                    updateMoveHistory();
                    gamefield.moveToCell(modelCell);
                    isPlayerMove = false;
                } else if (modelCell.getCellState() == CellState.ATTACKED) {
                    updateMoveHistory();
                    gamefield.captureToCell(modelCell);
                    isPlayerMove = false;
                }
            }

            Model.INSTANCE.setChanged(true);
        }
    }

    protected void giveMoveToPlayer() {
        if (hasPlayerAnyMove()) {
            gamefield.updatePiecesReadyToMove(ableToMoveList);
        } else {
            if (pieceSet.isKingUnderAttack(sidePlayer)) {
                String winnerSideName = (sidePlayer.equals(Side.WHITE)) ? "Black" : "White";
                checkWinConditionsResult = "Congratulations to winner - " + winnerSideName + " player!";
            } else {
                checkWinConditionsResult = "Stalemate. Draw.";
            }
        }

        Model.INSTANCE.setChanged(true);
        isPlayerMove = true;
    }

    private void updateMoveHistory() {
        moveHistory.put(moveIndex, new ChessPieceSet(pieceSet));
        pieceSet.applyPiecesToGamefield();

        if (moveHistory.containsKey(++moveIndex)) {
            cleanFurtherHistory();
        }
    }

    private void cleanFurtherHistory() {
        int index = moveIndex;

        while (moveHistory.containsKey(index)) {
            moveHistory.remove(index++);
        }
    }

    protected boolean hasPlayerAnyMove() {
        ableToMoveList = pieceSet.getPiecesAbleToMove(sidePlayer);
        return !ableToMoveList.isEmpty();
    }

}

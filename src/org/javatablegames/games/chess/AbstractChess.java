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

    protected Side sidePlayer;
    protected boolean isPlayerMove;
    protected String checkWinConditionsResult = "";
    protected List<ChessPiece> ableToMoveList;

    public AbstractChess(Model model) {
        super(model);
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
                    gamefield.moveToCell(modelCell);
                    isPlayerMove = false;
                } else if (modelCell.getCellState() == CellState.ATTACKED) {
                    gamefield.captureToCell(modelCell);
                    isPlayerMove = false;
                }
            }

            model.setChanged(true);
        }
    }

    protected void giveMoveToPlayer() {
        if (hasPlayerAnyMove()) {
            updateGameFieldForPlayer();
            isPlayerMove = true;
        } else {
            String winnerSideName = (sidePlayer.equals(Side.WHITE)) ? "Black" : "White";
            checkWinConditionsResult = "Congratulations to winner - " + winnerSideName + " player!";
        }
    }

    private boolean hasPlayerAnyMove() {
        ableToMoveList = pieceSet.getPiecesAbleToMove(sidePlayer);
        return !ableToMoveList.isEmpty();
    }

    private void updateGameFieldForPlayer() {
        gamefield.setTotalCellStateDefault();
        gamefield.updatePiecesReadyToMove(ableToMoveList);
        model.setChanged(true);
    }

}

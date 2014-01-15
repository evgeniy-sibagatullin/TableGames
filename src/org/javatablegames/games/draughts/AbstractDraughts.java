package org.javatablegames.games.draughts;

import org.javatablegames.core.enums.CellState;
import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.Model;
import org.javatablegames.core.model.game.Game;
import org.javatablegames.core.model.game.gamefield.ModelCell;
import org.javatablegames.core.model.position.Position;
import org.javatablegames.games.draughts.gamefield.DraughtsField;
import org.javatablegames.games.draughts.piece.DraughtsPiece;
import org.javatablegames.games.draughts.piece.DraughtsPieceSet;

import java.util.List;

public abstract class AbstractDraughts extends Game<DraughtsField, DraughtsPieceSet> {

    protected Side sidePlayer;
    protected boolean isPlayerMove;
    protected String checkWinConditionsResult = "";
    protected List<DraughtsPiece> ableToCaptureList;
    protected List<DraughtsPiece> ableToMoveList;

    protected AbstractDraughts(Model model) {
        super(model);
        gamefield = new DraughtsField();
        pieceSet = new DraughtsPieceSet(gamefield);
    }

    @Override
    public String checkWinConditions() {
        return checkWinConditionsResult;
    }

    @Override
    public void clickCell(Position position) {
        if (isPlayerMove) {
            ModelCell<DraughtsPiece> modelCell = gamefield.getCell(position);

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
                    capturePlayer(modelCell);
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
        ableToCaptureList = pieceSet.getPiecesAbleToCapture(sidePlayer);
        ableToMoveList = pieceSet.getPiecesAbleToMove(sidePlayer);
        return !ableToCaptureList.isEmpty() || !ableToMoveList.isEmpty();
    }

    private void updateGameFieldForPlayer() {
        gamefield.setTotalCellStateDefault();

        if (!ableToCaptureList.isEmpty()) {
            gamefield.updatePiecesReadyToMove(ableToCaptureList);
        } else {
            gamefield.updatePiecesReadyToMove(ableToMoveList);
        }

        model.setChanged(true);
    }

    private void capturePlayer(ModelCell<DraughtsPiece> modelCell) {
        gamefield.captureToCell(modelCell);
        boolean isAbleToCaptureAgain = gamefield.isAbleToCapture(modelCell);

        if (isAbleToCaptureAgain) {
            gamefield.setTotalCellStateDefault();
            gamefield.selectCell(modelCell);
        }

        isPlayerMove = isAbleToCaptureAgain;
    }

}
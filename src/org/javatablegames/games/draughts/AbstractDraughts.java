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
    protected boolean needToPrepareFieldForPlayer;
    protected String checkWinConditionsResult = "";

    protected AbstractDraughts(Model model) {
        super(model);
        gamefield = new DraughtsField();
        pieceSet = new DraughtsPieceSet(gamefield);
        gamefield.setPieceSet(pieceSet);
    }

    @Override
    public String checkWinConditions() {
        return checkWinConditionsResult;
    }

    protected void updateGameFieldForPlayer() {
        List<DraughtsPiece> ableToCaptureList = pieceSet.getPiecesAbleToCapture(sidePlayer);
        List<DraughtsPiece> ableToMoveList = pieceSet.getPiecesAbleToMove(sidePlayer);

        if (!ableToCaptureList.isEmpty()) {
            gamefield.updatePiecesReadyToMove(ableToCaptureList);
        } else if (!ableToMoveList.isEmpty()) {
            gamefield.updatePiecesReadyToMove(ableToMoveList);
        } else {
            checkWinConditionsResult = "You have lost this game.";
        }


        needToPrepareFieldForPlayer = false;
        model.setChanged(true);
    }

    @Override
    public void clickCell(Position position) {
        if (isPlayerMove) {
            boolean isModelChanged = false;
            ModelCell modelCell = gamefield.getCell(position);
            if (gamefield.getSelectedCell() != null) {
                if (modelCell.getCellState() == CellState.ATTACKED) {
                    capturePlayer(modelCell);
                    isModelChanged = true;
                } else if (modelCell.getCellState() == CellState.ALLOWED_MOVE) {
                    movePlayer(modelCell);
                    isModelChanged = true;
                } else if (modelCell.getCellState() == CellState.ALLOWED_PIECE) {
                    reselectPlayer(modelCell);
                    isModelChanged = true;
                }
            } else {
                if (modelCell.getCellState() == CellState.ALLOWED_PIECE) {
                    selectPlayer(modelCell);
                    isModelChanged = true;
                }
            }
            model.setChanged(isModelChanged);
        }
    }

    private void capturePlayer(ModelCell modelCell) {
        gamefield.captureToCell(modelCell);
        boolean isAbleToCaptureAgain = gamefield.isAbleToCapture(modelCell);
        if (isAbleToCaptureAgain) {
            gamefield.totalGameFieldCleanUp();
            selectPlayer(modelCell);
        }
        isPlayerMove = isAbleToCaptureAgain;
    }

    private void movePlayer(ModelCell modelCell) {
        gamefield.moveToCell(modelCell);
        isPlayerMove = false;
    }

    private void reselectPlayer(ModelCell modelCell) {
        gamefield.reselectCell(modelCell);
    }

    private void selectPlayer(ModelCell modelCell) {
        gamefield.selectCell(modelCell);
    }

    protected void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

}
package games.draughts;

import enums.CellState;
import enums.Side;
import games.draughts.gamefield.DraughtsField;
import games.draughts.piece.DraughtsPiece;
import games.draughts.piece.DraughtsPieces;
import model.Model;
import model.game.Game;
import model.game.gamefield.ModelCell;
import model.game.position.Position;

import java.util.List;

public abstract class AbstractDraughts extends Game<DraughtsField, DraughtsPieces> {

    protected Side sidePlayer = Side.WHITE;
    protected boolean isPlayerMove = true;
    protected boolean needToPrepareFieldForPlayer = true;
    protected String checkWinConditionsResult = "";

    public AbstractDraughts(Model model) {
        super(model);
        gamefield = new DraughtsField();
        pieces = new DraughtsPieces(gamefield);
        gamefield.setPieces(pieces);
    }

    @Override
    public String checkWinConditions() {
        return checkWinConditionsResult;
    }

    protected void updateGameFieldForPlayer() {
        List<DraughtsPiece> ableToCaptureList = pieces.getPiecesAbleToCapture(sidePlayer);
        List<DraughtsPiece> ableToMoveList = pieces.getPiecesAbleToMove(sidePlayer);

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
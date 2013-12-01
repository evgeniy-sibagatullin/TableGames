package games.draughts;

import enums.GameType;
import enums.Side;
import games.draughts.piece.DraughtsPiece;
import model.Model;

import java.util.List;

public class DraughtsVsAi extends AbstractDraughts {

    private final Side sideAI = Side.BLACK;

    public DraughtsVsAi(Model model) {
        super(model);
        gameType = GameType.DRAUGHTS_VS_AI;
    }

    @Override
    public void run() {
        while (isThreadNeeded) {
            if (needToPrepareFieldForPlayer) {
                updateGameFieldForPlayer();
            }
            if (!isPlayerMove) {
                performMoveAI();
            }
            delay(50);
        }
    }

    private void performMoveAI() {
        gamefield.totalGameFieldCleanUp();

        List<DraughtsPiece> ableToCaptureList = pieces.getPiecesAbleToCapture(sideAI);
        if (!ableToCaptureList.isEmpty()) {
            capturePieceAI(ableToCaptureList.get(0));
        } else {
            List<DraughtsPiece> ableToMoveList = pieces.getPiecesAbleToMove(sideAI);
            if (!ableToMoveList.isEmpty()) {
                movePieceAI(ableToMoveList.get(0));
            } else {
                checkWinConditionsResult = "Congratulations! You have win this game!";
            }
        }
        isPlayerMove = true;
        needToPrepareFieldForPlayer = true;
    }

    private void capturePieceAI(DraughtsPiece piece) {
        while (piece.isAbleToCapture()) {
            gamefield.setSelectedCellByPiece(piece);
            gamefield.captureToCell(piece.getCellsAllowedToCapture().get(0));
        }
    }

    private void movePieceAI(DraughtsPiece piece) {
        gamefield.setSelectedCellByPiece(piece);
        gamefield.moveToCell(piece.getCellsAllowedToMoveIn().get(0));
    }

}

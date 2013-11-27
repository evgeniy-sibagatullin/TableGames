package games.draughts;

import enums.GameType;
import enums.Side;
import games.draughts.piece.DraughtsPiece;
import model.Model;

import java.util.List;

public class ClassicDraughtsVsAi extends ClassicDraughts {

    private Side sideAI = Side.BLACK;

    public ClassicDraughtsVsAi(Model model) {
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
        totalGameFieldCleanUp();

        List<DraughtsPiece> ableToCaptureList = getPiecesAbleToCapture(sideAI);
        if (!ableToCaptureList.isEmpty()) {
            capturePieceAI(ableToCaptureList.get(0));
        } else {
            List<DraughtsPiece> ableToMoveList = getPiecesAbleToMove(sideAI);
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
            selectedCell = gameField[piece.getRow()][piece.getColumn()];
            captureToCell(piece.getCellsAllowedToCapture().get(0));
        }
    }

    private void movePieceAI(DraughtsPiece piece) {
        selectedCell = gameField[piece.getRow()][piece.getColumn()];
        moveToCell(piece.getCellsAllowedToMoveIn().get(0));
    }

}

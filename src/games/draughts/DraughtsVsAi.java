package games.draughts;

import enums.GameType;
import enums.Side;
import games.draughts.gamefield.DraughtsField;
import games.draughts.piece.DraughtsPiece;
import games.draughts.piece.DraughtsPieces;
import model.Model;
import model.game.gamefield.ModelCell;
import model.game.piece.Piece;

import java.util.List;
import java.util.Random;

public class DraughtsVsAi extends AbstractDraughts {

    private final Random random = new Random();
    private Side sideAI;
    private Side activeSide;

    public DraughtsVsAi(Model model) {
        super(model);
        gameType = GameType.DRAUGHTS_VS_AI;
        sidePlayer = Side.WHITE;
        sideAI = Side.BLACK;
        isPlayerMove = true;
        needToPrepareFieldForPlayer = true;
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
        DraughtsPieces bestMovePieces = new DraughtsPieces(new DraughtsField(), pieces);
        int bestMoveBalance = Integer.MIN_VALUE;
        activeSide = sideAI;

        List<DraughtsPiece> ableToCaptureList = pieces.getPiecesAbleToCapture(sideAI);
        if (!ableToCaptureList.isEmpty()) {
            for (DraughtsPiece piece : ableToCaptureList) {
                for (ModelCell modelCell : piece.getCellsAllowedToCapture()) {
                    gamefield.removePieces();
                    DraughtsPieces nextPieces = new DraughtsPieces(gamefield, pieces);

                    DraughtsPiece pieceToBeMoved = (DraughtsPiece) nextPieces.getPieceByPosition(piece.getPosition());
                    int nextCaptureBalance = checkNextCaptureMove(pieceToBeMoved, modelCell);
                    if (nextCaptureBalance > bestMoveBalance) {
                        bestMoveBalance = nextCaptureBalance;
                        bestMovePieces = new DraughtsPieces(gamefield, nextPieces);
                    }
                }
            }
            needToPrepareFieldForPlayer = true;
        } else {
            List<DraughtsPiece> ableToMoveList = pieces.getPiecesAbleToMove(sideAI);
            if (!ableToMoveList.isEmpty()) {
                activeSide = (activeSide.equals(sideAI)) ? sidePlayer : sideAI;

                for (DraughtsPiece piece : ableToMoveList) {
                    for (ModelCell modelCell : piece.getCellsAllowedToMoveIn()) {
                        gamefield.removePieces();
                        DraughtsPieces nextPieces = new DraughtsPieces(gamefield, pieces);

                        DraughtsPiece pieceToBeMoved = (DraughtsPiece) nextPieces.getPieceByPosition(piece.getPosition());
                        gamefield.setSelectedCellByPiece(pieceToBeMoved);
                        gamefield.moveToCell(modelCell);

                        int nextMoveBalance = checkNextMove(nextPieces);
                        if (nextMoveBalance > bestMoveBalance) {
                            bestMoveBalance = nextMoveBalance;
                            bestMovePieces = new DraughtsPieces(gamefield, nextPieces);
                        }
                    }
                }

                needToPrepareFieldForPlayer = true;
            } else {
                checkWinConditionsResult = "Congratulations! You have won this game!";
            }
        }

        gamefield.removePieces();
        pieces = new DraughtsPieces(gamefield, bestMovePieces);

        model.setChanged(true);
        isPlayerMove = true;
        gamefield.totalGameFieldCleanUp();
    }

    private int checkNextMove(DraughtsPieces inputPieces) {
        List<DraughtsPiece> ableToCaptureList = inputPieces.getPiecesAbleToCapture(activeSide);
        return (ableToCaptureList.isEmpty()) ? random.nextInt(100) : 0;
    }

    private int checkNextCaptureMove(DraughtsPiece piece, ModelCell modelCell) {
        gamefield.setSelectedCellByPiece(piece);
        gamefield.captureToCell(modelCell);
        if (piece.isAbleToCapture()) {
            return checkNextCaptureMove(piece, piece.getCellsAllowedToCapture().get(0));
        } else {
            return checkBalance();
        }
    }

    private int checkBalance() {
        int balance = 0;
        for (Piece piece : gamefield.getPieces()) {
            if (piece.getSide().equals(Side.BLACK)) {
                balance++;
            } else {
                balance--;
            }
        }
        return balance;
    }
}

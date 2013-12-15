package games.draughts;

import enums.GameType;
import enums.Side;
import games.draughts.piece.DraughtsPiece;
import games.draughts.piece.DraughtsPieceSet;
import model.Model;
import model.game.gamefield.ModelCell;
import model.game.piece.Piece;
import model.game.piece.PieceSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DraughtsVsAi extends AbstractDraughts {

    private final Random random = new Random();
    private Side sideAI;
    private Side activeSide;
    private int depth;
    private static final int MAX_DEPTH = 6;
    private List<DraughtsPieceSet> tempCapturePieceSetList = new ArrayList<DraughtsPieceSet>();

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
            delay(200);
        }
    }

    private synchronized void performMoveAI() {
        List<DraughtsPieceSet> nextPieceSetList = new ArrayList<DraughtsPieceSet>();
        activeSide = sideAI;
        depth = 0;

        List<DraughtsPiece> ableToCaptureList = pieceSet.getPiecesAbleToCapture(sideAI);
        if (!ableToCaptureList.isEmpty()) {
            nextPieceSetList = getCapturePieceSets(pieceSet, ableToCaptureList);
        } else {
            List<DraughtsPiece> ableToMoveList = pieceSet.getPiecesAbleToMove(sideAI);
            if (!ableToMoveList.isEmpty()) {
                nextPieceSetList = getMovePieceSets(pieceSet, ableToMoveList);
            } else {
                checkWinConditionsResult = "Congratulations! You have won this game!";
            }
        }

        if (!nextPieceSetList.isEmpty()) {
            DraughtsPieceSet bestMovePieces = new DraughtsPieceSet(pieceSet);
            int bestMoveBalance = Integer.MIN_VALUE;

            for (DraughtsPieceSet nextPieceSet : nextPieceSetList) {
                nextPieceSet.applyPiecesToGamefield();

                int nextMoveBalance = checkNextMoveQuality(nextPieceSet);
                if (nextMoveBalance >= bestMoveBalance) {
                    bestMoveBalance = nextMoveBalance;
                    bestMovePieces = new DraughtsPieceSet(nextPieceSet);
                }
            }

            pieceSet = new DraughtsPieceSet(bestMovePieces);
            isPlayerMove = true;
            needToPrepareFieldForPlayer = true;
        }

        gamefield.totalGameFieldCleanUp();
        model.setChanged(true);
    }

    private int checkNextMoveQuality(DraughtsPieceSet pieceSet) {
        depth++;
        if (depth == MAX_DEPTH) {
            depth--;
            return checkBalance(pieceSet);
        }

        List<DraughtsPieceSet> nextPieceSetList = new ArrayList<DraughtsPieceSet>();
        activeSide = (activeSide.equals(sideAI)) ? sidePlayer : sideAI;
        int bestMoveBalance = (activeSide.equals(sideAI)) ? Integer.MIN_VALUE / 2 : Integer.MAX_VALUE / 2;

        List<DraughtsPiece> ableToCaptureList = pieceSet.getPiecesAbleToCapture(activeSide);
        if (!ableToCaptureList.isEmpty()) {
            nextPieceSetList = getCapturePieceSets(pieceSet, ableToCaptureList);
        } else {
            List<DraughtsPiece> ableToMoveList = pieceSet.getPiecesAbleToMove(activeSide);
            if (!ableToMoveList.isEmpty()) {
                nextPieceSetList = getMovePieceSets(pieceSet, ableToMoveList);
            } else {
                bestMoveBalance = (activeSide.equals(sidePlayer)) ? Integer.MAX_VALUE - 5 : Integer.MIN_VALUE + 5;
            }
        }

        if (!nextPieceSetList.isEmpty()) {
            for (DraughtsPieceSet nextPieceSet : nextPieceSetList) {
                nextPieceSet.applyPiecesToGamefield();

                int nextMoveBalance = checkNextMoveQuality(nextPieceSet);
                if ((activeSide.equals(sideAI) && nextMoveBalance > bestMoveBalance)
                        || ((activeSide.equals(sidePlayer) && nextMoveBalance < bestMoveBalance))) {
                    bestMoveBalance = nextMoveBalance;
                }
            }
        }

        depth--;
        activeSide = (activeSide.equals(sideAI)) ? sidePlayer : sideAI;

        return bestMoveBalance;
    }

    private int checkBalance(PieceSet pieceSet) {
        int balance = 0;
        for (Piece piece : pieceSet.getPieces()) {
            if (piece.getSide().equals(sideAI)) {
                balance += piece.getPower();
            } else {
                balance -= piece.getPower();
            }
        }
        return balance - 5 + random.nextInt(10);
    }

    private List<DraughtsPieceSet> getCapturePieceSets(DraughtsPieceSet pieceSet, List<DraughtsPiece> ableToCaptureList) {
        List<DraughtsPieceSet> pieceSetList = new ArrayList<DraughtsPieceSet>();

        tempCapturePieceSetList.clear();

        for (DraughtsPiece piece : ableToCaptureList) {
            pieceSet.applyPiecesToGamefield();
            findPossibleCapturesByPiece(pieceSet, piece);

            for (DraughtsPieceSet capturePieceSet : tempCapturePieceSetList) {
                pieceSetList.add(capturePieceSet);
            }
        }

        return pieceSetList;
    }

    private void findPossibleCapturesByPiece(DraughtsPieceSet pieceSet, DraughtsPiece piece) {
        if (!piece.isAbleToCapture()) {
            tempCapturePieceSetList.add(pieceSet);
        } else {
            for (ModelCell modelCell : piece.getCellsAllowedToCapture()) {
                DraughtsPieceSet nextPieceSet = new DraughtsPieceSet(pieceSet);
                DraughtsPiece pieceToCapture = (DraughtsPiece) nextPieceSet.getPieceByPosition(piece.getPosition());
                gamefield.setSelectedCellByPiece(pieceToCapture);
                gamefield.captureToCell(modelCell);
                pieceToCapture = (DraughtsPiece) nextPieceSet.getPieceByPosition(pieceToCapture.getPosition());
                findPossibleCapturesByPiece(nextPieceSet, pieceToCapture);
            }
        }
    }

    private List<DraughtsPieceSet> getMovePieceSets(DraughtsPieceSet pieceSet, List<DraughtsPiece> ableToMoveList) {
        List<DraughtsPieceSet> pieceSetList = new ArrayList<DraughtsPieceSet>();

        for (DraughtsPiece piece : ableToMoveList) {
            for (ModelCell modelCell : piece.getCellsAllowedToMoveIn()) {
                DraughtsPieceSet movePieceSet = new DraughtsPieceSet(pieceSet);

                DraughtsPiece pieceToBeMoved = (DraughtsPiece) movePieceSet.getPieceByPosition(piece.getPosition());
                gamefield.setSelectedCellByPiece(pieceToBeMoved);
                gamefield.moveToCell(modelCell);

                pieceSetList.add(movePieceSet);
            }
        }

        return pieceSetList;
    }

}

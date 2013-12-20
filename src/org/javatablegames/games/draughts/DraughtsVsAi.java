package org.javatablegames.games.draughts;

import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.Model;
import org.javatablegames.core.model.game.gamefield.ModelCell;
import org.javatablegames.core.model.game.piece.Piece;
import org.javatablegames.core.model.game.piece.PieceSet;
import org.javatablegames.games.draughts.piece.DraughtsPiece;
import org.javatablegames.games.draughts.piece.DraughtsPieceSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DraughtsVsAi extends AbstractDraughts {

    private final Random random = new Random();
    private final Side sideAI;
    private final List<DraughtsPieceSet> tempCapturePieceSetList = new ArrayList<DraughtsPieceSet>();

    private Side activeSide;
    private int depth = 0;
    private static final int MAX_DEPTH = 6;

    public DraughtsVsAi(Model model) {
        super(model);
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

    private synchronized void performMoveAI() {
        activeSide = sidePlayer;
        List<DraughtsPieceSet> nextPieceSetList = getNextPieceSets(pieceSet);

        if (!nextPieceSetList.isEmpty()) {
            DraughtsPieceSet bestMovePieces = null;
            int bestMoveBalance = Integer.MIN_VALUE;

            for (DraughtsPieceSet nextPieceSet : nextPieceSetList) {
                nextPieceSet.applyPiecesToGamefield();

                int nextMoveBalance = checkNextMoveQuality(nextPieceSet);
                if (nextMoveBalance > bestMoveBalance) {
                    bestMoveBalance = nextMoveBalance;
                    bestMovePieces = new DraughtsPieceSet(nextPieceSet);
                }
            }

            pieceSet = new DraughtsPieceSet(bestMovePieces);
            needToPrepareFieldForPlayer = true;
            isPlayerMove = true;
        } else {
            checkWinConditionsResult = "Congratulations! You have won this game!";
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

        List<DraughtsPieceSet> nextPieceSetList = getNextPieceSets(pieceSet);

        int bestMoveBalance = (activeSide.equals(sideAI)) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        if (!nextPieceSetList.isEmpty()) {
            for (DraughtsPieceSet nextPieceSet : nextPieceSetList) {
                nextPieceSet.applyPiecesToGamefield();

                int nextMoveBalance = checkNextMoveQuality(nextPieceSet);
                if ((activeSide.equals(sideAI) && nextMoveBalance > bestMoveBalance)
                        || ((activeSide.equals(sidePlayer) && nextMoveBalance < bestMoveBalance))) {
                    bestMoveBalance = nextMoveBalance;
                }
            }
        } else {
            bestMoveBalance = (activeSide.equals(sidePlayer)) ? Integer.MAX_VALUE / 2 : Integer.MIN_VALUE / 2;
        }

        depth--;
        activeSide = (activeSide.equals(sideAI)) ? sidePlayer : sideAI;

        return bestMoveBalance;
    }

    private List<DraughtsPieceSet> getNextPieceSets(DraughtsPieceSet pieceSet) {
        List<DraughtsPieceSet> nextPieceSetList = new ArrayList<DraughtsPieceSet>();
        activeSide = (activeSide.equals(sideAI)) ? sidePlayer : sideAI;

        List<DraughtsPiece> ableToCaptureList = pieceSet.getPiecesAbleToCapture(activeSide);
        if (!ableToCaptureList.isEmpty()) {
            nextPieceSetList = getCapturePieceSets(pieceSet, ableToCaptureList);
        } else {
            List<DraughtsPiece> ableToMoveList = pieceSet.getPiecesAbleToMove(activeSide);
            if (!ableToMoveList.isEmpty()) {
                nextPieceSetList = getMovePieceSets(pieceSet, ableToMoveList);
            }
        }

        return nextPieceSetList;
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

}

package org.javatablegames.games.draughts;

import org.javatablegames.core.enums.CellState;
import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.Model;
import org.javatablegames.core.model.game.gamefield.ModelCell;
import org.javatablegames.games.draughts.piece.DraughtsPiece;
import org.javatablegames.games.draughts.piece.DraughtsPieceSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.javatablegames.core.enums.Side.oppositeSide;

public class DraughtsVsAi extends AbstractDraughts {

    private static final int DELAY_AI = 500;
    private static final int MAX_DEPTH = 6;
    private final Random random = new Random();
    private final Side sideAI;
    private final List<DraughtsPieceSet> tempCapturePieceSetList = new ArrayList<DraughtsPieceSet>();
    private Side activeSide;
    private int depth = 0;

    public DraughtsVsAi(Model model) {
        super(model);
        sidePlayer = Side.WHITE;

        sideAI = oppositeSide(sidePlayer);
        isPlayerMove = (sidePlayer.equals(Side.WHITE));
    }

    @Override
    public void run() {
        if (isPlayerMove) {
            giveMoveToPlayer();
        }

        while (isThreadNeeded) {
            if (!isPlayerMove) {
                updateGameFieldForAI();
                performMoveAI();
                giveMoveToPlayer();
            }

            delay(50);
        }
    }

    private void updateGameFieldForAI() {
        gamefield.setTotalCellStateDefault();
        model.setChanged(true);
        delay(DELAY_AI);
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

            showMoveAI(bestMovePieces);
            pieceSet = new DraughtsPieceSet(bestMovePieces);
        } else {
            checkWinConditionsResult = "Congratulations! You have won this game!";
        }
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
        activeSide = oppositeSide(activeSide);
        return bestMoveBalance;
    }

    private List<DraughtsPieceSet> getNextPieceSets(DraughtsPieceSet pieceSet) {
        List<DraughtsPieceSet> nextPieceSetList = new ArrayList<DraughtsPieceSet>();
        activeSide = oppositeSide(activeSide);
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
                DraughtsPiece pieceToCapture = nextPieceSet.getPieceByPosition(piece.getPosition());

                gamefield.setSelectedCellByPosition(piece.getPosition());
                gamefield.captureToCell(modelCell);

                pieceToCapture = nextPieceSet.getPieceByPosition(pieceToCapture.getPosition());
                findPossibleCapturesByPiece(nextPieceSet, pieceToCapture);
            }
        }
    }

    private List<DraughtsPieceSet> getMovePieceSets(DraughtsPieceSet pieceSet, List<DraughtsPiece> ableToMoveList) {
        List<DraughtsPieceSet> pieceSetList = new ArrayList<DraughtsPieceSet>();

        for (DraughtsPiece piece : ableToMoveList) {
            for (ModelCell modelCell : piece.getCellsAllowedToMoveIn()) {
                DraughtsPieceSet movePieceSet = new DraughtsPieceSet(pieceSet);

                gamefield.setSelectedCellByPosition(piece.getPosition());
                gamefield.moveToCell(modelCell);

                pieceSetList.add(movePieceSet);
            }
        }

        return pieceSetList;
    }

    private int checkBalance(DraughtsPieceSet pieceSet) {
        int balance = 0;

        for (DraughtsPiece piece : pieceSet.getPieces()) {
            if (piece.getSide().equals(sideAI)) {
                balance += piece.getPower();
            } else {
                balance -= piece.getPower();
            }
        }

        return balance - 5 + random.nextInt(10);
    }

    private void showMoveAI(DraughtsPieceSet bestMovePieces) {
        int numberOfChangedPieces = 0;
        pieceSet.applyPiecesToGamefield();

        for (DraughtsPiece pieceOfPresentSet : pieceSet.getPieces()) {
            if (isPieceChanged(pieceOfPresentSet, bestMovePieces)) {
                numberOfChangedPieces++;

                if (pieceOfPresentSet.getSide().equals(sideAI)) {
                    gamefield.getCell(pieceOfPresentSet.getPosition()).updateCellState(CellState.SELECTED);
                } else {
                    gamefield.getCell(pieceOfPresentSet.getPosition()).updateCellState(CellState.ATTACKED);
                }
            }
        }

        model.setChanged(true);
        delay(numberOfChangedPieces * DELAY_AI);
    }

    private boolean isPieceChanged(DraughtsPiece pieceOfPresentSet, DraughtsPieceSet bestMovePieces) {
        boolean changed = true;

        for (DraughtsPiece pieceOfBestSet : bestMovePieces.getPieces()) {
            if (pieceOfBestSet.getPosition().equals(pieceOfPresentSet.getPosition())) {
                changed = false;
            }
        }

        return changed;
    }

}

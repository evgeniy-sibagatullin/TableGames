package org.javatablegames.games.chess;

import org.javatablegames.core.enums.CellState;
import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.Model;
import org.javatablegames.core.model.game.gamefield.ModelCell;
import org.javatablegames.games.chess.piece.ChessPiece;
import org.javatablegames.games.chess.piece.ChessPieceSet;
import org.javatablegames.games.chess.piece.Pawn;

import java.util.ArrayList;
import java.util.List;

import static org.javatablegames.core.enums.Side.oppositeSide;

public class ChessVsAi extends AbstractChess {

    private static final int MAX_DEPTH = 4;
    private final Side sideAI;
    private Side activeSide;
    private int depth = 0;

    public ChessVsAi(Model model) {
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

            delay(DELAY_PERIOD);
        }
    }

    private void updateGameFieldForAI() {
        gamefield.setTotalCellStateDefault();
        model.setChanged(true);
        delay(DELAY_PERIOD);
    }

    private synchronized void performMoveAI() {
        activeSide = sidePlayer;
        List<ChessPieceSet> nextPieceSetList = getNextPieceSets(pieceSet);

        if (!nextPieceSetList.isEmpty()) {
            ChessPieceSet bestMovePieces = null;
            int bestMoveBalance = Integer.MIN_VALUE;

            System.out.println("Total moves - " + nextPieceSetList.size());
            int pieceSetNumber = 0;

            for (ChessPieceSet nextPieceSet : nextPieceSetList) {
                pieceSetNumber++;
                nextPieceSet.applyPiecesToGamefield();

                int nextMoveBalance = checkNextMoveQuality(nextPieceSet);
                System.out.println(pieceSetNumber + "move of " + nextPieceSetList.size() + " checked");

                if (nextMoveBalance > bestMoveBalance) {
                    bestMoveBalance = nextMoveBalance;
                    bestMovePieces = new ChessPieceSet(nextPieceSet);

                    if (bestMoveBalance == Integer.MAX_VALUE) {
                        break;
                    }
                }
            }
            showMoveAI(bestMovePieces);

            pieceSet = new ChessPieceSet(bestMovePieces);
        } else if (pieceSet.isKingUnderAttack(sideAI)) {
            checkWinConditionsResult = "Congratulations! You have won this game!";
        } else {
            checkWinConditionsResult = "Stalemate. Draw.";
        }
    }

    private int checkNextMoveQuality(ChessPieceSet pieceSet) {
        depth++;
        int balance = checkBalance(pieceSet);

        if (depth == MAX_DEPTH) {
            depth--;
            return balance;
        }

        List<ChessPieceSet> nextPieceSetList = getNextPieceSets(pieceSet);
        int bestMoveBalance = (activeSide.equals(sideAI)) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        if (!nextPieceSetList.isEmpty()) {
            for (ChessPieceSet nextPieceSet : nextPieceSetList) {
                nextPieceSet.applyPiecesToGamefield();

                int nextMoveBalance = checkNextMoveQuality(nextPieceSet) + balance;

                if ((activeSide.equals(sideAI) && nextMoveBalance > bestMoveBalance)
                        || ((activeSide.equals(sidePlayer) && nextMoveBalance < bestMoveBalance))) {
                    bestMoveBalance = nextMoveBalance;
                }
            }
        } else {
            if (pieceSet.isKingUnderAttack(activeSide)) {
                bestMoveBalance = (activeSide.equals(sidePlayer)) ?
                        Integer.MAX_VALUE / depth :
                        Integer.MIN_VALUE / depth;
            } else {
                bestMoveBalance = 0;
            }

        }

        depth--;
        activeSide = oppositeSide(activeSide);

        return bestMoveBalance;

    }

    private List<ChessPieceSet> getNextPieceSets(ChessPieceSet pieceSet) {
        List<ChessPieceSet> nextPieceSetList = new ArrayList<ChessPieceSet>();
        activeSide = oppositeSide(activeSide);

        List<ChessPiece> ableToMoveList = pieceSet.getPiecesAbleToMove(activeSide);
        if (!ableToMoveList.isEmpty()) {
            nextPieceSetList = getMovePieceSets(pieceSet, ableToMoveList);
        }

        return nextPieceSetList;
    }

    private List<ChessPieceSet> getMovePieceSets(ChessPieceSet pieceSet, List<ChessPiece> ableToMoveList) {
        List<ChessPieceSet> pieceSetList = new ArrayList<ChessPieceSet>();

        for (ChessPiece piece : ableToMoveList) {
            for (ModelCell modelCell : piece.getCellsAllowedToMoveIn()) {
                ChessPieceSet movePieceSet = new ChessPieceSet(pieceSet);
                gamefield.setSelectedCellByPosition(piece.getPosition());

                if (gamefield.isCellOpponent(modelCell.getPosition(), activeSide)) {
                    gamefield.captureToCell(modelCell);
                } else {
                    if (piece instanceof Pawn && ((Pawn) piece).getElPassantCells().contains(modelCell)) {
                        gamefield.captureToCell(modelCell);
                    } else {
                        gamefield.moveToCell(modelCell);
                    }
                }

                pieceSetList.add(movePieceSet);
            }
        }

        return pieceSetList;
    }

    private int checkBalance(ChessPieceSet pieceSet) {
        int balance = 0;

        for (ChessPiece piece : pieceSet.getPieces()) {
            int power = piece.getPower();

            if (piece.getSide().equals(sideAI)) {
                balance += power;
            } else {
                balance -= power;
            }

            if (piece instanceof Pawn) {
                balance += getPawnBalanceBonus(piece);
            }
        }

        return balance;
    }

    private int getPawnBalanceBonus(ChessPiece piece) {
        int pawnBonus;

        if (piece.getSide().equals(Side.BLACK)) {
            pawnBonus = 7 - piece.getPosition().getRow();
        } else {
            pawnBonus = piece.getPosition().getRow();
        }

        return (piece.getSide().equals(sideAI)) ? pawnBonus : -pawnBonus;
    }

    private void showMoveAI(ChessPieceSet bestMovePieces) {
        int numberOfChangedPieces = 0;
        pieceSet.applyPiecesToGamefield();

        for (ChessPiece pieceOfPresentSet : pieceSet.getPieces()) {
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
        delay(numberOfChangedPieces * DELAY_PERIOD);
    }

    private boolean isPieceChanged(ChessPiece pieceOfPresentSet, ChessPieceSet bestMovePieces) {
        boolean changed = true;

        for (ChessPiece pieceOfBestSet : bestMovePieces.getPieces()) {
            if (pieceOfBestSet.getPosition().equals(pieceOfPresentSet.getPosition())) {
                changed = false;
            }
        }

        return changed;
    }

}

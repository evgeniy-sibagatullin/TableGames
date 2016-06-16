package org.javatablegames.games.chess;

import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.Model;
import org.javatablegames.games.chess.piece.SuperChessPieceSet;

public class SuperChessDuel extends ChessDuel {

    public SuperChessDuel() {
        pieceSet = new SuperChessPieceSet(gamefield);
    }

    @Override
    protected void giveMoveToPlayer() {
        if (hasPlayerAnyMove()) {
            gamefield.updatePiecesReadyToMove(ableToMoveList);
        } else {
            if (sidePlayer.equals(Side.WHITE)) {
                checkWinConditionsResult = "Congratulations to winner - " + Side.BLACK + " player!";
            } else if (pieceSet.isKingUnderAttack(Side.BLACK)) {
                checkWinConditionsResult = "Congratulations to winner - " + Side.WHITE + " player!";
            } else {
                checkWinConditionsResult = "Stalemate. Draw.";
            }
        }

        Model.INSTANCE.setChanged(true);
        isPlayerMove = true;
    }
}

package org.javatablegames.games.draughts;

import org.javatablegames.core.enums.Side;
import org.javatablegames.games.draughts.piece.DraughtsPieceSet;

import static org.javatablegames.core.enums.Side.oppositeSide;

public class DraughtsDuel extends AbstractDraughts {

    public DraughtsDuel() {
        sidePlayer = Side.WHITE;
        isPlayerMove = true;
    }

    @Override
    public void run() {
        giveMoveToPlayer();

        while (isThreadNeeded) {
            if (!isPlayerMove) {
                sidePlayer = oppositeSide(sidePlayer);
                giveMoveToPlayer();
            }
            delay(50);
        }
    }

    @Override
    public void undoMove() {
        if (moveHistory.containsKey(moveIndex - 2)) {
            moveHistory.put(moveIndex, new DraughtsPieceSet(pieceSet));
            moveIndex -= 2;
            pieceSet = new DraughtsPieceSet(moveHistory.get(moveIndex));

            isPlayerMove = false;
        }
    }

    @Override
    public void redoMove() {
        if (moveHistory.containsKey(moveIndex + 1)) {
            pieceSet = new DraughtsPieceSet(moveHistory.get(moveIndex + 1));

            isPlayerMove = false;
        }
    }

}

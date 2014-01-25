package org.javatablegames.games.chess;

import org.javatablegames.core.enums.Side;
import org.javatablegames.core.model.Model;
import org.javatablegames.games.chess.piece.ChessPieceSet;

import static org.javatablegames.core.enums.Side.oppositeSide;

public class ChessDuel extends AbstractChess {

    public ChessDuel(Model model) {
        super(model);
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
            delay(DELAY_PERIOD);
        }
    }

    @Override
    public void undoMove() {
        if (moveHistory.containsKey(moveIndex - 1)) {
            moveHistory.put(moveIndex, new ChessPieceSet(pieceSet));
            pieceSet = new ChessPieceSet(moveHistory.get(--moveIndex));

            isPlayerMove = false;
        }
    }

    @Override
    public void redoMove() {
        if (moveHistory.containsKey(moveIndex + 1)) {
            pieceSet = new ChessPieceSet(moveHistory.get(++moveIndex));

            isPlayerMove = false;
        }
    }
}


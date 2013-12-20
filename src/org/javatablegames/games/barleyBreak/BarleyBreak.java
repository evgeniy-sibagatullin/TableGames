package org.javatablegames.games.barleyBreak;

import org.javatablegames.core.enums.CellState;
import org.javatablegames.core.model.Model;
import org.javatablegames.core.model.game.Game;
import org.javatablegames.core.model.game.piece.PieceSet;
import org.javatablegames.core.model.position.Position;
import org.javatablegames.games.barleyBreak.gamefield.BarleyBreakField;

public class BarleyBreak extends Game<BarleyBreakField, PieceSet> {

    private static final int PREPARATION_RANDOM_MOVES_NEEDED = 20;

    private boolean isFieldPrepared = false;
    private int randomMovesCount = 0;

    public BarleyBreak(Model model) {
        super(model);
        gamefield = new BarleyBreakField();
    }

    @Override
    public void clickCell(Position position) {
        if (gamefield.getCell(position).getCellState() == CellState.ALLOWED_PIECE) {
            gamefield.moveEmptyCellToPosition(position);
            model.setChanged(true);
        }
    }

    @Override
    public void run() {
        while (isThreadNeeded && !isFieldPrepared) {
            delay(250);
            performRandomMove();
            if (randomMovesCount > PREPARATION_RANDOM_MOVES_NEEDED) {
                isFieldPrepared = true;
            }
        }

        while (isThreadNeeded && isFieldPrepared) {
            delay(3000);
            performRandomMove();
        }
    }

    public String checkWinConditions() {
        if (isFieldPrepared) {
            int size = gamefield.getSize();
            for (int row = 0; row < size; row++) {
                for (int column = 0; column < size; column++) {
                    int power = gamefield.getCell(new Position(row, column))
                            .getPower();
                    if (row * size + column + 1 != power
                            && (row != size - 1 || column != size - 1)) {
                        return "";
                    }
                }
            }
            return "Congratulations. You win!";
        } else {
            return "";
        }
    }

    private void performRandomMove() {
        gamefield.performRandomMove(randomMovesCount);
        randomMovesCount++;
        model.setChanged(true);
    }

    private void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

}

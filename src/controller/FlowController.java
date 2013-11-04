package controller;

public interface FlowController {
    /**
     * Performs check of clicked cell in defined coordinates
     */
    void clickCell(int row, int column);

    /**
     * Checks if win conditions satisfied
     */
    void checkWinConditions();

    /**
     * Sends signal to model, that view update complete
     */
    void viewUpdateComplete();
}

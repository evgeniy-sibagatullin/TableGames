package view;

import controller.FlowController;

public interface View {

    void initializeView();

    void setFlowController(FlowController flowController);

    void enableSelectGameMenu();

    void disableSelectGameMenu();

    void enableManageGameMenu();

    void disableManageGameMenu();

    void constructGameField();

    void updateAfterClick();

    void showWinPopup();
}
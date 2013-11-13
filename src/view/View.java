package view;

public interface View {

    void initializeView();

    void enableSelectGameMenu();

    void disableSelectGameMenu();

    void enableManageGameMenu();

    void disableManageGameMenu();

    void showMessage(String message);
}
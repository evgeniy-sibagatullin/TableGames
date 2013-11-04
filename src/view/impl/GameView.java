package view.impl;

import controller.FlowController;
import controller.MenuController;
import enums.GameType;
import model.Model;
import model.ModelCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import view.View;

public class GameView implements View {

    private static final String APPLICATION_TITLE_TEXT = "Table Games";
    private static final String SELECT_MENU_HEADER_TEXT = "Select Game";
    private static final String MANAGE_MENU_HEADER_TEXT = "Manage Game";
    private static final String CHESS_MENUITEM_TEXT = "Chess";
    private static final String CORNERS_MENUITEM_TEXT = "Corners";
    private static final String RESTART_MENUITEM_TEXT = "Restart game";
    private static final String ANOTHER_MENUITEM_TEXT = "Choose another game";
    private static final int BOARD_SHELL_SIZE = 900;
    private static final String MENUITEM_KEY_GAMETYPE = "gameName";

    private Model model;
    private MenuController menuController;
    private FlowController flowController;

    private Display menuDisplay;
    private int monitorCenterX;
    private int monitorCenterY;
    private int gameFieldSize;

    private Shell boardShell;
    private GridData gridData;
    private Composite gameFieldComposite;
    private MenuItem selectGameMenuHeader;
    private MenuItem manageGameMenuHeader;
    private GameType gameType;

    private ModelCell[][] modelGameField;
    private ViewCell[][] viewGameField;

    public GameView(MenuController menuController, Model model) {
        this.menuController = menuController;
        this.model = model;
    }

    public void setFlowController(FlowController flowController) {
        this.flowController = flowController;
    }

    @Override
    public void initializeView() {
        menuDisplay = new Display();
        setMonitorCenter();
        constructGridData();
        constructBoardShell();
        constructGameField();

        enableSelectGameMenu();
        disableManageGameMenu();

        deliverView();

        while (!boardShell.isDisposed()) {
            if (!menuDisplay.readAndDispatch()) {
                menuDisplay.sleep();
            }
        }
    }

	/*
     * inner magic
	 */

    private void setMonitorCenter() {
        Monitor primary = menuDisplay.getPrimaryMonitor();
        Rectangle bounds = primary.getBounds();
        monitorCenterX = bounds.width / 2;
        monitorCenterY = bounds.height / 2;
    }

    private void constructGridData() {
        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        this.gridData = gridData;
    }

    private void constructBoardShell() {
        boardShell = new Shell(menuDisplay, SWT.SHELL_TRIM & (~SWT.RESIZE));
        boardShell.setText(APPLICATION_TITLE_TEXT);
        boardShell.setMinimumSize(BOARD_SHELL_SIZE, BOARD_SHELL_SIZE);
        boardShell.setLocation(monitorCenterX - BOARD_SHELL_SIZE / 2,
                monitorCenterY - BOARD_SHELL_SIZE / 2);
        boardShell.setMenuBar(constructBarMenu());
        boardShell.setLayout(new GridLayout(1, true));
    }

    private Menu constructBarMenu() {
        Menu barMenu = new Menu(boardShell, SWT.BAR);
        constructSelectGameMenuHeader(barMenu);
        constructManageGameMenuHeader(barMenu);
        return barMenu;
    }

    private void constructSelectGameMenuHeader(Menu barMenu) {
        selectGameMenuHeader = new MenuItem(barMenu, SWT.CASCADE);
        selectGameMenuHeader.setText(SELECT_MENU_HEADER_TEXT);

        Menu selectGameMenu = new Menu(boardShell, SWT.DROP_DOWN);
        selectGameMenuHeader.setMenu(selectGameMenu);

        MenuItem chessMenuItem = new MenuItem(selectGameMenu, SWT.PUSH);
        chessMenuItem.addSelectionListener(menuItemListener);
        chessMenuItem.setText(CHESS_MENUITEM_TEXT);
        chessMenuItem.setData(MENUITEM_KEY_GAMETYPE, GameType.CHESS);

        MenuItem cornersMenuItem = new MenuItem(selectGameMenu, SWT.PUSH);
        cornersMenuItem.addSelectionListener(menuItemListener);
        cornersMenuItem.setText(CORNERS_MENUITEM_TEXT);
        cornersMenuItem.setData(MENUITEM_KEY_GAMETYPE, GameType.CORNERS);
    }

    private void constructManageGameMenuHeader(Menu barMenu) {
        manageGameMenuHeader = new MenuItem(barMenu, SWT.CASCADE);
        manageGameMenuHeader.setText(MANAGE_MENU_HEADER_TEXT);

        Menu manageGameMenu = new Menu(boardShell, SWT.DROP_DOWN);
        manageGameMenuHeader.setMenu(manageGameMenu);

        MenuItem restartMenuItem = new MenuItem(manageGameMenu, SWT.PUSH);
        restartMenuItem.addSelectionListener(menuItemListener);
        restartMenuItem.setText(RESTART_MENUITEM_TEXT);

        MenuItem anotherMenuItem = new MenuItem(manageGameMenu, SWT.PUSH);
        anotherMenuItem.addSelectionListener(menuItemListener);
        anotherMenuItem.setText(ANOTHER_MENUITEM_TEXT);
    }

    private void renewGameField() {
        if (gameFieldComposite != null) {
            gameFieldComposite.dispose();
        }
        gameFieldSize = model.getGame().getFieldSize();
        gameFieldComposite = new Composite(boardShell, SWT.NONE);
        gameFieldComposite.setLayoutData(gridData);
        gameFieldComposite.setLayout(new GridLayout(gameFieldSize, true));
        fillGameFieldByCells();
        gameFieldComposite.pack();
    }

    private void fillGameFieldByCells() {
        viewGameField = new ViewCell[gameFieldSize][gameFieldSize];
        for (int row = 0; row < gameFieldSize; row++) {
            for (int column = 0; column < gameFieldSize; column++) {
                ModelCell modelCell = modelGameField[row][column];
                viewGameField[row][column] = constructCell(modelCell, gridData);
            }
        }
    }

    private ViewCell constructCell(ModelCell modelCell, GridData gridData) {
        ViewCell viewCell = new ViewCell(gameFieldComposite, SWT.PUSH);
        viewCell.setModelCell(modelCell);
        viewCell.setLayoutData(gridData);
        viewCell.addMouseListener(cellListener);
        return viewCell;
    }

    private void updateGameField() {
        for (int row = 0; row < gameFieldSize; row++) {
            for (int column = 0; column < gameFieldSize; column++) {
                if (modelGameField[row][column].getChanged()) {
                    viewGameField[row][column].setModelCell(modelGameField[row][column]);
                    viewGameField[row][column].redraw();
                    modelGameField[row][column].setChanged(false);
                }
            }
        }
        flowController.viewUpdateComplete();
    }

    private void deliverView() {
        boardShell.layout();
        boardShell.pack();
        boardShell.open();
    }

    private final SelectionListener menuItemListener = new SelectionAdapter() {

        @Override
        public void widgetSelected(SelectionEvent e) {
            String buttonText = ((MenuItem) e.widget).getText();
            GameType gameType = (GameType) (e.widget).getData(MENUITEM_KEY_GAMETYPE);
            // manage model.game menu entries
            if (buttonText.equals(RESTART_MENUITEM_TEXT)) {
                menuController.restartGame();
            } else if (buttonText.equals(ANOTHER_MENUITEM_TEXT)) {
                menuController.reselectGame();
                // select model.game menu entries
            } else {
                menuController.startGame(gameType);
            }
        }

    };

    private final MouseListener cellListener = new MouseAdapter() {

        @Override
        public void mouseUp(MouseEvent e) {
            ViewCell cell = (ViewCell) e.widget;
            flowController.clickCell(cell.getModelCell().getRow(), cell.getModelCell().getColumn());
        }

    };

	/*
     * logic
	 */

    @Override
    public void enableSelectGameMenu() {
        selectGameMenuHeader.setEnabled(true);
    }

    @Override
    public void disableSelectGameMenu() {
        selectGameMenuHeader.setEnabled(false);
    }

    @Override
    public void enableManageGameMenu() {
        manageGameMenuHeader.setEnabled(true);
    }

    @Override
    public void disableManageGameMenu() {
        manageGameMenuHeader.setEnabled(false);
    }

    public void constructGameField() {
        if (model.getGame().getGameType() != gameType) {
            modelGameField = model.getGame().getGameField();
            renewGameField();
            gameType = model.getGame().getGameType();
            deliverView();
        }
    }

    public void updateAfterClick() {
        modelGameField = model.getGame().getGameField();
        updateGameField();
        deliverView();
    }

    @Override
    public void showWinPopup() {
        MessageBox messageBox = new MessageBox(boardShell, SWT.ICON_INFORMATION);
        messageBox.setMessage("Congratulations. You win!");
        int rc = messageBox.open();

        switch (rc) {
            case SWT.OK:
                break;
        }
    }
}

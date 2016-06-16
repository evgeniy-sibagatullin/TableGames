package org.javatablegames.core.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.javatablegames.core.controller.Controller;
import org.javatablegames.core.model.Model;
import org.javatablegames.core.model.game.gamefield.Gamefield;
import org.javatablegames.core.model.game.gamefield.ModelCell;
import org.javatablegames.core.model.position.Position;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class GameView implements View {

    private static final int BOARD_SHELL_SIZE = 900;
    private static final String APPLICATION_TITLE_TEXT = "Table Games";
    private static final String SELECT_MENU_HEADER_TEXT = "Select Game";
    private static final String MANAGE_MENU_HEADER_TEXT = "Manage Game";
    private static final String UNDO_MOVE_MENUITEM_TEXT = "Undo Move";
    private static final String REDO_MOVE_MENUITEM_TEXT = "Redo Move";
    private static final String RESTART_MENUITEM_TEXT = "Restart Game";
    private static final String ANOTHER_MENUITEM_TEXT = "Choose Another Game";
    private static final String MENUITEM_KEY_GAMETYPE = "gameName";
    private final Controller controller;
    private final Display display;
    private final ShellListener shellListener = new ShellAdapter() {

        @Override
        public void shellClosed(ShellEvent event) {
            int style = SWT.YES | SWT.NO;
            MessageBox messageBox = new MessageBox(boardShell, style);
            messageBox.setText("Confirm exit");
            messageBox.setMessage("Are you sure you want to exit \"Table Games\"?");
            event.doit = messageBox.open() == SWT.YES;
        }

    };
    private final SelectionListener menuItemListener = new SelectionAdapter() {

        @Override
        public void widgetSelected(SelectionEvent e) {
            String buttonText = ((MenuItem) e.widget).getText();
            String gameClass = (String) (e.widget).getData(MENUITEM_KEY_GAMETYPE);

            switch (buttonText) {
                case UNDO_MOVE_MENUITEM_TEXT:
                    controller.undoMove();
                    break;
                case REDO_MOVE_MENUITEM_TEXT:
                    controller.redoMove();
                    break;
                case RESTART_MENUITEM_TEXT:
                    controller.restartGame();
                    break;
                case ANOTHER_MENUITEM_TEXT:
                    controller.startDefaultGame();
                    break;
                default:
                    controller.startGame(gameClass);
                    break;
            }
        }

    };
    private final MouseListener cellListener = new MouseAdapter() {

        @Override
        public void mouseUp(MouseEvent e) {
            ViewCell cell = (ViewCell) e.widget;
            controller.clickCell(cell.getPosition());
        }

    };
    private int monitorCenterX;
    private int monitorCenterY;
    private int gameFieldSize;
    private Shell boardShell;
    private GridData gridData;
    private Composite gameFieldComposite;
    private MenuItem selectGameMenuHeader;
    private MenuItem manageGameMenuHeader;
    private String gameClassName;
    private Gamefield modelGameField;
    private ViewCell[][] viewGameField;

    public GameView(Controller menuController) {
        this.controller = menuController;
        display = Display.getDefault();

        setMonitorCenter();
        constructGridData();
        constructBoardShell();
    }

    @Override
    public void initializeView() {
        enableSelectGameMenu();
        disableManageGameMenu();

        while (!boardShell.isDisposed()) {
            updateView();
            display.readAndDispatch();
        }
    }

    private void updateView() {
        modelGameField = Model.INSTANCE.getGame().getGameField();
        if (!Model.INSTANCE.getGame().getGameClassName().equals(gameClassName)) {
            disposeAndCreateNewGameField();
            gameClassName = Model.INSTANCE.getGame().getGameClassName();
        }
        if (Model.INSTANCE.isChanged()) {
            redrawChangedCellsOnGameField();
            deliverView();
            Model.INSTANCE.setChanged(false);
            controller.checkWinConditions();
        }
    }

    private void deliverView() {
        boardShell.layout();
        boardShell.pack();
        boardShell.open();
    }

    private void setMonitorCenter() {
        Rectangle bounds = display.getPrimaryMonitor().getBounds();
        monitorCenterX = bounds.width / 2;
        monitorCenterY = bounds.height / 2;
    }

    private void constructGridData() {
        this.gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
    }

    private void constructBoardShell() {
        boardShell = new Shell(display, SWT.TITLE | SWT.MIN);
        boardShell.setText(APPLICATION_TITLE_TEXT);
        boardShell.setMinimumSize(BOARD_SHELL_SIZE, BOARD_SHELL_SIZE);
        boardShell.setLocation(monitorCenterX - BOARD_SHELL_SIZE / 2,
                monitorCenterY - BOARD_SHELL_SIZE / 2);
        boardShell.setMenuBar(constructBarMenu());
        boardShell.setLayout(new GridLayout(1, true));
        boardShell.addShellListener(shellListener);
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

        Map<String, String> gamesTitlesClasses = getGamesTitlesClasses();
        for (String gameTitle : gamesTitlesClasses.keySet()) {
            constructSelectGameMenuItem(selectGameMenu, gameTitle.substring(2), gamesTitlesClasses.get(gameTitle));
        }
    }

    private Map<String, String> getGamesTitlesClasses() {
        Map<String, String> gamesTitlesClasses = new TreeMap<>();

        try {
            Properties properties = readProperties();
            for (Object key : properties.keySet()) {
                String gameTitle = (String) key;
                gamesTitlesClasses.put(gameTitle, properties.getProperty(gameTitle));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gamesTitlesClasses;
    }

    private Properties readProperties() throws IOException {
        Properties properties = new Properties();
        properties.loadFromXML(this.getClass().getClassLoader().getResourceAsStream("org/javatablegames/core/tablegames.xml"));
        return properties;
    }

    private void constructSelectGameMenuItem(Menu selectGameMenu, String menuItemText, String gameType) {
        MenuItem menuItem = new MenuItem(selectGameMenu, SWT.PUSH);
        menuItem.addSelectionListener(menuItemListener);
        menuItem.setText(menuItemText);
        menuItem.setData(MENUITEM_KEY_GAMETYPE, gameType);
    }

    private void constructManageGameMenuHeader(Menu barMenu) {
        manageGameMenuHeader = new MenuItem(barMenu, SWT.CASCADE);
        manageGameMenuHeader.setText(MANAGE_MENU_HEADER_TEXT);

        Menu manageGameMenu = new Menu(boardShell, SWT.DROP_DOWN);
        manageGameMenuHeader.setMenu(manageGameMenu);

        constructManageGameMenuItem(manageGameMenu, UNDO_MOVE_MENUITEM_TEXT);
        constructManageGameMenuItem(manageGameMenu, REDO_MOVE_MENUITEM_TEXT);
        constructManageGameMenuItem(manageGameMenu, RESTART_MENUITEM_TEXT);
        constructManageGameMenuItem(manageGameMenu, ANOTHER_MENUITEM_TEXT);
    }

    private void constructManageGameMenuItem(Menu manageGameMenu, String menuItemText) {
        MenuItem menuItem = new MenuItem(manageGameMenu, SWT.PUSH);
        menuItem.addSelectionListener(menuItemListener);
        menuItem.setText(menuItemText);
    }

    private void disposeAndCreateNewGameField() {
        if (gameFieldComposite != null) {
            gameFieldComposite.dispose();
        }
        gameFieldSize = Model.INSTANCE.getGame().getFieldSize();
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
                ModelCell modelCell = modelGameField.getCell(new Position(row, column));
                viewGameField[row][column] = constructCell(modelCell, gridData);
            }
        }
    }

    private ViewCell constructCell(ModelCell modelCell, GridData gridData) {
        ViewCell viewCell = new ViewCell(gameFieldComposite, display, modelCell);
        viewCell.setLayoutData(gridData);
        viewCell.addMouseListener(cellListener);
        return viewCell;
    }

    private void redrawChangedCellsOnGameField() {
        for (int row = 0; row < gameFieldSize; row++) {
            for (int column = 0; column < gameFieldSize; column++) {
                ModelCell modelCell = modelGameField.getCell(new Position(row, column));
                if (modelCell.getChanged()) {
                    ViewCell viewCell = viewGameField[row][column];
                    viewCell.updateViewCellData(modelCell);
                    viewCell.redraw();
                    modelCell.setChanged(false);
                }
            }
        }
    }

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

    @Override
    public void showMessage(String message) {
        MessageBox messageBox = new MessageBox(boardShell, SWT.ICON_INFORMATION);
        messageBox.setMessage(message);
        int rc = messageBox.open();

        switch (rc) {
            case SWT.OK:
                break;
        }
    }

}

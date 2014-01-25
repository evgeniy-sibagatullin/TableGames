package org.javatablegames.core.enums;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public enum CellState {

    FOCUSED(SWT.LINE_DOT, 8, new Color(Display.getCurrent(), 50, 50, 250)),

    DEFAULT(SWT.LINE_SOLID, 2, new Color(Display.getCurrent(), 200, 200, 200)),

    ALLOWED_MOVE(SWT.LINE_DOT, 4, new Color(Display.getCurrent(), 50, 250, 50)),

    ALLOWED_PIECE(SWT.LINE_DASH, 4, new Color(Display.getCurrent(), 50, 250, 250)),

    ATTACKED(SWT.LINE_DOT, 4, new Color(Display.getCurrent(), 250, 50, 50)),

    SELECTED(SWT.LINE_DASH, 4, new Color(Display.getCurrent(), 250, 250, 50));

    private final int borderStyle;
    private final int borderWidth;
    private final Color borderColor;

    private CellState(int borderStyle, int borderWidth, Color borderColor) {
        this.borderStyle = borderStyle;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
    }

    public int getBorderStyle() {
        return this.borderStyle;
    }

    public int getBorderWidth() {
        return this.borderWidth;
    }

    public Color getBorderColor() {
        return this.borderColor;
    }

}

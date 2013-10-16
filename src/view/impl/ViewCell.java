package view.impl;

import enums.CellState;
import model.impl.ModelCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class ViewCell extends Canvas {

    private static final int BORDER_PADDING = 5;

    private static final int BORDER_STYLE_DEFAULT = SWT.LINE_SOLID;
    private static final int BORDER_WIDTH_DEFAULT = 5;

    private static final int BORDER_STYLE_FOCUSED = SWT.LINE_DOT;
    private static final int BORDER_WIDTH_FOCUSED = 10;

    private static final int BORDER_STYLE_CHOOSE = SWT.LINE_DASH;
    private static final int BORDER_WIDTH_CHOOSE = 5;

    private static final Font POWER_DEFAULT_FONT = new Font(Display.getCurrent(), "Georgia", 20, SWT.BOLD);

    private static final int PIECE_IMAGE_PADDING = (BORDER_PADDING + BORDER_WIDTH_DEFAULT) * 2;

    private static final Color COLOR_DEFAULT = new Color(Display.getCurrent(),
            50, 250, 50);
    private static final Color COLOR_FOCUSED = new Color(Display.getCurrent(),
            50, 50, 250);
    private static final Color COLOR_CHOOSE = new Color(Display.getCurrent(),
            250, 50, 50);

    private ModelCell modelCell;
    private int borderStyle = BORDER_STYLE_DEFAULT;
    private int borderWidth = BORDER_WIDTH_DEFAULT;
    private Color borderColor = COLOR_DEFAULT;
    private CellState focusedCellState = CellState.DEFAULT;

    public ViewCell(Composite parent, int style) {
        super(parent, style);
        addListeners();
    }

    public ModelCell getModelCell() {
        return modelCell;
    }

    public void setModelCell(ModelCell modelCell) {
        this.modelCell = modelCell;
    }

    public void addListeners() {
        addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                ViewCell.this.paintControl(e);
            }
        });

        addListener(SWT.MouseEnter, new Listener() {
            @Override
            public void handleEvent(Event event) {
                focusedCellState = modelCell.getCellState();
                modelCell.setCellState(CellState.FOCUSED);
                redraw();
            }
        });

        addListener(SWT.MouseExit, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if (modelCell.getCellState() != CellState.CHOOSE) {
                    modelCell.setCellState(focusedCellState);
                    redraw();
                }
            }
        });
    }

    protected void paintControl(PaintEvent e) {
        GC gc = e.gc;
        Rectangle rectangle = getClientArea();
        drawBackgroundImage(gc, rectangle);
        drawPieceImage(gc, rectangle);
        drawBorder(gc, rectangle);
        drawPowerText(gc);
    }

    private void drawBackgroundImage(GC gc, Rectangle rectangle) {
        if (getModelCell().getBackgroundImage() == null) {
            return;
        }
        // image stretched
        Image backgroundImage = new Image(Display.getCurrent(), getModelCell()
                .getBackgroundImage());
        Rectangle imgBounds = backgroundImage.getBounds();
        gc.drawImage(backgroundImage, 0, 0, imgBounds.width, imgBounds.height,
                rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    private void drawPieceImage(GC gc, Rectangle rectangle) {
        if (getModelCell().getPieceImage() == null) {
            return;
        }
        // image centered with padding
        Image pieceImage = new Image(Display.getCurrent(), getModelCell()
                .getPieceImage());
        Rectangle imgBounds = pieceImage.getBounds();
        gc.drawImage(pieceImage, 0, 0, imgBounds.width, imgBounds.height,
                rectangle.x + PIECE_IMAGE_PADDING, rectangle.y
                + PIECE_IMAGE_PADDING, rectangle.width
                - PIECE_IMAGE_PADDING * 2, rectangle.height
                - PIECE_IMAGE_PADDING * 2);
    }

    private void drawBorder(GC gc, Rectangle rectangle) {
        if (modelCell != null && modelCell.getCellState() != null) {
            if (modelCell.getCellState() == CellState.DEFAULT) {
                setBorderSettings(BORDER_STYLE_DEFAULT, BORDER_WIDTH_DEFAULT, COLOR_DEFAULT);
            } else if (modelCell.getCellState() == CellState.CHOOSE) {
                setBorderSettings(BORDER_STYLE_CHOOSE, BORDER_WIDTH_CHOOSE, COLOR_CHOOSE);
            } else if (modelCell.getCellState() == CellState.FOCUSED) {
                setBorderSettings(BORDER_STYLE_FOCUSED, BORDER_WIDTH_FOCUSED, COLOR_FOCUSED);
            }
        }

        gc.setLineStyle(borderStyle);
        gc.setLineWidth(borderWidth);
        gc.setForeground(borderColor);
        if (borderWidth > 0) {
            Point point = this.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
            int arcLength = Math.max(5, (point.y / 10));
            // border centered with padding
            gc.drawRoundRectangle(rectangle.x + BORDER_PADDING + borderWidth
                    / 2, rectangle.y + BORDER_PADDING + borderWidth / 2,
                    rectangle.width - BORDER_PADDING * 2 - borderWidth,
                    rectangle.height - BORDER_PADDING * 2 - borderWidth,
                    arcLength, arcLength);
        }
    }

    private void drawPowerText(GC gc) {
        if (modelCell != null && modelCell.getPower() > 0) {
            gc.setFont(POWER_DEFAULT_FONT);
            gc.setForeground(new Color(Display.getCurrent(), 250, 250, 250));
            gc.drawText(Integer.toString(modelCell.getPower()), 20, 15, SWT.DRAW_TRANSPARENT);
        }
    }

    private void setBorderSettings(int borderStyle, int borderWidth, Color borderColor) {
        this.borderStyle = borderStyle;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
    }

}

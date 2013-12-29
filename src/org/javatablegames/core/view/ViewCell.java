package org.javatablegames.core.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.javatablegames.core.enums.CellState;
import org.javatablegames.core.model.game.gamefield.ModelCell;
import org.javatablegames.core.model.game.piece.Piece;

public class ViewCell extends Canvas {

    private final Display display;
    private static final int BORDER_PADDING = 5;
    private static final int POWER_DEFAULT_FONT_SIZE = 20;
    private static final int PIECE_IMAGE_PADDING = (BORDER_PADDING + 5) * 2;
    private ModelCell modelCell;
    private boolean isMouseEnterEvent;

    public ViewCell(Composite parent, Display display, ModelCell modelCell) {
        super(parent, SWT.PUSH);

        this.display = display;
        this.modelCell = modelCell;

        addListeners();
    }

    public ModelCell getModelCell() {
        return modelCell;
    }

    public void setModelCell(ModelCell modelCell) {
        this.modelCell = modelCell;
    }

    private void addListeners() {
        addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                ViewCell.this.paintControl(e);
            }
        });

        addListener(SWT.MouseEnter, new Listener() {
            @Override
            public void handleEvent(Event event) {
                isMouseEnterEvent = true;
                redraw();
            }
        });

        addListener(SWT.MouseExit, new Listener() {
            @Override
            public void handleEvent(Event event) {
                redraw();
            }
        });
    }

    private void paintControl(PaintEvent e) {
        GC gc = e.gc;
        Rectangle clientArea = getClientArea();

        drawBackgroundImage(gc, clientArea);
        drawPieceImage(gc, clientArea);
        drawBorder(gc, clientArea);
        drawPowerText(gc, clientArea);
    }

    private void drawBackgroundImage(GC gc, Rectangle rectangle) {
        String imageName = modelCell.getBackgroundImage();

        if (imageName != null) {
            Image image = new Image(display, imageName);
            // image stretched
            Rectangle bounds = image.getBounds();
            gc.drawImage(image, 0, 0, bounds.width, bounds.height,
                    rectangle.x, rectangle.y, rectangle.width, rectangle.height);

            image.dispose();
        }
    }

    private void drawPieceImage(GC gc, Rectangle rectangle) {
        Piece piece = modelCell.getPiece();

        if (piece != null) {
            // image centered with padding
            Image image = new Image(display, piece.getImagePath());
            Rectangle bounds = image.getBounds();
            gc.drawImage(image, 0, 0, bounds.width, bounds.height,
                    rectangle.x + PIECE_IMAGE_PADDING, rectangle.y
                    + PIECE_IMAGE_PADDING, rectangle.width
                    - PIECE_IMAGE_PADDING * 2, rectangle.height
                    - PIECE_IMAGE_PADDING * 2);

            image.dispose();
        }
    }

    private void drawBorder(GC gc, Rectangle rectangle) {
        CellState cellState;

        if (isMouseEnterEvent) {
            cellState = CellState.FOCUSED;
            isMouseEnterEvent = false;
        } else {
            cellState = modelCell.getCellState();
        }

        int style = cellState.getBorderStyle();
        int width = cellState.getBorderWidth();
        Color color = new Color(display, cellState.getBorderColor().getRGB());
        int arcLength = Math.max(5, (this.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).y / 10));

        gc.setLineStyle(style);
        gc.setLineWidth(width);
        gc.setForeground(color);
        gc.drawRoundRectangle(rectangle.x + BORDER_PADDING + width
                / 2, rectangle.y + BORDER_PADDING + width / 2,
                rectangle.width - BORDER_PADDING * 2 - width,
                rectangle.height - BORDER_PADDING * 2 - width,
                arcLength, arcLength);

        color.dispose();
    }

    private void drawPowerText(GC gc, Rectangle rectangle) {
        if (modelCell.getPower() != 0) {
            Font font = new Font(display, "Georgia", POWER_DEFAULT_FONT_SIZE, SWT.BOLD);
            Color color = new Color(display, 0, 250, 0);

            gc.setFont(font);
            gc.setForeground(color);
            gc.drawText(Integer.toString(modelCell.getPower()), (rectangle.width - POWER_DEFAULT_FONT_SIZE) / 2,
                    (rectangle.height - POWER_DEFAULT_FONT_SIZE) / 2, SWT.DRAW_TRANSPARENT);

            font.dispose();
            color.dispose();
        }
    }

}

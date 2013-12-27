package org.javatablegames.core.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.javatablegames.core.enums.CellState;
import org.javatablegames.core.model.game.gamefield.ModelCell;
import org.javatablegames.core.model.game.piece.Piece;

public class ViewCell extends Canvas {

    private static final Display DISPLAY = Display.getCurrent();
    private static final int BORDER_PADDING = 5;
    private static final int POWER_DEFAULT_FONT_SIZE = 20;
    private static final int PIECE_IMAGE_PADDING = (BORDER_PADDING + 5) * 2;
    private static final Font POWER_DEFAULT_FONT = new Font(DISPLAY, "Georgia", POWER_DEFAULT_FONT_SIZE, SWT.BOLD);
    private static final Color POWER_FONT_COLOR = new Color(DISPLAY, 0, 250, 0);
    private ModelCell modelCell;

    public ViewCell(Composite parent) {
        super(parent, SWT.PUSH);
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
    }

    private void paintControl(PaintEvent e) {
        GC gc = e.gc;
        Rectangle rectangle = getClientArea();
        drawBackgroundImage(gc, rectangle);
        drawPieceImage(gc, rectangle);
        drawBorder(gc, rectangle);
        drawPowerText(gc, rectangle);
    }

    private void drawBackgroundImage(GC gc, Rectangle rectangle) {
        if (getModelCell().getBackgroundImage() == null) {
            return;
        }
        // image stretched
        Image backgroundImage = new Image(DISPLAY, getModelCell()
                .getBackgroundImage());
        Rectangle imgBounds = backgroundImage.getBounds();

        gc.drawImage(backgroundImage, 0, 0, imgBounds.width, imgBounds.height,
                rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    private void drawPieceImage(GC gc, Rectangle rectangle) {
        Piece piece = getModelCell().getPiece();
        if (piece == null) {
            return;
        }
        // image centered with padding
        Image pieceImage = new Image(DISPLAY, piece.getImagePath());
        Rectangle imgBounds = pieceImage.getBounds();
        gc.drawImage(pieceImage, 0, 0, imgBounds.width, imgBounds.height,
                rectangle.x + PIECE_IMAGE_PADDING, rectangle.y
                + PIECE_IMAGE_PADDING, rectangle.width
                - PIECE_IMAGE_PADDING * 2, rectangle.height
                - PIECE_IMAGE_PADDING * 2);
    }

    private void drawBorder(GC gc, Rectangle rectangle) {
        CellState cellState = modelCell.getCellState();
        int borderStyle = cellState.getBorderStyle();
        int borderWidth = cellState.getBorderWidth();
        Color borderColor = cellState.getBorderColor();

        gc.setLineStyle(borderStyle);
        gc.setLineWidth(borderWidth);
        gc.setForeground(borderColor);

        if (borderWidth > 0) {
            Point point = this.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
            int arcLength = Math.max(5, (point.y / 10));
            gc.drawRoundRectangle(rectangle.x + BORDER_PADDING + borderWidth
                    / 2, rectangle.y + BORDER_PADDING + borderWidth / 2,
                    rectangle.width - BORDER_PADDING * 2 - borderWidth,
                    rectangle.height - BORDER_PADDING * 2 - borderWidth,
                    arcLength, arcLength);
        }
    }

    private void drawPowerText(GC gc, Rectangle rectangle) {
        if (modelCell != null && modelCell.getPower() != 0) {
            gc.setFont(POWER_DEFAULT_FONT);
            gc.setForeground(POWER_FONT_COLOR);
            gc.drawText(Integer.toString(modelCell.getPower()), (rectangle.width - POWER_DEFAULT_FONT_SIZE) / 2,
                    (rectangle.height - POWER_DEFAULT_FONT_SIZE) / 2, SWT.DRAW_TRANSPARENT);
        }
    }

}

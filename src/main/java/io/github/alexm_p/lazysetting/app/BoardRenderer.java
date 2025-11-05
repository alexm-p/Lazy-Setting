package io.github.alexm_p.lazysetting.app;

import io.github.alexm_p.lazysetting.model.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;

public class BoardRenderer {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private double scale = 1.0;
    private double manualZoom = 1.0;
    private double panX = 0;
    private double panY = 0;
    private double boardWidth = 0;
    private double boardHeight = 0;

    private static final double HOLD_SIZE = 30;
    private static final Color BACKGROUND_COLOR = Color.web("#2a2a2a");
    private static final Color GRID_COLOR = Color.web("#3a3a3a");
    private static final Color ARROW_COLOR = Color.CYAN;
    private static final Color STROKE_COLOR = Color.WHITE;
    private static final Color LABEL_COLOR = Color.WHITE;

    public BoardRenderer(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public void setManualZoom(double zoom) {
        this.manualZoom = zoom;
    }

    public double getScale() {
        return scale;
    }

    public void clear() {
        gc.setFill(BACKGROUND_COLOR);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void render(Board board, boolean showGrid, boolean showLabels, double translateX, double translateY) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.save();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.translate(translateX, translateY);
        gc.scale(scale, scale);

        clear();

        // Calculate board bounds
        calculateBoardBounds(board);

        // Auto-fit board to canvas
        //autoFitBoard();

        if (showGrid) {
            drawGrid();
        }

        for (Hold hold : board.holds()) {
            drawHold(hold, showLabels);
        }
        gc.restore();
    }

    private void calculateBoardBounds(Board board) {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        for (Hold hold : board.holds()) {
            double x = hold.getPosition().x;
            double y = hold.getPosition().y;
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }

        boardWidth = maxX - minX + HOLD_SIZE * 2;
        boardHeight = maxY - minY + HOLD_SIZE * 2;
        panX = -minX + HOLD_SIZE;
        panY = -minY + HOLD_SIZE;
    }

    private void autoFitBoard() {
        if (boardWidth <= 0 || boardHeight <= 0) return;

        double scaleX = canvas.getWidth() / boardWidth;
        double scaleY = canvas.getHeight() / boardHeight;
        scale = Math.min(scaleX, scaleY) * 0.95; // 95% to add some padding
    }

    private void drawGrid() {
        gc.setStroke(GRID_COLOR);
        gc.setLineWidth(0.5);

        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();

        for (int x = 0; x < width; x += 50) {
            gc.strokeLine(x, 0, x, height);
        }
        for (int y = 0; y < height; y += 50) {
            gc.strokeLine(0, y, width, y);
        }
    }

    private void drawHold(Hold hold, boolean showLabel) {
        double x = (hold.getPosition().x + panX) * scale;
        double y = (hold.getPosition().y + panY) * scale;
        double size = HOLD_SIZE * scale;

        Color fillColor = getDifficultyColor(hold.difficulty());

        gc.save();
        gc.translate(x, y);
        gc.rotate(hold.getRotation());

        // Draw hold shape based on type
        gc.setFill(fillColor);
        gc.setStroke(STROKE_COLOR);
        gc.setLineWidth(2);

        drawHoldShape(hold.holdType(), size);

        // Draw rotation arrow
        drawRotationArrow(size);

        gc.restore();

        // Draw labels if enabled
        if (showLabel) {
            drawHoldLabel(hold, x, y, size);
        }
    }

    private void drawHoldShape(HoldType type, double size) {
        switch (type) {
            case JUG -> {
                // Round shape for jugs
                gc.fillOval(-size/2, -size/2, size, size);
                gc.strokeOval(-size/2, -size/2, size, size);
            }
            case CRIMP -> {
                // Small rectangle for crimps
                double w = size;
                double h = size / 1.5;
                gc.fillRect(-w/2, -h/2, w, h);
                gc.strokeRect(-w/2, -h/2, w, h);
            }
            case SLOPER -> {
                // Arc shape for slopers
                gc.fillArc(-size/2, -size/2, size, size, 0, 180, ArcType.CHORD);
                gc.strokeArc(-size/2, -size/2, size, size, 0, 180, ArcType.CHORD);
            }
            case PINCH -> {
                // Diamond for pinches
                double[] xPoints = {0, size/2, 0, -size/2};
                double[] yPoints = {-size/2, 0, size/2, 0};
                gc.fillPolygon(xPoints, yPoints, 4);
                gc.strokePolygon(xPoints, yPoints, 4);
            }
            case POCKET -> {
                // Small circles for pockets with inner hole
                double pocketSize = size * 0.6;
                gc.fillOval(-pocketSize/2, -pocketSize/2, pocketSize, pocketSize);
                gc.strokeOval(-pocketSize/2, -pocketSize/2, pocketSize, pocketSize);
                gc.setFill(BACKGROUND_COLOR);
                double innerSize = pocketSize / 2;
                gc.fillOval(-innerSize/2, -innerSize/2, innerSize, innerSize);
            }
            case EDGE -> {
                // Rectangle for edges
                double w = size;
                double h = size / 2;
                gc.fillRect(-w/2, -h/2, w, h);
                gc.strokeRect(-w/2, -h/2, w, h);
            }
        }
    }

    private void drawRotationArrow(double size) {
        gc.setStroke(ARROW_COLOR);
        gc.setLineWidth(2 * scale);

        double arrowLen = size * 0.6;
        gc.strokeLine(0, 0, 0, -arrowLen);

        // Arrow head
        double arrowSize = 5 * scale;
        gc.strokeLine(0, -arrowLen, -arrowSize, -arrowLen + arrowSize);
        gc.strokeLine(0, -arrowLen, arrowSize, -arrowLen + arrowSize);
    }

    private void drawHoldLabel(Hold hold, double x, double y, double size) {
        gc.setFill(LABEL_COLOR);
        gc.setFont(Font.font(10 * scale));

        String label = String.format("%s (%.1f)",
                hold.holdType().name().substring(0, 1),
                hold.difficulty());

        gc.fillText(label, x + size/2 + 5, y - size/2);
    }

    private Color getDifficultyColor(double difficulty) {
        if (difficulty < 2) return Color.GREEN;
        if (difficulty < 4) return Color.YELLOW;
        if (difficulty < 6) return Color.ORANGE;
        if (difficulty < 8) return Color.RED;
        return Color.DARKRED;
    }
}
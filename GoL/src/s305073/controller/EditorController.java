package s305073.controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import model.Cell;
import model.DynamicGameOfLife;

/**
 * Created by remibengtpettersen.
 */
public class EditorController {

    @FXML private Canvas canvas;
    @FXML private Canvas strip;

    private DynamicGameOfLife gol;
    private Cell cell;
    private GraphicsContext gc;
    private double dragStartX;
    private double dragStartY;
    private double canvasOffsetX;
    private double canvasOffsetY;
    private boolean dragging = false;

    public EditorController() {
        cell = new Cell();
        cell.setSize(50);
        cell.setSpacing(0.1);
    }

    public void initialize() {
        gc = canvas.getGraphicsContext2D();
        setCanvasColor();
        drawGridLines();
    }

    private void setCanvasColor() {
        gc.setFill(cell.getDeadColor());
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void loadEditor() {
        scaleToCanvasSize();
        centerOnCanvas();

        draw();
    }

    private void centerOnCanvas() {
        gol.fitBoardToPattern();

        double patternWidth = gol.getGridWidth();
        double patternPixelWidth = patternWidth * cell.getSize();
        System.out.println("Pattern width pixels :" + patternWidth * cell.getSize());

        double patternHeight = gol.getGridHeight();
        double patternPixelHeight = patternHeight * cell.getSize();
        System.out.println("Pattern height pixels :" + patternHeight * cell.getSize());

        double patternWidthCenter = patternPixelWidth / 2;
        System.out.println("Pattern width (px) center :" + patternWidthCenter);

        double patternHeightCenter = patternPixelHeight / 2;
        System.out.println("Pattern height (px) center" + patternHeightCenter);

        double canvasWidthCenter = canvas.getWidth() / 2;
        System.out.println("Canvas W center (px): " + canvasWidthCenter);
        double canvasHeightCenter = canvas.getHeight() / 2;
        System.out.println("Canvas H center (px): " + canvasHeightCenter);

        double patternLengthHeightRatio = patternWidth / patternHeight;
        System.out.println("Lenght/Height: " + patternLengthHeightRatio);

        canvasOffsetX = canvasWidthCenter - patternWidthCenter + gol.getOffsetX() * cell.getSize();
        canvasOffsetY = canvasHeightCenter - patternHeightCenter + gol.getOffsetY() * cell.getSize();

        System.out.println("Pattern start x on screen: " + canvasOffsetX);
        System.out.println("Pattern start y on screen: " + canvasOffsetY);

        if (patternLengthHeightRatio > 1) {
            System.out.println("Width is greater than height");
        }
        else {
            System.out.println("Height is grater than width");
        }
    }

    private void scaleToCanvasSize() {
    }


    private void draw() {
        gc.setFill(cell.getDeadColor());
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(cell.getColor());
        gc.strokeRect(getCommonOffsetX(), getCommonOffsetY(), gol.getGridWidth() * cell.getSize(), gol.getGridHeight() * cell.getSize());
        for (int i = 0; i < gol.getGridWidth(); i++) {
            for (int j = 0; j < gol.getGridHeight(); j++) {
                if (gol.isCellAlive(i, j)) {
                    drawCell(i, j);
                }
            }
        }
    }

    public void getDeepCopyGol(DynamicGameOfLife gol) {
        this.gol = gol.clone();
    }

    @FXML
    private void onMousePressed(MouseEvent event) {
        dragStartX = event.getX() - canvasOffsetX;
        dragStartY = event.getY() - canvasOffsetY;
    }

    @FXML
    private void onMouseDragged(MouseEvent event) {
        dragging = true;

        if (event.isSecondaryButtonDown()) {
            canvasOffsetX = event.getX() - dragStartX;
            canvasOffsetY = event.getY() - dragStartY;
            draw();
        }
    }

    @FXML
    private void onMouseReleased(Event event) {
    }

    private void fitTo(int x, int y) {
        if(x < 0){
            gol.increaseXLeft(Math.abs(x));
        }
        if(y < 0){
            gol.increaseYTop(Math.abs(y));
        }
    }

    public void onMouseClicked(MouseEvent event) {
        if (!dragging) {
            int pos_x = (int) (Math.floor((event.getX() - getCommonOffsetX()) / cell.getSize()));
            int pos_y = (int) (Math.floor((event.getY() - getCommonOffsetY()) / cell.getSize()));

            System.out.println("(" + pos_x + ", " + pos_y + ")");
            MouseButton mouseButton = event.getButton();

            fitTo(pos_x, pos_y);

            if (pos_x < 0) {
                gol.getOffsetX();
                pos_x = 0;
            }
            if (pos_y < 0) {
                gol.getOffsetY();
                pos_y = 0;
            }

            if (mouseButton == MouseButton.PRIMARY) {
                gc.setFill(cell.getColor());
                gol.setCellAlive(pos_x, pos_y);
            }

            if (mouseButton == MouseButton.SECONDARY) {
                gc.setFill(cell.getDeadColor());
                gol.setCellDead(pos_x, pos_y);
            }

            draw();
        }

        dragging = false;
    }

    public void onScroll(ScrollEvent event) {
    }

    double getCommonOffsetX(){
        return (canvasOffsetX - gol.getOffsetX() * cell.getSize());
    }

    double getCommonOffsetY(){
        return (canvasOffsetY - gol.getOffsetY() * cell.getSize());
    }

    private double getCanvasPosX(int x) {
        return ((x) * cell.getSize()) + getCommonOffsetX();
    }

    private double getCanvasPosY(int y) {
        return ((y) * cell.getSize()) + getCommonOffsetY();
    }

    private void drawCell(int x, int y) {
        gc.fillRect(getCanvasPosX(x),
                    getCanvasPosY(y),
                    cell.getSize() - cell.getSize() * cell.getSpacing(),
                    cell.getSize() - cell.getSize() * cell.getSpacing());
    }

    /**
     * Draws the grid lines on the canvas
     */
    private void drawGridLines() {
        gc.setLineWidth(0.1);
        gc.setStroke(Color.BLACK);

        for (int i = 0; i < canvas.getHeight(); i += cell.getSize()) {
            for (int j = 0; j < canvas.getWidth() ; j += cell.getSize()) {
                gc.strokeLine(0, i + canvasOffsetY ,canvas.getWidth(), i + canvasOffsetY);
                gc.strokeLine(j + canvasOffsetX, 0, j + canvasOffsetX, canvas.getHeight());
            }
        }
    }

    public void closeEditor(ActionEvent actionEvent) {

    }

    public void onMouseMoved(MouseEvent event) {
        if (event.isAltDown()) {
            System.out.println("(" + event.getX() + ", " + event.getY() + ")");
        }
    }
}
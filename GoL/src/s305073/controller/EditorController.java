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
    private boolean gridLines;
    private double dragStartX;
    private double dragStartY;
    private double dragX;
    private double dragY;
    private double canvasOffsetX;
    private double canvasOffsetY;
    private boolean clickEnable = true;
    private int cellOffset;
    private boolean dragging = false;
    private int gridOffsetX;
    private int gridOffsetY;
    private int offset = 0;


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

    public void loadPattern() {
        draw();
        //drawGridLines();
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
        gridOffsetX = 0;
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
            //drawGridLines();
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
        offset = 50;
        System.out.println(canvasOffsetX);
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
}
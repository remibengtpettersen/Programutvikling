package s305073.controller;

import javafx.event.ActionEvent;
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
    private DynamicGameOfLife golStrip;
    private Cell editorCell;
    private Cell stripCell;
    private GraphicsContext gc;
    private double dragStartX;
    private double dragStartY;
    private double canvasOffsetX;
    private double canvasOffsetY;
    private boolean dragging = false;

    public EditorController() {
        // create cell for editor window.
        editorCell = new Cell();
        editorCell.setSize(100);
        editorCell.setSpacing(0.1);


        // create cell for stip window.
        stripCell = new Cell();
        stripCell.setSize(100);
        editorCell.setSpacing(0.1);
    }

    public void initialize() {
        setStripColor();
        setCanvasColor();
        drawGridLines();
    }

    private void setStripColor() {
        gc = strip.getGraphicsContext2D();
        gc.setFill(editorCell.getDeadColor());
        gc.fillRect(0, 0, strip.getWidth(), strip.getHeight());
    }

    private void setCanvasColor() {
        gc = canvas.getGraphicsContext2D();
        gc.setFill(editorCell.getDeadColor());
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void displayPattern() {
        // editor canvas.
        scaleToEditorCanvas();
        centerOnEditorCanvas();
        drawEditorCanvas();

        // strip
        updateStrip();
    }

    @FXML
    private void updateStrip() {
        golStrip = gol.clone();
        gc = strip.getGraphicsContext2D();
        gc.setFill(Color.BLACK);

        System.out.println(golStrip.getOffsetX());

        for (int i = 0; i < golStrip.getGridWidth(); i++) {
            for (int j = 0; j < golStrip.getGridHeight(); j++) {
                if (golStrip.isCellAlive(i, j)) {
                    drawCellOnStripCanvas(i, j);
                }
            }
        }



        /*String[] string = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};

        editorCell.setSize(5);
        gc = strip.getGraphicsContext2D();

        golStrip = gol.clone();

        Affine form = new Affine();
        double padding = 0;
        double tx = padding;

        for (int i = 0; i < 20; i++) {
            form.setTx(tx);
            gc.setTransform(form);
            gc.setFill(Color.OLIVEDRAB);
            gc.fillRect(0, 0, 25, 180);
            gc.setFill(Color.BLACK);
            String number = "1";
            gc.fillText(number, 5, 15);
            gc.setFill(editorCell.getColor());
            gc.strokeRect(30, 5, 285, 170);
            golStrip.nextGeneration();
            drawStrip();
            tx += 320;
        }

        form.setTx(0.0);
        gc.setTransform(form);*/
    }

    private void drawCellOnStripCanvas(int i, int j) {
        
    }

    private void drawStrip() {
        editorCell.setSize(1);
        // Wet asphalt
        gc.setFill(Color.web("#34495e"));
        gc.fillRect(25, 0, 295, 180);
        // cloud
        gc.setFill(Color.web("#fbfcfc"));
        gc.fillRect(30, 5, 285, 170);

        gc.setFill(editorCell.getColor());
        for (int i = 0; i < golStrip.getGridWidth(); i++) {
            for (int j = 0; j < golStrip.getGridHeight(); j++) {
                if (golStrip.isCellAlive(i, j)) {
                    drawCellOnEditorCanvas(i, j);
                }
            }
        }
    }

    private void centerOnEditorCanvas() {
        gol.fitBoardToPattern();

        // calculate pattern center in 2D.
        double patternWidthCenter = gol.getGridWidth() * editorCell.getSize() / 2;
        double patternHeightCenter = gol.getGridHeight() * editorCell.getSize() / 2;

        // calculate canvas center in 2D.
        double canvasWidthCenter = canvas.getWidth() / 2;
        double canvasHeightCenter = canvas.getHeight() / 2;

        // set drawEditorCanvas coordinates, x and y.
        canvasOffsetX = canvasWidthCenter - patternWidthCenter + gol.getOffsetX() * editorCell.getSize();
        canvasOffsetY = canvasHeightCenter - patternHeightCenter + gol.getOffsetY() * editorCell.getSize();
    }

    private void scaleToEditorCanvas() {
        double patternWidth = gol.getGridWidth() * editorCell.getSize();
        double patternHeight = gol.getGridHeight() * editorCell.getSize();

        if (patternHeight > canvas.getHeight()) {
            while (patternHeight > canvas.getHeight()) {
                editorCell.setSize(editorCell.getSize() * 0.99);
                patternHeight = gol.getGridHeight() * editorCell.getSize();
            }
        }

        if (patternWidth > canvas.getWidth()) {
            while (patternWidth > canvas.getWidth()) {
                editorCell.setSize(editorCell.getSize() * 0.99);
                patternWidth = gol.getGridWidth() * editorCell.getSize();
            }
        }
    }

    private void drawEditorCanvas() {
        gc.setFill(editorCell.getDeadColor());
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(editorCell.getColor());
        gc.strokeRect(getCommonOffsetX(), getCommonOffsetY(), gol.getGridWidth() * editorCell.getSize(), gol.getGridHeight() * editorCell.getSize());
        for (int i = 0; i < gol.getGridWidth(); i++) {
            for (int j = 0; j < gol.getGridHeight(); j++) {
                if (gol.isCellAlive(i, j)) {
                    drawCellOnEditorCanvas(i, j);
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
            drawEditorCanvas();
        }
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
            int pos_x = (int) (Math.floor((event.getX() - getCommonOffsetX()) / editorCell.getSize()));
            int pos_y = (int) (Math.floor((event.getY() - getCommonOffsetY()) / editorCell.getSize()));

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
                gc.setFill(editorCell.getColor());
                gol.setCellAlive(pos_x, pos_y);
            }

            if (mouseButton == MouseButton.SECONDARY) {
                gc.setFill(editorCell.getDeadColor());
                gol.setCellDead(pos_x, pos_y);
            }

            drawEditorCanvas();
        }
        dragging = false;
    }

    public void onScroll(ScrollEvent event) {
    }

    double getCommonOffsetX(){
        return (canvasOffsetX - gol.getOffsetX() * editorCell.getSize());
    }

    double getCommonOffsetY(){
        return (canvasOffsetY - gol.getOffsetY() * editorCell.getSize());
    }

    private double getCanvasPosX(int x) {
        return ((x) * editorCell.getSize()) + getCommonOffsetX();
    }

    private double getCanvasPosY(int y) {
        return ((y) * editorCell.getSize()) + getCommonOffsetY();
    }

    private void drawCellOnEditorCanvas(int x, int y) {
        gc.fillRect(getCanvasPosX(x),
                    getCanvasPosY(y),
                    editorCell.getSize() - editorCell.getSize() * editorCell.getSpacing(),
                    editorCell.getSize() - editorCell.getSize() * editorCell.getSpacing());
    }

    private void drawGridLines() {
        gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(0.1);
        gc.setStroke(Color.BLACK);

        for (int i = 0; i < canvas.getHeight(); i += editorCell.getSize()) {
            for (int j = 0; j < canvas.getWidth() ; j += editorCell.getSize()) {
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
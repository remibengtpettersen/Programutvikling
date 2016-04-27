package s305073.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
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
    private double editorCanvasOffsetX;
    private double editorCanvasOffsetY;
    private boolean dragging = false;
    private double stripCanvasOffsetX;
    private double stripCanvasOffsetY;

    public EditorController() {
        // create cell for editor window.
        editorCell = new Cell();
        editorCell.setSize(100);
        editorCell.setSpacing(0.1);


        // create cell for strip window.
        stripCell = new Cell();
        stripCell.setSize(100);
        editorCell.setSpacing(0.1);
    }

    public void initialize() {
        setStripColor();
        setCanvasColor();
        drawEditorGridLines();
    }

    public void displayPattern() {
        // editor canvas.
        scaleToEditorCanvas();
        centerOnEditorCanvas();
        drawEditorCanvas();

        // strip
        updateStrip();
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

    @FXML
    private void updateStrip() {
        golStrip = gol.clone();
        gc = strip.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        stripCell.setSize(editorCell.getSize());

        System.out.println(golStrip.getOffsetX());

        for (int i = 0; i < golStrip.getGridWidth(); i++) {
            for (int j = 0; j < golStrip.getGridHeight(); j++) {
                if (golStrip.isCellAlive(i, j)) {
                    drawCellOnStripCanvas(i, j);
                }
            }
        }

        /*editorCell.setSize(5);
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

    private void drawCellOnStripCanvas(int x, int y) {
        editorCell.setSize(20);
        stripCell.setSize(20);
        double temp_x = getCanvasStripPosX(x);
        double temp_y = getCanvasStripPosY(y);
        gc.fillRect(getCanvasStripPosX(x),
                getCanvasStripPosY(y),
                stripCell.getSize() - stripCell.getSize() * stripCell.getSpacing(),
                stripCell.getSize() - stripCell.getSize() * stripCell.getSpacing());
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
        editorCanvasOffsetX = canvasWidthCenter - patternWidthCenter + gol.getOffsetX() * editorCell.getSize();
        editorCanvasOffsetY = canvasHeightCenter - patternHeightCenter + gol.getOffsetY() * editorCell.getSize();
    }

    private void centerOnStripCanvas() {
        stripCanvasOffsetX = editorCanvasOffsetX * 0.36;
        stripCanvasOffsetY = editorCanvasOffsetY * 0.36;
    }

    private void scaleToEditorCanvas() {
        double patternWidth = gol.getGridWidth() * editorCell.getSize();
        double patternHeight = gol.getGridHeight() * editorCell.getSize();

        if (patternHeight > canvas.getHeight()) {
            while (patternHeight - 10 > canvas.getHeight()) {
                editorCell.setSize(editorCell.getSize() * 0.99);
                patternHeight = gol.getGridHeight() * editorCell.getSize();
            }
        }

        if (patternWidth > canvas.getWidth()) {
            while (patternWidth - 10 > canvas.getWidth()) {
                editorCell.setSize(editorCell.getSize() * 0.99);
                patternWidth = gol.getGridWidth() * editorCell.getSize();
            }
        }
    }

    private void drawEditorCanvas() {
        gc.setFill(editorCell.getDeadColor());
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(editorCell.getColor());
        gc.strokeRect(getCommonEditorOffsetX(), getCommonEditorOffsetY(), gol.getGridWidth() * editorCell.getSize(), gol.getGridHeight() * editorCell.getSize());
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
        dragStartX = event.getX() - editorCanvasOffsetX;
        dragStartY = event.getY() - editorCanvasOffsetY;
    }

    @FXML
    private void onMouseDragged(MouseEvent event) {
        dragging = true;

        if (event.isSecondaryButtonDown()) {
            editorCanvasOffsetX = event.getX() - dragStartX;
            editorCanvasOffsetY = event.getY() - dragStartY;
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
            int pos_x = (int) (Math.floor((event.getX() - getCommonEditorOffsetX()) / editorCell.getSize()));
            int pos_y = (int) (Math.floor((event.getY() - getCommonEditorOffsetY()) / editorCell.getSize()));

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

    public void EditorCanvas(ScrollEvent event) {
    }

    double getCommonEditorOffsetX(){
        return (editorCanvasOffsetX - gol.getOffsetX() * editorCell.getSize());
    }

    double getCommonEditorOffsetY(){
        return (editorCanvasOffsetY - gol.getOffsetY() * editorCell.getSize());
    }

    private double getCanvasEditorPosX(int x) {
        return ((x) * editorCell.getSize()) + getCommonEditorOffsetX();
    }

    private double getCanvasEditorPosY(int y) {
        return ((y) * editorCell.getSize()) + getCommonEditorOffsetY();
    }

    double getCommonStripOffsetX(){
        return (stripCanvasOffsetX - gol.getOffsetX() * stripCell.getSize());
    }

    double getCommonStripOffsetY(){
        return (stripCanvasOffsetY - gol.getOffsetY() * stripCell.getSize());
    }

    private double getCanvasStripPosX(int x) {
        return ((x) * stripCell.getSize()) + getCommonEditorOffsetX();
    }

    private double getCanvasStripPosY(int y) {
        return ((y) * stripCell.getSize()) + getCommonEditorOffsetY();
    }

    private void drawCellOnEditorCanvas(int x, int y) {
        double temp_x = getCanvasEditorPosX(x);
        double temp_y = getCanvasEditorPosY(y);
        gc.fillRect(getCanvasEditorPosX(x),
                    getCanvasEditorPosY(y),
                    editorCell.getSize() - editorCell.getSize() * editorCell.getSpacing(),
                    editorCell.getSize() - editorCell.getSize() * editorCell.getSpacing());
    }

    private void drawEditorGridLines() {
        gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(0.1);
        gc.setStroke(Color.BLACK);

        for (int i = 0; i < canvas.getHeight(); i += editorCell.getSize()) {
            for (int j = 0; j < canvas.getWidth() ; j += editorCell.getSize()) {
                gc.strokeLine(0, i + editorCanvasOffsetY,canvas.getWidth(), i + editorCanvasOffsetY);
                gc.strokeLine(j + editorCanvasOffsetX, 0, j + editorCanvasOffsetX, canvas.getHeight());
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
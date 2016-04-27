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

import java.awt.*;

/**
 * Created by remibengtpettersen.
 */
public class EditorController {

    private GraphicsContext gc;

    @FXML private Canvas editor;
    @FXML private Canvas strip;

    private DynamicGameOfLife golEditor;
    private DynamicGameOfLife golStrip;
    private Cell editorCell;
    private Cell stripCell;
    private Point mouseClickStart;
    private Point editorOffset;
    private Point stripOffset;
    private boolean isDrag = false;

    public EditorController() {
        // instantiate gol for strip
        golStrip = new DynamicGameOfLife();

        // instantiate editor properties
        editorOffset = new Point();
        stripOffset = new Point();
        mouseClickStart = new Point();

        // create cell for editor window.
        editorCell = new Cell();
        editorCell.setSpacing(0.1);

        // create cell for strip window.
        stripCell = new Cell();
        editorCell.setSpacing(0.1);
    }

    public void initialize() {

    }

    public void getDeepCopyGol(DynamicGameOfLife gol) {
        this.golEditor = gol.clone();
    }

    public void displayPattern() {
        centerPatternInEditor();
        updateEditor();

        updateStrip();
    }

    private void centerPatternInEditor() {
        editorCell.setSize(Cell.MAX_SIZE);
        editorCell = scalePatternToCanvas(golEditor, editor, editorCell);
        editorOffset = centerPatternOnCanvas(golEditor, editor, editorCell);
    }

    private void setStripColor() {
        setGraphicsContectForEditor();
        gc.setFill(editorCell.getDeadColor());
        gc.fillRect(0, 0, strip.getWidth(), strip.getHeight());
    }

    private void setCanvasColor() {
        setGraphicsContectForEditor();
        gc.setFill(editorCell.getDeadColor());
        gc.fillRect(0, 0, editor.getWidth(), editor.getHeight());
    }

    private void drawOnStrip(int x, int y) {
        gc.fillRect(
                getStripPosX(x),
                getStripPosY(y),
                stripCell.getSize() - stripCell.getSize() * stripCell.getSpacing(),
                stripCell.getSize() - stripCell.getSize() * stripCell.getSpacing());
    }

    private Point centerPatternOnCanvas(DynamicGameOfLife gol, Canvas canvas, Cell cell) {

        Point point = new Point();
        gol.fitBoardToPattern();

        // calculate pattern center in 2D.
        double patternWidthCenter = gol.getGridWidth() * cell.getSize() / 2;
        double patternHeightCenter = gol.getGridHeight() * cell.getSize() / 2;

        // calculate editor center in 2D.
        double canvasWidthCenter = canvas.getWidth() / 2;
        double canvasHeightCenter = canvas.getHeight() / 2;

        // set updateEditor coordinates, x and y.
        point.setLocation(canvasWidthCenter - patternWidthCenter + gol.getOffsetX() * cell.getSize(),
                          canvasHeightCenter - patternHeightCenter + gol.getOffsetY() * cell.getSize());

        return point;
    }

    private Cell scalePatternToCanvas(DynamicGameOfLife gol, Canvas canvas, Cell cell) {
        int padding = 5;

        double patternWidth = gol.getGridWidth() * cell.getSize();
        double patternHeight = gol.getGridHeight() * cell.getSize();

        if (patternHeight + cell.getSize() *  padding > canvas.getHeight()) {
            while (patternHeight + cell.getSize() * padding > canvas.getHeight()) {
                cell.setSize(cell.getSize() * 0.99);
                patternHeight = gol.getGridHeight() * cell.getSize();
            }
        }

        if (patternWidth + cell.getSize() * padding > canvas.getWidth()) {
            while (patternWidth + cell.getSize() * padding > canvas.getWidth()) {
                cell.setSize(cell.getSize() * 0.99);
                patternWidth = gol.getGridWidth() * cell.getSize();
            }
        }

        return cell;
    }

    private void updateEditor() {
        setGraphicsContectForEditor();
        gc.setFill(editorCell.getColor());

        for (int i = 0; i < golEditor.getGridWidth(); i++) {
            for (int j = 0; j < golEditor.getGridHeight(); j++) {
                if (golEditor.isCellAlive(i, j)) {
                    drawCellOnEditorCanvas(i, j);
                }
            }
        }
    }

    private double getCommonEditorOffsetX(){
        return (editorOffset.getX() - golEditor.getOffsetX() * editorCell.getSize());
    }

    private double getCommonEditorOffsetY(){
        return (editorOffset.getY() - golEditor.getOffsetY() * editorCell.getSize());
    }

    private double getEditorPosX(int x) {
        return ((x) * editorCell.getSize()) + getCommonEditorOffsetX();
    }

    private double getEditorPosY(int y) {
        return ((y) * editorCell.getSize()) + getCommonEditorOffsetY();
    }

    private double getCommonStripOffsetX(){
        return (stripOffset.getX() - golStrip.getOffsetX() * stripCell.getSize());
    }

    private double getCommonStripOffsetY(){
        return (stripOffset.getY() - golStrip.getOffsetY() * stripCell.getSize());
    }

    private double getStripPosX(int x) {
        return ((x) * stripCell.getSize()) + getCommonStripOffsetX();
    }

    private double getStripPosY(int y) {
        return ((y) * stripCell.getSize()) + getCommonStripOffsetY();
    }

    private void drawCellOnEditorCanvas(int x, int y) {
        gc.fillRect(
                getEditorPosX(x),
                getEditorPosY(y),
                editorCell.getSize() - editorCell.getSize() * editorCell.getSpacing(),
                editorCell.getSize() - editorCell.getSize() * editorCell.getSpacing());
    }

    private void drawEditorGridLines() {
        setGraphicsContectForEditor();
        gc.setLineWidth(0.1);
        gc.setStroke(Color.BLACK);

        for (int i = 0; i < editor.getHeight(); i += editorCell.getSize()) {
            for (int j = 0; j < editor.getWidth() ; j += editorCell.getSize()) {
                gc.strokeLine(0, i + editorOffset.getY(), editor.getWidth(), i + editorOffset.getY());
                gc.strokeLine(j + editorOffset.getX(), 0, j + editorOffset.getY(), editor.getHeight());
            }
        }
    }

    private void fitTo(int x, int y) {
        if(x < 0){
            golEditor.increaseXLeft(Math.abs(x));
        }
        if(y < 0){
            golEditor.increaseYTop(Math.abs(y));
        }
    }

    @FXML
    private void updateStrip() {
        Affine form = new Affine();

        double offset = 150;
        double width = 290;
        double padding = offset - strip.getWidth() / 2;
        double tx = padding;

        // setup pattern.
        deepCopyPatternInEditor();
        centerPatternInStrip();
        setGraphicsContextForStrip();
        configureStripParameters();

        // draws twenty generations in strip.
        for (int i = 0; i < 20; i++) {
            form.setTx(tx);
            gc.setTransform(form);
            golStrip.nextGeneration();
            drawStrip();
            drawLine();
            tx += width;
        }

        // resets strip
        form.setTx(0.0);
        gc.setTransform(form);

        // sets GC back to editor.
        setGraphicsContectForEditor();
    }

    private void setGraphicsContectForEditor() {
        gc = editor.getGraphicsContext2D();
    }

    private void centerPatternInStrip() {
        stripCell.setSize(Cell.MAX_SIZE);
        stripCell = scalePatternToCanvas(golStrip, strip, stripCell);
        stripOffset = centerPatternOnCanvas(golStrip, strip, stripCell);
    }

    private void setGraphicsContextForStrip() {
        gc = strip.getGraphicsContext2D();
    }

    private void configureStripParameters() {
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.OLIVEDRAB);
        gc.setLineWidth(10);
    }

    private void deepCopyPatternInEditor() {
        golStrip = golEditor.clone();
    }

    public void drawStrip() {
        //gc.clearRect(0, 0, strip.widthProperty().doubleValue(), strip.heightProperty().doubleValue());
        for (int i = 0; i < 288 / stripCell.getSize(); i++) {
            for (int j = 0; j < 180 / stripCell.getSize() ; j++) {
                if (golStrip.isCellAlive(i, j)) {
                    drawOnStrip(i, j);
                }
            }
        }
    }

    private void drawLine() {
        gc.strokeLine(290 + 140, 0, 290 + 140, 180);
    }

    @FXML
    private void closeEditor(ActionEvent actionEvent) {

    }

    @FXML
    private void onMouseMovedEditor(MouseEvent event) {
        if (event.isAltDown()) {
            System.out.println("(" + event.getX() + ", " + event.getY() + ")");
        }
    }

    @FXML
    private void onMouseMovedStrip(MouseEvent event) {
        if (event.isAltDown()) {
            System.out.println("(" + event.getX() + ", " + event.getY() + ")");
        }
    }

    @FXML
    private void onMousePressedEditor(MouseEvent event) {
        mouseClickStart.setLocation(event.getX() - editorOffset.getX(), event.getY() - editorOffset.getY());
    }

    @FXML
    private void onMouseDraggedEditor(MouseEvent event) {
        isDrag = true;

        if (event.isSecondaryButtonDown()) {
            editorOffset.setLocation(event.getX() - mouseClickStart.getX(), event.getY() - mouseClickStart.getY());
            clearEditor();
            updateEditor();
        }
    }

    private void clearEditor() {
        gc.clearRect(0, 0, editor.widthProperty().doubleValue(), editor.heightProperty().doubleValue());
    }

    @FXML
    private void EditorCanvas(ScrollEvent event) {
    }

    @FXML
    private void onMouseClickedEditor(MouseEvent event) {
        MouseButton mouseButton = event.getButton();
        gc.setFill(editorCell.getColor());

        if (!isDrag) {
            int pos_x = (int) (Math.floor((event.getX() - getCommonEditorOffsetX()) / editorCell.getSize()));
            int pos_y = (int) (Math.floor((event.getY() - getCommonEditorOffsetY()) / editorCell.getSize()));

            fitTo(pos_x, pos_y);

            if (pos_x < 0) {
                golEditor.getOffsetX();
                pos_x = 0;
            }
            if (pos_y < 0) {
                golEditor.getOffsetY();
                pos_y = 0;
            }

            if (mouseButton == MouseButton.PRIMARY) {
                golEditor.setCellAlive(pos_x, pos_y);
            }

            if (mouseButton == MouseButton.SECONDARY) {
                golEditor.setCellDead(pos_x, pos_y);
            }

            clearEditor();
            updateEditor();

            clearStrip();
            updateStrip();
        }
        isDrag = false;
    }

    private void clearStrip() {
        setGraphicsContextForStrip();
        gc.clearRect(0, 0, strip.widthProperty().doubleValue(), strip.heightProperty().doubleValue());
    }
}
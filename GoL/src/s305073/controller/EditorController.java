package s305073.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import model.Cell;
import model.DynamicGameOfLife;

import lieng.*;

import java.awt.*;
import java.io.IOException;

/**
 * Created by remibengtpettersen.
 */
public class EditorController {

    private GraphicsContext gc;

    @FXML private Canvas editor;
    @FXML private Canvas strip;
    @FXML private ScrollPane scrollPane;

    // GoL object
    private DynamicGameOfLife golEditor;
    private DynamicGameOfLife golStrip;

    // cell used in editor and strip
    private Cell editorCell;
    private Cell stripCell;
    private Point mouseClickStart;
    private Point editorOffset;
    private Point stripOffset;
    private boolean isDrag = false;

    private int viewPadding = 10;
    private int frameWidth = 300;
    private double lineWeight = 10;
    private int padding = 6;


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

    public void getDeepCopyGol(DynamicGameOfLife gol) {
        this.golEditor = gol.clone();
    }

    public void setPattern() {
        scalePatternToEditor();
        editorOffset = centerPatternEditor();
        updateEditor();
        setGridLines();

        updateStrip();

        try {
            makeGif();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawOnStrip(int x, int y) {
        gc.fillRect(
                getStripPosX(x),
                getStripPosY(y),
                stripCell.getSize() - stripCell.getSize() * stripCell.getSpacing(),
                stripCell.getSize() - stripCell.getSize() * stripCell.getSpacing());
    }

    private Point centerPatternEditor() {
        Point point = new Point();
        golEditor.fitBoardToPattern();

        // calculate pattern center in 2D.
        double patternWidthCenter = (golEditor.getGridWidth()) * editorCell.getSize() / 2;
        double patternHeightCenter = (golEditor.getGridHeight()) * editorCell.getSize() / 2;

        // calculate editor center in 2D.
        double canvasWidthCenter = editor.getWidth() / 2;
        double canvasHeightCenter = editor.getHeight() / 2;

        // set updateEditor coordinates, x and y.
        point.setLocation(canvasWidthCenter - patternWidthCenter + golEditor.getOffsetX() * editorCell.getSize(),
                          canvasHeightCenter - patternHeightCenter + golEditor.getOffsetY() * editorCell.getSize());

        return point;
    }

    private Point centerPatternFrame() {
        Point point = new Point();
        golStrip.fitBoardToPattern();

        // calculate pattern center in 2D.
        double patternWidthCenter = (golStrip.getGridWidth()) * stripCell.getSize() / 2;
        double patternHeightCenter = (golStrip.getGridHeight()) * stripCell.getSize() / 2;

        // calculate editor center in 2D.
        double canvasWidthCenter = frameWidth / 2;
        double canvasHeightCenter = strip.getHeight() / 2;

        // set updateEditor coordinates, x and y.
        point.setLocation(canvasWidthCenter - patternWidthCenter + golStrip.getOffsetX() * stripCell.getSize(),
                canvasHeightCenter - patternHeightCenter + golStrip.getOffsetY() * stripCell.getSize());

        return point;
    }

    private void scalePatternToEditor() {
        editorCell.setSize(Cell.MAX_SIZE);

        double patternWidth = (golEditor.getGridWidth() + padding) * editorCell.getSize();
        double patternHeight = (golEditor.getGridHeight() + padding) * editorCell.getSize();

        if (patternWidth > editor.getWidth()) {
            editorCell.setSize((editor.getWidth())/ (golEditor.getGridWidth() + padding));
        }

        if (patternHeight > editor.getHeight()) {
            editorCell.setSize((editor.getHeight())/ (golEditor.getGridHeight() + padding));
        }
    }

    private void scalePatternToStrip() {
        stripCell.setSize(Cell.MAX_SIZE);

        double patternWidth = (golStrip.getGridWidth() + padding) * stripCell.getSize();
        double patternHeight = (golStrip.getGridHeight() + padding) * stripCell.getSize();

        if (patternWidth > frameWidth) {
            stripCell.setSize(frameWidth / (golStrip.getGridWidth() + padding));
        }

        if (patternHeight > strip.getHeight()) {
            stripCell.setSize(strip.getHeight() / (golStrip.getGridHeight() + padding));
        }
    }

    private void updateEditor() {
        setGraphicsContentToEditor();

        gc.setFill(editorCell.getColor());

        for (int i = 0; i < golEditor.getGridWidth(); i++) {
            for (int j = 0; j < golEditor.getGridHeight(); j++) {
                if (golEditor.isCellAlive(i, j)) {
                    drawCellOnEditorCanvas(i, j);
                }
            }
        }
    }

    // region getters
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
    //endregion

    private void drawCellOnEditorCanvas(int x, int y) {
        gc.fillRect(
                getEditorPosX(x),
                getEditorPosY(y),
                editorCell.getSize() - editorCell.getSize() * editorCell.getSpacing(),
                editorCell.getSize() - editorCell.getSize() * editorCell.getSpacing());
    }

    private void setGridLines() {
        setGraphicsContentToEditor();
        gc.setLineWidth(0.1);
        gc.setStroke(Color.BLACK);

        for (int i = 0; i < editor.getHeight() + editorOffset.getY(); i += editorCell.getSize()) {
            for (int j = 0; j < editor.getWidth() + editorOffset.getX(); j += editorCell.getSize()) {
                gc.strokeLine(0, i - editorOffset.getY() + (editorCell.getSize() / 2), editor.getWidth(), i - editorOffset.getY() + (editorCell.getSize() / 2));
                gc.strokeLine(j - editorOffset.getX(), 0, j - editorOffset.getX() , editor.getHeight());
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

        double padding = 0;
        double tx = padding;

        // setup pattern.
        deepCopyPatternInEditor();
        scalePatternToStrip();
        stripOffset = centerPatternFrame();
        configureStripApperanc();

        setGraphicsContextToStrip();

        // draws twenty generations in strip.
        for (int i = 0; i < 20; i++) {
            form.setTx(tx);
            gc.setTransform(form);
            golStrip.nextGeneration();
            drawStrip();
            drawLine();
            tx += frameWidth;
        }

        // resets strip
        form.setTx(0.0);
        gc.setTransform(form);

        // sets GC back to editor.
        setGraphicsContentToEditor();
    }

    private void setGraphicsContentToEditor() {
        gc = editor.getGraphicsContext2D();
    }

    private void setGraphicsContextToStrip() {
        gc = strip.getGraphicsContext2D();
    }

    private void configureStripApperanc() {
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.OLIVEDRAB);
        gc.setLineWidth(lineWeight);
    }

    private void deepCopyPatternInEditor() {
        golStrip = golEditor.clone();
    }

    public void drawStrip() {
        for (int i = 0; i < frameWidth / stripCell.getSize(); i++) {
            for (int j = 0; j < strip.getHeight() / stripCell.getSize() ; j++) {
                if (golStrip.isCellAlive(i, j)) {
                    drawOnStrip(i, j);
                }
            }
        }
    }

    private void drawLine() {
        gc.strokeLine(frameWidth, 0, frameWidth, strip.getHeight());
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
            setGridLines();
        }
    }

    @FXML
    private void onScrollEditorCanvas(ScrollEvent event) {
        if (event.getDeltaY() > 1) {
            editorCell.setSize(editorCell.getSize() * 1.01);
            clearEditor();
            updateEditor();
            setGridLines();
        }
        else {
            editorCell.setSize(editorCell.getSize() * 0.99);
            clearEditor();
            updateEditor();
            setGridLines();
        }
    }

    @FXML
    private void saveToFile(ActionEvent actionEvent) {
        // to be developed...
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
            setGridLines();

            clearStrip();
            updateStrip();
        }
        isDrag = false;
    }

    private void clearStrip() {
        setGraphicsContextToStrip();
        gc.clearRect(0, 0, strip.widthProperty().doubleValue(), strip.heightProperty().doubleValue());
    }

    private void clearEditor() {
        gc.clearRect(0, 0, editor.widthProperty().doubleValue(), editor.heightProperty().doubleValue());
    }

    public void resetEditor(ActionEvent actionEvent) {
        golEditor.clearGrid();
        clearEditor();
        clearStrip();
        setGridLines();
    }

    public void makeGif() throws IOException {
        String fileName = "test.gif";
        int width = 100;
        int height = 100;
        int timeMilliSeconds = 1000;

        DynamicGameOfLife golGif;
        lieng.GIFWriter gifWriter = new GIFWriter(width, height, fileName, timeMilliSeconds);
        golGif = golEditor.clone();

        for (int i = 0; i < golGif.getGridWidth(); i++) {
            for (int j = 0; j < golGif.getGridHeight(); j++) {
                if (golGif.isCellAlive(i, j)) {

                }
            }
        }

        gifWriter.fillRect(0, width - 1, 0, height - 1, java.awt.Color.BLACK);
        gifWriter.insertAndProceed();

        gifWriter.fillRect(0, width - 1, 0, height - 1, java.awt.Color.RED);
        gifWriter.insertAndProceed();

        gifWriter.insertCurrentImage();

        gifWriter.close();
    }
}
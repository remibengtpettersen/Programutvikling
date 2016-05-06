package s305073.controller;

import controller.MasterController;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.stage.FileChooser;
import model.CameraView;
import model.Cell;

import model.DynamicGameOfLife;
import model.GameOfLife;
import s305073.model.PatternConverter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by remibengtpettersen.
 */
public class EditorController {

    @FXML private Canvas editor;
    @FXML private Canvas strip;
    @FXML private ScrollPane scrollPane;
    @FXML private TextField txtName;
    @FXML private TextField txtDescription;
    @FXML private TextField txtAuthor;
    @FXML private TextField txtRule;
    @FXML private Button btnSave;

    private int currentMousePositionX;
    private int currentMousePositionY;

    private int previewsMousePositionX;
    private int previewsMousePositionY;

    private GraphicsContext gcEditor;
    private GraphicsContext gcStrip;
    private CameraView cameraEditorView = new CameraView();
    private CameraView cameraViewStrip = new CameraView();
    private CameraView parentCameraView;

    private boolean mouseOnCanvas;
    private boolean mouseDrag;

    private GameOfLife golEditor;
    private GameOfLife golStrip;

    private Cell editorCell;
    private Cell stripCell;

    private MasterController masterController;
    private GameOfLife parentGol;
    private double parentCellSize;
    private Thread thread = new Thread();

    public EditorController() {
        // instantiate cell for editor and setting size and color
        editorCell = new Cell();
        editorCell.setSize(20.0);
        editorCell.setColor(Color.GREEN);

        // instantiate cell for strip and setting size and color
        stripCell = new Cell();
        stripCell.setSize(10);
        stripCell.setColor(Color.GREEN);

        cameraEditorView = new CameraView();
    }

    public void initialize(MasterController masterController) {
        this.masterController = masterController;

        parentCameraView = masterController.getCanvasController().getCameraView();
        parentGol = masterController.getCanvasController().gol;
        parentCellSize = masterController.getCanvasController().getCell().getSize();

        txtRule.setText(masterController.getCanvasController().gol.getRule().toString());
        btnSave.requestFocus();

        gcEditor = editor.getGraphicsContext2D();
        gcStrip = strip.getGraphicsContext2D();
    }

    public void getDeepCopyGol(GameOfLife gol) {
        this.golEditor = gol.clone();
    }

    public void setPattern() {
        // set offset
        setBoardOffset();

        // display on canvas
        renderEditor();

        // display strip
        updateStrip();
    }

    private void setBoardOffset() {
        // get board offset horizontally from parent view
        cameraEditorView.boardOffsetX = (int) (parentCameraView.getCommonOffsetX(
                                                                    parentGol,
                                                                    parentCellSize) * editorCell.getSize() / parentCellSize);

        // get board offset vertically from parent view
        cameraEditorView.boardOffsetY = (int) (parentCameraView.getCommonOffsetY(
                                                                    parentGol,
                                                                    parentCellSize) * editorCell.getSize() / parentCellSize);
    }

    private void renderEditor() {

        // set canvas view for editor equal to game of life canvas
        cameraEditorView.updateView(golEditor, editorCell.getSize(), (int)editor.getWidth(), (int)editor.getHeight());

        gcEditor.setFill(editorCell.getColor());
        for (int i = cameraEditorView.currViewMinX; i < cameraEditorView.currViewMaxX; i++) {
            for (int j = cameraEditorView.currViewMinY; j < cameraEditorView.currViewMaxY; j++) {
                if (golEditor.isCellAlive(i, j))
                    fillEditorPositionAt(i, j);
            }
        }
    }

    private void fillEditorPositionAt(int i, int j) {
        drawEditorAt(i, j);
    }

    public void drawEditorAt(int x, int y) {
        gcEditor.fillRect(getEditorCanvasPosX(x), getEditorCanvasPosY(y), editorCell.getSize() - editorCell.getSpacingInPixels(), editorCell.getSize() - editorCell.getSpacingInPixels());
    }

    public void drawStripAt(int x, int y) {
        gcStrip.fillRect(getStripCanvasPosX(x), getStripCanvasPosY(y), stripCell.getSize() - stripCell.getSpacingInPixels(), stripCell.getSize() - stripCell.getSpacingInPixels());
    }

    @FXML
    private void updateStrip() {
        // clone gol used for editor to strip
        golStrip = golEditor.clone();

        // calculate width and height for strip view
        double number = editor.getHeight() / editorCell.getSize();
        double size = strip.getHeight() / number;
        int width = (int)(size * (editor.getWidth() / editorCell.getSize()));

        // set cell size for strip
        stripCell.setSize(strip.getHeight() / number);

        // set offset X and Y
        cameraViewStrip.boardOffsetX = (int) (cameraEditorView.getCommonOffsetX(golEditor, editorCell.getSize()) * stripCell.getSize() / editorCell.getSize());
        cameraViewStrip.boardOffsetY = (int) (cameraEditorView.getCommonOffsetY(golEditor, editorCell.getSize()) * stripCell.getSize() / editorCell.getSize());

        // show same view as editor controller
        cameraViewStrip.updateView(golStrip, stripCell.getSize(), width, (int)strip.getHeight());

        Affine form = new Affine();
        double tx = 0;

        for (int i = 0; i < 20; i++) {
            form.setTx(tx);

            gcStrip.setTransform(form);
            golStrip.nextGeneration();

            clearStrip();
            fillStrip();

            tx += 400;
        }

        form.setTx(0.0);
        gcStrip.setTransform(form);
    }

    private void clearStrip() {
        gcStrip.clearRect(0, 0, strip.widthProperty().doubleValue(), strip.heightProperty().doubleValue());
    }

    private void fillStrip() {
        gcStrip.setFill(stripCell.getColor());
        for (int i = cameraViewStrip.currViewMinX; i < cameraViewStrip.currViewMaxX; i++) {
            for (int j = cameraViewStrip.currViewMinY; j < cameraViewStrip.currViewMaxY; j++) {
                if (golStrip.isCellAlive(i, j)) {
                    drawStripAt(i, j);
                }
            }
        }
    }

    @FXML
    private void closeEditor(ActionEvent actionEvent) {

    }

    @FXML
    private void onMouseDraggedEditor(MouseEvent event) {
        // checks if mouse is on canvas
        if (!mouseOnCanvas)
            return;

        // gets the button clicked
        MouseButton b = event.getButton();
        mouseDrag = true;

        // gets mouse coordinates on canvas.
        currentMousePositionX = (int) event.getX();
        currentMousePositionY = (int) event.getY();


        // checks if left click
        if (b == MouseButton.PRIMARY) {

            // gets cell on event position
            int x = getEditorGridPosX(currentMousePositionX);
            int y = getEditorGridPosY(currentMousePositionY);

            // makes sure the cell is on the grid
            fitTo(x, y);

            // sets cell alive
            golEditor.setCellAlive(x = (x < 0) ? 0 : x, y = (y < 0) ? 0 : y);

            // draws the cell at (x, y)
            drawEditorAt(x, y);
        }

        // checks if right click
        if (event.getButton() == MouseButton.SECONDARY) {

            // moves board to current position
            moveBoard(currentMousePositionX, currentMousePositionY);
        }

        clearEditor();
        renderEditor();
        updateStrip();

        // stores current mouse position for later use
        previewsMousePositionX = currentMousePositionX;
        previewsMousePositionY = currentMousePositionY;
    }

    /**
     * Moves the board in the same distance and direction as it is from the previous mouse position to the current
     * mouse position
     * @param currMousePosX Current mouse X coordinate
     * @param currMousePosY Current mouse Y coordinate
     */
    private void moveBoard(int currMousePosX, int currMousePosY) {

        // checks if last mouse position is present
        if (previewsMousePositionX != 0 || previewsMousePositionY != 0) {

            // moves the board using the offset
            cameraEditorView.boardOffsetX += previewsMousePositionX - currMousePosX;
            cameraEditorView.boardOffsetY += previewsMousePositionY - currMousePosY;
        }
    }

    /**
     * makes sure that x and y are inside the game board, if the board is dynamic
     * @param x The X coordinate of the cell to fit the board to
     * @param y The Y coordinate of the cell to fit the board to
     */
    private void fitTo(int x, int y) {
        // checks x is negative
        if(x < 0){
            // extends the board to fit to x
            ((DynamicGameOfLife) golEditor).increaseXLeft(Math.abs(x));

        }
        // checks if y is negative
        if(y < 0){
            // extends the board to fit to x
            ((DynamicGameOfLife)golEditor).increaseYTop(Math.abs(y));
        }
    }

    public void makeGif(GameOfLife gol) throws IOException {
        // data related to the GIF image file
        String path = "testgif.gif";
        int width;
        int height;
        int size = 20;
        int counter = 0;

        int timePerMilliSecond = 1000; // 1 second

        GameOfLife golClone = gol.clone();

        golClone.getBoundingBox();

        width = (golClone.getBoundingBox()[1] - golClone.getBoundingBox()[0] + 1) * size;
        height = (golClone.getBoundingBox()[3] - golClone.getBoundingBox()[2] + 1) * size;

        // create the GIFWriter object
        lieng.GIFWriter gifWriter = new lieng.GIFWriter(width, height, path, timePerMilliSecond);

        int value = width / size;

        int lowCol = golClone.getBoundingBox()[0]; //golClone.getMinRowBoundingBox();
        int length = width;
        int lowRow = golClone.getBoundingBox()[2];
        int down = height;

        while (counter < 2) {
            // iterates through a square of tiles
            for (int i = lowRow; i < length + lowRow; i++) {
                for (int j = lowCol; j < down + lowCol; j++) {
                    if (golClone.isCellAlive(j, i)) {
                        // fill one tile of the image with orange - (j, i) coordinate
                        gifWriter.fillRect(
                                value * (i + 3 - lowRow),
                                value * ((i + 3 - lowRow) + 1) - 1,
                                value * (j + 3 - lowCol),
                                value * ((j + 3 - lowCol) + 1) - 1,
                                java.awt.Color.BLACK);
                        // insert the painted image into the animation sequence
                    }
                }
            }

            gifWriter.insertAndProceed();

            golClone.nextGeneration();

            counter++;
        }

        // insert the painted image into the animation sequence
        gifWriter.insertCurrentImage();

        // close the GIF stream.
        gifWriter.close();

        System.out.println("done!");
    }

    @FXML
    private void saveToFile(ActionEvent actionEvent) throws IOException {
        String content = "";

        // get pattern size
        int[] patternSize = PatternConverter.getPatternSize(golEditor);

        // get pattern as string
        String pattern = PatternConverter.ConvertPatternToRle(golEditor);

        // build content string
        content = setRleContentString(patternSize , pattern);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save RLE");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("RLE", "*.rle"));

        File file = fileChooser.showSaveDialog(masterController.stage);

        try {
            FileWriter fileWriter = null;

            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            //Logger.getLogger(JavaFX_Text.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String setRleContentString(int[] size, String pattern) {
        return  "#N " + txtName.getText() + "\n" +
                "#C " + txtDescription.getText() + "\n"+
                "#O " + txtAuthor.getText() + "\n" +
                "#r " + txtRule.getText() + "\n" +
                "x = " + size[1] + " , " + "y = " + size[0] + "\n" +
                pattern;
    }


    @FXML
    private void onMouseClickedEditor(MouseEvent event) {

        // check if mouse click is end of drag
        if (mouseDrag) {
            mouseDrag = false;
            previewsMousePositionX = 0;
            previewsMousePositionY = 0;
            return;
        }

        // gets button from event
        MouseButton mouseButton = event.getButton();

        // checks if left click
        if (mouseButton == MouseButton.PRIMARY) {

            // gets position of cell on click position
            int gridClickX = getEditorGridPosX(event.getX());
            int gridClickY = getEditorGridPosY(event.getY());

            // makes sure the cell is on the grid
            fitTo(gridClickX, gridClickY);

            // changes state of cell
            golEditor.changeCellState((gridClickX < 0)? 0 : gridClickX, (gridClickY < 0)? 0 : gridClickY);
        }

        // clear editor
        clearEditor();

        // draw pattern
        renderEditor();

        updateStrip();
    }

    private void clearEditor() {
        gcEditor.clearRect(0, 0, editor.widthProperty().doubleValue(), editor.heightProperty().doubleValue());
    }

    @FXML
    private void clear() {
        clearEditor();
        golEditor.clearGrid();

        clearStrip();
        golStrip.clearGrid();
    }


    private int getEditorGridPosX(double x) {
        return (int)Math.floor((x + cameraEditorView.getCommonOffsetX(golEditor, editorCell.getSize())) / editorCell.getSize());
    }

    private int getEditorGridPosY(double y) {
        return (int) Math.floor((y + cameraEditorView.getCommonOffsetY(golEditor, editorCell.getSize())) / editorCell.getSize());
    }


    /**
     * Converts grid coordinate x to canvas coordinate x.
     *
     * @param x a x coordinate in the grid.
     * @return a x coordinate on the canvas.
     */
    private double getEditorCanvasPosX(int x) {
        return x * editorCell.getSize() - cameraEditorView.getCommonOffsetX(golEditor, editorCell.getSize());
    }

    /**
     * Converts grid coordinate y to canvas coordinate y.
     *
     * @param y a y coordinate in the grid.
     * @return a y coordinate on the canvas.
     */
    private double getEditorCanvasPosY(int y) {
        return y * editorCell.getSize() - cameraEditorView.getCommonOffsetY(golEditor, editorCell.getSize());
    }

    /**
     * Converts grid coordinate x to canvas coordinate x.
     *
     * @param x a x coordinate in the grid.
     * @return a x coordinate on the canvas.
     */
    private double getStripCanvasPosX(int x) {
        return x * stripCell.getSize() - cameraViewStrip.getCommonOffsetX(golStrip, stripCell.getSize());
    }

    /**
     * Converts grid coordinate y to canvas coordinate y.
     *
     * @param y a y coordinate in the grid.
     * @return a y coordinate on the canvas.
     */
    private double getStripCanvasPosY(int y) {
        return y * stripCell.getSize() - cameraViewStrip.getCommonOffsetY(golStrip, stripCell.getSize());
    }
    //endregion

    /**
     * Changes the cellSize to give the effect of zooming.
     * @param scrollEvent Event created by mouse scroll
     */

    public void onScrollEditorCanvas(ScrollEvent scrollEvent) {

        // gets exact cell position at mouse coordinates
        double absMPosXOnGrid = (cameraEditorView.getCommonOffsetX(golEditor, editorCell.getSize()) + scrollEvent.getX()) / editorCell.getSize();
        double absMPosYOnGrid = (cameraEditorView.getCommonOffsetY(golEditor, editorCell.getSize()) + scrollEvent.getY()) / editorCell.getSize();

        // changes cell size
        editorCell.setSize(editorCell.getSize() * (1 + (scrollEvent.getDeltaY() / 150)));

        // moves the board so the mouse gets the exact same position on the board as before

        cameraEditorView.boardOffsetX = (int) ((absMPosXOnGrid - golEditor.getOffsetX()) * editorCell.getSize() - scrollEvent.getX());
        cameraEditorView.boardOffsetY = (int) ((absMPosYOnGrid - golEditor.getOffsetY()) * editorCell.getSize() - scrollEvent.getY());

        clearEditor();
        renderEditor();
    }

    /**
     * To keep track if mouse is located on the canvas.
     * @param event Event created by mouse action
     */
    public void onMouseEnteredEditor(MouseEvent event) {
        mouseOnCanvas = true;
    }

    /**
     * To keep track if mouse is located outside of the canvas.
     * Sets previous mouse coordinates to zero if exited.
     * @param event Event created by mouse action
     */
    public void onMouseExited(MouseEvent event) {
        mouseOnCanvas = false;
        previewsMousePositionX = 0;
        previewsMousePositionY = 0;
    }

    public void onMouseMovedStrip(MouseEvent event) {
    }

    public void onMouseMovedEditor(MouseEvent event) {
        //System.out.println(event.getX());
    }
}
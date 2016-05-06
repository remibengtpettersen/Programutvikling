package s305073.controller;

import controller.MasterController;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import model.CameraView;
import model.Cell;

import model.DynamicGameOfLife;
import model.GameOfLife;

/**
 * Created by s305073
 *
 * Editor class for standalone editor for manipulating pattern and get feedback on evolution of pattern.
 */
public class EditorController {

    @FXML private Canvas editor;
    @FXML private Canvas strip;
    @FXML private TextField txtName;
    @FXML private TextField txtDescription;
    @FXML private TextField txtAuthor;
    @FXML private TextField txtRule;

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

    /**
     * Constructor for Editor controller.
     *
     * Instantiate cells used to represent grid position in editor and strip.
     */
    public EditorController() {
        // instantiate cell for editor and setting size and color
        editorCell = new Cell();
        editorCell.setSize(20.0);
        editorCell.setColor(Color.GREEN);

        // instantiate cell for strip and setting size and color
        stripCell = new Cell();
        stripCell.setSize(10);
        stripCell.setColor(Color.GREEN);

        // camera view for displaying grid
        cameraEditorView = new CameraView();
    }

    /**
     * Passes master controller to editor to enable communication between controllers.
     *
     * @param masterController container for references connected to master controller object.
     */
    public void initialize(MasterController masterController) {
        this.masterController = masterController;

        // gets view parameters for main Game of life window
        parentCameraView = masterController.getCanvasController().getCameraView();

        // gets gol reference used in main window
        parentGol = masterController.getCanvasController().getGol();

        // gets cell size from main window
        parentCellSize = masterController.getCanvasController().getCell().getSize();

        // gets current rule from main program
        txtRule.setText(masterController.getCanvasController().getGol().getRule().toString());

        //btnSave.requestFocus();

        gcEditor = editor.getGraphicsContext2D();
        gcStrip = strip.getGraphicsContext2D();
    }

    /**
     * Gets a deep copy of game board grid
     * @param gol
     */
    public void getDeepCopyGol(GameOfLife gol) {
        this.golEditor = gol.clone();
    }

    /**
     * Populates editor canvas with the same pattern as present in main GUI.
     */
    public void setPattern() {
        // set offset
        setBoardOffset();

        // display on canvas
        renderEditor();

        // display strip
        updateStrip();
    }

    /**
     * Sets the board offset according to offset in main window at the time editor is launched.
     * Content will be scalled to fit current canvas.
     */
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

    /**
     * Render alive and dead cells in game board to canvas.
     */
    private void renderEditor() {
        // set canvas view for editor equal to game of life canvas
        cameraEditorView.updateView(golEditor, editorCell.getSize(), (int)editor.getWidth(), (int)editor.getHeight());

        // set color
        gcEditor.setFill(editorCell.getColor());

        // draws alive cells that are inside camera view
        for (int x = cameraEditorView.currViewMinX; x < cameraEditorView.currViewMaxX; x++) {
            for (int y = cameraEditorView.currViewMinY; y < cameraEditorView.currViewMaxY; y++) {

                if (golEditor.isCellAlive(x, y))
                    // draw one cell at position (x, y)
                    fillEditorPositionAt(x, y);
            }
        }
    }

    /**
     *
     * Will fill one grid cell on canvas.
     *
     * @param x_coordinate x position on canvas.
     * @param y_coordinate y position on canvas.
     */
    private void fillEditorPositionAt(int x_coordinate, int y_coordinate) {
        drawEditorAt(x_coordinate, y_coordinate);
    }


    private void drawEditorAt(int x, int y) {
        gcEditor.fillRect(getEditorCanvasPosX(x), getEditorCanvasPosY(y), editorCell.getSize() - editorCell.getSpacingInPixels(), editorCell.getSize() - editorCell.getSpacingInPixels());
    }

    private void drawStripAt(int x, int y) {
        gcStrip.fillRect(getStripCanvasPosX(x), getStripCanvasPosY(y), stripCell.getSize() - stripCell.getSpacingInPixels(), stripCell.getSize() - stripCell.getSpacingInPixels());
    }

    @FXML
    private void updateStrip() {
        // clone gol used for editor to strip
        golStrip = golEditor.clone();

        // calculate width and height for strip view
        double horizontalCells = editor.getHeight() / editorCell.getSize();

        // calculate strip cell size
        double cellSizeStrip = strip.getHeight() / horizontalCells;

        // calculate strip width for one generation
        int width = (int)(cellSizeStrip * (editor.getWidth() / editorCell.getSize()));

        // set cell size for strip
        stripCell.setSize(strip.getHeight() / horizontalCells);

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

            // increment one generation
            golStrip.nextGeneration();

            clearStrip();

            // render strip with alive cells
            renderStrip();

            // set transformation
            tx += 400;
        }

        // reset
        form.setTx(0.0);
        gcStrip.setTransform(form);
    }

    /**
     * Clear strip - prepare for new update.
     */
    private void clearStrip() {
        gcStrip.clearRect(0, 0, strip.widthProperty().doubleValue(), strip.heightProperty().doubleValue());
    }

    /**
     * Render alive cells in grid to strip canvas.
     */
    private void renderStrip() {
        gcStrip.setFill(stripCell.getColor());
        for (int i = cameraViewStrip.currViewMinX; i < cameraViewStrip.currViewMaxX; i++) {
            for (int j = cameraViewStrip.currViewMinY; j < cameraViewStrip.currViewMaxY; j++) {

                if (golStrip.isCellAlive(i, j)) {
                    drawStripAt(i, j);
                }
            }
        }
    }

    /**
     * Handles mouse drag event.
     *
     * @param event
     */
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

    /**
     * Handles mouse click on canvas
     * @param event
     */
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

    /**
     * Clear editor and strip canvas
     */
    @FXML
    private void clear() {
        clearEditor();
        golEditor.clearGrid();

        clearStrip();
        golStrip.clearGrid();
    }


    /**
     * Gets editor grid position based on camera offset and mouse position on grid.
     * @param x position on editor canvas.
     * @return grid position.
     */
    private int getEditorGridPosX(double x) {
        return (int)Math.floor((x + cameraEditorView.getCommonOffsetX(golEditor, editorCell.getSize())) / editorCell.getSize());
    }

    /**
     * Gets editor grid position based on camera offset and mouse position on grid
     * @param y position on editor canvas.
     * @return grid position
     */
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
}
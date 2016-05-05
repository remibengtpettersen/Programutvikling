package s305073.controller;

import controller.MasterController;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import model.CameraView;
import model.Cell;

import model.DynamicGameOfLife;
import model.GameOfLife;

import java.awt.*;

/**
 * Created by remibengtpettersen.
 */
public class EditorController {

    @FXML private Canvas editor;
    @FXML private Canvas strip;
    @FXML private ScrollPane scrollPane;

    private Point currentMousePosition = new Point(0, 0);
    private Point2D previewsMousePosition = new Point2D(0.0, 0.0);

    private Point cameraViewGridPositionMin = new Point(0, 0); // board offset
    private Point cameraViewGridPositionMax = new Point(0, 0); // board offset
    private Point gridOffset = new Point(50, 50);

    private GraphicsContext gc;
    private CameraView cameraView = new CameraView();

    private boolean mouseOnCanvas;
    private boolean mouseDrag;

    private GameOfLife golEditor;
    private GameOfLife golStrip;

    private Cell editorCell;
    private Cell stripCell;

    private int viewPadding = 10;
    private int frameWidth = 300;
    private double lineWeight = 10;
    private int padding = 6;

    private MasterController masterController;

    public EditorController() {
        editorCell = new Cell();
        editorCell.setSize(20.0);
        editorCell.setColor(Color.GREEN);
    }

    public void init(MasterController masterController) {
        this.masterController = masterController;
    }

    public void getDeepCopyGol(GameOfLife gol) {
        this.golEditor = gol.clone();
    }

    public void setPattern() {

        // set Graphics content
        setGraphicsContentToEditor();

        // set color
        setColorToGC(Color.GREEN);

        // display alive cell on canvas
        renderEditor();
    }

    private void setColorToGC(Color color) {
        gc.setFill(color);
    }

    private void renderEditor() {
        for (int i = 0; i < golEditor.getGridWidth(); i++) {
            for (int j = 0; j < golEditor.getGridHeight(); j++) {
                if (golEditor.isCellAlive(i, j))
                    fillEditorPositionAt(i, j);
            }
        }
    }

    private void fillEditorPositionAt(int i, int j) {
        drawAt(i, j, gc);
    }

    public void drawAt(int x, int y, GraphicsContext gc) {
        gc.fillRect(getCanvasPosX(x), getCanvasPosY(y), editorCell.getSize() - editorCell.getSpacingInPixels(), editorCell.getSize() - editorCell.getSpacingInPixels());

    }

    @FXML
    private void updateStrip() {

    }

    private void setGraphicsContentToEditor() {
        gc = editor.getGraphicsContext2D();
    }

    private void setGraphicsContextToStrip() {
        gc = strip.getGraphicsContext2D();
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
        currentMousePosition.setLocation((int) event.getX(), (int) event.getY());

        // checks if left click
        if (b == MouseButton.PRIMARY) {

            // gets cell on event position
            int x = getGridPosX(currentMousePosition.getX());
            int y = getGridPosY(currentMousePosition.getY());

            // makes sure the cell is on the grid
            fitTo(x, y);

            // sets cell alive
            golEditor.setCellAlive(x = (x < 0) ? 0 : x, y = (y < 0) ? 0 : y);

            // draws the cell at (x, y)
            drawAt(x, y, gc);
        }

        // checks if right click
        if (event.getButton() == MouseButton.SECONDARY) {

            // moves board to current position
            moveBoard((int)currentMousePosition.getX(), (int)currentMousePosition.getY());
        }

        // clears and render editor canvas
        clearEditor();
        renderEditor();

        // stores current mouse position for later use
        previewsMousePosition.add(currentMousePosition.getX(), currentMousePosition.getY());
    }

    /**
     * Moves the board in the same distance and direction as it is from the previous mouse position to the current
     * mouse position
     * @param currMousePosX Current mouse X coordinate
     * @param currMousePosY Current mouse Y coordinate
     */
    private void moveBoard(int currMousePosX, int currMousePosY) {

        // checks if last mouse position is present
        if (previewsMousePosition.getX() != 0 || previewsMousePosition.getY() != 0) {

            // moves the board using the offset
            //boardOffsetX += prevMousePosX - currMousePosX;
            //boardOffsetY += prevMousePosY - currMousePosY;
            gridOffset.setLocation(
                    gridOffset.getX() + previewsMousePosition.getX() - currMousePosX,
                    gridOffset.getY() + previewsMousePosition.getY() - currMousePosY);
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

    @FXML
    private void saveToFile(ActionEvent actionEvent) {
        // to be developed...
    }

    @FXML
    private void onMouseClickedEditor(MouseEvent event) {

        // if click is end of drag, reset variables used in drag
        if (mouseDrag) {
            mouseDrag = false;
            previewsMousePosition.add(0, 0);
            //prevMousePosX = 0;
            //prevMousePosY = 0;
            return;
        }

        // gets button from event
        MouseButton mouseButton = event.getButton();

        // checks if left click
        if (mouseButton == MouseButton.PRIMARY) {

            // gets position of cell on click position
            int gridClickX = getGridPosX(event.getX());
            int gridClickY = getGridPosY(event.getY());

            // makes sure the cell is on the grid
            fitTo(gridClickX, gridClickY);

            // changes state of cell
            golEditor.changeCellState((gridClickX < 0)? 0 : gridClickX, (gridClickY < 0)? 0 : gridClickY);
        }

        clearEditor();
        renderEditor();
    }

    private void clearEditor() {
        gc.clearRect(0, 0, editor.widthProperty().doubleValue(), editor.heightProperty().doubleValue());
    }

    @FXML
    private void clear() {
        clearEditor();
        golEditor.clearGrid();
    }


    private int getGridPosX(double x) {
        return (int)Math.floor((x + cameraView.getCommonOffsetX(golEditor, editorCell.getSize())) / editorCell.getSize());
    }

    private int getGridPosY(double y) {
        return (int) Math.floor((y + cameraView.getCommonOffsetY(golEditor, editorCell.getSize())) / editorCell.getSize());
    }


    /**
     * Converts grid coordinate x to canvas coordinate x.
     *
     * @param x a x coordinate in the grid.
     * @return a x coordinate on the canvas.
     */
    private double getCanvasPosX(int x) {
        return x * editorCell.getSize() - cameraView.getCommonOffsetX(golEditor, editorCell.getSize());
    }

    /**
     * Converts grid coordinate y to canvas coordinate y.
     *
     * @param y a y coordinate in the grid.
     * @return a y coordinate on the canvas.
     */
    private double getCanvasPosY(int y) {
        return y * editorCell.getSize() - cameraView.getCommonOffsetY(golEditor, editorCell.getSize());
    }
    //endregion

    /**
     * Changes the cellSize to give the effect of zooming.
     * @param scrollEvent Event created by mouse scroll
     */

    public void onScrollEditorCanvas(ScrollEvent scrollEvent) {

        // gets exact cell position at mouse coordinates
        double absMPosXOnGrid = (cameraView.getCommonOffsetX(golEditor, editorCell.getSize()) + scrollEvent.getX()) / editorCell.getSize();
        double absMPosYOnGrid = (cameraView.getCommonOffsetY(golEditor, editorCell.getSize()) + scrollEvent.getY()) /  editorCell.getSize();

        // changes cell size
        editorCell.setSize(editorCell.getSize() * ( 1 + (scrollEvent.getDeltaY() / 150)));

        // moves the board so the mouse gets the exact same position on the board as before
        gridOffset.setLocation(
                (int) ((absMPosXOnGrid - golEditor.getOffsetX()) * editorCell.getSize() - scrollEvent.getX()),
                (int) ((absMPosYOnGrid - golEditor.getOffsetY()) * editorCell.getSize() - scrollEvent.getY())
        );

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
        previewsMousePosition.add(0, 0);
    }

    public void onMouseMovedStrip(MouseEvent event) {

    }
}
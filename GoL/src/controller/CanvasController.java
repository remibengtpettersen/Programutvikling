package controller;

import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.GameOfLife2D;

/**
 * Created by Andreas on 09.03.2016.
 */
public class CanvasController {

    private MasterController masterController;

    @FXML
    private Canvas canvas;

    public Color cellColor = Color.BLACK;        //these should be private, etc etc
    public Color backgroundColor = Color.WHITE;

    GameOfLife2D gol;
    GraphicsContext gc;

    private AnimationTimer animationTimer;
    private long timer;

    private short boardSize = 500;
    private double cellSize = 10;
    private boolean[][] grid;

    private int frameDelay = 30;
    private boolean running = true;

    int gridClickX;
    int gridClickY;

    // Is used to calculate the distance the mouse has traveled since last MouseDragEvent.
    int currMousePosX;
    int currMousePosY;
    int prevMousePosX;
    int prevMousePosY;

    MouseButton mouseButton;
    private boolean mouseDrag;
    private boolean mouseOnCanvas;



    int boardOffsetX = 50;
    int boardOffsetY = 50;


    /**
     * Sets master controller, creates an instance of the GameOfLife2D object and prepares the grid and gets the Graphics Content of the canvas.
     * Also it sets up listeners and prepare the animation. Final it launches the animation.
     */
    public void initialize(MasterController masterController) {

        this.masterController = masterController;

        gol = new GameOfLife2D(boardSize);
        grid = gol.getGrid();
        gc = canvas.getGraphicsContext2D();

        initializeListeners();
        initializeAnimation();
        startAnimation();

    }

    /**
     * Initializes the animation. The "timer" and "frameDelay" are used for having dynamic frame rate.
     */
    private void initializeAnimation() {
        animationTimer = new AnimationTimer() {

            @Override
            public void handle(long now) {

                //To control game speed
                if(now/1000000 - timer > frameDelay) {

                    if(running) {
                        gol.nextGeneration();
                        renderLife();
                    }
                    timer = now/1000000;
                }
            }
        };
    }

    /**
     * Sets up all the listeners needed for the simulation of Game of Life
     */
    private void initializeListeners() {
        canvas.setOnMouseClicked(this::mouseClick);
        canvas.setOnMouseDragged(this::mouseDrag);
        //canvas.setOnMouseMoved(this::mouseTrace);
        //canvas.setOnScroll(this::mouseScroll);
        canvas.setOnMouseExited(this::mouseCanvasExit);
        canvas.setOnMouseEntered(this::mouseCanvasEnter);

        //backgroundColorPicker.setOnAction(this::changeBackgroundColor);
        //cellColorPicker.setOnAction(this::changeCellColor);

        //master.theScene.setOnKeyPressed(this::keyPressed);




    }

    /**
     * To keep track if mouse is located on the canvas.
     * @param mouseEvent
     */
    private void mouseCanvasEnter(MouseEvent mouseEvent) {
        mouseOnCanvas = true;
    }

    /**
     * To keep track if mouse is located outside of the canvas.
     * Sets previous mouse coordinates to zero if exited.
     * @param mouseDragEvent
     */
    private void mouseCanvasExit(MouseEvent mouseDragEvent) {
        mouseOnCanvas = false;
        prevMousePosX = 0;
        prevMousePosY = 0;
    }

    /**
     * Handles mouse click on canvas. Calculates which cell is clicked on canvas.
     * Calculation based on cell size and grid offset.
     * Changes state on clicked cell.
     * If right click, simulation will pause.
     * @param mouseEvent
     */
    private void mouseClick(MouseEvent mouseEvent) {
        if(mouseDrag){
            prevMousePosX = 0;
            prevMousePosY = 0;
            mouseDrag = false;
            return;
        }

        mouseButton = mouseEvent.getButton();
        if (mouseButton == MouseButton.PRIMARY) {
            gridClickX = getGridPosX(mouseEvent.getX());
            gridClickY = getGridPosY(mouseEvent.getY());
            gol.changeCellState(gridClickX, gridClickY);
            renderLife();
        } else if (mouseEvent.getButton() == MouseButton.SECONDARY)
            running = !running;
    }

    /**
     * If left mouse button clicked and mouse dragged, will draw path on canvas.
     * If right mouse button clicked and mouse dragged, will pan the grid.
     * @param mouseEvent
     */
    private void mouseDrag(MouseEvent mouseEvent) {

        if(!mouseOnCanvas)
            return;

        MouseButton b = mouseEvent.getButton();
        mouseDrag = true;
        if(b == MouseButton.PRIMARY) {
            gridClickX = getGridPosX(mouseEvent.getX());
            gridClickY = getGridPosY(mouseEvent.getY());
            gol.setCellAlive(gridClickX, gridClickY);
        }
        else if(mouseEvent.getButton() == MouseButton.SECONDARY){
            // gets mouse coordinates on canvas.
            currMousePosX = (int)mouseEvent.getX();
            currMousePosY = (int)mouseEvent.getY();

            if(prevMousePosX != 0 || prevMousePosY != 0){
                boardOffsetX += prevMousePosX - currMousePosX;
                boardOffsetY += prevMousePosY - currMousePosY;
            }

            prevMousePosX = currMousePosX;
            prevMousePosY = currMousePosY;
            clampView();
        }

        renderLife();
    }

    /**
     * Keeps the board inside the canvas
     */
    private void clampView() {

        boardOffsetX = clamp(boardOffsetX, 0 , (int)(cellSize * boardSize - canvas.getWidth()));
        boardOffsetY = clamp(boardOffsetY, 0 , (int)(cellSize * boardSize - canvas.getHeight()));
    }

    /**
     * Keeps the val between min and max
     * @param val input to be checked
     * @param min minimum value
     * @param max maximum value
     * @return val in range of min and max
     */
    public int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    // region canvas to grid converter
    private int getGridPosX(double x) {
        return (int)((x + boardOffsetX) / cellSize);
    }

    private int getGridPosY(double y) {
        return (int)((y + boardOffsetY) / cellSize);
    }
    // endregion

    //region grid to canvas converter
    /**
     * Converts grid coordinate x to canvas coordinate x.
     * @param x a x coordinate in the grid.
     * @return a x coordinate on the canvas.
     */
    private double getCanvasPosX(int x) {
        return x * cellSize - boardOffsetX;
    }

    /**
     * Converts grid coordinate y to canvas coordinate y.
     * @param y a y coordinate in the grid.
     * @return a y coordinate on the canvas.
     */
    private double getCanvasPosY(int y) {
        return y * cellSize - boardOffsetY;
    }
    //endregion

    public void testDraw(){

        int w = canvas.widthProperty().intValue();
        int h = canvas.heightProperty().intValue();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        //gc.clearRect(0, 0, w, h);

        gc.setFill(backgroundColor);
        gc.fillRect(0, 0, w, h);        //draws background color

        gc.setStroke(Color.BLACK);
        gc.strokeRect(0, 0, w, h);      //draws a border around the entire canvas

        gc.setFill(cellColor);
        gc.fillOval(0, 0, w, h);        //draws an oval
    }

    /**
     * Renders the current state of the game of life simulation to the canvas.
     * Sets background color and cell color.
     */
    public void renderLife() {
        gc.setFill(backgroundColor);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(cellColor);
        for(int x = 0; x < boardSize; x++){
            for(int y = 0; y < boardSize; y++){

                if(grid[x][y])
                    drawCell(x, y);
            }
        }
    }

    /**
     * Draws the cell at the x, y coordinate in the grid
     * @param x The x coordinate in the game of life grid.
     * @param y The y coordinate in the game of life grid.
     */
    private void drawCell(int x, int y) {
        gc.fillRect(getCanvasPosX(x), getCanvasPosY(y), cellSize * 0.9, cellSize * 0.9);

        // TBD ....Check cycle time for different algo.
        //gc.fillRect(x * cellSize - boardOffsetX, y * cellSize - boardOffsetY, cellSize * 0.9, cellSize * 0.9);
    }


    //region Animation-controlls

    /**
     * Starts the animation
     */
    private void startAnimation() {
        animationTimer.start();
    }

    /**
     * Stops the animation
     */
    private void stopAnimation() {
        animationTimer.stop();
    }
    //endregion
}

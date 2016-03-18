package controller;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import model.GameOfLife2D;
import model.rules.Rule;

/**
 * Created by Andreas on 09.03.2016.
 */
public class CanvasController {

    private MasterController masterController;

    @FXML
    private Canvas canvas;

    private Color cellColor = Color.BLACK;        //these should be private, etc etc
    public Color backgroundColor = Color.WHITE;
    private Color ghostColor;

    GameOfLife2D gol;
    GraphicsContext gc;

    private AnimationTimer animationTimer;
    private long timer;

    private short boardSize = 1500;
    private double cellSize = 5;
    private double minCellSize;
    private boolean[][] grid;

    private int frameDelay = 1000;
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

    boolean[][] importPattern;
    boolean importing = false;



    public Canvas getCanvas(){ return canvas; }

    /**
     * Sets master controller, creates an instance of the GameOfLife2D object and prepares the grid and gets the Graphics Content of the canvas.
     * Also it sets up listeners and prepare the animation. Final it launches the animation.
     */
    public void initialize(MasterController masterController) {

        this.masterController = masterController;

        initializeGameParameters();

        gol = new GameOfLife2D(boardSize);
        grid = gol.getGrid();
        gc = canvas.getGraphicsContext2D();


        initializeListeners();
        initializeAnimation();
        startAnimation();
    }

    private void initializeGameParameters() {

        try {
            cellColor = (Color) Color.class.getField(masterController.configuration.getCellColor()).get(null);
            backgroundColor =(Color) Color.class.getField(masterController.configuration.getBackgroundColor()).get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        cellSize = masterController.configuration.getCellSize();
        frameDelay = masterController.configuration.getGameSpeed();
        boardSize = masterController.configuration.getGameSize();

        masterController.toolController.setSpeed(frameDelay);

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


                        gol.nextGeneration();

                        renderLife();

                    if(importing)
                        renderImport();


                    timer = now/1000000;
                    masterController.toolController.giveCellCount(gol.getCellCount());
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
        canvas.setOnMouseMoved(this::mouseTrace);
        canvas.setOnScroll(this::mouseScroll);
        canvas.setOnMouseExited(this::mouseCanvasExit);
        canvas.setOnMouseEntered(this::mouseCanvasEnter);

        //backgroundColorPicker.setOnAction(this::changeBackgroundColor);
        //cellColorPicker.setOnAction(this::changeCellColor);

        //master.theScene.setOnKeyPressed(this::keyPressed);

        canvas.widthProperty().addListener(evt -> {
            clampCellSize();
            clampView();
            calculateMinCellSize();
            if(!running || frameDelay > 0)
                renderLife();
        });
        canvas.heightProperty().addListener(evt -> {
            clampCellSize();
            clampView();
            calculateMinCellSize();
            if(!running || frameDelay > 0)
                renderLife();

        });


    }

    private void mouseTrace(MouseEvent mouseEvent) {
        if(importing){
            currMousePosX = (int) mouseEvent.getX();
            currMousePosY = (int) mouseEvent.getY();
            if(!running || frameDelay > 0){
                renderLife();
                renderImport();
            }
        }
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
        if (mouseDrag) {
            prevMousePosX = 0;
            prevMousePosY = 0;
            mouseDrag = false;
            return;
        }

        mouseButton = mouseEvent.getButton();
        if (mouseButton == MouseButton.PRIMARY) {
            if (importing) {
                insertImport();
                importing = false;
                return;
            }

            gridClickX = getGridPosX(mouseEvent.getX());
            gridClickY = getGridPosY(mouseEvent.getY());
            gol.changeCellState(gridClickX, gridClickY);
            renderLife();
        } else if (mouseEvent.getButton() == MouseButton.SECONDARY)
            if (running) {
                masterController.toolController.changeIconToPlay();
                stopAnimation();
                running = false;
            } else {
                masterController.toolController.changeIconToPause();
                startAnimation();
                running = true;
            }
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

        // gets mouse coordinates on canvas.
        currMousePosX = (int)mouseEvent.getX();
        currMousePosY = (int)mouseEvent.getY();

        if(b == MouseButton.PRIMARY) {

            if(prevMousePosX != 0 || prevMousePosY != 0){
                drawLine(getGridPosX(currMousePosX), getGridPosY(currMousePosY), getGridPosX(prevMousePosX), getGridPosY(prevMousePosY));

            }
            //gol.setCellAlive(gridClickX, gridClickY);
        }
        else if(mouseEvent.getButton() == MouseButton.SECONDARY){


            if(prevMousePosX != 0 || prevMousePosY != 0){
                boardOffsetX += prevMousePosX - currMousePosX;
                boardOffsetY += prevMousePosY - currMousePosY;
            }


            clampView();
        }

        prevMousePosX = currMousePosX;
        prevMousePosY = currMousePosY;

        if(!running || frameDelay > 0)
            renderLife();
    }

    /**
     * Changes the cellSize to give the effect of zooming.
     * @param scrollEvent
     */
    private void mouseScroll(ScrollEvent scrollEvent) {
        double ratio1 = (boardOffsetX + scrollEvent.getX()) / cellSize;
        double ratio2 = (boardOffsetY + scrollEvent.getY()) / cellSize;


        cellSize += cellSize * (scrollEvent.getDeltaY() / 150);

       // System.out.println(cellSize);
        clampCellSize();
        masterController.toolController.setZoom(cellSize);
        boardOffsetX = (int) (cellSize * ratio1 - scrollEvent.getX());
        boardOffsetY = (int) (cellSize * ratio2 - scrollEvent.getY());

        clampView();

        masterController.toolController.addSpeedValue(scrollEvent.getDeltaX()/5);

        System.out.println("3: "+ frameDelay);
        if(!running || frameDelay > 0)
            renderLife();
    }

    public void setCellSize(double newCellSize) {
        double ratio1 = (boardOffsetX + canvas.getWidth()/2) / cellSize;
        double ratio2 = (boardOffsetY + canvas.getHeight()/2) / cellSize;

       // System.out.println(newCellSize);
        cellSize = newCellSize;
        clampCellSize();
        boardOffsetX = (int) (cellSize * ratio1 - canvas.getWidth()/2);
        boardOffsetY = (int) (cellSize * ratio2 - canvas.getHeight()/2);
        clampView();

        if(!running || frameDelay > 0)
            renderLife();
    }
    /**
     * Keeps the cell size within it´s boundaries.
     */
    private void clampCellSize() {
        double limit = (canvas.getWidth() > canvas.getHeight()) ? canvas.getWidth() : canvas.getHeight();
        if(cellSize * boardSize < limit){
            cellSize = limit / boardSize;
        }
        else if(cellSize > 28){
            cellSize = 28;
        }
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
     * Renders the imported pattern on the canvas around the mouse position
     */
    public void renderImport() {

        gc.setFill(ghostColor);
        for (int x = 0; x < importPattern.length; x++) {
            for (int y = 0; y < importPattern[x].length; y++) {
                if (importPattern[x][y] == true) {

                    //drawGhost(x + (int) (currMousePosX / cellSize) - importPattern.length / 2 - boardOffsetX, y + (int) (currMousePosY / cellSize) - importPattern[x].length / 2 - boardOffsetY);
                    drawGhost(getGridPosX(currMousePosX)-importPattern.length / 2 + x, getGridPosY(currMousePosY) - importPattern[x].length/2 + y);

                }
            }
        }
    }

    /**
     * Inserts the imported pattern into the cell grid
     */
    private void insertImport() {

        for (int x = 0; x < importPattern.length; x++) {
            for (int y = 0; y < importPattern[x].length; y++) {
                if (importPattern[x][y] == true) {

                    //drawGhost(x + (int) (currMousePosX / cellSize) - importPattern.length / 2 - boardOffsetX, y + (int) (currMousePosY / cellSize) - importPattern[x].length / 2 - boardOffsetY);
                    gol.setCellAlive(getGridPosX(currMousePosX)-importPattern.length / 2 + x, getGridPosY(currMousePosY) - importPattern[x].length/2 + y);

                }
            }
        }
        importing = false;
    }

    public void clearGrid() {
        gol.clearGrid();
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

    private void drawGhost(int x, int y){
        gc.fillRect(getCanvasPosX(x), getCanvasPosY(y), cellSize * 0.9, cellSize * 0.9);
    }

    /**
     * creates a line of alive cells on the grid from cell (x,y) to cell (x2, y2)
     * @param x THe first coordinate of the first cell
     * @param y The second coordinate of the first cell
     * @param x2 The first coordinate of the end cell
     * @param y2 The second coordinate of the end cell
     */
    public void drawLine(int x, int y, int x2, int y2) {


        //region Bresenham Algorithm
        /*
        int w = x2 - x;
        int h = y2 - y;
        int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
        if (w < 0) dx1 = -1;
        else if (w > 0) dx1 = 1;
        if (h < 0) dy1 = -1;
        else if (h > 0) dy1 = 1;
        if (w < 0) dx2 = -1;
        else if (w > 0) dx2 = 1;
        int longest = Math.abs(w);
        int shortest = Math.abs(h);
        if (!(longest > shortest)) {
            longest = Math.abs(h);
            shortest = Math.abs(w);
            if (h < 0) dy2 = -1;
            else if (h > 0) dy2 = 1;
            dx2 = 0;
        }
        int numerator = longest >> 1;
        for (int i = 0; i <= longest; i++) {
                setAlive(x, y);
                numerator += shortest;
            if (!(numerator < longest)) {
                numerator -= longest;
                x += dx1;
                y += dy1;
            } else {
                x += dx2;
                y += dy2;
            }
        }*/
        //endregion

        int width = x2 -x;
        int height = y2 - y;

        int lineLength = Math.abs((Math.abs(width) < Math.abs(height))? height : width);

        for(int i = 0; i < lineLength; i++){
            gol.setCellAlive(x + i * width / lineLength, y + i * height / lineLength);
        }

    }


    //region Animation-controlls

    /**
     * Starts the animation
     */
    public void startAnimation() {
        animationTimer.start();
        running = true;
    }

    /**
     * Stops the animation
     */
    public void stopAnimation() {
        animationTimer.stop();
        running = false;
    }

    //endregion

    //region Setters


    public void setCellColor(Color cellColor) {
        this.cellColor = cellColor;
        calculateGhostColor();
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        calculateGhostColor();
    }

    /**
     * Calculates the color just in between the background color and the cell color
     */
    private void calculateGhostColor() {
        double blue = (cellColor.getBlue() + backgroundColor.getBlue()) / 2;
        double red = (cellColor.getRed() + backgroundColor.getRed()) / 2;
        double green = (cellColor.getGreen() + backgroundColor.getGreen()) / 2;

        ghostColor = new Color(red, green, blue, 1);
    }

    private void calculateMinCellSize() {
         minCellSize = (canvas.getWidth() > canvas.getHeight()) ? canvas.getWidth() / boardSize : canvas.getHeight() / boardSize;
        masterController.toolController.setMinZoom(minCellSize);
    }

    /**
     * Sets the pattern that is importet from a file
     * @param importPattern the pattern that is imported from a file
     */
    public void setImportPattern(boolean[][] importPattern) {
        this.importPattern = importPattern;
        if(importPattern != null) {
            importing = true;
        }
        else{
            System.out.println("ISNULL");
        }
    }

    public void setFrameDelay(int frameDelay) {
        if(frameDelay < 17) // 60 fps
            this.frameDelay = 0;
        else
            this.frameDelay = frameDelay;

        System.out.println("1: "+ frameDelay);
     //   System.out.println(frameDelay);
    }

    public void setRule(String ruleText){
        gol.setRule(ruleText);
    }




    //endregion
}

package controller;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import model.Cell;
import model.DynamicGameOfLife;
import s305080.PatternSaver.ToFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The controller that handles everything that happens on the canvas.
 * mouse controll
 * zoom controll
 * grid positoning
 * Draw gameboard on canvas
 * animation
 * keyboard handeling
 * animation speed
 * save to gif
 */
public class CanvasController {

     private List<Thread> workers = new ArrayList<Thread>();


    public DynamicGameOfLife gol;
    private MasterController masterController;
    @FXML
    private Canvas canvas;
    private GraphicsContext gc;

    private AnimationTimer animationTimer;
    private long timer;

    public Cell cell;

    private short boardWidth;
    private short boardHeight;
    private ArrayList<ArrayList<AtomicBoolean>> grid;

    private int frameDelay;
    private boolean running = true;
    public boolean busy = false;

    // Is used to calculate the distance the mouse has traveled since last MouseDragEvent.
    private int currMousePosX;
    private int currMousePosY;
    private int prevMousePosX;
    private int prevMousePosY;

    private boolean mouseDrag;
    private boolean mouseOnCanvas;

    private int boardOffsetX = 50;
    private int boardOffsetY = 50;



    private int currViewMinX;
    private int currViewMaxX;
    private int currViewMinY;
    private int currViewMaxY;

    private boolean[][] importPattern;
    private boolean importing = false;
    private boolean gridLines;
    private boolean userWantsGridLines;
    private int threads = Runtime.getRuntime().availableProcessors();
    private Thread thread;

    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * Sets master controller, creates an instance of the GameOfLife object and prepares the grid and gets the Graphics Content of the canvas.
     * Also it sets up listeners and prepare the animation. Final it launches the animation.
     */
    public void initialize(MasterController masterController) {

        this.masterController = masterController;
        initializeGameParameters();

        gol = new DynamicGameOfLife();
        grid = gol.getGrid();
        gc = canvas.getGraphicsContext2D();

        updateView();
        initializeListeners();
        initializeAnimation();
        checkIfShouldStillDrawGrid();
        masterController.getToolController().setZoom(cell.getSize());
        startAnimation();
    }

    /**
     * Gets the game parameters from the config file
     */
    private void initializeGameParameters() {

        cell = new Cell(masterController.getConfig());

        frameDelay = masterController.configuration.getGameSpeed();
        boardWidth = masterController.configuration.getGameWidth();
        boardHeight = masterController.configuration.getGameHeight();
        userWantsGridLines = masterController.configuration.isGridLinesOn();
        masterController.getToolController().setSpeed(frameDelay);

    }

    /**
     * Updates the fields that contains the grid minimum and maximum x and y values displayed on the canvas
     */
    private void updateView() {

        currViewMinX = (int) (getCommonOffsetX() / cell.getSize());
        currViewMaxX = (int) ((getCommonOffsetX() + canvas.getWidth()) / cell.getSize()) + 1;
        if (currViewMaxX > grid.size())
            currViewMaxX = grid.size();

        currViewMinY = (int)(getCommonOffsetY() / cell.getSize());
        currViewMaxY = (int) ((getCommonOffsetY() + canvas.getHeight()) / cell.getSize()) + 1;
        if (currViewMaxY > grid.get(0).size())
            currViewMaxY = grid.get(0).size();

        if (currViewMinY < 0)
            currViewMinY = 0;
        if (currViewMinX < 0)
            currViewMinX = 0;
    }

    /**
     * Initializes the animation. The "timer" and "frameDelay" are used for having dynamic frame rate.
     */
    private void initializeAnimation() {

        thread = new Thread();
        animationTimer = new AnimationTimer() {

            @Override
            public void handle(long now) {

                //To control game speed
                if (now / 1000000 - timer > frameDelay) {

                    if(!busy) {
                        gol.nextGeneration();
                        renderCanvas();
                        if(!thread.isAlive()){
                            thread = new Thread(() -> {

                            });
                            thread.start();
                        }


                        timer = now / 1000000;

                        giveCellCount();
                    }
                }
            }
        };
    }

    /**
     * Sets up all the listeners needed for the simulation of Game of Life
     */
    private void initializeListeners() {

        canvas.setOnMouseReleased(this::mouseClick);
        canvas.setOnMouseDragged(this::mouseDrag);
        canvas.setOnMouseMoved(this::mouseTrace);
        canvas.setOnScroll(this::mouseScroll);
        canvas.setOnMouseExited(this::mouseCanvasExit);
        canvas.setOnMouseEntered(this::mouseCanvasEnter);

        masterController.scene.setOnKeyPressed(this::keyPressed);

        canvas.widthProperty().addListener(evt -> fitContentToWindow());
        canvas.heightProperty().addListener(evt -> fitContentToWindow());
    }

    private void fitContentToWindow() {
        calculateMinCellSize();
        if (!running || frameDelay > 0)
            renderCanvas();
    }

    /**
     * @param keyEvent
     */
    private void keyPressed(KeyEvent keyEvent) {

        String code = keyEvent.getCode().toString();

        if (code.equals("S")) {
            busy = true;
            try {
               // saveToGif();
                throw new IOException();
            } catch (IOException e) {
                e.printStackTrace();
            }
            busy = false;
        }
        if (code.equals("D")) {

           // saveToFile();
            System.out.println(gol.getBoundingBox()[0] + " " + gol.getBoundingBox()[1] + " " + gol.getBoundingBox()[2] + " " + gol.getBoundingBox()[3] + " ");

        }
    }

    /**
     * Stores the position of the event
     *
     * @param mouseEvent Where the mouse is at the current time
     */
    private void mouseTrace(MouseEvent mouseEvent) {

        if (importing) {
            currMousePosX = (int) mouseEvent.getX();
            currMousePosY = (int) mouseEvent.getY();
            if (!running || frameDelay > 0) {
                renderCanvas();
            }
        }
    }

    /**
     * To keep track if mouse is located on the canvas.
     *
     * @param mouseEvent
     */
    private void mouseCanvasEnter(MouseEvent mouseEvent) {
        mouseOnCanvas = true;
    }

    /**
     * To keep track if mouse is located outside of the canvas.
     * Sets previous mouse coordinates to zero if exited.
     *
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
     *
     * @param mouseEvent
     */
    private void mouseClick(MouseEvent mouseEvent) {

        if (mouseDrag) {
            prevMousePosX = 0;
            prevMousePosY = 0;
            mouseDrag = false;
            return;
        }

        MouseButton mouseButton = mouseEvent.getButton();

        if (mouseButton == MouseButton.PRIMARY) {
            if (importing) {
                insertImport();
                importing = false;
                return;
            }

            int gridClickX = getGridPosX(mouseEvent.getX());
            int gridClickY = getGridPosY(mouseEvent.getY());
            System.out.println(gridClickX + " " + gridClickY);

            fitTo(gridClickX, gridClickY);

            gol.changeCellState((gridClickX < 0)? 0 : gridClickX, (gridClickY < 0)? 0 : gridClickY);
            giveCellCount();

            if (!running || frameDelay > 0)
                renderCanvas();

        } else if (mouseEvent.getButton() == MouseButton.SECONDARY)
            if (running) {
                masterController.getToolController().changeIconToPlay();
                stopAnimation();
                running = false;
            } else {
                masterController.getToolController().changeIconToPause();
                startAnimation();
                running = true;
            }
    }

    /**
     * If left mouse button clicked and mouse dragged, will draw path on canvas.
     * If right mouse button clicked and mouse dragged, will pan the grid.
     *
     * @param mouseEvent
     */
    private void mouseDrag(MouseEvent mouseEvent){

        if (!mouseOnCanvas)
            return;

        MouseButton b = mouseEvent.getButton();
        mouseDrag = true;

        // gets mouse coordinates on canvas.
        currMousePosX = (int) mouseEvent.getX();
        currMousePosY = (int) mouseEvent.getY();

        if (b == MouseButton.PRIMARY) {
            if (prevMousePosX != 0 || prevMousePosY != 0) {

                drawLine(getGridPosX(currMousePosX), getGridPosY(currMousePosY), getGridPosX(prevMousePosX), getGridPosY(prevMousePosY));

                giveCellCount();

            } else {
                int x = getGridPosX(currMousePosX);
                int y = getGridPosY(currMousePosY);
                fitTo(x, y);
                gol.setCellAlive(x =(x < 0)?0:x, y = (y < 0)? 0 : y);
                giveCellCount();
                drawCell(x, y);
            }
            } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            if (prevMousePosX != 0 || prevMousePosY != 0) {
                boardOffsetX += prevMousePosX - currMousePosX;
                boardOffsetY += prevMousePosY - currMousePosY;
            }

            if (!running || frameDelay > 0)
                renderCanvas();
        }

        prevMousePosX = currMousePosX;
        prevMousePosY = currMousePosY;


    }

    private void fitTo(int x, int y) {
        if(x < 0){
            gol.increaseXLeft(Math.abs(x));
           // boardOffsetX -= (x - 1) * cell.getSize();
        }
        if(y < 0){
            gol.increaseYTop(Math.abs(y));
            //boardOffsetY -= (y - 1) * cell.getSize();
        }

    }

    /**
     * Changes the cellSize to give the effect of zooming.
     *
     * @param scrollEvent
     */
    private void mouseScroll(ScrollEvent scrollEvent) {

        double absMPosXOnGrid = (getCommonOffsetX() + scrollEvent.getX()) / cell.getSize();
        double absMPosYOnGrid = (getCommonOffsetY() + scrollEvent.getY()) /  cell.getSize();

        cell.setSize(cell.getSize() * ( 1 + (scrollEvent.getDeltaY() / 150)));

        masterController.getToolController().setZoom( cell.getSize());

        boardOffsetX = (int) ((absMPosXOnGrid - gol.getOffsetX()) * cell.getSize() - scrollEvent.getX());
        boardOffsetY = (int) ((absMPosYOnGrid - gol.getOffsetY()) * cell.getSize() - scrollEvent.getY());


        checkIfShouldStillDrawGrid();

        masterController.getToolController().addSpeedValue(scrollEvent.getDeltaX() / 5);

        if (!running || frameDelay > 0)
            renderCanvas();
    }

    /**
     * Checks if grid lines should be displayed
     */
    private void checkIfShouldStillDrawGrid() {
        gridLines = cell.getSize() > 5;
    }


    //region Clamping


    /**
     * Keeps the val between min and max
     *
     * @param val input to be checked
     * @param min minimum value
     * @param max maximum value
     * @return val in range of min and max
     */
    private int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }
    //endregion

    // region canvas to grid converter
    private int getGridPosX(double x) {
        return (int)Math.floor((x + getCommonOffsetX()) / cell.getSize());
    }

    private int getGridPosY(double y) {
        return (int) Math.floor((y + getCommonOffsetY()) / cell.getSize());
    }
    // endregion

    //region grid to canvas converter

    /**
     * Converts grid coordinate x to canvas coordinate x.
     *
     * @param x a x coordinate in the grid.
     * @return a x coordinate on the canvas.
     */
    private double getCanvasPosX(int x) {
        return x * cell.getSize() - getCommonOffsetX();
    }

    /**
     * Converts grid coordinate y to canvas coordinate y.
     *
     * @param y a y coordinate in the grid.
     * @return a y coordinate on the canvas.
     */
    private double getCanvasPosY(int y) {
        return y * cell.getSize() - getCommonOffsetY();
    }
    //endregion

    public double getCommonOffsetX(){
         return (boardOffsetX + gol.getOffsetX() * cell.getSize());
     }
    public double getCommonOffsetY(){
        return (boardOffsetY + gol.getOffsetY() * cell.getSize());
    }


    /**
     * Renders everything on the canvas
     */
    public void renderCanvas() {

        updateView();
        renderLife();

        if (importing)
            renderImport();
        if (gridLines)
            if (userWantsGridLines)
                renderGridLines();
        //to see where the grid is
        //gc.strokeRect(-getCommonOffsetX(), -getCommonOffsetY(), grid.size() * cell.getSize(), grid.get(0).size() * cell.getSize());

    }

    /**
     * Renders the current state of the game of life simulation to the canvas.
     * Sets background color and cell color.
     */
    private void renderLife() {

        gc.setFill(cell.getDeadColor());
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(cell.getColor());
        for (int x = currViewMinX; x < currViewMaxX; x++) {
            for (int y = currViewMinY; y < currViewMaxY; y++) {

                    if (gol.isCellAlive(x, y))
                        drawCell(x, y);

            }
        }
    }

    /**
     * Draws the grid lines on the canvas
     */
    private void renderGridLines() {

        gc.setStroke(cell.getGhostColor());
        double x;
        for (double i = 0; i < canvas.getWidth(); i += cell.getSize()){
            gc.strokeLine( x = -getCommonOffsetX() % cell.getSize() + i - cell.getSize() * cell.getSpacing() /2, 0, x, canvas.getHeight());
        }
        double y;
        for (double i = 0; i < canvas.getHeight(); i += cell.getSize()){
            gc.strokeLine(0, y = -getCommonOffsetY() % cell.getSize() + i - cell.getSize() * cell.getSpacing() /2,canvas.getWidth(), y);
        }

    }

    /**
     * Renders the imported pattern on the canvas around the mouse position
     */
    private void renderImport() {

        gc.setFill(cell.getGhostColor());

        for (int x = 0; x < importPattern.length; x++) {
            for (int y = 0; y < importPattern[x].length; y++) {
                if (importPattern[x][y]) {
                    drawCell(getGridPosX(currMousePosX) - importPattern.length / 2 + x, getGridPosY(currMousePosY) - importPattern[x].length / 2 + y);
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
                if (importPattern[x][y]) {
                    int posX = getGridPosX(currMousePosX) - importPattern.length / 2 + x;
                    int posY = getGridPosY(currMousePosY) - importPattern[x].length / 2 + y;

                    if (posX < 0 || posY < 0) {
                        fitTo(posX, posY);
                        posX = getGridPosX(currMousePosX) - importPattern.length / 2 + x;
                        posY = getGridPosY(currMousePosY) - importPattern[x].length / 2 + y;
                    }
                    gol.setCellAlive(posX, posY);
                }

            }
        }
        importing = false;

        if (!running || frameDelay > 0)
            renderCanvas();
    }


    /**
     * Kills all cells on grid
     */
    public void clearGrid() {

        gol.clearGrid();
        renderCanvas();
    }

    /**
     * Draws the cell at the x, y coordinate in the grid
     *
     * @param x The x coordinate in the game of life grid.
     * @param y The y coordinate in the game of life grid.
     */
    private void drawCell(int x, int y) {
        gc.fillRect(getCanvasPosX(x), getCanvasPosY(y), cell.getSize() - cell.getSize() * cell.getSpacing(), cell.getSize() - cell.getSize() * cell.getSpacing());
    }

    /**
     * creates a line of alive cells on the grid from cell (x,y) to cell (x2, y2)
     *
     * @param x  THe first coordinate of the first cell
     * @param y  The second coordinate of the first cell
     * @param x2 The first coordinate of the end cell
     * @param y2 The second coordinate of the end cell
     */
    private void drawLine(int x, int y, int x2, int y2) {

        int ofsetX = gol.getOffsetX();
        int ofsetY = gol.getOffsetY();
        fitTo(x, y);
        ofsetX -= gol.getOffsetX();
        ofsetY -= gol.getOffsetY();

        x -= ofsetX;
        x2 -= ofsetX;
        y -= ofsetY;
        y2 -= ofsetY;

        int width = x2 - x;
        int height = y2 - y;

        int lineLength = Math.abs((Math.abs(width) < Math.abs(height)) ? height : width);
        int x1, y1;
        for (int i = 0; i < lineLength; i++) {

            fitTo(x1 = (x + i * width / lineLength), y1 = (y + i * height / lineLength));
            gol.setCellAlive(x1 =(x1 < 0) ? 0 : x1, y1 = (y1 < 0)?0:y1);
            drawCell(x1, y1);
        }
    }

    //region Animation control

    /**
     * Start animation
     */
    public void startAnimation() {

        animationTimer.start();
        running = true;
    }

    /**
     * Stop animation
     */
    public void stopAnimation() {

        animationTimer.stop();
        running = false;
    }
    //endregion

    //region Setters


    public void setBackgroundColor(Color backgroundColor) {
        cell.setDeadColor(backgroundColor);
    }



    /**
     * Gives the minimum cell size to the zoom slider
     */
    private void calculateMinCellSize() {
        double minCellSize = (canvas.getWidth() / boardWidth > canvas.getHeight() / boardHeight) ? canvas.getWidth() / boardHeight : canvas.getHeight() / boardHeight;
        masterController.getToolController().setMinZoom(minCellSize);
    }

    /**
     * Setts the cell size to be the newCellSize
     *
     * @param newCellSize the new cell size
     */
    public void setCellSize(double newCellSize) {

        double canvasCenterX = (getCommonOffsetX() + canvas.getWidth() / 2) / cell.getSize();
        double canvasCenterY = (getCommonOffsetY() + canvas.getHeight() / 2) / cell.getSize();

        cell.setSize(newCellSize);

        boardOffsetX = (int) (cell.getSize() * canvasCenterX - canvas.getWidth() / 2 - gol.getOffsetX() * cell.getSize());
        boardOffsetY = (int) (cell.getSize() * canvasCenterY - canvas.getHeight() / 2 - gol.getOffsetY() * cell.getSize());


        checkIfShouldStillDrawGrid();

        if (!running || frameDelay > 0)
            renderCanvas();
    }

    /**
     * Sets the pattern that is importet from a file
     *
     * @param importPattern the pattern that is imported from a file
     */
    public void setImportPattern(boolean[][] importPattern) {

        this.importPattern = importPattern;

        if (importPattern != null) {
            importing = true;
        }
    }

    public void setFrameDelay(int frameDelay) {

        if (frameDelay < 17) // 60 fps
            this.frameDelay = 0;
        else
            this.frameDelay = frameDelay;
    }

    public void setRule(String ruleText) {

        gol.setRule(ruleText);
    }

    private void giveCellCount() {
        masterController.getToolController().giveCellCount(gol.getCellCount());
    }

    //endregion
/*
    // region s305080 extra task
    private void saveToGif() throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose save directory");

        String path = fileChooser.showSaveDialog(masterController.stage).toString() + ".gif";

        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();

        int milliSeconds = frameDelay;

        boolean[][] gridCopy = new boolean[grid.size()][grid.get(0).size()];

        for (int i = 0; i < grid.size(); i++) {
            gridCopy[i] = (boolean[]) grid.get(i).clone();
        }

        GIFWriter gifWriter = new GIFWriter(width, height, path, milliSeconds);

        int gifWidth = currViewMaxX - currViewMinX;
        int gifHeight = currViewMaxY - currViewMinY;

        for (int i = 0; i < 100; i++) {
            gifWriter.fillRect(0, width - 1, 0, height - 1, new java.awt.Color((int) (255 * cell.getDeadColor().getRed()), (int) (255 * cell.getDeadColor().getGreen()), (int) (255 * cell.getDeadColor().getBlue())));
            for (int x = 0; x < gifWidth - 1; x++) {
                for (int y = 0; y < gifHeight - 1; y++) {
                    if (grid.get(x + currViewMinX).get(y + currViewMinY)) {
                        gifWriter.fillRect(
                                x * width / gifWidth, (x + 1) * width / gifWidth,
                                y * height / gifHeight, (y + 1) * height / gifHeight,
                                new java.awt.Color(
                                        (int) (255 * cell.getColor().getRed()),
                                        (int) (255 * cell.getColor().getGreen()),
                                        (int) (255 * cell.getColor().getBlue())));
                    }
                }
            }

            gol.nextGeneration();


            gifWriter.insertAndProceed();
        }

        gifWriter.close();

        for (int i = 0; i < grid.size(); i++) {
            grid.set(i, gridCopy.get(i));
        }

        System.out.println("Done");
    }*/

    void saveToFile() {
        busy = true;
        new ToFile().writeToFile(gol, masterController.stage);
        busy = false;
    }

    //endregion



    //region getters
    public int getCurrViewMinX() {
        return currViewMinX;
    }

    public int getCurrViewMaxX() {
        return currViewMaxX;
    }

    public int getCurrViewMinY() {
        return currViewMinY;
    }

    public int getCurrViewMaxY() {
        return currViewMaxY;
    }

    public Cell getCell() {
        return cell;
    }
    //endregion
}
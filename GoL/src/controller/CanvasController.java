package controller;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import model.Cell;
import model.DynamicGameOfLife;
import model.GameOfLife;
import s305080.Gif.GifSaver;
import s305080.PatternSaver.ToFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public GameOfLife gol;
    private MasterController masterController;
    @FXML
    private Canvas canvas;
    private GraphicsContext gc;

    private AnimationTimer animationTimer;
    private long timer;

    public Cell cell;

    private short boardWidth;
    private short boardHeight;

    // controlls the framerate
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

    // tracks board position
    private int boardOffsetX = 50;
    private int boardOffsetY = 50;

    // tracks cells in view.
    private int currViewMinX;
    private int currViewMaxX;
    private int currViewMinY;
    private int currViewMaxY;

    // holds pattern to be imported
    private boolean[][] clipBoardPattern;
    private boolean importing = false;

    // whether or not to draw grid lines
    private boolean gridLines;
    private boolean userWantsGridLines;

    private Thread thread;

    //region s305080

    // bounding box for selected area
    double [] markup;

    //list of buttons being pressed right now
    private List<String> buttonsPressed;

    //endregion

    /**
     * Sets master controller, creates an instance of the StaticGameOfLife object and prepares the grid and gets the Graphics Content of the canvas.
     * Also it sets up listeners and prepare the animation. Final it launches the animation.
     * @param masterController
     */
    public void initialize(MasterController masterController) {

        this.masterController = masterController;

        initializeGameParameters();

        //gol = new StaticGameOfLife(1000,1000);
        gol = new DynamicGameOfLife();

        gc = canvas.getGraphicsContext2D();
        buttonsPressed = new ArrayList<>();

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

        // gets parameters from config gile
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
        if (currViewMaxX > gol.getGridWidth())
            currViewMaxX = gol.getGridWidth();

        currViewMinY = (int)(getCommonOffsetY() / cell.getSize());
        currViewMaxY = (int) ((getCommonOffsetY() + canvas.getHeight()) / cell.getSize()) + 1;
        if (currViewMaxY > gol.getGridHeight())
            currViewMaxY = gol.getGridHeight();

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

                        double timer = System.currentTimeMillis();
                        gol.nextGeneration();
                        System.out.println(System.currentTimeMillis() - timer);

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

        // sets up listeners for mouse actions on canvas
        canvas.setOnMouseReleased(this::mouseClick);
        canvas.setOnMouseDragged(this::mouseDrag);
        canvas.setOnMouseMoved(this::mouseTrace);
        canvas.setOnScroll(this::mouseScroll);
        canvas.setOnMouseExited(this::mouseCanvasExit);
        canvas.setOnMouseEntered(this::mouseCanvasEnter);

        //sets up keyboard listeners
        masterController.scene.setOnKeyPressed(this::keyPressed);
        masterController.scene.setOnKeyReleased(this::keyReleased);

        // binds the canvas to the stage size
        canvas.widthProperty().addListener(evt -> canvasFollowWindow());
        canvas.heightProperty().addListener(evt -> canvasFollowWindow());
    }

    /**
     * Called when the stage changes size
     */
    private void canvasFollowWindow() {
        if (!running || frameDelay > 0)
            renderCanvas();
    }

    /**
     * Called when key is pressed
     * @param keyEvent Contains information about which key is pressed
     */
    private void keyPressed(KeyEvent keyEvent) {

        // stores the key code
        String code = keyEvent.getCode().toString();

        // checks if key is already in list
        if (!buttonsPressed.contains(code)){

            // adds key in key list
            buttonsPressed.add(code);
        }
        if (code.equals("S")) {
            saveToGif();
        }

        // checks if "C" is pressed
        if (code.equals("C")) {

            // checks if "CONTROL" or "COMMAND" is held
            if (buttonsPressed.contains("CONTROL") || buttonsPressed.contains("COMMAND")){
                copyMarkedArea();
            }
        }
        // checks if "X" is pressed
        else if (code.equals("X")) {

            // checks if "CONTROL" or "COMMAND" is held
            if (buttonsPressed.contains("CONTROL") || buttonsPressed.contains("COMMAND")){
                cutMarkedArea();
            }
        }
        // checks if "V" is pressed
        else if (code.equals("V")){

            // checks if "CONTROL" or "COMMAND" is held
            if (buttonsPressed.contains("CONTROL") || buttonsPressed.contains("COMMAND")){
                pasteClipBoard();
            }
        }
    }


    /**
     * Called when key is released
     * @param keyEvent Contains information about which key is released
     */
    private void keyReleased(KeyEvent keyEvent) {

        // stores the key code
        String code = keyEvent.getCode().toString();

        // checks if list contains code
        if (buttonsPressed.contains(code)){
            // removes code from list
            buttonsPressed.remove(code);
        }
    }

    /**
     * Stores the position of the event
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
            moveBoard(currMousePosX, currMousePosY);
            if (!running || frameDelay > 0)
                renderCanvas();
        }

        prevMousePosX = currMousePosX;
        prevMousePosY = currMousePosY;


    }

    private void moveBoard(int currMousePosX, int currMousePosY) {

        if (prevMousePosX != 0 || prevMousePosY != 0) {
            boardOffsetX += prevMousePosX - currMousePosX;
            boardOffsetY += prevMousePosY - currMousePosY;
        }
    }

    private void fitTo(int x, int y) {
        if(x < 0){
            ((DynamicGameOfLife) gol).increaseXLeft(Math.abs(x));
           // boardOffsetX -= (x - 1) * cell.getSize();
        }
        if(y < 0){
            ((DynamicGameOfLife)gol).increaseYTop(Math.abs(y));
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

        if (markup != null){
            renderMarkup();
        }
        //to see where the grid is
        gc.strokeRect(-getCommonOffsetX(), -getCommonOffsetY(), gol.getGridWidth() * cell.getSize(), gol.getGridHeight() * cell.getSize());

    }



    private void drawMarkup(int x, int y) {
        gc.fillRect(x * cell.getSize() - boardOffsetX, y * cell.getSize() - boardOffsetY, cell.getSize() - cell.getSize() * cell.getSpacing(), cell.getSize() - cell.getSize() * cell.getSpacing());
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

        for (int x = 0; x < clipBoardPattern.length; x++) {
            for (int y = 0; y < clipBoardPattern[x].length; y++) {
                if (clipBoardPattern[x][y]) {
                    drawCell(getGridPosX(currMousePosX) - clipBoardPattern.length / 2 + x, getGridPosY(currMousePosY) - clipBoardPattern[x].length / 2 + y);
                }
            }
        }
    }

    /**
     * Inserts the imported pattern into the cell grid
     */
    private void insertImport() {

        for (int x = 0; x < clipBoardPattern.length; x++) {
            for (int y = 0; y < clipBoardPattern[x].length; y++) {
                if (clipBoardPattern[x][y]) {
                    int posX = getGridPosX(currMousePosX) - clipBoardPattern.length / 2 + x;
                    int posY = getGridPosY(currMousePosY) - clipBoardPattern[x].length / 2 + y;

                    if (posX < 0 || posY < 0) {
                        fitTo(posX, posY);
                        posX = getGridPosX(currMousePosX) - clipBoardPattern.length / 2 + x;
                        posY = getGridPosY(currMousePosY) - clipBoardPattern[x].length / 2 + y;
                    }
                    gol.setCellAlive(posX, posY);
                }

            }
        }
        importing = false;
        giveCellCount();

        if (!running || frameDelay > 0)
            renderCanvas();
    }


    /**
     * Kills all cells on grid
     */
    public void clearGrid() {

        gol.clearGrid();
        giveCellCount();
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
     * @param clipBoardPattern the pattern that is imported from a file
     */
    public void setClipBoardPattern(boolean[][] clipBoardPattern) {

        this.clipBoardPattern = clipBoardPattern;

        if (clipBoardPattern != null) {
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

    //region getters
    public Canvas getCanvas() {
        return canvas;
    }

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

    // region s305080 extra task

    private void saveToGif(){
        try {
            new GifSaver().saveToGifBeta(masterController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void saveToFile() {
        busy = true;
        new ToFile().writeToFile(gol, masterController.stage);
        busy = false;
    }


    public void activateMarkup() {
        canvas.setOnMouseDragged(this::mouseDragMarkup);
    }

    public void deactivateMarkup() {
        canvas.setOnMouseDragged(this::mouseDrag);
        removeMarkedArea();
    }

    private void renderMarkup() {

        gc.setStroke(Color.RED);
        gc.setLineWidth(4);

        gc.strokeRect(getCanvasPosXMarkup(Math.floor((markup[0] < markup[2])?markup[0]:markup[2])) - cell.getSize() * cell.getSpacing() / 2,
                getCanvasPosYMarkup(Math.floor((markup[1] < markup[3])?markup[1]:markup[3])) - cell.getSize() * cell.getSpacing() / 2,
                (Math.abs(Math.floor(markup[0]) - Math.floor(markup[2])) + 1) * cell.getSize(),
                (Math.abs(Math.floor(markup[1]) - Math.floor(markup[3])) + 1) * cell.getSize());

        gc.setLineWidth(1);



        //getCanvasPosX(markup[1])
        //getCanvasPosY(markup[3])
    }
    private double getCanvasPosXMarkup(double x) {
        return x * cell.getSize() - boardOffsetX;
    }

    private double getCanvasPosYMarkup(double y) {
        return y * cell.getSize() - boardOffsetY;
    }

    private void mouseDragMarkup(MouseEvent mouseEvent){


        if (!mouseOnCanvas)
            return;

        MouseButton b = mouseEvent.getButton();



        // gets mouse coordinates on canvas.
        currMousePosX = (int) mouseEvent.getX();
        currMousePosY = (int) mouseEvent.getY();

        if (b == MouseButton.PRIMARY) {
            if (markup == null){
                markup = new double[4];
            }
            if(!mouseDrag){
                markup[0] = (mouseEvent.getX() + boardOffsetX) / cell.getSize();
                markup[1] = (mouseEvent.getY() + boardOffsetY) / cell.getSize();
                markup[2] = (mouseEvent.getX() + boardOffsetX) / cell.getSize();
                markup[3] = (mouseEvent.getY() + boardOffsetY) / cell.getSize();
            }
            else{
                markup[2] = (mouseEvent.getX() + boardOffsetX) / cell.getSize();
                markup[3] = (mouseEvent.getY() + boardOffsetY) / cell.getSize();
            }
        } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            moveBoard(currMousePosX, currMousePosY);
        }
        if (!running || frameDelay > 0)
            renderCanvas();
        prevMousePosX = currMousePosX;
        prevMousePosY = currMousePosY;

        mouseDrag = true;
    }

    public void pasteClipBoard() {
        if (clipBoardPattern != null){
            importing = true;
        }
    }

    public void copyMarkedArea() {
        if (markup == null){
            return;
        }

        int [] cornerCells = getCornerCells();

        boolean[][] clipboard = new boolean[1 + cornerCells[2] - cornerCells[0]][1 + cornerCells[3] - cornerCells[1]];

        int cX = 0, cY = 0;
        for (int x = cornerCells[0]; x <=  cornerCells[2]; x++) {
            for (int y = cornerCells[1]; y <= cornerCells[3]; y++) {
                try{
                    if (gol.isCellAlive(x, y)){
                        clipboard[cX][cY] = true;
                    }
                }
                catch (IndexOutOfBoundsException e){

                }
                cY++;
            }
            cX++;
            cY = 0;
        }

        clipBoardPattern = clipboard;

        if (!running || frameDelay > 0)
            renderCanvas();


    }

    public void cutMarkedArea() {

        if (markup == null){
            return;
        }
        int [] cornerCells = getCornerCells();

        boolean[][] clipboard = new boolean[1 + cornerCells[2] - cornerCells[0]][1 + cornerCells[3] - cornerCells[1]];

        int cX = 0, cY = 0;
        for (int x = cornerCells[0]; x <=  cornerCells[2]; x++) {
            for (int y = cornerCells[1]; y <= cornerCells[3]; y++) {
                try{
                    if (gol.isCellAlive(x, y)){
                        clipboard[cX][cY] = true;
                    }
                    gol.setCellDead(x, y);
                }
                catch (IndexOutOfBoundsException e){

                }
                cY++;
            }
            cX++;
            cY = 0;
        }

        clipBoardPattern = clipboard;
        if (!running || frameDelay > 0)
            renderCanvas();

    }
    public int[] getCornerCells() {
        int [] cornerCells = new int [4];
        cornerCells[0] = getGridPosX(getCanvasPosXMarkup((markup[0] < markup[2]) ? markup[0] : markup[2]));
        cornerCells[1] = getGridPosY(getCanvasPosYMarkup((markup[1] < markup[3]) ? markup[1] : markup[3]));
        cornerCells[2] = getGridPosX(getCanvasPosXMarkup((markup[0] > markup[2]) ? markup[0] : markup[2]));
        cornerCells[3] = getGridPosY(getCanvasPosYMarkup((markup[1] > markup[3]) ? markup[1] : markup[3]));
        return cornerCells;
    }

    private void removeMarkedArea() {
        markup = null;
    }


    //endregion

}
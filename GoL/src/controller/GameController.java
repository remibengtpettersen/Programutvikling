package controller;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.GameOfLife2D;

/**
 * Created by remibengtpettersen on 12.02.2016.
 */
public class GameController{

    MasterController master;
    GameOfLife2D gol;
    GraphicsContext gc;

    //region FXML-properties
    @FXML
    Canvas canvas;
    @FXML
    Slider speedSlider;
    @FXML
    Slider zoomSlider;
    @FXML
    ColorPicker cellColorPicker;
    @FXML
    ColorPicker backgroundColorPicker;
    @FXML
    ChoiceBox ruleChoiceBox;
    @FXML
    Button saveBtn;
    @FXML
    Button loadBtn;
    //endregion

    private AnimationTimer animationTimer;
    private long timer;


    private short boardSize = 500;
    private double cellSize = 10;
    private boolean[][] grid;

    private Color backgroundColor = Color.WHITE;
    private Color cellColor = Color.BLACK;
    private int frameDelay = 30;


    /**
     * To pass reference between controllers
     * @param master
     */
    public void setMaster(MasterController master) {
        this.master = master;
    }


    /**
     * Creates an instance of the GameOfLife2D object and prepares the grid and gets the Graphics Content of the canvas.
     * Also it sets up listeners and prepare the animation. Final it launches the animation.
     */
    void initialize() {

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

                    gol.nextGeneration();

                    renderLife();
                   

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
        canvas.setOnMouseMoved(this::mouseTrace);

        backgroundColorPicker.setOnAction(this::changeBackgroundColor);
        cellColorPicker.setOnAction(this::changeCellColor);

        master.theScene.setOnKeyPressed(this::keyPressed);
        
    }

    private void changeBackgroundColor(ActionEvent event) {
    }

    private void changeCellColor(ActionEvent event) {
    }


    private void keyPressed(KeyEvent keyEvent) {
       System.out.println(keyEvent.getCode());
    }

    private void mouseTrace(MouseEvent mouseEvent) {
    }

    private void mouseDrag(MouseEvent mouseEvent) {
    }

    private void mouseClick(MouseEvent mouseEvent) {
    }

    /**
     * Renders the current state of the game of life simulation to the canvas.
     * Sets background color and cell color.
     */
    private void renderLife() {
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
     * Draws the cell at the x, y position in the grid
     * @param x
     * @param y
     */
    private void drawCell(int x, int y) {
        gc.fillRect(x * cellSize, y * cellSize, cellSize * 0.9, cellSize * 0.9);
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

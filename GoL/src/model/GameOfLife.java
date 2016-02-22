package model;

import javafx.animation.AnimationTimer;

/**
 * Created by remibengtpettersen on 12.02.2016.
 */
public class GameOfLife {

    private boolean[][] grid;
    private byte[][] neighbours;

    private AnimationTimer animationTimer;

    long last;

    public GameOfLife(int gameSize) {

        createGameBoard(gameSize);
        initializeAnimation();
        startAnimation();
    }




    //region startup-sequence
    /**
     * Creates the boolean 2D Array to keep track of dead and alivelive cells, and the 2D byte-
     * array to keep track of the neighbourcount to the corresponding cells in the other array
     * @param gameSize
     */
    public void createGameBoard(int gameSize) {
        grid = new boolean[gameSize][gameSize];
        neighbours = new byte[gameSize][gameSize];
    }
    private void initializeAnimation() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(now/1000000 - last > 500) {
                    System.out.println((now/1000000)+"\n"+System.currentTimeMillis());
                    last = now/1000000;
                }


            }
        };
    }

    //endregion

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

    //region Getters
    /**
     * Getter for neighbour-2D-array
     * @return
     */
    public byte[][] getNeighbours() {
        return neighbours;
    }

    /**
     * Getter for the cell-2D-array
     * @return
     */
    public boolean[][] getGrid() {
        return grid;
    }
    //endregion


}

package model;

import javafx.animation.AnimationTimer;

/**
 * Created by remibengtpettersen on 12.02.2016.
 */
public class GameOfLife {

    private boolean[][] grid;
    private byte[][] neighbours;

    Rule rule = new ClassicRule();
    


    public GameOfLife(int gameSize) {

        createGameBoard(gameSize);
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

        grid[3][1] = true;
        grid[3][2] = true;
        grid[3][3] = true;
        grid[2][3] = true;
        grid[1][2] = true;

    }

    //endregion

    //region NextGeneration
    /**
     * Evolves the game board one generation ahead.
     */
    public void nextGeneration(){
        aggregateNeighbours();
        evolve();
    }

    /**
     * For each alive cell, it increments the adjacent cells neighbour count.
     */
    private void aggregateNeighbours() {

        for(int x = 1; x < grid.length -1; x++){

            for(int y = 1; y < grid[x].length -1; y++){

                if(grid[x][y]){
                    for(int a = x-1; a <= x+1; a++){

                        for(int b = y - 1; b <= y+1; b++){

                            if( a != x || b != y){
                                neighbours[a][b]++;
                            }
                        }
                    }

                }

            }
        }
    }

    /**
     * Given the number of neighbours for each cell, decides if it should live or die.
     */
    private void evolve() {

        for(int x = 0; x < grid.length; x++){
            for(int y = 0; y < grid[x].length; y++){

                grid[x][y] = rule.evolve(neighbours[x][y], grid[x][y]);

                neighbours[x][y] = 0;
            }
        }
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

    //region Setters
    public void setGrid(boolean[][] grid) {
        this.grid = grid;
    }

    //endregion



}

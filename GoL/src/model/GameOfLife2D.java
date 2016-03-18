package model;

import model.rules.ClassicRule;

/**
 * Created on 12.02.2016.
 * @Author The group through pair programing.
 */
public class GameOfLife2D extends GameOfLife{

   private boolean[][] grid;
   private byte[][] neighbours;


    public GameOfLife2D(int width, int height) {

        super(width, height);

        rule = new ClassicRule(grid, neighbours);
    }

    //region startup-sequence
    /**
     * Creates the boolean 2D Array to keep track of dead and alivelive cells, and the 2D byte-
     * array to keep track of the neighbourcount to the corresponding cells in the other array
     */
    @Override
    public void createGameBoard(int width, int height) {
        grid = new boolean[width][height];
        neighbours = new byte[width][height];

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
    @Override
    public void nextGeneration(){
        aggregateNeighbours();

        rule.evolve();

    }

    /**
     * For each alive cell, it increments the adjacent cells neighbour count.
     */
    @Override
    public void aggregateNeighbours() {
        cellCount=0;
        for(int x = 1; x < grid.length -1; x++){
            for(int y = 1; y < grid[x].length -1; y++){

                if(grid[x][y]){
                    cellCount++;
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

    //region Getters
    /**
     * Getter for neighbour-2D-array
     * @return
     */
    @Override
    public byte[][] getNeighbours() {
        return neighbours;
    }

    /**
     * Getter for the cell-2D-array
     * @return
     */
    @Override
    public boolean[][] getGrid() {
        return grid;
    }

   
    //endregion

    //region Setters
    @Override
    public void setGrid(boolean[][] grid) {
        this.grid = grid;
    }

    /**
     * Will set cell state to true regardless of current state.
     * @param x the x coordinate in the grid.
     * @param y the y coordinate in the grid.
     */
    public void setCellAlive(int x, int y){
        grid[x][y] = true;
    }

    /**
     * Will set cell state to false regardless of current state.
     * @param x the x coordinate in the grid.
     * @param y the y coordinate in the grid.
     */
    public void setCellDead(int x, int y){
        grid[x][y] = false;
    }

    /**
     * Changes the state of a cell based on the grid coordinate.
     * @param x the x coordinate in the grid.
     * @param y the y coordinate in the grid.
     */
    public void changeCellState(int x, int y) {
        grid[x][y] = !grid[x][y];
    }

    public void createNeighboursGrid() {
        neighbours = new byte[grid.length][grid[0].length];
    }

    public void updateRuleGrid() {
        rule.setGrid(grid);
        rule.setNeighbours(neighbours);
    }

    //endregion




}

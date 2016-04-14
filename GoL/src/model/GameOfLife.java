package model;

import model.rules.ClassicRule;
import model.rules.CustomRule;
import model.rules.HighLifeRule;
import model.rules.Rule;

import java.util.Arrays;

/**
 * Created on 12.02.2016.
 * @author The group through pair programing.
 */
public class GameOfLife {

    private boolean[][] grid;
    private byte[][] neighbours;

    private Rule rule;
    private int cellCount = 0;

    /**
     * GameOfLife Constructor. Sets the classic Conway rule (B3/S23) as default rule.
     * @param width
     * @param height
     */
    public GameOfLife(int width, int height) {

        createGameBoard(width, height);
        setRule("classic");
    }

    //region startup-sequence
    /**
     * Creates the boolean 2D Array to keep track of dead and live cells, and the 2D byte-
     * array to keep track of the neighbour count to the corresponding cells in the other array
     */
    private void createGameBoard(int width, int height) {
        grid = new boolean[width][height];
        neighbours = new byte[width][height];
    }
    //endregion

    //region NextGeneration
    /**
     * Evolves the grid one generation
     */
    public void nextGeneration(){
        aggregateNeighbours();

        try {
            rule.evolve();
        } catch (EvolveException e) {
            e.printStackTrace();
        }
    };

    /**
     * For each alive cell, it increments the adjacent cells neighbour count.
     * Also calculates the live cell count
     */
    public void aggregateNeighbours() {
        cellCount=0;
        for(int x = 1; x < grid.length - 1; x++){
            for(int y = 1; y < grid[x].length - 1; y++){
                if(grid[x][y]){
                    cellCount++;
                    for(int a = x - 1; a <= x + 1; a++){
                        for(int b = y - 1; b <= y + 1; b++){
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
     * @return The neighbour-2D-array
     */
    public byte[][] getNeighbours() {
        return neighbours;
    }

    /**
     * Getter for the cell-2D-array
     * @return The cell-2D-array
     */
    public boolean[][] getGrid() {
        return grid;
    }

    /**
     * Returns the number of live cells in grid
     * @return The live cell count
     */
    public int getCellCount() {
        return cellCount;
    }

    /**
     * Returns the rule used for evolution
     * @return The rule
     */
    public Rule getRule() {
        return rule;
    }
    //endregion

    //region Setters
    /**
     * Sets the cell grid to be used
     * @param grid Cell grid
     */
    public void setGrid(boolean[][] grid) { this.grid = grid; }

    /**
     * Sets a specific rule to be used.
     * @param ruleText The rule text
     */
    public void setRule(String ruleText) {

        ruleText = ruleText.toLowerCase();

        switch(ruleText){
            case "classic":
                rule = new ClassicRule(grid, neighbours);
                break;
            case "highlife":
                rule = new HighLifeRule(grid, neighbours);
                break;
            default:
                rule = new CustomRule(grid, neighbours, ruleText);
                System.out.println("Custom");
        }
    }

    /**
     * Sets cell state to true regardless of current state.
     * @param x the x coordinate in the grid.
     * @param y the y coordinate in the grid.
     */
    public void setCellAlive(int x, int y){
        grid[x][y] = true;
    }

    /**
     * Changes the state of a cell based on the grid coordinate.
     * @param x the x coordinate in the grid.
     * @param y the y coordinate in the grid.
     */
    public void changeCellState(int x, int y) {
        grid[x][y] = !grid[x][y];
    }

    /**
     * Creates a new neighbour grid if a cell grid is already set.
     * The neighbour grid will get the same dimensions as the cell grid
     * If a cell grid is not yet set, use createGameBoard() instead.
     */
    public void createNeighboursGrid() {
        neighbours = new byte[grid.length][grid[0].length];
    }

    /**
     * Clears the grid of live cells
     */
    public void clearGrid(){

        for(int i = 0; i < grid.length; i++){
            Arrays.fill(grid[i], false);
            Arrays.fill(neighbours[i], (byte)0);
        }
    }

    /**
     * Updates the rule's references to this class' cell grid and neighbour grid
     */
    public void updateRuleGrid() {
        rule.setGrid(grid);
        rule.setNeighbours(neighbours);
    }
    //endregion
}
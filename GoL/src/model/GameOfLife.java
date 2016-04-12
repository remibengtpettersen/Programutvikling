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

    public Rule rule;
    public int cellCount = 0;

    public GameOfLife(int width, int height) {

        createGameBoard(width, height);
        setRule("classic");
    }

    //region startup-sequence
    /**
     * Creates the boolean 2D Array to keep track of dead and alivelive cells, and the 2D byte-
     * array to keep track of the neighbourcount to the corresponding cells in the other array
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
        rule.evolve();
    };

    /**
     * For each alive cell, it increments the adjacent cells neighbour count.
     */
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
     * @return the neighbour-2D-array
     */
    public byte[][] getNeighbours() {
        return neighbours;
    }

    /**
     * Getter for the cell-2D-array
     * @return the cell-2D-array
     */
    public boolean[][] getGrid() {
        return grid;
    }

    /**
     * Returns the cell count
     * @return the cell count
     */
    public int getCellCount() {
        return cellCount;
    }

    /**
     * Returns the Rule
     * @return the rule
     */
    public Rule getRule() {
        return rule;
    }
    //endregion

    //region Setters

    public void setGrid(boolean[][] grid) {
        this.grid = grid;
    }

    /**
     * Should be used to set a spesific rule
     * @param ruleText the rule
     */
    public void setRule(String ruleText) {

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

    /**
     * Should be used to clear the grid of live cells
     */
    public void clearGrid(){

        for(int i = 0; i < grid.length; i++){
            Arrays.fill(grid[i], false);
            Arrays.fill(neighbours[i], (byte)0);
        }
    }

    public void updateRuleGrid() {
        rule.setGrid(grid);
        rule.setNeighbours(neighbours);
    }
    //endregion
}

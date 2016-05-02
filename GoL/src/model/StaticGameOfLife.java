package model;

import model.rules.ClassicRule;
import model.rules.CustomRule;
import model.rules.HighLifeRule;
import model.rules.Rule;

import java.util.Arrays;

/**
 * Created on 12.02.2016.
 *
 * @author The group through pair programing.
 */
@Deprecated
public class StaticGameOfLife extends GameOfLife{

    private boolean[][] grid;
    private byte[][] neighbours;

    private Rule rule;
    private int cellCount = 0;

    /**
     * StaticGameOfLife Constructor. Sets the classic Conway rule (B3/S23) as default rule.
     *
     * @param width
     * @param height
     */
    public StaticGameOfLife(int width, int height) {

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
    public void nextGeneration() {
        aggregateNeighbours();

        try {
           rule.evolve(0,1);
        } catch (EvolveException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fitBoardToPattern() {

    }

    ;

    /**
     * For each alive cell, it increments the adjacent cells neighbour count.
     * Also calculates the live cell count
     */
    public void aggregateNeighbours() {
        cellCount = 0;
        for (int x = 1; x < grid.length - 1; x++) {
            for (int y = 1; y < grid[x].length - 1; y++) {
                if (grid[x][y]) {
                    cellCount++;
                    for (int a = x - 1; a <= x + 1; a++) {
                        for (int b = y - 1; b <= y + 1; b++) {
                            if (a != x || b != y) {
                                neighbours[a][b]++;
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Clones the StaticGameOfLife object
     * @return the cloned StaticGameOfLife object
     */
    @Override
    public StaticGameOfLife clone() {

        StaticGameOfLife staticGameOfLife = new StaticGameOfLife(getGrid().length, getGrid()[0].length);
        staticGameOfLife.deepCopyOnSet(grid);
        staticGameOfLife.setRule(getRule().toString());
        staticGameOfLife.setCellCount(cellCount);

        //staticGameOfLife.setCell(getCell());

        return staticGameOfLife;
    }

    /**
     * Deep copies the grid and sets it.
     * @param grid the grid to be deep copied and set.
     */
    public void deepCopyOnSet(boolean[][] grid) {

        boolean[][] copiedBoard = new boolean[grid.length][grid.length];
        neighbours = new byte[grid.length][grid.length];

        for(int i = 0; i < grid.length; i++){
            copiedBoard[i] = grid[i].clone();
        }
        this.grid = copiedBoard;
        cellCount = 0;
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                if(grid[x][y]){
                    cellCount++;
                }
            }
        }
    }

    //region Getters

    /**
     * Getter for neighbour-2D-array
     *
     * @return The neighbour-2D-array
     */
    public byte[][] getNeighbours() {
        return neighbours;
    }

    /**
     * Getter for the cell-2D-array
     *
     * @return The cell-2D-array
     */
    public boolean[][] getGrid() {
        return grid;
    }

    /**
     * Returns the number of live cells in grid
     *
     * @return The live cell count
     */
    public int getCellCount() {
        return cellCount;
    }

    /**
     * Returns the rule used for evolution
     *
     * @return The rule
     */
    public Rule getRule() {
        return rule;
    }

    /**
     * We copied getBoundingBox from the assignment
     *
     * @return
     */
    public int[] getBoundingBox() {

        int[] boundingBox = new int[4]; // minrow maxrow mincolumn maxcolumn boundingBox[0] = board.length;

        boundingBox[0] = grid.length;
        boundingBox[1] = 0;
        boundingBox[2] = grid[0].length;
        boundingBox[3] = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (!grid[i][j]) continue;
                if (i < boundingBox[0]) {
                    boundingBox[0] = i;
                }
                if (i > boundingBox[1]) {
                    boundingBox[1] = i;
                }
                if (j < boundingBox[2]) {
                    boundingBox[2] = j;
                }
                if (j > boundingBox[3]) {
                    boundingBox[3] = j;
                }
            }
        }
        return boundingBox;
    }

    @Override
    public int getGridWidth() {
        return 0;
    }

    @Override
    public int getGridHeight() {
        return 0;
    }

    @Override
    public int getNeighboursAt(int x, int y) {
        return 0;
    }

    @Override
    public boolean isCellAlive(int x, int y) {
        return false;
    }

    public boolean[][] getPatternFromGrid() {
        int[] pattern = getBoundingBox();

        int startPoint_x = pattern[0];
        int endPoint_x = pattern[1] + 1;
        int startPoint_y = pattern[2];
        int endPoint_y = pattern[3] + 1;

        int length_x = endPoint_x - startPoint_x;
        int length_y = endPoint_y - startPoint_y;

        boolean[][] gridExtracted = new boolean[length_x][length_y];

        int k = 0, t = 0;

        for (int i = startPoint_x; i < startPoint_x + length_x; i++) {
            for (int j = startPoint_y; j < startPoint_y + length_y; j++) {
                if (getGrid()[i][j]) {
                    gridExtracted[k][t] = getGrid()[i][j];
                }
                t++;
            }
            k++;
            t = 0;
        }

        return gridExtracted;
    }
    //endregion

    //region Setters

    /**
     * Sets the cell grid to be used
     *
     * @param grid Cell grid
     */
    public void setGrid(boolean[][] grid) {
        this.grid = grid;
    }

    /**
     * Sets a specific rule to be used.
     *
     * @param ruleText The rule text
     */
    public void setRule(String ruleText) {

        ruleText = ruleText.toLowerCase();

        switch (ruleText) {
            case "classic":
                rule = new ClassicRule(this);
                break;
            case "highlife":
                rule = new HighLifeRule(this);
                break;
            default:
                rule = new CustomRule(this, ruleText);
                System.out.println("Custom");
        }
    }

    /**
     * Sets cell state to true regardless of current state.
     *
     * @param x the x coordinate in the grid.
     * @param y the y coordinate in the grid.
     */
    public void setCellAlive(int x, int y) {
        grid[x][y] = true;
    }

    @Override
    public void setCellDead(int x, int y) {

    }

    /**
     * Changes the state of a cell based on the grid coordinate.
     *
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
    public void clearGrid() {

        for (int i = 0; i < grid.length; i++) {
            Arrays.fill(grid[i], false);
            Arrays.fill(neighbours[i], (byte) 0);
        }

        cellCount = 0;
    }
    public void setCellCount(int cellCount) {
        this.cellCount = cellCount;
    }

    @Override
    public void resetNeighboursAt(int x, int y) {

    }

    @Override
    public int getOffsetX() {
        return 0;
    }

    @Override
    public int getOffsetY() {
        return 0;
    }
    //endregion
}
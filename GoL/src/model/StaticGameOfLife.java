package model;

import model.rules.ClassicRule;
import model.rules.CustomRule;
import model.rules.HighLifeRule;
import model.rules.Rule;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author The group through pair programing.
 */

public class StaticGameOfLife extends GameOfLife{

    private AtomicBoolean[][] grid;
    private AtomicInteger[][] neighbours;

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

    /**
     * Creates the boolean 2D Array to keep track of dead and live cells, and the 2D byte-
     * array to keep track of the neighbour count to the corresponding cells in the other array
     */
    private void createGameBoard(int width, int height) {
        grid = new AtomicBoolean[width][height];
        neighbours = new AtomicInteger[width][height];

        for (int x = 0; x < width; x++){

            for (int y = 0; y < height; y++){
                grid[x][y] = new AtomicBoolean(false);
                neighbours[x][y] = new AtomicInteger(0);
            }
        }
    }

    /**
     * Evolves the game board one generation.
     */
    public void nextGeneration() {
        createCountingThreads();
        try {
            runThreads();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        createEvolveThreads();
        try {
            runThreads();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * For each alive cell, it increments the adjacent cells neighbour count.
     * Also calculates the live cell count
     */
    public void aggregateNeighbours(int start, int stop) {
        cellCount.set(0);
        for (int x = start; x < stop; x++) {
            for (int y = 1; y < grid[x].length - 1; y++) {
                if (grid[x][y].get()) {
                    cellCount.incrementAndGet();
                    for (int a = x - 1; a <= x + 1; a++) {
                        for (int b = y - 1; b <= y + 1; b++) {
                            if (a != x || b != y) {
                                neighbours[a][b].incrementAndGet();
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

        StaticGameOfLife staticGameOfLife = new StaticGameOfLife(getGridWidth(), getGridHeight());
        staticGameOfLife.deepCopyOnSet(grid);
        staticGameOfLife.setRule(getRule().toString());
        staticGameOfLife.setCellCount(cellCount.get());

        //staticGameOfLife.setCell(getCell());

        return staticGameOfLife;
    }

    /**
     * Deep copies the grid and sets it.
     * @param grid the grid to be deep copied and set.
     */
    public void deepCopyOnSet(AtomicBoolean[][] grid) {

        AtomicBoolean[][] copiedBoard = new AtomicBoolean[grid.length][grid[0].length];
        neighbours = new AtomicInteger[grid.length][grid[0].length];
        cellCount.set(0);

        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {

                copiedBoard[x][y] = new AtomicBoolean(grid[x][y].get());
                neighbours[x][y] = new AtomicInteger(0);

                if(grid[x][y].get()) {
                    cellCount.incrementAndGet();
                }
            }
        }
        this.grid = copiedBoard;
    }

    //region Getters

    /**
     * Getter for neighbour-2D-array
     *
     * @return The neighbour-2D-array
     */
    public AtomicInteger[][] getNeighbours() {
        return neighbours;
    }

    /**
     * Getter for the cell-2D-array
     *
     * @return The cell-2D-array
     */
    public AtomicBoolean[][] getGrid() {
        return grid;
    }

    /**
     * Returns the number of live cells in grid
     *
     * @return The live cell count
     */
    public int getCellCount() {
        return cellCount.get();
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
                if (!grid[i][j].get()) continue;
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
        return grid.length;
    }

    @Override
    public int getGridHeight() {
        return grid[0].length;
    }

    @Override
    public int getNeighboursAt(int x, int y) {
        return neighbours[x][y].get();
    }

    @Override
    public boolean isCellAlive(int x, int y) {
        return grid[x][y].get();
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
                if (grid[i][j].get()) {
                    gridExtracted[k][t] = grid[i][j].get();
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
    public void setGrid(AtomicBoolean[][] grid) {
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
        grid[x][y].set(true);
    }

    /**
     * Sets cell state to false regardless of current state.
     *
     * @param x the x coordinate in the grid.
     * @param y the y coordinate in the grid.
     */
    @Override
    public void setCellDead(int x, int y) {
        grid[x][y].set(false);
    }

    /**
     * Changes the state of a cell based on the grid coordinate.
     *
     * @param x the x coordinate in the grid.
     * @param y the y coordinate in the grid.
     */
    public void changeCellState(int x, int y) {
        grid[x][y].set(!grid[x][y].get());
    }

    /**
     * Creates a new neighbour grid if a cell grid is already set.
     * The neighbour grid will get the same dimensions as the cell grid
     * If a cell grid is not yet set, use createGameBoard() instead.
     */
    public void createNeighboursGrid() {
        neighbours = new AtomicInteger[grid.length][grid[0].length];
    }
    /**
     * Clears the grid of live cells
     */
    public void clearGrid() {

        for (int i = 0; i < grid.length; i++) {
            Arrays.fill(grid[i], false);
            Arrays.fill(neighbours[i], (byte) 0);
        }

        cellCount.set(0);
    }
    public void setCellCount(int cellCount) {
        this.cellCount.set(cellCount);
    }

    @Override
    public void resetNeighboursAt(int x, int y) {
        neighbours[x][y].set(0);
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
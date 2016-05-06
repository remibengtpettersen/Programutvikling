package model;

import model.rules.RuleParser;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Pair programming
 *
 * Game class with a game board with static size.
 */
public class StaticGameOfLife extends GameOfLife{

    // game board
    private AtomicBoolean[][] grid;
    private AtomicInteger[][] neighbours;

    //region start-up

    /**
     * StaticGameOfLife Constructor. Sets the classic Conway rule (B3/S23) as default rule.
     *
     * @param width Width of the game board
     * @param height Height of the game board
     */
    public StaticGameOfLife(int width, int height) {

        this(width, height, RuleParser.CLASSIC_RULESTRING);
    }

    /**
     * StaticGameOfLife Constructor.
     * Sets the rule based on the parameter rulestring.
     */
    public StaticGameOfLife(int width, int height, String rulestring) {

        createGameBoard(width, height);
        setRule(rulestring);
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

    //endregion

    //region getters

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
        try {
            return grid[x][y].get();
        } catch (ArrayIndexOutOfBoundsException e){
            return false;
        }
    }

    /**
     * Clones the StaticGameOfLife object
     *
     * @return the cloned StaticGameOfLife object
     */
    @Override
    public StaticGameOfLife clone() {

        StaticGameOfLife staticGameOfLife = new StaticGameOfLife(
                getGridWidth(), getGridHeight(), getRule().toString());
        staticGameOfLife.deepCopyOnSet(grid);
        staticGameOfLife.setCellCount(cellCount.get());

        return staticGameOfLife;
    }

    /**
     * Deep copies the grid and sets it.
     *
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

    //endregion

    //region setters

    /**
     * Sets the cell grid to be used
     *
     * @param grid Cell grid
     */
    public void setGrid(AtomicBoolean[][] grid) {
        this.grid = grid;
    }

    @Override
    public void setCellAlive(int x, int y) {

        if(!isCellAlive(x,y)){

            try {
                grid[x][y].set(true);
                cellCount.incrementAndGet();
            } catch (IndexOutOfBoundsException ignored){
            }
        }
    }

    @Override
    public void setCellDead(int x, int y) {

        if(isCellAlive(x,y)) {

            grid[x][y].set(false);
            cellCount.decrementAndGet();
        }
    }

    @Override
    public void clearGrid() {

        for (int x = 0; x < getGridWidth(); x++) {
            for (int y = 0; y < getGridHeight(); y++) {
                grid[x][y].set(false);
                neighbours[x][y].set(0);
            }
        }

        cellCount.set(0);
    }

    @Override
    protected void incrementNeighboursAt(int x, int y){ neighbours[x][y].incrementAndGet();}

    @Override
    public void resetNeighboursAt(int x, int y) {
        neighbours[x][y].set(0);
    }

    //endregion
}
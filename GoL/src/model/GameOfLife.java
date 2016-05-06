package model;

import model.rules.*;
import tools.MessageBox;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Pair programming
 *
 * Game class.
 * Handles logic related to Game of Life. Contains a game board of cells,
 * and evolves these cells according to a specific rule.
 * Is abstract and needs to be extended by StaticGameOfLife or DynamicGameOfLife.
 * Supports concurrent programming
 */
public abstract class GameOfLife {

    private int availableProcessors = Runtime.getRuntime().availableProcessors();
    private List<Thread> threads = new ArrayList<>();

    protected AtomicInteger cellCount = new AtomicInteger(0);
    protected Rule rule;

    // offset to use when grid is expanded to left and upwards
    protected int cellOffsetX = 0;
    protected int cellOffsetY = 0;

    //region next generation

    /**
     * Evolves the game board one generation.
     * Will use a number of threads to count neighbours first,
     * then use a number of threads to evolve the cells
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
     *
     * @param startColumn First column from the left
     * @param stopColumn Last column from the right
     */
    public void aggregateNeighbours(int startColumn, int stopColumn) {

        for (int x = startColumn; x < stopColumn; x++) {
            for (int y = 1; y < getGridHeight() - 1; y++) {

                if (isCellAlive(x,y)) {

                    for (int a = x - 1; a <= x + 1; a++) {
                        for (int b = y - 1; b <= y + 1; b++) {

                            if (a != x || b != y) {
                                incrementNeighboursAt(a,b);
                            }
                        }
                    }
                }
            }
        }
    }
    //endregion

    //region concurrency

    /**
     * Creates threads for counting neighbours.
     * Each thread is assigned a number of columns to count neighbours for.
     */
    private void createCountingThreads() {

        for (int i = 0; i < availableProcessors; i++) {
            final int finalI = i;

            threads.add(new Thread(() -> {

                int start = finalI * getGridWidth()/ availableProcessors;
                int stop = (finalI + 1) * getGridWidth()/ availableProcessors;

                start = (start == 0)? 1: start;
                stop = (stop == getGridWidth())?getGridWidth() - 1 : stop;

                aggregateNeighbours(start, stop);
            }));
        }
    }

    /**
     * Creates threads for evolving cells.
     * Each thread is assigned a number of columns to evolve.
     */
    private void createEvolveThreads() {

        for (int i = 0; i < availableProcessors; i++) {
            final int finalI = i;

            threads.add(new Thread(() -> {
                try {
                    rule.evolve(finalI * getGridWidth()/ availableProcessors, (finalI + 1) * getGridWidth()/ availableProcessors);
                } catch (EvolveException e) {
                    MessageBox.alert(e.getMessage());
                    e.printStackTrace();
                }
            }));
        }
    }

    /**
     * Runs all active threads, waits for them to complete their task,
     * and joins them before clearing the thread list.
     *
     * @throws InterruptedException Thrown if a thread is interrupted
     */
    private void runThreads() throws InterruptedException {

        threads.forEach(Thread::start);

        // wait for all the threads to finish before joining
        for (Thread t : threads) {
            t.join();
        }
        threads.clear();
    }

    //endregion

    //region getters

    /**
     * Gets the smallest possible bounding box around the pattern.
     * The bounding box is an int array containing min row, max row,
     * min column and max column for the active cells in game board.
     *
     * @return Min row (left), max row (right), min column (top), max column (bottom)
     */
    public int[] getBoundingBox() {

        int[] boundingBox = new int[4];

        boundingBox[0] = getGridWidth();
        boundingBox[1] = 0;
        boundingBox[2] = getGridHeight();
        boundingBox[3] = 0;

        for (int i = 0; i < getGridWidth(); i++) {
            for (int j = 0; j < getGridHeight(); j++) {
                if (!isCellAlive(i,j)) continue;
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
        if (boundingBox[1] < boundingBox[0]){
            boundingBox[0] = 1;
            boundingBox[1] = 1;
            boundingBox[2] = 1;
            boundingBox[3] = 1;
        }
        return boundingBox;
    }

    /**
     * Gets the grid width, or number of columns.
     *
     * @return Width of the grid
     */
    public abstract int getGridWidth();

    /**
     * Gets the grid height, or number of rows
     *
     * @return Height of the grid
     */
    public abstract int getGridHeight();

    /**
     * Will get game board's horizontal offset from origin.
     * Will always be 0 for static game boards
     *
     * @return X offset
     */
    public int getOffsetX() { return cellOffsetX; }

    /**
     * Will get game board's vertical offset from origin.
     * Will always be 0 for static game boards
     *
     * @return Y offset
     */
    public int getOffsetY() { return cellOffsetY; }

    /**
     * Gets the number of neighbours for a cell at (x,y)
     *
     * @param x X coordinate at grid.
     * @param y Y coordinate at grid.
     * @return Number of neighbours
     */
    public abstract int getNeighboursAt(int x, int y);

    /**
     * Checks if a cell at (x,y) is alive
     *
     * @param x X coordinate at grid.
     * @param y Y coordinate at grid.
     * @return True if cell is alive
     */
    public abstract boolean isCellAlive(int x, int y);

    /**
     * Clones the StaticGameOfLife object
     *
     * @return the cloned StaticGameOfLife object
     */
    @Override
    public abstract GameOfLife clone();

    /**
     * Gets the number of live cells at game board
     *
     * @return Number of live cells
     */
    public int getCellCount(){
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

    //endregion

    //region setters
    /**
     * Sets cell state to true regardless of current state.
     *
     * @param x X coordinate at grid.
     * @param y Y coordinate at grid.
     */
    public abstract void setCellAlive(int x, int y);


    /**
     * Sets cell state to false regardless of current state.
     *
     * @param x X coordinate at grid.
     * @param y Y coordinate at grid.
     */
    public abstract void setCellDead(int x, int y);

    /**
     * Changes the state of a cell based on the grid coordinate.
     *
     * @param x X coordinate at grid.
     * @param y Y coordinate at grid.
     */
    public void changeCellState(int x, int y) {

        if(isCellAlive(x,y))
            setCellDead(x,y);
        else
            setCellAlive(x,y);
    }

    /**
     * Sets a specific rule to be used.
     *
     * @param ruleText The rule text
     */
    public void setRule(String ruleText) {
        rule = RuleParser.createRule(this, ruleText);
    }

    /**
     * Sets the cell count
     *
     * @param cellCount Number of live cells on game board
     */
    public void setCellCount(int cellCount){
        this.cellCount.set(cellCount);
    }

    /**
     * Clears the game board of live cells
     */
    public abstract void clearGrid();

    /**
     * Increment neighbour count for a cell at (x,y)
     *
     * @param x X coordinate at grid.
     * @param y Y coordinate at grid.
     */
    protected abstract void incrementNeighboursAt(int x, int y);

    /**
     * Reset neighbour count for a cell at (x,y)
     *
     * @param x X coordinate at grid.
     * @param y Y coordinate at grid.
     */
    public abstract void resetNeighboursAt(int x, int y);

    public String getAggregatedNeighbours() throws InterruptedException {

        createCountingThreads();

        runThreads();

        StringBuilder neighbours = new StringBuilder();

        for (int y = 0; y < getGridHeight(); y++) {
            for (int x = 0; x < getGridWidth(); x++){
                neighbours.append(getNeighboursAt(x, y));
                resetNeighboursAt(x, y);
            }
            neighbours.append(" ");
        }
        neighbours.deleteCharAt(neighbours.length() - 1);

        return neighbours.toString();
    }

    @Override
    public String toString() {

        StringBuilder string = new StringBuilder();

        for (int y = 0; y < getGridHeight(); y++) {
            for (int x = 0; x < getGridWidth(); x++) {
                if (isCellAlive(x, y)) {
                    string.append(1);
                }
                else {
                    string.append(0);
                }
            }
            string.append(" ");
        }
        string.deleteCharAt(string.length() - 1);

        return string.toString();
    }
    //endregion
}

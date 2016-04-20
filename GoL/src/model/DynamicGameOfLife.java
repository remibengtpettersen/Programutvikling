package model;

import model.rules.*;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Truls on 20/04/16.
 */
public class DynamicGameOfLife{




    private CopyOnWriteArrayList<CopyOnWriteArrayList<Boolean>> grid;
    private CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>> neighbours;
    private ClassicDynamicRule rule;
    private int cellCount = 0;

    /**
     * GameOfLife Constructor. Sets the classic Conway rule (B3/S23) as default rule.
     *
     * @param width
     * @param height
     */
    public DynamicGameOfLife(int width, int height) {

        createGameBoard(width, height);
        setRule("classic");
    }

    //region startup-sequence

    /**
     * Creates the boolean 2D Array to keep track of dead and live cells, and the 2D byte-
     * array to keep track of the neighbour count to the corresponding cells in the other array
     */
    private void createGameBoard(int width, int height) {
        grid = new CopyOnWriteArrayList<>();
        neighbours = new CopyOnWriteArrayList<>();

        for (int x = 0; x < width; x++) {
            grid.add(new CopyOnWriteArrayList<>());
            neighbours.add(new CopyOnWriteArrayList<>());
            for (int y = 0; y < height; y++) {
                grid.get(x).add(false);
                neighbours.get(x).add(0);
            }
        }
    }
    //endregion

    //region NextGeneration

    /**
     * Evolves the grid one generation
     */
    public void nextGeneration() {
        aggregateNeighbours();

        try {
            rule.evolve();
        } catch (EvolveException e) {
            e.printStackTrace();
        }
    }

    ;

    /**
     * For each alive cell, it increments the adjacent cells neighbour count.
     * Also calculates the live cell count
     */
    public void aggregateNeighbours() {
        cellCount = 0;
        for (int x = 1; x < grid.size() - 1; x++) {
            for (int y = 1; y < grid.get(x).size() - 1; y++) {
                if (grid.get(x).get(y)) {
                    cellCount++;
                    for (int a = x - 1; a <= x + 1; a++) {
                        for (int b = y - 1; b <= y + 1; b++) {
                            if (a != x || b != y) {
                                neighbours.get(a).set(b, neighbours.get(a).get(b) + 1);
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Clones the GameOfLife object
     * @return the cloned GameOfLife object
     */
    @Override
    public GameOfLife clone() {
        return null;
    }

    /**
     * Deep copies the grid and sets it.
     * @param grid the grid to be deep copied and set.
     */
    public void deepCopyOnSet(boolean[][] grid) {

    }

    //region Getters

    /**
     * Getter for neighbour-2D-array
     *
     * @return The neighbour-2D-array
     */
    public CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>> getNeighbours() {
        return neighbours;
    }

    /**
     * Getter for the cell-2D-array
     *
     * @return The cell-2D-array
     */
    public CopyOnWriteArrayList<CopyOnWriteArrayList<Boolean>> getGrid() {
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
    public ClassicDynamicRule getRule() {
        return rule;
    }

    /**
     * We copied getBoundingBox from the assignment
     *
     * @return
     */
    public int[] getBoundingBox() {
        return null;
    }

    public boolean[][] getPatternFromGrid() {
        return null;
    }
    //endregion

    //region Setters

    /**
     * Sets the cell grid to be used
     *
     * @param grid Cell grid
     */
    public void setGrid(CopyOnWriteArrayList<CopyOnWriteArrayList<Boolean>> grid) {
        this.grid = grid;
    }

    /**
     * Sets a specific rule to be used.
     *
     * @param ruleText The rule text
     */
    public void setRule(String ruleText) {

                rule = new ClassicDynamicRule(grid, neighbours);
        }

    /**
     * Sets cell state to true regardless of current state.
     *
     * @param x the x coordinate in the grid.
     * @param y the y coordinate in the grid.
     */
    public void setCellAlive(int x, int y) {
        grid.get(x).set(y, true);
    }

    /**
     * Changes the state of a cell based on the grid coordinate.
     *
     * @param x the x coordinate in the grid.
     * @param y the y coordinate in the grid.
     */
    public void changeCellState(int x, int y) {
        grid.get(x).set(y, !grid.get(x).get(y));
    }

    /**
     * Creates a new neighbour grid if a cell grid is already set.
     * The neighbour grid will get the same dimensions as the cell grid
     * If a cell grid is not yet set, use createGameBoard() instead.
     */
    public void createNeighboursGrid() {

    }
    /**
     * Clears the grid of live cells
     */
    public void clearGrid() {

    }

    /**
     * Updates the rule's references to this class' cell grid and neighbour grid
     */
    public void updateRuleGrid() {
    }

    public void setCellCount(int cellCount) {
        this.cellCount = cellCount;
    }
    //endregion
}

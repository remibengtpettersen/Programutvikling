package model;

import model.rules.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Truls on 20/04/16.
 */
public class DynamicGameOfLife{

    int treads = Runtime.getRuntime().availableProcessors();

    private List<Thread> workers = new ArrayList<>();

    // Offset to use when grid is expanded to left and upwards
    public int cellOffsetX = 0;
    public int cellOffsetY = 0;

    private ArrayList<ArrayList<AtomicBoolean>> grid;
    private ArrayList<ArrayList<AtomicInteger>> neighbours;
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
        grid = new ArrayList<>();
        neighbours = new ArrayList<>();

        for (int x = 0; x < width; x++) {
            grid.add(new ArrayList<>());
            neighbours.add(new ArrayList<>());
            for (int y = 0; y < height; y++) {
                grid.get(x).add(new AtomicBoolean(false));
                neighbours.get(x).add(new AtomicInteger(0));
            }
        }
    }
    //endregion

    //region NextGeneration

    /**
     * Evolves the grid one generation
     */
    public void nextGeneration() {
        createWorkers1();
        try {
            runWorkers();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        workers.clear();
        createWorkers2();
        try {
            runWorkers();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        workers.clear();
    }



    public void createWorkers1() {
        for (int i = 0; i < treads; i++) {
            final int finalI = i;
            workers.add(new Thread(() -> {
                aggregateNeighbours(finalI * grid.size()/treads, (finalI + 1) * grid.size()/treads);
            }));
        }
    }
    public void createWorkers2() {
        for (int i = 0; i < treads; i++) {
            final int finalI = i;
            workers.add(new Thread(() -> {
                try {
                    rule.evolve(finalI * grid.size()/treads, (finalI + 1) * grid.size()/treads);
                } catch (EvolveException e) {
                    e.printStackTrace();
                }
            }));
        }
    }

    public void runWorkers() throws InterruptedException {
        for (Thread t : workers) {
            t.start();
        }

        // vent på at alle trådene har kjørt ferdig før vi returnerer
        for (Thread t : workers) {
            t.join();
        }
    }



    /**
     * For each alive cell, it increments the adjacent cells neighbour count.
     * Also calculates the live cell count
     * @param start
     * @param stop
     */
    public void aggregateNeighbours(int start, int stop) {
        cellCount = 0;
        for (int x = start; x < stop; x++) {
            for (int y = 0; y < grid.get(x).size(); y++) {
                if (grid.get(x).get(y).get()) {
                    cellCount++;
                    for (int a = x - 1; a <= x + 1; a++) {
                        for (int b = y - 1; b <= y + 1; b++) {
                            if (a != x || b != y) {
                               try{
                                   neighbours.get(a).get(b).incrementAndGet();
                               }
                               catch (IndexOutOfBoundsException e){
                                   if (a < 0)
                                       increaseXLeft(1);
                                   if(a >= grid.size())
                                       increaseXRight(1);
                                   if (b < 0)
                                       increaseYTop(1);
                                   if(b >= grid.get(0).size())
                                       increaseYBottom(1);
                                   neighbours.get((a < 0)? 0 : a).get((b < 0)? 0 : b).incrementAndGet();
                               }

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
    public ArrayList<ArrayList<AtomicInteger>> getNeighbours() {
        return neighbours;
    }

    /**
     * Getter for the cell-2D-array
     *
     * @return The cell-2D-array
     */
    public ArrayList<ArrayList<AtomicBoolean>> getGrid() {
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
    public void setGrid(ArrayList<ArrayList<AtomicBoolean>> grid) {
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
        try {
            grid.get(x).get(y).set(true);
        }
        catch (IndexOutOfBoundsException e) {
            int diffX = x - grid.size() + 1;

            if(diffX > 0)
                increaseXRight(diffX);

            int diffY = y - grid.get(0).size() + 1;
            if(diffY > 0)
                increaseYBottom(diffY);

            grid.get(x).get(y).set(true);
        }
    }

    /**
     * Changes the state of a cell based on the grid coordinate.
     *
     * @param x the x coordinate in the grid.
     * @param y the y coordinate in the grid.
     */
    public void changeCellState(int x, int y) {


        try {
            grid.get(x).get(y).set(!grid.get(x).get(y).get());
        }
        catch (IndexOutOfBoundsException e) {
            int diffX = x - grid.size() + 1;

            if(diffX > 0)
                increaseXRight(diffX);

            int diffY = y - grid.get(0).size() + 1;
            if(diffY > 0)
                increaseYBottom(diffY);

            grid.get(x).get(y).set(!grid.get(x).get(y).get());
        }
    }


    public void increaseXRight(int diffX) {
        for (int i = 0; i <= diffX; i++){
            grid.add(new ArrayList<>());
            neighbours.add(new ArrayList<>());

            for (int j = 0; j < grid.get(0).size(); j++) {
                grid.get(grid.size() - 1).add(new AtomicBoolean(false));
                neighbours.get(grid.size() - 1).add(new AtomicInteger(0));
            }
        }
    }

    public void increaseYBottom(int diffY){
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j <= diffY; j++){
                grid.get(i).add(new AtomicBoolean(false));
                neighbours.get(i).add(new AtomicInteger(0));
            }
        }
    }

    public void increaseXLeft(int diffX) {
        for (int i = 0; i <= diffX; i++){
            grid.add(0, new ArrayList<>());
            neighbours.add(0, new ArrayList<>());

            for (int j = 0; j < grid.get(1).size(); j++) {
                grid.get(0).add(new AtomicBoolean(false));
                neighbours.get(0).add(new AtomicInteger(0));
            }
        }
    }

    public void increaseYTop(int diffY){
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j <= diffY; j++){
                grid.get(i).add(0, new AtomicBoolean(false));
                neighbours.get(i).add(0, new AtomicInteger(0));
            }
        }
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

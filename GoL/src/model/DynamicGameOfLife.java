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

    private int availableProcessors = Runtime.getRuntime().availableProcessors();
    //int availableProcessors = 1;


    private List<Thread> threads = new ArrayList<>();

    // Offset to use when grid is expanded to left and upwards


    private int cellOffsetX = 0;
    private int cellOffsetY = 0;

    private ArrayList<ArrayList<AtomicBoolean>> grid;
    private ArrayList<ArrayList<AtomicInteger>> neighbours;
    private ClassicDynamicRule rule;
    private int cellCount = 0;

    /**
     * GameOfLife Constructor. Sets the classic Conway rule (B3/S23) as default rule.
     *
     */
    public DynamicGameOfLife() {

        createGameBoard();
        setRule("classic");
    }

    //region startup-sequence

    /**
     * Creates the boolean 2D Array to keep track of dead and live cells, and the 2D byte-
     * array to keep track of the neighbour count to the corresponding cells in the other array
     */
    private void createGameBoard() {
        grid = new ArrayList<>();
        neighbours = new ArrayList<>();

        grid.add(new ArrayList<>());
        neighbours.add(new ArrayList<>());
        grid.get(0).add(new AtomicBoolean(false));
        neighbours.get(0).add(new AtomicInteger(0));

    }
    //endregion

    //region NextGeneration

    /**
     * Evolves the grid one generation
     */
    public void nextGeneration() {
        cellCount = 0;
        fitBoardToPattern();
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

    private void fitBoardToPattern() {
        int [] bBox = getBoundingBox();
        if (bBox[0] > 1){
            decreaseXLeft(bBox[0] - 1);
            bBox[1] -= bBox[0] - 1;
        }
        else if(bBox[0] == 0){
            increaseXLeft(1);
            bBox[1] += 1;
        }
        if (bBox[1] < grid.size() - 2){
            decreaseXRight(grid.size() - bBox[1] - 2);
        }
        else if(bBox[1] == grid.size() - 1){
            increaseXRight(1);
        }

        if (bBox[2] > 1){
            decreaseYTop(bBox[2] - 1);
            bBox[3] -= bBox[2] - 1;
        }
        else if (bBox[2] == 0){
            increaseYTop(1);
            bBox[3] += 1;
        }
        if (bBox[3] < grid.get(0).size() - 2){
            decreaseYBottom(grid.get(0).size() - bBox[3] - 2);
        }
        else if(bBox[3] == grid.get(0).size() - 1){
            increaseYBottom(1);
        }

    }


    public void createCountingThreads() {
        for (int i = 0; i < availableProcessors; i++) {
            final int finalI = i;
            threads.add(new Thread(() -> {
                int start = finalI * grid.size()/ availableProcessors;
                int stop = (finalI + 1) * grid.size()/ availableProcessors;
                start = (start == 0)? 1: start;
                stop = (stop == grid.size() - 1)?grid.size() - 1 : stop;
                aggregateNeighbours(start, stop);
            }));
        }
    }
    public void createEvolveThreads() {
        for (int i = 0; i < availableProcessors; i++) {
            final int finalI = i;
            threads.add(new Thread(() -> {
                try {
                    rule.evolve(finalI * grid.size()/ availableProcessors, (finalI + 1) * grid.size()/ availableProcessors);
                } catch (EvolveException e) {
                    e.printStackTrace();
                }
            }));
        }
    }

    public void runThreads() throws InterruptedException {
        for (Thread t : threads) {
            t.start();
        }

        // vent på at alle trådene har kjørt ferdig før vi returnerer
        for (Thread t : threads) {
            t.join();
        }
        threads.clear();
    }



    /**
     * For each alive cell, it increments the adjacent cells neighbour count.
     * Also calculates the live cell count
     * @param start
     * @param stop
     */
    public void aggregateNeighbours(int start, int stop) {

        for (int x = start; x < stop; x++) {
            for (int y = 1; y < grid.get(x).size() - 1; y++) {
                if (grid.get(x).get(y).get()) {
                    cellCount++;
                    for (int a = x - 1; a <= x + 1; a++) {
                        for (int b = y - 1; b <= y + 1; b++) {
                            if (a != x || b != y) {
                                neighbours.get(a).get(b).incrementAndGet();
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
    public DynamicGameOfLife clone() {
        DynamicGameOfLife gameOfLife = new DynamicGameOfLife();
        gameOfLife.deepCopyOnSet(grid);
        gameOfLife.setRule(getRule().toString());
        gameOfLife.setCellCount(cellCount);

        //gameOfLife.setCell(getCell());

        return gameOfLife;
    }

    /**
     * Deep copies the grid and sets it.
     * @param grid the grid to be deep copied and set.
     */
    public void deepCopyOnSet(ArrayList<ArrayList<AtomicBoolean>> grid) {
        cellCount = 0;
        neighbours.clear();
        this.grid.clear();
        for (int x = 0; x < grid.size(); x++) {
            this.grid.add(new ArrayList<>());
            neighbours.add(new ArrayList<>());
            for (int y = 0; y < grid.get(x).size(); y++) {
                if(grid.get(x).get(y).get())
                    cellCount++;
                this.grid.get(x).add(new AtomicBoolean(grid.get(x).get(y).get()));
                neighbours.get(x).add(new AtomicInteger(0));
            }
        }

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
        int[] boundingBox = new int[4]; // minrow maxrow mincolumn maxcolumn boundingBox[0] = board.length;

        boundingBox[0] = grid.size();
        boundingBox[1] = 0;
        boundingBox[2] = grid.get(0).size();
        boundingBox[3] = 0;

        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(i).size(); j++) {
                if (!grid.get(i).get(j).get()) continue;
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
        for (int i = 0; i < diffX; i++){
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
            for (int j = 0; j < diffY; j++){
                grid.get(i).add(new AtomicBoolean(false));
                neighbours.get(i).add(new AtomicInteger(0));
            }
        }
    }

    public void increaseXLeft(int diffX) {
        cellOffsetX += diffX;
        for (int i = 0; i < diffX; i++){
            grid.add(0, new ArrayList<>());
            neighbours.add(0, new ArrayList<>());

            for (int j = 0; j < grid.get(1).size(); j++) {
                grid.get(0).add(new AtomicBoolean(false));
                neighbours.get(0).add(new AtomicInteger(0));
            }
        }
    }

    public void increaseYTop(int diffY){
        cellOffsetY += diffY;
        System.out.println(diffY);
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < diffY; j++){
                grid.get(i).add(0, new AtomicBoolean(false));
                neighbours.get(i).add(0, new AtomicInteger(0));
            }
        }
    }

    public void decreaseXRight(int diffX) {
        for (int i = 0; i < diffX; i++){
            grid.remove(grid.size() - 1);
            neighbours.remove(neighbours.size() - 1);
        }
    }

    public void decreaseYBottom(int diffY){
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < diffY; j++){
                grid.get(i).remove(grid.get(i).size() - 1);
                neighbours.get(i).remove(neighbours.get(i).size() - 1);
            }
        }
    }

    public void decreaseXLeft(int diffX) {
        cellOffsetX -= diffX;
        for (int i = 0; i < diffX; i++){
            grid.remove(0);
            neighbours.remove(0);
        }
    }

    public void decreaseYTop(int diffY){
        cellOffsetY -= diffY;
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < diffY; j++){
                grid.get(i).remove(0);
                neighbours.get(i).remove(0);
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
        rule.setGrid(grid);
        rule.setNeighbours(neighbours);
    }

    public void setCellCount(int cellCount) {
        this.cellCount = cellCount;
    }

    public int getOffsetX() {
        return cellOffsetX;
    }

    public int getOffsetY() {
        return cellOffsetY;
    }
    //endregion
}

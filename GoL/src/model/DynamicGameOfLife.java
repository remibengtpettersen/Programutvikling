package model;

import model.rules.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Truls on 20/04/16.
 */
public class DynamicGameOfLife extends GameOfLife{

    //int availableProcessors = 1;



    // Offset to use when grid is expanded to left and upwards


    private int cellOffsetX = 0;
    private int cellOffsetY = 0;

    private ArrayList<ArrayList<AtomicBoolean>> grid;
    private ArrayList<ArrayList<AtomicInteger>> neighbours;



    /**
     * StaticGameOfLife Constructor. Sets the classic Conway rule (B3/S23) as default rule.
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

    public void fitBoardToPattern() {
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

    //endregion

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



    public int getGridWidth(){ return grid.size(); }

    public int getGridHeight(){ return grid.get(0).size(); }

    public int getNeighboursAt(int x, int y){
        return neighbours.get(x).get(y).get();
    }

    public boolean isCellAlive(int x, int y){
        try{
            return  grid.get(x).get(y).get();
        }
        catch(IndexOutOfBoundsException e){
            return false;
        }
    }

    /**
     * Clones the StaticGameOfLife object
     * @return the cloned StaticGameOfLife object
     */
    @Override
    public DynamicGameOfLife clone() {
        DynamicGameOfLife gameOfLife = new DynamicGameOfLife();
        gameOfLife.deepCopyOnSet(grid);
        gameOfLife.setRule(getRule().toString());
        gameOfLife.setCellCount(cellCount.get());

        //gameOfLife.setCell(getCell());

        return gameOfLife;
    }

    /**
     * Deep copies the grid and sets it.
     * @param grid the grid to be deep copied and set.
     */
    public void deepCopyOnSet(ArrayList<ArrayList<AtomicBoolean>> grid) {
        neighbours.clear();
        cellOffsetX = 0;
        cellOffsetY = 0;
        this.grid.clear();
        for (int x = 0; x < grid.size(); x++) {
            this.grid.add(new ArrayList<>());
            neighbours.add(new ArrayList<>());
            for (int y = 0; y < grid.get(x).size(); y++) {
                this.grid.get(x).add(new AtomicBoolean(grid.get(x).get(y).get()));
                neighbours.get(x).add(new AtomicInteger(0));
            }
        }

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
     * Sets cell state to true regardless of current state.
     *
     * @param x the x coordinate in the grid.
     * @param y the y coordinate in the grid.
     */
    public void setCellAlive(int x, int y) {

        if(!isCellAlive(x,y)) {

            cellCount.incrementAndGet();

            try {
                grid.get(x).get(y).set(true);
            }
            catch (IndexOutOfBoundsException e) {

                int diffX = x - getGridWidth() + 1;
                if(diffX > 0)
                    increaseXRight(diffX);

                int diffY = y - getGridHeight() + 1;
                if(diffY > 0)
                    increaseYBottom(diffY);

                grid.get(x).get(y).set(true);
            }
        }
    }

    /**
     * Sets cell state to false regardless of current state.
     *
     * @param x the x coordinate in the grid.
     * @param y the y coordinate in the grid.
     */
    public void setCellDead(int x, int y) {


            if(isCellAlive(x,y)) {

                cellCount.decrementAndGet();

                    grid.get(x).get(y).set(false);



                    int diffX = x - getGridWidth() + 1;
                    if(diffX > 0)
                        increaseXRight(diffX);

                    int diffY = y - getGridHeight() + 1;
                    if(diffY > 0)
                        increaseYBottom(diffY);

                    grid.get(x).get(y).set(false);

            }


    }

    /**
     * Changes the state of a cell based on the grid coordinate.
     *
     * @param x the x coordinate in the grid.
     * @param y the y coordinate in the grid.
     */
    public void changeCellState(int x, int y) {

        if(isCellAlive(x,y))
            setCellDead(x,y);
        else
            setCellAlive(x,y);
    }

    public void resetNeighboursAt(int x, int y){
        neighbours.get(x).get(y).set(0);
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

        cellCount.set(0);

        grid.clear();
        neighbours.clear();

        grid.add(new ArrayList<>());
        neighbours.add(new ArrayList<>());

        grid.get(0).add(new AtomicBoolean(false));
        neighbours.get(0).add(new AtomicInteger(0));
    }

    /**
     * Updates the rule's references to this class' cell grid and neighbour grid
     */
    /*public void updateRuleGrid() {
        rule.setGol(grid);
        rule.setNeighbours(neighbours);
    }*/

    public int getOffsetX() {
        return cellOffsetX;
    }

    public int getOffsetY() {
        return cellOffsetY;
    }
    //endregion
}

package model;

import model.rules.Rule;

/**
 * Created by Andreas on 29.02.2016.
 */
public abstract class GameOfLife {

    protected Rule rule;

    // For statistics
    public int cellCount = 0;

    public GameOfLife(int gameSize){
        createGameBoard(gameSize);
    }

    public abstract void createGameBoard(int gameSize);

    public abstract void nextGeneration();
    public abstract void aggregateNeighbours();


    public abstract byte[][] getNeighbours();
    public abstract boolean[][] getGrid();

    public abstract void setGrid(boolean[][] grid);

    public int getCellCount() {
        return cellCount;
    }
}

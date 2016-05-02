package model;

import model.rules.Rule;

/**
 * Created by Truls on 02/05/16.
 */
public abstract class GameOfLife {

    public abstract void nextGeneration();

    public abstract void fitBoardToPattern();

    public abstract int getCellCount();

    public abstract Rule getRule();

    public abstract int[] getBoundingBox();

    public abstract int getGridWidth();

    public abstract int getGridHeight();

    public abstract int getNeighboursAt(int x, int y);

    public abstract boolean isCellAlive(int x, int y);

    public abstract GameOfLife clone();


    //public abstract void setGrid();

    public abstract void setRule(String ruleText);

    public abstract void setCellAlive(int x, int y);

    public abstract void setCellDead(int x, int y);

    public abstract void changeCellState(int x, int y);

    public abstract void clearGrid();

    public abstract void setCellCount(int cellCount);

    public abstract void resetNeighboursAt(int x, int y);

    public abstract  int getOffsetX();

    public abstract  int getOffsetY();


}

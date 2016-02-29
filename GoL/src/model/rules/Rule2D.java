package model.rules;

/**
 * Created by Andreas on 29.02.2016.
 */
public abstract class Rule2D implements Rule {

    protected final boolean[][] grid;
    protected final byte[][] neighbours;

    public Rule2D(boolean[][] grid, byte[][] neighbours){

        this.grid = grid;
        this.neighbours = neighbours;
    }


    public boolean[][] getGrid(){ return grid; };
}

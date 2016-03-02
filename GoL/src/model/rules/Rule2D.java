package model.rules;

/**
 * Created by Andreas on 29.02.2016.
 */
public abstract class Rule2D implements Rule {

    //region data fields
    protected final boolean[][] grid;
    protected final byte[][] neighbours;
    //endregion

    //region constructor
    public Rule2D(boolean[][] grid, byte[][] neighbours){

        this.grid = grid;
        this.neighbours = neighbours;
    }
    //endregion

    //region public methods
    public boolean[][] getGrid(){ return grid; };
    //endregion

    //region private methods
    //endregion
}

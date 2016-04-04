package model.rules;

/**
 * A base class for two-dimensional rules
 * Created on 29.02.2016.
 * @author The group through pair programming
 */
public abstract class Rule2D implements Rule {

    //region fields
    protected boolean[][] grid;
    protected byte[][] neighbours;
    protected String ruleText = "";
    //endregion

    //region constructor
    /**
     * Default constructor
     * @param grid the grid to be evolved
     * @param neighbours the neighbours grid used during evolution
     */
    public Rule2D(boolean[][] grid, byte[][] neighbours){

        this.grid = grid;
        this.neighbours = neighbours;
    }
    //endregion

    //region setters
    @Override
    public void setGrid(boolean[][] grid){this.grid = grid;}
    @Override
    public void setNeighbours(byte[][] neighbours){this.neighbours = neighbours;}
    //endregion

    //region getters
    @Override
    public boolean[][] getGrid(){ return grid; }
    @Override
    public byte[][] getNeighbours(){ return neighbours; }
    //endregion


    /**
     * @return The rule text of the rule
     */
    @Override
    public String toString(){
        return ruleText;
    }
}

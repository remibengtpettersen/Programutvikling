package model.rules;

/**
 * A base class for two-dimensional rules
 * Created on 29.02.2016.
 * @author The group through pair programming
 */
public abstract class Rule {

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
    public Rule(boolean[][] grid, byte[][] neighbours){

        this.grid = grid;
        this.neighbours = neighbours;
    }
    //endregion

    //region setters
    /**
     * Sets the reference to the grid to be evolved
     * @param grid Reference to grid
     */
    public void setGrid(boolean[][] grid){this.grid = grid;}

    /**
     * Sets the reference to the neighbourhood grid to be used during evolution
     * @param neighbours Reference to neighbourhood grid
     */
    public void setNeighbours(byte[][] neighbours){this.neighbours = neighbours;}
    //endregion

    //region getters
    /**
     * Gets the reference to the grid to be evolved
     */
    public boolean[][] getGrid(){ return grid; }

    /**
     * Gets the reference to the neighbourhood grid to be used during evolution
     */
    public byte[][] getNeighbours(){ return neighbours; }
    //endregion

    /**
     * Evolves the board one generation, based on the number of neighbours per cell.
     */
    public abstract void evolve();

    /**
     * @return The rule text of the rule
     */
    @Override
    public String toString(){
        return ruleText;
    }

    /**
     * Checks if this rule is equivalent to a rule text
     * @param otherRuleText
     * @return true if this rule's ruleText is equal to otherRuleText
     */
    public boolean isEqual(String otherRuleText){
        return ruleText.equalsIgnoreCase(otherRuleText);
    }
}

package model.rules;

import model.EvolveException;

/**
 * A base class for two-dimensional rules
 * Created on 29.02.2016.
 * @author The group through pair programming
 */
@Deprecated
public abstract class Rule {

    //region fields
    protected boolean[][] grid;
    protected byte[][] neighbours;
    protected String ruleText = "";
    //endregion

    //region constructor
    /**
     * Default constructor
     * @param grid The grid to be evolved
     * @param neighbours The neighbour grid used during evolution
     */
    public Rule(boolean[][] grid, byte[][] neighbours){

        this.grid = grid;
        this.neighbours = neighbours;
    }
    //endregion

    //region setters
    /**
     * Sets the reference to the grid to be evolved
     * @param grid Cell grid
     */
    public void setGrid(boolean[][] grid){this.grid = grid;}

    /**
     * Sets the reference to the neighbour grid to be used during evolution
     * @param neighbours Neighbour grid
     */
    public void setNeighbours(byte[][] neighbours){this.neighbours = neighbours;}
    //endregion

    //region getters
    /**
     * Gets the reference to the grid to be evolved
     * @return Cell grid
     */
    public boolean[][] getGrid(){ return grid; }

    /**
     * Gets the reference to the neighbour grid to be used during evolution
     * @return Neighbour grid
     */
    public byte[][] getNeighbours(){ return neighbours; }
    //endregion

    /**
     * Evolves the board one generation, based on the number of neighbours per cell.
     */
    public abstract void evolve() throws EvolveException;

    /**
     * Returns the rule text of the rule
     * @return Rule text
     */
    @Override
    public String toString(){
        return ruleText;
    }

    /**
     * Returns true if this rule's rule text is equivalent to another rule text
     * @param otherRuleText the other rule text
     * @return true if this rule's ruleText is equal to otherRuleText
     */
    public boolean isEqual(String otherRuleText){
        return ruleText.equalsIgnoreCase(otherRuleText);
    }
}
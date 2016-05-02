package model.rules;

import model.DynamicGameOfLife;
import model.EvolveException;
import model.GameOfLife;

/**
 * A base class for two-dimensional rules
 * Created on 29.02.2016.
 * @author The group through pair programming
 */
public abstract class Rule {

    //region fields
    //protected boolean[][] grid;
    //protected byte[][] neighbours;
    protected GameOfLife gol;
    protected String ruleText = "";
    //endregion

    //region constructor
    /**
     * Default constructor
     */
    public Rule(GameOfLife gol){

        this.gol = gol;
    }
    //endregion

    /**
     * Evolves the board one generation, based on the number of neighbours per cell.
     */
    public abstract void evolve(int start, int stop) throws EvolveException;

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

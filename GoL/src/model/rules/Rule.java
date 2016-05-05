package model.rules;

import model.EvolveException;
import model.GameOfLife;

/**
 * An abstract base class for all game rules
 */
public abstract class Rule {

    protected GameOfLife gol;
    protected String rulestring = "";

    /**
     * Default constructor.
     * Sets a reference to the GameOfLife object to evolve
     */
    public Rule(GameOfLife gol){

        this.gol = gol;
    }

    /**
     * Evolves the board one generation, based on the number of neighbours per cell.
     * The interval of columns to be evolved is specified through parameters,
     * to enable concurrent evolution of the game board.
     *
     * @param start First column from the left side to be evolved
     * @param stop Last column on the right side to be evolved
     * @throws EvolveException
     */
    public abstract void evolve(int start, int stop) throws EvolveException;

    /**
     * Returns the rulestring of this rule
     *
     * @return Rulestring
     */
    @Override
    public String toString(){
        return rulestring;
    }
}

package model;

import model.rules.Rule;

/**
 * Created by Andreas on 29.02.2016.
 */
public abstract class GameOfLife {

    protected Rule rule;

    protected int cellCount = 0;
    protected int iterationCount = 0;

    /**
     * Evolves the grid one generation
     */
    public void nextGeneration(){
        aggregateNeighbours();
        rule.evolve();
        iterationCount++;
    };

    /**
     * Counts the neighbours for each cell
     */
    public abstract void aggregateNeighbours();

    /**
     * Should be used to clear the grid of live cells
     */
    public void clearGrid(){
        iterationCount = 0;
    };

    /**
     * Should be used to set a spesific rule
     * @param ruleText the rule
     */
    public abstract void setRule(String ruleText);

    /**
     * Returns the cell count
     * @return the cell count
     */
    public int getCellCount() {
        return cellCount;
    }

    /**
     * @return the number of generations iterated since last clear
     */
    public int getIterationCount() { return iterationCount; }

    /**
     * Returns the Rule
     * @return the rule
     */
    public Rule getRule() {
        return rule;
    }
}

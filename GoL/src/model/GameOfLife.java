package model;

import model.rules.ClassicRule;
import model.rules.CustomRule;
import model.rules.HighLifeRule;
import model.rules.Rule;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author The group through pair programing.
 */
public abstract class GameOfLife {

    private int availableProcessors = Runtime.getRuntime().availableProcessors();
    private List<Thread> threads = new ArrayList<>();

    AtomicInteger cellCount = new AtomicInteger(0);
    protected Rule rule;

    /**
     * 
     */
    public abstract void nextGeneration();

    /**
     * Gets the array containing min row, max row, min column and max column for the active cells in game board.
     * @return
     */
    public abstract int[] getBoundingBox();

    /**
     * Gets the value of the property grid width.
     * @return A integer value for gridWidth
     */
    public abstract int getGridWidth();

    /**
     * Gets the value of the property grid height
     * @return A integer value for gridHeight
     */
    public abstract int getGridHeight();

    /**
     * Gets the value for the property neighbours.
     * @param x the x coordinate in game grid.
     * @param y the y coordinate in game grid.
     * @return
     */
    public abstract int getNeighboursAt(int x, int y);

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public abstract boolean isCellAlive(int x, int y);

    /**
     *
     * @return
     */
    public abstract GameOfLife clone();

    /**
     *
     * @param x
     * @param y
     */
    public abstract void setCellAlive(int x, int y);

    /**
     *
     * @param x
     * @param y
     */
    public abstract void setCellDead(int x, int y);

    /**
     *
     * @param x
     * @param y
     */
    public abstract void changeCellState(int x, int y);

    /**
     *
     */
    public abstract void clearGrid();

    /**
     *
     * @param x
     * @param y
     */
    public abstract void resetNeighboursAt(int x, int y);

    /**
     *
     * @return
     */
    public abstract  int getOffsetX();

    /**
     *
     * @return
     */
    public abstract  int getOffsetY();

    /**
     *
     * @param start
     * @param stop
     */
    public abstract void aggregateNeighbours(int start, int stop);

    /**
     *
     */
    public void createCountingThreads() {
        for (int i = 0; i < availableProcessors; i++) {
            final int finalI = i;
            threads.add(new Thread(() -> {
                int start = finalI * getGridWidth()/ availableProcessors;
                int stop = (finalI + 1) * getGridWidth()/ availableProcessors;
                start = (start == 0)? 1: start;
                stop = (stop == getGridWidth())?getGridWidth() - 1 : stop;
                aggregateNeighbours(start, stop);
            }));
        }
    }

    /**
     *
     */
    public void createEvolveThreads() {
        for (int i = 0; i < availableProcessors; i++) {
            final int finalI = i;
            threads.add(new Thread(() -> {
                try {
                    rule.evolve(finalI * getGridWidth()/ availableProcessors, (finalI + 1) * getGridWidth()/ availableProcessors);
                } catch (EvolveException e) {
                    e.printStackTrace();
                }
            }));
        }
    }

    /**
     *
     * @throws InterruptedException
     */
    public void runThreads() throws InterruptedException {
        for (Thread t : threads) {
            t.start();
        }

        // vent på at alle trådene har kjørt ferdig før vi returnerer
        for (Thread t : threads) {
            t.join();
        }
        threads.clear();
    }

    /**
     * Sets a specific rule to be used.
     *
     * @param ruleText The rule text
     */
    public void setRule(String ruleText) {

        ruleText = ruleText.toLowerCase();

        switch (ruleText) {
            case "classic":
                rule = new ClassicRule(this);
                break;
            case "highlife":
                rule = new HighLifeRule(this);
                break;
            default:
                rule = new CustomRule(this, ruleText);
        }
    }

    /**
     *
     * @param cellCount
     */
    public void setCellCount(int cellCount){
        this.cellCount.set(cellCount);
    }

    /**
     *
     * @return
     */
    public int getCellCount(){
        return cellCount.get();
    }

    /**
     * Returns the rule used for evolution
     *
     * @return The rule
     */
    public Rule getRule() {
        return rule;
    }
}

package model;

import model.rules.ClassicRule;
import model.rules.CustomRule;
import model.rules.HighLifeRule;
import model.rules.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Truls on 02/05/16.
 */
public abstract class GameOfLife {

    private int availableProcessors = Runtime.getRuntime().availableProcessors();
    private List<Thread> threads = new ArrayList<>();

    AtomicInteger cellCount = new AtomicInteger(0);
    protected Rule rule;


    public abstract void nextGeneration();

    public abstract void fitBoardToPattern();

    public abstract int[] getBoundingBox();

    public abstract int getGridWidth();

    public abstract int getGridHeight();

    public abstract int getNeighboursAt(int x, int y);

    public abstract boolean isCellAlive(int x, int y);

    public abstract GameOfLife clone();


    //public abstract void setGrid();



    public abstract void setCellAlive(int x, int y);

    public abstract void setCellDead(int x, int y);

    public abstract void changeCellState(int x, int y);

    public abstract void clearGrid();


    public abstract void resetNeighboursAt(int x, int y);

    public abstract  int getOffsetX();

    public abstract  int getOffsetY();

    public abstract void aggregateNeighbours(int start, int stop);

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

        //rule = new ClassicRule(this);

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
    public void setCellCount(int cellCount){
        this.cellCount.set(cellCount);
    }

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

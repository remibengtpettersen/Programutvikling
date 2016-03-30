package model;

import model.rules.ClassicRule;

/**
 * Created by Andreas on 29.02.2016.
 */
public class GameOfLife3D extends GameOfLife {

    private boolean[][][] grid;
    private byte[][][] neighbours;

    public GameOfLife3D(int width, int height, int depth) {
        createGameBoard(width, height, depth);
    }


    public void createGameBoard(int width, int height, int depth) {

    }

    @Override
    public void nextGeneration() {

    }

    @Override
    public void aggregateNeighbours() {

    }


    public byte[][] getNeighbours() {
        return new byte[0][];
    }


    public boolean[][] getGrid() {
        return new boolean[0][];
    }


    public void setGrid(boolean[][] grid) {

    }

    @Override
    public void clearGrid() {

    }

    @Override
    public void setRule(String ruleText) {

    }
}

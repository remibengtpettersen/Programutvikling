package model.rules;

/**
 * Created by remibengtpettersen on 12.02.2016.
 */
public interface Rule {

    void evolve();

    void setGrid(boolean[][] grid);

    void setNeighbours(byte[][] neighbours);
}

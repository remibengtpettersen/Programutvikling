package model.rules;

/**
 * Interface for both two- and three-dimensional rules
 * Created on 12.02.2016.
 * @author The group through pair programming
 */
public interface Rule {

    /**
     * Evolves the board one generation, based on the number of neighbours per cell.
     */
    void evolve();

    /**
     * Sets the reference to the grid to be evolved
     * @param grid Reference to grid
     */
    void setGrid(boolean[][] grid);

    /**
     * Sets the reference to the neighbourhood grid to be used during evolution
     * @param neighbours Reference to neighbourhood grid
     */
    void setNeighbours(byte[][] neighbours);

    /**
     * Gets the reference to the grid to be evolved
     */
    boolean[][] getGrid();

    /**
     * Gets the reference to the neighbourhood grid to be used during evolution
     */
    byte[][] getNeighbours();
}

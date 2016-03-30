package model.rules;

/**
 * Created on 12.02.2016.
 * This class is provides the default rules for game of life as defined by Conway.
 */
public class ClassicRule extends Rule2D {

    /**
     * The constructor of classic rule, this constructor allows to define the game board and
     * a neighbour array mirroring the game board with the numbers of neighbours per cell.
     *
     * @param grid The 2D array representing the game of life board
     * @param neighbours The 2D array representing the number of neighbours per cell.
     */
    public ClassicRule(boolean[][] grid, byte[][] neighbours){
        super(grid, neighbours);
    }

    /**
     * Evolves the board 1 generation forward, based on the number of neighbours per cell.
     */
    @Override
    public void evolve() {

        // Double for loop to iterate through the grid.
        for(int x = 0; x < grid.length; x++){
            for(int y = 0; y < grid[x].length; y++){

                // if a cell has 3 neighbours it wil become alive independent if it is alive or dead
                if(neighbours[x][y] == 3)
                    grid[x][y] = true;

                // If a cell has 2 neighbours it should either stay alive or stay dead, else it should die.
                else if (neighbours[x][y] != 2)
                    grid[x][y] = false;

                neighbours[x][y] = 0;
            }
        }

    }

    @Override
    public String toString(){
        return "B3/S23";
    }
}

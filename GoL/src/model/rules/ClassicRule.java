package model.rules;

import model.EvolveException;

/**
 * The original Game of Life rule made by John Conway in 1970. Rulestring: B3/S23
 * Created on 12.02.2016.
 * This class provides the default rules for game of life as defined by Conway.
 */
public class ClassicRule extends Rule {

    /**
     * ClassicRule constructor
     * @param grid The cell grid to be evolved
     * @param neighbours The neighbour grid used during evolution
     */
    public ClassicRule(boolean[][] grid, byte[][] neighbours){

        super(grid, neighbours);

        ruleText = "B3/S23";
    }

    @Override
    public void evolve() throws EvolveException {

        for(int x = 0; x < grid.length; x++){
            for(int y = 0; y < grid[x].length; y++){

                byte neighbourCount = neighbours[x][y];

                if (neighbourCount < 0 || neighbourCount > 8)
                    throw new EvolveException("Tried setting " + neighbourCount + " neighbours");

                // if a cell has 3 neighbours it wil become alive independent whether it's alive or dead
                else if (neighbourCount == 3) {
                    grid[x][y] = true;
                    System.out.print("1");
                }
                // if a cell has 2 neighbours it should either stay alive or stay dead, else it should die.
                else if (neighbourCount != 2) {
                    grid[x][y] = false;
                    System.out.print("0");
                }
                // reset neighbour count for this cell
                neighbours[x][y] = 0;
            }
        }

        System.out.println();
    }
}

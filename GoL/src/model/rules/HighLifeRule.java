package model.rules;

import model.EvolveException;

/**
 * A rule similar to the classic Conway rule, but will also allow cells to be born if they have 6 neighbours. Rulestring: B36/S23
 * Created by Andreas on 16.03.2016.
 */
public class HighLifeRule extends Rule {

    /**
     * Constructor for the High Life rule
     * @param grid reference to the cell grid to be evolved
     * @param neighbours reference to the neighbours grid used during evolution
     */
    public HighLifeRule(boolean[][] grid, byte[][] neighbours) {

        super(grid, neighbours);

        ruleText = "B36/S23";
    }

    @Override
    public void evolve() throws EvolveException {

        for(int x = 0; x < grid.length; x++){
            for(int y = 0; y < grid[x].length; y++){

                if (neighbours[x][y] < 0 || neighbours[x][y] > 8)
                    throw new EvolveException();

                // if a cell has 3 neighbours it wil become alive independent if it is alive or dead
                else if(neighbours[x][y] == 3)
                    grid[x][y] = true;

                // if a cell is dead and has 6 neighbours, it will become alive. This is highLife's only difference
                else if ((neighbours[x][y] == 6) && !grid[x][y])
                    grid[x][y] = true;

                    // if a cell has 2 neighbours it should either stay alive or stay dead, else it should die.
                else if (neighbours[x][y] != 2)
                    grid[x][y] = false;

                neighbours[x][y] = 0;
            }
        }
    }
}

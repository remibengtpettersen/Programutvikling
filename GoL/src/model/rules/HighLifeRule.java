package model.rules;

/**
 * Created by Andreas on 16.03.2016.
 */
public class HighLifeRule extends Rule2D {

    public HighLifeRule(boolean[][] grid, byte[][] neighbours) {
        super(grid, neighbours);
    }

    @Override
    public void evolve() {
        // Double for loop to iterate through the grid.
        for(int x = 0; x < grid.length; x++){
            for(int y = 0; y < grid[x].length; y++){

                // if a cell has 3 neighbours it wil become alive independent if it is alive or dead
                if(neighbours[x][y] == 3)
                    grid[x][y] = true;

                // if a cell is dead and has 6 neighbours, it will become alive. This is highLife's only difference
                else if ((neighbours[x][y] == 6) && grid[x][y] == false)
                    grid[x][y] = true;

                    // If a cell has 2 neighbours it should either stay alive or stay dead, else it should die.
                else if (neighbours[x][y] != 2)
                    grid[x][y] = false;



                neighbours[x][y] = 0;
            }
        }
    }
}

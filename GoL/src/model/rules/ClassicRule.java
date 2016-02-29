package model.rules;

/**
 * Created by remibengtpettersen on 12.02.2016.
 */
public class ClassicRule extends Rule2D {

    public ClassicRule(boolean[][] grid, byte[][] neighbours){

        super(grid, neighbours);
    }

    @Override
    public void evolve() {

        for(int x = 0; x < grid.length; x++){
            for(int y = 0; y < grid[x].length; y++){

                if(neighbours[x][y] == 3)
                    grid[x][y] = true;
                else if (neighbours[x][y] != 2)
                    grid[x][y] = false;

                neighbours[x][y] = 0;
            }
        }

    }
}

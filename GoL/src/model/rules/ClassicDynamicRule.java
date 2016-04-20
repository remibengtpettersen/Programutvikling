package model.rules;

import model.EvolveException;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Truls on 20/04/16.
 */
public class ClassicDynamicRule {


    private final String ruleText;
    private CopyOnWriteArrayList<CopyOnWriteArrayList<Boolean>> grid;
    private CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>> neighbours;

    /**
     * ClassicRule constructor
     * @param grid The cell grid to be evolved
     * @param neighbours The neighbour grid used during evolution
     */
    public ClassicDynamicRule(CopyOnWriteArrayList<CopyOnWriteArrayList<Boolean>> grid, CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>> neighbours){

        this.grid = grid;
        this.neighbours = neighbours;

        ruleText = "B3/S23";
    }


    public void evolve() throws EvolveException {

        for(int x = 0; x < grid.size(); x++){
            for(int y = 0; y < grid.get(x).size(); y++){

                int neighbourCount = neighbours.get(x).get(y);

                if (neighbourCount < 0 || neighbourCount > 8)
                    throw new EvolveException("Tried setting " + neighbourCount + " neighbours");

                    // if a cell has 3 neighbours it wil become alive independent whether it's alive or dead
                else if (neighbourCount == 3)
                    grid.get(x).set(y, true);

                    // if a cell has 2 neighbours it should either stay alive or stay dead, else it should die.
                else if (neighbourCount != 2)
                    grid.get(x).set(y, false);

                // reset neighbour count for this cell
                neighbours.get(x).set(y, 0);
            }
        }
    }
}

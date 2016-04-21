package model.rules;

import model.EvolveException;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Truls on 20/04/16.
 */
public class ClassicDynamicRule {


    private final String ruleText;
    private ArrayList<ArrayList<AtomicBoolean>> grid;
    private ArrayList<ArrayList<AtomicInteger>> neighbours;

    /**
     * ClassicRule constructor
     * @param grid The cell grid to be evolved
     * @param neighbours The neighbour grid used during evolution
     */
    public ClassicDynamicRule(ArrayList<ArrayList<AtomicBoolean>> grid, ArrayList<ArrayList<AtomicInteger>> neighbours){

        this.grid = grid;
        this.neighbours = neighbours;

        ruleText = "B3/S23";
    }


    public void evolve() throws EvolveException {

        for(int x = 0; x < grid.size(); x++){
            for(int y = 0; y < grid.get(x).size(); y++){

                int neighbourCount = neighbours.get(x).get(y).get();

                if (neighbourCount < 0 || neighbourCount > 8)
                    throw new EvolveException("Tried setting " + neighbourCount + " neighbours");

                    // if a cell has 3 neighbours it wil become alive independent whether it's alive or dead
                else if (neighbourCount == 3)
                    grid.get(x).get(y).set(true);

                    // if a cell has 2 neighbours it should either stay alive or stay dead, else it should die.
                else if (neighbourCount != 2)
                    grid.get(x).get(y).set(false);

                // reset neighbour count for this cell
                neighbours.get(x).get(y).set(0);
            }
        }
    }
}

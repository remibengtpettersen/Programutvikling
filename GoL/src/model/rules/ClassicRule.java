package model.rules;

import model.DynamicGameOfLife;
import model.EvolveException;
import model.GameOfLife;

/**
 * Created by Truls on 20/04/16.
 */
public class ClassicRule extends Rule {


    private final String ruleText;
    //private ArrayList<ArrayList<AtomicBoolean>> grid;
    //private ArrayList<ArrayList<AtomicInteger>> neighbours;

    /**
     * ClassicRule constructor
     */
    public ClassicRule(GameOfLife gol){

        super(gol);

        ruleText = "B3/S23";
    }

    @Override
    public void evolve(int start, int stop) throws EvolveException {

        for(int x = start; x < stop; x++){
            for(int y = 0; y < gol.getGridHeight(); y++){

                int neighbourCount = gol.getNeighboursAt(x,y);

                if (neighbourCount < 0 || neighbourCount > 8)
                {
                    gol.resetNeighboursAt(x,y);
                    throw new EvolveException("Tried setting " + neighbourCount + " neighbours");
                }

                    // if a cell has 3 neighbours it wil become alive independent whether it's alive or dead
                else if (neighbourCount == 3)
                    gol.setCellAlive(x,y);

                    // if a cell has 2 neighbours it should either stay alive or stay dead, else it should die.
                else if (neighbourCount != 2)
                    gol.setCellDead(x,y);

                // reset neighbour count for this cell
                gol.resetNeighboursAt(x,y);
            }
        }
    }


}

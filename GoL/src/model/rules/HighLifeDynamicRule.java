package model.rules;

import model.DynamicGameOfLife;
import model.EvolveException;

/**
 * A rule similar to the classic Conway rule, but will also allow cells to be born if they have 6 neighbours. Rulestring: B36/S23
 * Created by Andreas on 16.03.2016.
 */
public class HighLifeDynamicRule extends DynamicRule {

    /**
     * HighLife constructor
     */
    public HighLifeDynamicRule(DynamicGameOfLife gol) {

        super(gol);

        ruleText = "B36/S23";
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
                else if (neighbourCount == 3 )
                    gol.setCellAlive(x,y);

                else if (neighbourCount == 6 && !gol.isCellAlive(x,y))
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

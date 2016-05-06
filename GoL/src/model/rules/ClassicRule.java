package model.rules;

import model.EvolveException;
import model.GameOfLife;

/**
 * The original Game of Life rule made by John Conway in 1970. Rulestring: B3/S23
 */
public class ClassicRule extends Rule {

    /**
     * ClassicRule constructor.
     * Sets the rulestring to "B3/S23"
     */
    public ClassicRule(GameOfLife gol){
        super(gol);

        rulestring = RuleParser.CLASSIC_RULESTRING;
    }

    @Override
    public void evolve(int start, int stop) throws EvolveException {

        for(int x = start; x < stop; x++){
            for(int y = 0; y < gol.getGridHeight(); y++){

                int neighbourCount = gol.getNeighboursAt(x,y);

                // if a cell has an impossible number of neighbours, throw EvolveException
                if (neighbourCount < 0 || neighbourCount > 8)
                    throw new EvolveException("Tried setting " + neighbourCount + " neighbours");

                // if a cell has 3 neighbours, it wil become alive or stay alive independent whether it's alive or dead
                else if (neighbourCount == 3)
                    gol.setCellAlive(x,y);

                // if a cell has 2 neighbours, it should either stay alive or stay dead, else it should die.
                else if (neighbourCount != 2)
                    gol.setCellDead(x,y);

                // reset neighbour count for this cell
                gol.resetNeighboursAt(x,y);
            }
        }
    }
}

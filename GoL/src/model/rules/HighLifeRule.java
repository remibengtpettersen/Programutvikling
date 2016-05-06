package model.rules;

import model.EvolveException;
import model.GameOfLife;

/**
 * A rule similar to the classic Conway rule, but will also allow cells to be born if they have 6 neighbours. Rulestring: B36/S23
 */
public class HighLifeRule extends Rule {

    /**
     * HighLife constructor.
     * Sets the rulestring to "B36/S23"
     */
    public HighLifeRule(GameOfLife gol) {
        super(gol);

        rulestring = RuleParser.HIGHLIFE_RULESTRING;
    }

    @Override
    public void evolve(int start, int stop) throws EvolveException {

        for(int x = start; x < stop; x++){
            for(int y = 0; y < gol.getGridHeight(); y++){

                int neighbourCount = gol.getNeighboursAt(x,y);

                // if a cell has an impossible number of neighbours, throw EvolveException
                if (neighbourCount < 0 || neighbourCount > 8)
                    throw new EvolveException("Tried setting " + neighbourCount + " neighbours");

                // if a cell has 3 neighbours it will be born or stay alive
                else if (neighbourCount == 3 )
                    gol.setCellAlive(x,y);

                // if a dead cell has 6 neighbours, it will be born
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

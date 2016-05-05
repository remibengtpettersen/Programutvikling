package model.rules;

import model.DynamicGameOfLife;
import model.EvolveException;
import model.GameOfLife;
import tools.MessageBox;

/**
 * A custom rule based on a rulestring input from user.
 */
public class CustomRule extends Rule {

    private boolean[] shouldBeBorn;
    private boolean[] shouldSurvive;

    /**
     * CustomRule constructor.
     *
     * @param gol GameOfLife object to be sent to super
     * @param rawRuleText The input rulestring to be parsed
     */
    public CustomRule(GameOfLife gol, String rawRuleText) {
        super(gol);

        // format rulestring to standard Bx/Sx notation.
        try {
            rulestring = RuleParser.formatRuleText(rawRuleText);
        } catch (RuleFormatException e){
            rulestring = "B3/S23";
            MessageBox.alert(e.getMessage());
        }

        // parse the rulestring to the two boolean arrays shouldBeBorn and shouldSurvive
        shouldBeBorn = RuleParser.parseDigitsAfterChar(rulestring, 'B');
        shouldSurvive = RuleParser.parseDigitsAfterChar(rulestring, 'S');

        System.out.println("Parsed: " + rulestring);
    }

    /**
     * Evolves the grid one generation. The evolution rules are based on the
     * shouldBeBorn and shouldSurvive arrays which again are based on a custom rulestring
     */
    @Override
    public void evolve(int start, int stop) throws EvolveException {

        for(int x = start; x < stop; x++) {
            for (int y = 0; y < gol.getGridHeight(); y++) {

                int neighbourCount = gol.getNeighboursAt(x,y);

                // if a cell has an impossible number of neighbours, throw EvolveException
                if (neighbourCount < 0 || neighbourCount > 8)
                    throw new EvolveException("Tried setting " + neighbourCount + " neighbours");

                if(gol.isCellAlive(x,y)){                   // If cell is alive and
                    if(!shouldSurvive[neighbourCount])      // the cell isn't supposed to survive,
                        gol.setCellDead(x,y);               // the cell would die
                }
                else {                                      // If cell is dead and
                    if(shouldBeBorn[neighbourCount])        // the cell is supposed to be born,
                        gol.setCellAlive(x,y);              // the cell should be born
                }

                //resets number of neighbours
                gol.resetNeighboursAt(x,y);
            }
        }
    }
}
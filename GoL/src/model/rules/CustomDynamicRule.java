package model.rules;

import model.DynamicGameOfLife;
import model.EvolveException;

/**
 * A custom rule based on an input string
 * Created on 12.02.2016.
 * @author The group through pair programming.
 */
public class CustomDynamicRule extends DynamicRule {

    private boolean[] shouldBeBorn;
    private boolean[] shouldSurvive;

    /**
     * CustomRule constructor.
     * @param rawRuleText the input rule text
     */
    public CustomDynamicRule(DynamicGameOfLife gol, String rawRuleText) {

        super(gol);

        try {
            ruleText = RuleParser.formatRuleText(rawRuleText);
        } catch (RuleFormatException e){
            ruleText = "B3/S23";
            System.out.println(e.getMessage());
        }

        parseRuleText();

        System.out.println("Parsed: " + ruleText);
    }

    /**
     * Parses the rule text, converts it into the shouldBeBorn and shouldSurvive arrays to be used in evolve
     */
    private void parseRuleText(){

        shouldBeBorn = new boolean[9];
        shouldSurvive = new boolean[9];

        boolean isAfterB = false;
        boolean isAfterS = false;

        for(int i = 0; i < ruleText.length(); i++){

            char currentChar = ruleText.charAt(i);

            if(currentChar == 'B') {

                isAfterB = true;
                isAfterS = false;
            } else if(currentChar == 'S'){

                isAfterB = false;
                isAfterS = true;
            } else if(Character.isDigit(currentChar) && currentChar != '9'){

                int currentIndex = Character.getNumericValue(currentChar);

                if(isAfterB){
                    shouldBeBorn[currentIndex] = true;
                } else if(isAfterS) {
                    shouldSurvive[currentIndex] = true;
                }
            }
        }
    }

    /**
     * Evolves the grid one generation. The evolution rules are based on the
     * shouldBeBorn and shouldSurvive arrays which again are based on the rule text
     */
    @Override
    public void evolve(int start, int stop) throws EvolveException {

        for(int x = start; x < stop; x++) {
            for (int y = 0; y < gol.getGridHeight(); y++) {

                int neighbourCount = gol.getNeighboursAt(x,y);

                if (neighbourCount < 0 || neighbourCount > 8)
                    throw new EvolveException("Tried setting " + neighbourCount + " neighbours");

                if(gol.isCellAlive(x,y)){                           // If cell is alive
                    if(!shouldSurvive[neighbourCount])      // If the cell isn't supposed to survive,
                        gol.setCellDead(x,y);               // the cell would die
                }
                else {                                      // If cell is dead
                    if(shouldBeBorn[neighbourCount])        // If the cell is supposed to be born,
                        gol.setCellAlive(x,y);              // the cell should be born
                }

                gol.resetNeighboursAt(x,y);                //resets number of neighbours
            }
        }
    }
}
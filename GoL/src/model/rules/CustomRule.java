package model.rules;

/**
 * A custom rule based on an input string
 * Created on 12.02.2016.
 * @author The group through pair programming.
 */
public class CustomRule extends Rule {

    private boolean[] shouldBeBorn;
    private boolean[] shouldSurvive;

    /**
     * Constructor for the custom rule
     * @param grid reference to the cell grid to be evolved
     * @param neighbours reference to the neighbours grid used during evolution
     * @param rawRuleText the input rule text
     */
    public CustomRule(boolean[][] grid, byte[][] neighbours, String rawRuleText) {

        super(grid, neighbours);

        ruleText = RuleParser.formatRuleText(rawRuleText);
        parseRuleText();
    }

    /**
     * Parses the rule text, converts it into the shouldBeBorn and shouldSurvive arrays to be used in evolve
     */
    private void parseRuleText(){   //move this to RuleParser?

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

        System.out.println("Parsed: " + ruleText);
    }

    /**
     * Evolves the grid one generation. The evolution rules are based on the
     * shouldBeBorn and shouldSurvive arrays which again are based on the rule text
     */
    @Override
    public void evolve() {

        for(int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {

                byte neighbourCount = neighbours[x][y];

                if(grid[x][y]){                         // If cell is alive
                    if(!shouldSurvive[neighbourCount])  // If the cell isn't supposed to survive,
                        grid[x][y] = false;             // the cell would die
                }
                else {                                  // If cell is dead
                    if(shouldBeBorn[neighbourCount])    // If the cell is supposed to be born,
                        grid[x][y] = true;              // the cell should be born
                }

                neighbours[x][y] = 0;                   //resets number of neighbours
            }
        }
    }
}

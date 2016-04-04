package model.rules;

/**
 * A custom rule based on an input string
 * Created on 12.02.2016.
 * @author The group through pair programming.
 */
public class CustomRule extends Rule2D {

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

        ruleText = formatRuleText(rawRuleText);
        parseRuleText();
    }

    /**
     * Formats the rule text to be in the right order, with the B-section in front, followed by a "/", then the S-section
     * @param rawRuleText rule text to be formatted
     * @return formatted rule text
     */
    private String formatRuleText(String rawRuleText){

        rawRuleText = rawRuleText.toUpperCase();

        int bIndex = rawRuleText.indexOf('B');
        int sIndex = rawRuleText.indexOf('S');

        String newRuleText = "B";
        newRuleText += getFollowingDigits(rawRuleText, bIndex);
        newRuleText += "/S";
        newRuleText += getFollowingDigits(rawRuleText, sIndex);

        return newRuleText;
    }

    /**
     * Returns a substring of superRuleText entirely out of digits from 0 to 8, starting one character after index
     * @param superRuleText the original rule text
     * @param index the index for the character in front of the substring to be returned
     * @return a substring of digits 0 to 8
     */
    private String getFollowingDigits(String superRuleText, int index){

        String subRuleText = "";

        for(int i = ++index; i < superRuleText.length(); i++){

            char currentChar = superRuleText.charAt(i);

            if(Character.isDigit(currentChar) && currentChar != '9')
                subRuleText += currentChar;
            else
                break;
        }

        return subRuleText;
    }

    /**
     * Parses the rule text, converts it into the shouldBeBorn and shouldSurvive arrays to be used in evolve
     */
    private void parseRuleText(){

        shouldBeBorn = new boolean[9];
        shouldSurvive = new boolean[9];

        int bIndex = ruleText.indexOf('B');
        int sIndex = ruleText.indexOf('S');

        String bFollowing = getFollowingDigits(ruleText, bIndex);
        String sFollowing = getFollowingDigits(ruleText, sIndex);

        for(int i = 0; i < bFollowing.length(); i++){

            char currentChar = bFollowing.charAt(i);
            int currentInt = Character.getNumericValue(currentChar);

            shouldBeBorn[currentInt] = true;
        }

        for(int i = 0; i < sFollowing.length(); i++){

            char currentChar = sFollowing.charAt(i);
            int currentInt = Character.getNumericValue(currentChar);

            shouldSurvive[currentInt] = true;
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

package model.rules;

/**
 * Created on 12.02.2016.
 * @Author The group through pair programing.
 */
public class CustomRule extends Rule2D {

    private boolean[] shouldBeBorn;    //goes from 0 to 8 neighbours
    private boolean[] shouldSurvive;
    private String ruleText;

    /**
     * Constructor for the custom ruleText
     * @param grid the grid to be evolved
     * @param neighbours the neighbours grid used in evolve()
     * @param ruleText the text that containes the ruleText
     */
    public CustomRule(boolean[][] grid, byte[][] neighbours, String ruleText) {
        super(grid, neighbours);

        this.ruleText = format(ruleText);
        parseRuleText();
    }

    /**
     * Formats the ruleText to be in the right order, with B in front of S and separated with a "/".
     * @param ruleText the text to be formatted
     * @return The formatted string
     */
    private String format(String ruleText){

        String newRuleText = "B";

        ruleText = ruleText.toUpperCase();

        int bIndex = ruleText.indexOf('B');
        int sIndex = ruleText.indexOf('S');

        //System.out.println(bIndex + " " + sIndex);

        for(int i = bIndex; i < ruleText.length(); i++){

            if((i + 1) < ruleText.length()){
                if( Character.isDigit(ruleText.charAt(i + 1))) {

                    newRuleText += ruleText.charAt(i + 1);
                }
                else {
                    break;
                }
            }
        }

        newRuleText += "/S";

        for(int i = sIndex; i < ruleText.length(); i++){

            if((i + 1) < ruleText.length()){
                if( Character.isDigit(ruleText.charAt(i + 1))) {

                    newRuleText += ruleText.charAt(i + 1);
                }
                else {
                    break;
                }
            }
        }

        return newRuleText;
    }

    /**
     * converts the rule text into the arrays used in evolve
     */
    private void parseRuleText(){

        shouldBeBorn = new boolean[9];
        shouldSurvive = new boolean[9];

        int bIndex = ruleText.indexOf('B');
        int sIndex = ruleText.indexOf('S');

        for(int i = bIndex; i < ruleText.length(); i++){

            if((i + 1) < ruleText.length()){

                char nextChar = ruleText.charAt(i+1);

                if( Character.isDigit(nextChar) && (nextChar != '9')) {
                    shouldBeBorn[Character.getNumericValue(nextChar)] = true;
                }
                else {
                    break;
                }
            }
        }

        for(int i = sIndex; i < ruleText.length(); i++){

            if((i + 1) < ruleText.length()){

                char nextChar = ruleText.charAt(i+1);

                if( Character.isDigit(nextChar) && (nextChar != '9')) {
                    shouldSurvive[Character.getNumericValue(nextChar)] = true;
                }
                else {
                    break;
                }
            }
        }

        System.out.println("Parsed: " + ruleText);
    }


    @Override
    public void evolve() {
        // Double for loop to iterate through the grid.
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

                neighbours[x][y] = 0;                    //resets number of neighbours
            }
        }
    }


    @Override
    public String toString(){
        return ruleText;
    }


}

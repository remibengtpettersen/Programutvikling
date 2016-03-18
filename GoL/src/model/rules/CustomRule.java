package model.rules;

/**
 * Created on 12.02.2016.
 * @Author The group through pair programing.
 */
public class CustomRule extends Rule2D {

    private boolean[] shouldBeBorn;    //goes from 0 to 8 neighbours
    private boolean[] shouldSurvive;

    public CustomRule(boolean[][] grid, byte[][] neighbours, String ruleText) {
        super(grid, neighbours);

        parseRuleText(ruleText);
    }

    private void parseRuleText(String ruleText){

        shouldBeBorn = new boolean[9];
        shouldSurvive = new boolean[9];

        ruleText = ruleText.toUpperCase();

        int bIndex = ruleText.indexOf('B');
        int sIndex = ruleText.indexOf('S');

        //System.out.println(bIndex + " " + sIndex);

        for(int i = bIndex; i < ruleText.length(); i++){

            if((i + 1) < ruleText.length()){
                if( Character.isDigit(ruleText.charAt(i + 1))) {
                    shouldBeBorn[Character.getNumericValue(
                            ruleText.charAt(i + 1))] = true;
                }
                else {
                    break;
                }
            }
        }

        for(int i = sIndex; i < ruleText.length(); i++){

            if((i + 1) < ruleText.length()){
                if( Character.isDigit(ruleText.charAt(i + 1))) {
                    shouldSurvive[Character.getNumericValue(
                            ruleText.charAt(i + 1))] = true;
                }
                else {
                    break;
                }
            }
        }
        System.out.println("Parsed: " + ruleText);

        //B1234/S237
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


}

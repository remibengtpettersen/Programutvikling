package model.rules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Andreas on 12.04.2016.
 */
public class RuleParser {

    /**
     * Formats the rule text to be in the right order, with the B-section in front, followed by a "/", then the S-section
     * @param rawRuleText rule text to be formatted
     * @return formatted rule text
     */
    public static String formatRuleText(String rawRuleText){

        rawRuleText = rawRuleText.toUpperCase();
        String newRuleText = "";
        String birthDigits = "";
        String survivalDigits = "";

        if(rawRuleText.contains("B") && rawRuleText.contains("S")){

            Pattern ruleParameters = Pattern.compile("(?=.*B([0-8]*))(?=.*S([0-8]*)).*");
            Matcher ruleMatcher = ruleParameters.matcher(rawRuleText);

            if(ruleMatcher.matches()){

                birthDigits = ruleMatcher.group(1);
                survivalDigits = ruleMatcher.group(2);

            } else {
                System.out.println("Rule didn't match");
            }

        } else if (rawRuleText.contains("/")){

            String[] parameters = rawRuleText.split("/");

            if(parameters.length == 2){

                birthDigits = parameters[1];
                survivalDigits = parameters[0];
            } else {
                System.out.println("Rule had more slashes or some shit?");
            }
        }

        newRuleText = "B" + simplifyDigits(birthDigits) + "/S" + simplifyDigits(survivalDigits);

        return newRuleText;
    }

    /**
     * Returns a string with the same digits as in oldDigits, but in order, and just one occurence each
     * @param oldDigits the original string of digits
     * @return an ordered string of digits from oldDigits
     */
    private static String simplifyDigits(String oldDigits){

        if(oldDigits == null){
            return "";
        }

        String newDigits = "";

        for(int i = 0; i < 9; i++){
            if(oldDigits.contains(""+Character.forDigit(i, 10))){
                newDigits += i;
            }
        }

        return newDigits;
    }
}

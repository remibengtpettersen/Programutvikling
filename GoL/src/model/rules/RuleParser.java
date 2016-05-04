package model.rules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Functions responsible for formatting and parsing of rulestrings
 */
public class RuleParser {

    /**
     * Formats the rulestring to be in the right order, with the B-section (birth)
     * in front, followed by a "/", then the S-section (survival)
     *
     * @param rawRulestring Input rulestring to be formatted
     * @return Formatted rulestring
     */
    public static String formatRuleText(String rawRulestring) throws RuleFormatException {

        rawRulestring = rawRulestring.toUpperCase();
        String newRuleText = "";
        String birthDigits = "";
        String survivalDigits = "";

        // if the input rulestring contains both B and S
        if(rawRulestring.contains("B") && rawRulestring.contains("S")){

            // find groups of digits. One group after B, and one group after S
            Pattern ruleParameters = Pattern.compile("(?=.*B([0-8]*))(?=.*S([0-8]*)).*");
            Matcher ruleMatcher = ruleParameters.matcher(rawRulestring);

            if(ruleMatcher.matches()){

                birthDigits = ruleMatcher.group(1);
                survivalDigits = ruleMatcher.group(2);
            }
            else
                throw new RuleFormatException(rawRulestring);
        }
        // if the input rulestring does not contain B and S, but contains /
        else if (rawRulestring.contains("/")){

            String[] ruleParameters = rawRulestring.split("/");

            if(ruleParameters.length > 0)
                survivalDigits = ruleParameters[0];

            if(ruleParameters.length > 1)
                birthDigits = ruleParameters[1];
        }
        // if the input rulestring is unreadable
        else
            throw new RuleFormatException(rawRulestring);

        newRuleText = "B" + simplifyDigits(birthDigits) + "/S" + simplifyDigits(survivalDigits);

        return newRuleText;
    }

    /**
     * Returns a string with the same digits as in oldDigits, but in order, and just one occurrence each
     *
     * @param oldDigits Original string of digits
     * @return An ordered string of unique digits from oldDigits
     */
    private static String simplifyDigits(String oldDigits){

        if(oldDigits == null)
            return "";

        String newDigits = "";

        for(int i = 0; i <= 8; i++){
            if(oldDigits.contains(""+Character.forDigit(i, 10)))
                newDigits += i;
        }

        return newDigits;
    }

    /**
     * Parses a group of digits after a specified character to a boolean array.
     * The boolean array consists of 9 elements with corresponding digits 0 to 8. If digit occurs in rulestring, the element is true.
     *
     * @param rulestring String to search through
     * @param character Character before group of digits
     * @return Boolean array showing which digits were found in the string
     */
    public static boolean[] parseDigitsAfterChar(String rulestring, char character){

        boolean[] parsedDigits = new boolean[9];

        int characterIndex = rulestring.indexOf(character);

        // if the string does not contain the character, return the empty array
        if(characterIndex == -1)
            return parsedDigits;

        // check digits until a non-digit is reached
        for(int i = ++characterIndex; i < rulestring.length(); i++){

            char currentChar = rulestring.charAt(i);

            if(Character.isDigit(currentChar)) {

                // add digit to array
                int digit = Character.getNumericValue(currentChar);
                parsedDigits[digit] = true;
            }
            else
                break;
        }
        return parsedDigits;
    }
}

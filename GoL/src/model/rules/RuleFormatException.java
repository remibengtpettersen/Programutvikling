package model.rules;

/**
 * Created by Andreas on 13.04.2016.
 */
public class RuleFormatException extends Exception {

    public String errorMessage;

    /**
     * RuleFormatException constructor. Will set an error message, which is available from getMessage()
     * @param ruleText The rule text which couldn't be formatted
     */
    public RuleFormatException(String ruleText) {

        if(ruleText.isEmpty())
            errorMessage = "Couldn't format empty rule";
        else
            errorMessage = "Couldn't format \"" + ruleText + "\" as a rule";
    }

    /**
     * Returns the error message of the exception
     * @return The error message
     */
    @Override
    public String getMessage(){
        return errorMessage;
    }
}

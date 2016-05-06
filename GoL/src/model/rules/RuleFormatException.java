package model.rules;

import tools.MessageBox;

/**
 * Exception to be thrown in case of problems related to rule formatting or parsing
 */
public class RuleFormatException extends Exception {

    private String errorMessage;

    /**
     * RuleFormatException constructor. Will set an error message, which is available from getMessage()
     *
     * @param ruleText The rule text which couldn't be formatted
     */
    public RuleFormatException(String ruleText) {

        if(ruleText.isEmpty())
            errorMessage = "Couldn't format empty rule";
        else
            errorMessage = "Couldn't format \"" + ruleText + "\" as a rule";
    }

    /**
     * Returns the error message of the expectedException
     *
     * @return Error message
     */
    @Override
    public String getMessage(){
        return errorMessage;
    }
}

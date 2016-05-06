package model;

import java.io.IOException;

/**
 * Exception to be thrown in case of problems related to pattern parsing or loading
 */
public class PatternFormatException extends IOException {

    private String errorMessage;

    /**
     * PatternFormatException constructor. Called for exceptions related to the formatting or parsing of rules
     * Will set an error message, which is available from getMessage()
     * @param errorMessage The error message
     */
    public PatternFormatException(String errorMessage) {
        this.errorMessage  = errorMessage;
    }

    /**
     * Returns the error message of the expectedException
     * @return The error message
     */
    @Override
    public String getMessage() {
        return this.errorMessage;
    }
}

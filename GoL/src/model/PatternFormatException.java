package model;

import java.io.IOException;

/**
 * Created by remibengtpettersen on 14.03.2016.
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
     * Returns the error message of the exception
     * @return The error message
     */
    @Override
    public String getMessage() {
        return this.errorMessage;
    }
}

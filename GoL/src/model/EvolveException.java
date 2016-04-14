package model;

/**
 * Created by remibengtpettersen on 12.04.2016.
 */
public class EvolveException extends Exception {

    public String errorMessage;

    /**
     * EvolveException constructor. Called for exceptions related to cell evolution
     * Will set an error message, which is available from getMessage()
     * @param errorMessage The error message
     */
    public EvolveException(String errorMessage) {
        this.errorMessage = errorMessage;
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

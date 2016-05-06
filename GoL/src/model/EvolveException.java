package model;
/**
 * Exception to be thrown in case of problems related to evolution of the game board
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
     * Returns the error message of the expectedException
     * @return The error message
     */
    @Override
    public String getMessage() {
        return this.errorMessage;
    }
}

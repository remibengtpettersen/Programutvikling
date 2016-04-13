package model;

/**
 * Created by remibengtpettersen on 12.04.2016.
 */
public class EvolveException extends Exception {

    public String errorMessage;

    public EvolveException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}

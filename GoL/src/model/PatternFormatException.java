package model;

import java.io.IOException;

/**
 * Created by remibengtpettersen on 14.03.2016.
 */
public class PatternFormatException extends IOException {

    private String errorMessage;

    public PatternFormatException(String message) {
        this.errorMessage  = message;
    }

    public String getMessage() {
        return this.errorMessage;
    }
}

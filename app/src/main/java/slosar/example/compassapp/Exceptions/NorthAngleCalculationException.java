package slosar.example.compassapp.Exceptions;

/**
 * Exception - error during north angle calculation
 */
public class NorthAngleCalculationException extends Exception {

    public NorthAngleCalculationException(String cause) {
        super(cause);
    }
}

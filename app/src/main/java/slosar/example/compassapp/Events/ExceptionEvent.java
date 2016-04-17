package slosar.example.compassapp.Events;

/**
 * EventBus event object - exceptions
 */
public class ExceptionEvent {
    public Exception e;


    public ExceptionEvent(Exception e) {
        this.e = e;
    }
}

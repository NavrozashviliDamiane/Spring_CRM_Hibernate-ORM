package org.damiane.exception;

public class TraineeNotFoundException extends Exception {

    public TraineeNotFoundException() {
        super();
    }

    public TraineeNotFoundException(String message) {
        super(message);
    }

    public TraineeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TraineeNotFoundException(Throwable cause) {
        super(cause);
    }
}


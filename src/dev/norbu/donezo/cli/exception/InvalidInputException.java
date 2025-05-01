package dev.norbu.donezo.cli.exception;

public class InvalidInputException
        extends TaskManagementException {

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}

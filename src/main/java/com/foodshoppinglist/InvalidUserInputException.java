package com.foodshoppinglist;

/**
 * This exception is to be thrown when the user inputs an invalid string.
 */
public class InvalidUserInputException extends Exception {

    public InvalidUserInputException() {
        super();
    }

    public InvalidUserInputException(String message) {
        super(message);
    }
}

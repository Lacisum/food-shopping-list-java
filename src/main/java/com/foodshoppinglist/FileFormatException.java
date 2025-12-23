package com.foodshoppinglist;

/**
 * A class for exceptions about wrong file formats.
 */
public class FileFormatException extends Exception {

    public FileFormatException() {
        super();
    }

    public FileFormatException(String message) {
        super(message);
    }
}

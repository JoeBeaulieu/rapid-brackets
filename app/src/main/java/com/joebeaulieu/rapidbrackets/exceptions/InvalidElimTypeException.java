package com.joebeaulieu.rapidbrackets.exceptions;

/**
 * An {@code Exception} to be thrown when an invalid elimination type has been passed through the
 * system.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @since 1.0.0
 */
public class InvalidElimTypeException extends Exception{

    /**
     * The default constructor for the {@code InvalidElimTypeException} class.
     */
    public InvalidElimTypeException() {
    }

    /**
     * The parameterized constructor for the {@code InvalidElimTypeException} class which passes a
     * message to the {@code Exception} superclass.
     *
     * @param msg a String representation of the message to be passed to the Exception superclass
     */
    public InvalidElimTypeException(String msg) {
        super(msg);
    }
}

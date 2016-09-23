package com.joebeaulieu.rapidbrackets.exceptions;

/**
 * An {@code Exception} to be thrown when there is no active {@code Bracket}.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @since 1.0.0
 */
public class BracketNotCreatedException extends Exception {

    /**
     * The default constructor for the {@code BracketNotCreatedException} class.
     */
    public BracketNotCreatedException() {
    }

    /**
     * The parameterized constructor for the {@code BracketNotCreatedException} class which passes a
     * message to the {@code Exception} superclass.
     *
     * @param msg a String representation of the message to be passed to the Exception superclass
     */
    public BracketNotCreatedException(String msg) {
        super(msg);
    }
}

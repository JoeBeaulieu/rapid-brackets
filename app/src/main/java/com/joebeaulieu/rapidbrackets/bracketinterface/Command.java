package com.joebeaulieu.rapidbrackets.bracketinterface;

/**
 * The overlying {@code Command} class for the command design pattern. Stores the {@code Aggregator}
 * and enforces the inclusion of an {@code execute()} method for its subclasses.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @see Aggregator Aggregator
 * @since 1.0.0
 */
public abstract class Command {
    /**
     * The {@code Aggregator} to be accessed by all subclasses.
     */
    protected Aggregator agg;

    /**
     * The sole constructor for the {@code Command} abstract class. Initializes all class variables.
     *
     * @param agg the Aggregator to be accessed by all subclasses
     */
    public Command(Aggregator agg) {
        this.agg = agg;
    }

    /**
     * The {@code execute()} method to be utilized by all subclasses.
     *
     * @return returns an Object if information needs to be passed back to the caller, otherwise
     *         returns null
     */
    public abstract Object execute();
}

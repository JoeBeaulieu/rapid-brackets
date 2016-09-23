package com.joebeaulieu.rapidbrackets.bracketinterface;

import com.joebeaulieu.rapidbrackets.bracketds.Bracket;

/**
 * Contains, and grants access to, the {@code Bracket} that is currently being used by the application.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @see com.joebeaulieu.rapidbrackets.bracketds.Bracket Bracket
 * @since 1.0.0
 */
public class Aggregator {
    /**
     * The {@code Bracket} that is currently being used by the application.
     */
    private Bracket bracket;

    /**
     * The sole constructor for the {@code Aggregator} class. Initializes all class variables.
     *
     * @param bracket the Bracket currently being used by the application
     */
    public Aggregator(Bracket bracket) {
        this.bracket = bracket;
    }

    /**
     * Returns the current {@code Bracket}.
     *
     * @return returns the Bracket currently being used by the application
     */
    public Bracket getBracket() {
        return bracket;
    }
}

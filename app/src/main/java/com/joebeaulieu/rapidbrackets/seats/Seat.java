package com.joebeaulieu.rapidbrackets.seats;

/**
 * The interface for the different types of {@code Seat}s which make up the {@code Bracket}.
 * {@code Seat}s include: {@code Player}, {@code Remnant}, and {@code Bye}.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @since 1.0.0
 */
public interface Seat {

    /**
     * Sets the unique ID for the {@code Seat}, prepending it with an identifying {@code Character}
     * to denote its subtype. These {@code Character}s are either "p", "r", or "b" for {@code Player},
     * {@code Remnant}, or {@code Bye}, respectively.
     *
     * @param id a String representation of a portion of the unique ID for the Seat
     */
    void setID(String id);

    /**
     * Sets the tier of the {@code Seat}. Can be either {@code BracketInterface.WINNERS_BRACKET},
     * {@code BracketInterface.LOSERS_BRACKET}, or {@code BracketInterface.FINALISTS_SUBBRACKET}.
     *
     * @param tier the tier of the Seat
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#WINNERS_BRACKET      BracketInterface.WINNERS_BRACKET
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#LOSERS_BRACKET       BracketInterface.LOSERS_BRACKET
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#FINALISTS_SUBBRACKET BracketInterface.FINALISTS_SUBBRACKET
     */
    void setTier(int tier);

    /**
     * Returns the name associated with the {@code Seat}.
     *
     * @return a String representation of the name associated with the Seat
     */
    String getName();

    /**
     * Returns the seed associated with the {@code Seat}.
     *
     * @return the seed associated with the Seat
     */
    int getSeed();

    /**
     * Returns the unique ID associated with the {@code Seat}.
     *
     * @return a String representation of the unique ID associated with the Seat
     */
    String getID();

    /**
     * Returns the tier associated with the {@code Seat}.
     *
     * @return the tier associated with the Seat
     */
    int getTier();
}

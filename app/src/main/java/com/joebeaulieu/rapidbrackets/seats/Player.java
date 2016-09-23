package com.joebeaulieu.rapidbrackets.seats;

/**
 * A {@code Player} to be utilized as a {@code Seat} in the {@code Bracket}. Any currently standing
 * (not yet eliminated) {@code Seat} will be a {@code Player}. The exception to this would be in
 * double elimination, where a {@code Player} has been eliminated from the Winners' {@code Bracket}
 * and moves on to the Losers' {@code Bracket}. That {@code Player} has not yet been eliminated from
 * the tournament. However, its last spot in the Winners' {@code Bracket} will not be a {@code Player},
 * it will be a {@code Remnant}. But, its new spot in the Losers' {@code Bracket} will be a {@code Player}.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @see Remnant Remnant
 * @since 1.0.0
 */
public class Player implements Seat {
    /**
     * The name of the {@code Player}.
     */
    private String name;

    /**
     * The seed of the {@code Player}.
     */
    private int seed;

    /**
     * The ID of the {@code Player}. Of the form pXYY where
     * X is the column number and YY is the row number.
     */
    private String id;

    /**
     * The tier of the node in the overarching structure of the {@code Bracket}. Possible values are
     * {@code BracketInterface.WINNERS_BRACKET}, {@code BracketInterface.LOSERS_BRACKET}, and
     * {@code BracketInterface.FINALISTS_SUBBRACKET}.
     *
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#WINNERS_BRACKET      BracketInterface.WINNERS_BRACKET
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#LOSERS_BRACKET       BracketInterface.LOSERS_BRACKET
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#FINALISTS_SUBBRACKET BracketInterface.FINALISTS_SUBBRACKET
     */
    private int tier;

    /**
     * The constructor for creating a brand new {@code Player}.
     *
     * @param name a String representation of the name of the Player
     * @param seed the seed of the Player
     */
    public Player(String name, int seed) {
        this.name = name;
        this.seed = seed;
    }

    /**
     * A parameterized constructor for recreating a {@code Player} from the {@code SQLiteDatabase}.
     * Initializes all class variables.
     *
     * @param name a String representation of the name of the Player
     * @param id   a String representation of the Player's ID
     * @param tier the tier of the Player. Denotes whether the Player is in the Winners', Losers',
     *             or Finalists' sub-Bracket
     */
    public Player(String name, String id, int tier) {
        this.name = name;
        this.id = id;
        this.tier = tier;
        // seeds are irrelevant at this point as they are only
        // used for the initial sorting of Seats in the Bracket
        seed = 0;
    }

    /**
     * Sets the unique ID for the {@code Player}, prepending it with a "p" for {@code Player}.
     *
     * @param id a String representation of a portion of the unique ID for the Player
     */
    @Override
    public void setID(String id) {
        this.id = "p" + id;
    }

    /**
     * Sets the tier of the {@code Player}. Can be either {@code BracketInterface.WINNERS_BRACKET},
     * {@code BracketInterface.LOSERS_BRACKET}, or {@code BracketInterface.FINALISTS_SUBBRACKET}.
     *
     * @param tier the tier of the Player
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#WINNERS_BRACKET      BracketInterface.WINNERS_BRACKET
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#LOSERS_BRACKET       BracketInterface.LOSERS_BRACKET
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#FINALISTS_SUBBRACKET BracketInterface.FINALISTS_SUBBRACKET
     */
    @Override
    public void setTier(int tier) {
        this.tier = tier;
    }

    /**
     * Returns the name associated with the {@code Player}.
     *
     * @return a String representation of the name associated with the Player
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the seed associated with the {@code Player}.
     *
     * @return the seed associated with the Player
     */
    @Override
    public int getSeed() {
        return seed;
    }

    /**
     * Returns the unique ID associated with the {@code Player}.
     *
     * @return a String representation of the unique ID associated with the Player
     */
    @Override
    public String getID() {
        return id;
    }

    /**
     * Returns the tier associated with the {@code Player}.
     *
     * @return the tier associated with the Player
     */
    @Override
    public int getTier() {
        return tier;
    }
}

package com.joebeaulieu.rapidbrackets.seats;

/**
 * A {@code Bye} to be utilized as a {@code Seat} in a {@code Bracket}. It's utility stems from the
 * fact that the number of "players," or in this case, {@code Seat}s, in a {@code Bracket} must always
 * be a power of 2. This is because a {@code Bracket} is a full Binary Tree. When the number of
 * {@code Player}s isn't a power of 2, the space must be filled in with a {@code Bye}. The {@code Bye}
 * ends up being a free win for the {@code Player}. Because of this, {@code Bye}s are allocated to
 * {@code Player}s in ascending order (best {@code Player} first) based on their seeds.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @see Player Player
 * @since 1.0.0
 */
public class Bye implements Seat {
    /**
     * The name of the {@code Bye}.
     */
    private String name;

    /**
     * The ID of the {@code Bye}. Of the form bXYY where X is the column number and YY is the row
     * number.
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
     * The default constructor for the {@code Bye} class. Initializes the {@code name} variable.
     */
    public Bye() {
        name = "BYE";
    }

    /**
     * A parameterized constructor for recreating a {@code Bye} from the {@code SQLiteDatabase}.
     * Initializes all class variables.
     *
     * @param id   a String representation of the unique ID for the Bye
     * @param tier the tier this Bye belongs to
     */
    public Bye(String id, int tier) {
        name = "BYE";
        this.id = id;
        this.tier = tier;
    }

    /**
     * Sets the unique ID for the {@code Bye}, prepending it with a "b" for {@code Bye}.
     *
     * @param id a String representation of a portion of the unique ID for the Bye
     */
    @Override
    public void setID(String id) {
        this.id = "b" + id;
    }

    /**
     * Sets the tier of the {@code Bye}. Can be either {@code BracketInterface.WINNERS_BRACKET},
     * {@code BracketInterface.LOSERS_BRACKET}, or {@code BracketInterface.FINALISTS_SUBBRACKET}.
     *
     * @param tier the tier of the Bye
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#WINNERS_BRACKET      BracketInterface.WINNERS_BRACKET
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#LOSERS_BRACKET       BracketInterface.LOSERS_BRACKET
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#FINALISTS_SUBBRACKET BracketInterface.FINALISTS_SUBBRACKET
     */
    @Override
    public void setTier(int tier) {
        this.tier = tier;
    }

    /**
     * Returns the name associated with the {@code Bye}.
     *
     * @return a String representation of the name associated with the Bye
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the seed associated with the {@code Bye}.
     *
     * @return the seed associated with the Bye
     */
    @Override
    public int getSeed() {
        return 999;
    }

    /**
     * Returns the unique ID associated with the {@code Bye}.
     *
     * @return a String representation of the unique ID associated with the Bye
     */
    @Override
    public String getID() {
        return id;
    }

    /**
     * Returns the tier associated with the {@code Bye}.
     *
     * @return the tier associated with the Bye
     */
    @Override
    public int getTier() {
        return tier;
    }
}

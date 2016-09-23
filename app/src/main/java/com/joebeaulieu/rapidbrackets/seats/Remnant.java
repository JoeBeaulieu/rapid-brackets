package com.joebeaulieu.rapidbrackets.seats;

/**
 * A {@code Remnant} to be utilized as a {@code Seat} in the {@code Bracket}. When a {@code Player}
 * advances through the {@code Bracket}, it leaves {@code Remnant}s in its wake. These {@code Remnant}s
 * represent the path the {@code Player} took to get to where they're at, and are simply just
 * "greyed-out" {@code Player} nodes with no functionality.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @see Player Player
 * @since 1.0.0
 */
public class Remnant implements Seat {
    /**
     * The name of the {@code Remnant}.
     */
    private String name;

    /**
     * The ID of the {@code Remnant}. Of the form rXYY where
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
     * A copy constructor which copies the given {@code Player}. Does not copy the {@code Player}'s
     * seed as it is irrelevant at this point as seeds are only used for the initial sorting of
     * {@code Seat}s in the {@code Bracket}.
     *
     * @param player the Player to copy
     * @see Player Player
     */
    public Remnant(Player player){
        name = player.getName();
        id = "r" + player.getID().substring(1);
        tier = player.getTier();
    }

    /**
     * A parameterized constructor for recreating a {@code Remnant} from the SQLiteDatabase.
     * Initializes all class variables.
     *
     * @param name a String representation of the name of the Remnant
     * @param id   a String representation of the Remnant's ID
     * @param tier the tier of the Player. Denotes whether the Player is in the Winners', Losers',
     *             or Finalists' sub-Bracket
     */
    public Remnant(String name, String id, int tier) {
        this.name = name;
        this.id = id;
        this.tier = tier;
    }

    /**
     * Sets the unique ID for the {@code Remnant}, prepending it with an "r" for {@code Remnant}.
     *
     * @param id a String representation of a portion of the unique ID for the Remnant
     */
    @Override
    public void setID(String id) {
        this.id = "r" + id;
    }

    /**
     * Sets the tier of the {@code Remnant}. Can be either {@code BracketInterface.WINNERS_BRACKET},
     * {@code BracketInterface.LOSERS_BRACKET}, or {@code BracketInterface.FINALISTS_SUBBRACKET}.
     *
     * @param tier the tier of the Remnant
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#WINNERS_BRACKET      BracketInterface.WINNERS_BRACKET
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#LOSERS_BRACKET       BracketInterface.LOSERS_BRACKET
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#FINALISTS_SUBBRACKET BracketInterface.FINALISTS_SUBBRACKET
     */
    @Override
    public void setTier(int tier) {
        this.tier = tier;
    }

    /**
     * Returns the name associated with the {@code Remnant}.
     *
     * @return a String representation of the name associated with the Remnant
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the seed associated with the {@code Remnant}.
     *
     * @return the seed associated with the Remnant
     */
    @Override
    public int getSeed(){
        // TODO throw NoSeedException
        return -1;
    }

    /**
     * Returns the unique ID associated with the {@code Remnant}.
     *
     * @return a String representation of the unique ID associated with the Remnant
     */
    @Override
    public String getID() {
        return id;
    }

    /**
     * Returns the tier associated with the {@code Remnant}.
     *
     * @return the tier associated with the Remnant
     */
    @Override
    public int getTier() {
        return tier;
    }
}

package com.joebeaulieu.rapidbrackets.bracketds;

/**
 *  This is an unused class for holding constant values used throughout the {@code Bracket} data
 *  structure and interface.
 *
 *  @author Joe Beaulieu
 */
public enum BracketConstant {
    // Changing any of these constants will require an update of the database!
    SINGLE_ELIM(1),
    DOUBLE_ELIM(2),
    WINNERS_BRACKET(0),
    LOSERS_BRACKET(1),
    FINALISTS_BRACKET(2);

    /**
     * The value held by a particular {@code BracketConstant} constant.
     */
    private final int value;

    /**
     * A parameterized constructor for the {@code BracketConstant} class. This constructor is used
     * by the {@code BracketConstant} constants to create themselves. This constructor is implicitly
     * private.
     *
     * @param value the int to be assigned to the {@code BracketConstant} constant being constructed
     */
    BracketConstant(int value) {
        this.value = value;
    }

    /**
     * Returns the {@code int} values of the {@code BracketConstant} class's constants.
     * @return the {@code int} values of the {@code BracketConstant} class's constants
     */
    public int getIntValue() {
        return value;
    }

    /**
     * Returns {@code BracketConstant} constants. These values are converted from given {@code ints}.
     * @param num an int to be converted to a {@code BracketConstant} constant
     * @return    a BracketConstant constant converted from the given int
     */
    public BracketConstant fromInt(int num) {
        for (BracketConstant c : BracketConstant.values()) {
            if (c.value == num) {
                return c;
            }
        }
        return null;
    }
}

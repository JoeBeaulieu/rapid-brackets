package com.joebeaulieu.rapidbrackets.bracketds;

import com.joebeaulieu.rapidbrackets.seats.Seat;

/**
 * The interface for the different types of {@code Bracket}s the app supports. As of now, {@code BracketSE}
 * is the only implemented subclass.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @since 1.0.0
 */
public interface Bracket {

    /**
     * Takes the raw {@code Bracket} {@code Seat[]} and segments it into columns that represent each
     * "level" of the {@code Bracket} to later be displayed on-screen via the {@code CMDBuildBracketUI}
     * class.
     * <p>
     * Visually, it looks like:
     * <pre>
     *      [[P1, P2, P3, P4], [null, null], [null]]
     * </pre>
     * This appears on-screen like this ({@code null} values are shown as empty text):
     * <pre>
     *      P1
     *         > null
     *      P2
     *                > null
     *      P3
     *         > null
     *      P4
     * </pre>
     */
    void makeGrid();

    /**
     * Takes the current state of the {@code Bracket} and recreates it. The {@code Bracket}'s state
     * is comprised of {@code Seat}s from all levels of the {@code Bracket}, along with their
     * corresponding IDs and tier levels. This state is used to rebuild the original {@code Seat[][]}
     * of the {@code Bracket}, which is segmented into columns that represent each "level" of the
     * {@code Bracket} to later be displayed on-screen via the {@code CMDBuildBracketUI} class.
     * <p>
     * Visually, it looks like:
     * <pre>
     *      [[P1, P2, P3, P4], [null, null], [null]]
     * </pre>
     * This appears on-screen like this ({@code null} values are shown as empty text):
     * <pre>
     *      P1
     *         > null
     *      P2
     *                > null
     *      P3
     *         > null
     *      P4
     * </pre>
     *
     * @param seatNameState a String[] which represents the currently saved state of the Bracket in
     *                      terms of Seat names. This array does not contain information on the empty
     *                      places in the Bracket
     * @param seatIdState   a String[] which represents the IDs of each Seat in the Bracket. The ids
     *                      are of the format: L###, where L is a letter denoting the Seat type,
     *                      and ### are the column and row numbers, with the first digit being the
     *                      column, and the last two being the row. L can either be "p", "r", or "b"
     *                      for Player, Remnant, or Bye respectively. The index of each ID is the
     *                      index of its corresponding Seat in the seatNameState array
     * @param seatTierState an int[] which represents the sub-Bracket where each Seat is located. The
     *                      values can be either BracketInterface.WINNERS_BRACKET,
     *                      BracketInterface.LOSERS_BRACKET, or BracketInterface.FINALISTS_SUBBRACKET.
     *                      The index of each tier is the index of its corresponding Seat in the
     *                      seatNameState array
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#WINNERS_BRACKET      BracketInterface.WINNERS_BRACKET
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#LOSERS_BRACKET       BracketInterface.LOSERS_BRACKET
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#FINALISTS_SUBBRACKET BracketInterface.FINALISTS_SUBBRACKET
     */
    void reconstructGrid(String[] seatNameState, String[] seatIdState, int[] seatTierState);

    /**
     * Returns the name of the {@code Bracket}.
     *
     * @return the name of the Bracket
     */
    String getName();

    /**
     * Returns an entire column of the {@code Bracket} in the form of a {@code Seat[]}.
     *
     * @param i the index of the column to be returned
     * @return  the desired column as a Seat[]
     */
    Seat[] get(int i);

    /**
     * Returns an individual {@code Seat} from the {@code Bracket}.
     *
     * @param i the index of the row in which the desired Seat is located
     * @param j the index of the Seat to be returned
     * @return  the desired Seat
     */
    Seat get(int i, int j);

    /**
     * Returns the date when the {@code Bracket} was created; of the form: MM/DD/YYYY.
     *
     * @return a String representation of the date when the Bracket was created; of the form: MM/DD/YYYY
     */
    String getDateCreated();

    /**
     * Returns the elimination type of the {@code Bracket}. Can either be {@code BracketInterface.SINGLE_ELIM}
     * or {@code BracketInterface.DOUBLE_ELIM} for single and double elimination, respectively.
     *
     * @return the elimination type of the Bracket
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#SINGLE_ELIM BracketInterface.SINGLE_ELIM
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#DOUBLE_ELIM BracketInterface.DOUBLE_ELIM
     */
    int getElimType();

    /**
     * Returns the number of unique {@code Player}s in the {@code Bracket}. This number does not
     * include {@code Bye}s.
     *
     * @return the number of unique Players in the Bracket, not including Byes
     */
    int getNumPlayers();

    /**
     * "Places" a {@code Seat} at the given index in the {@code Bracket} {@code Seat[][]}.
     *
     * @param i    the target column to place the Seat
     * @param j    the index within the target column to place the Seat
     * @param seat the Seat to be placed in the Bracket
     */
    void set(int i, int j, Seat seat);

    /**
     * Returns the number of columns in the {@code Bracket}, which is representative of the number
     * of {@code Seat[]}s in the first dimension of the {@code Bracket}'s {@code Seat[][]}.
     *
     * @return the number of columns in the Bracket
     */
    int size();
}

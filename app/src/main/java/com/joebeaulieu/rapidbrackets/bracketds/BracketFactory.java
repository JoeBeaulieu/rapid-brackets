package com.joebeaulieu.rapidbrackets.bracketds;

import android.support.annotation.Nullable;

import com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface;

/**
 * This class creates {@code Bracket}s. It is an implementation of the factory class design pattern,
 * which is used to simplify {@code Bracket} creation.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @see Bracket Bracket
 * @since 1.0.0
 */
public class BracketFactory {

    /**
     * Creates a new {@code Bracket} with information parsed from an instance of the {@code NewBracket}
     * {@code AppCompatActivity}.
     *
     * @param type    the elimination type for the Bracket. Can be either BracketInterface.SINGLE_ELIM
     *                or BracketInterface.DOUBLE_ELIM
     * @param name    a String representation of the name of the Bracket
     * @param players a String[] containing the names of each Player in the Bracket, excluding Byes
     * @param seeds   an Integer[] containing the seeds of each Player in the Bracket (excluding
     *                Byes). The index of each seed is the index of its corresponding Player in
     *                the players array. The seeds array may contain null values. null values
     *                represent a Player that doesn't have a seed
     * @return        returns a Bracket, which is upcasted from a BracketSE, or null if an invalid
     *                type has been passed as a parameter
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#SINGLE_ELIM BracketInterface.SINGLE_ELIM
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#DOUBLE_ELIM BracketInterface.DOUBLE_ELIM
     */
    @Nullable
    public static Bracket getBracket(int type, String name, String[] players, Integer[] seeds) {
        switch (type) {
            case BracketInterface.SINGLE_ELIM: return new BracketSE(name, players, seeds, new PlanterSE());
            //case BracketInterface.DOUBLE_ELIM: return new BracketDE(players, seeds, new PlanterDE());
            default: return null;
        }
    }

    /**
     * Recreates a {@code Bracket} from the {@code SQLiteDatabase}.
     *
     * @param type          the elimination type for the Bracket. Can be either BracketInterface.SINGLE_ELIM
     *                      or BracketInterface.DOUBLE_ELIM
     * @param name          a String representation of the name of the Bracket
     * @param dateCreated   a String representation of the date the Bracket was created of the
     *                      form: MM/DD/YYYY
     * @param seatNameState a String[] which represents the currently saved state of the Bracket.
     *                      It contains a Seat name for each slot that is occupied in the Bracket.
     *                      This array does not contain information on the empty places in the Bracket
     * @param seatIdState   a String[] which represents the ids of each Seat in the Bracket. The
     *                      ids are of the format: L##, where L is a letter denoting the Seat type,
     *                      and ## are the column and row numbers respectively. L can either be
     *                      "p", "r", or "b" for Player, Remnant, or Bye respectively. The index of
     *                      each id is the index of its corresponding Seat in the playerState array
     * @param seatTierState an int[] which represents the sub-Bracket where each Seat is located.
     *                      The values can be either BracketInterface.WINNERS_BRACKET,
     *                      BracketInterface.LOSERS_BRACKET, or BracketInterface.FINALISTS_SUBBRACKET.
     *                      The index of each tier is the index of its corresponding Seat in the
     *                      playerState array
     * @return              returns a Bracket, which is upcasted from a BracketSE, or null if an
     *                      invalid type has been passed as a parameter
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#SINGLE_ELIM          BracketInterface.SINGLE_ELIM
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#DOUBLE_ELIM          BracketInterface.DOUBLE_ELIM
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#WINNERS_BRACKET      BracketInterface.WINNERS_BRACKET
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#LOSERS_BRACKET       BracketInterface.LOSERS_BRACKET
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#FINALISTS_SUBBRACKET BracketInterface.FINALISTS_SUBBRACKET
     */
    @Nullable
    public static Bracket getBracket(int type, String name, String dateCreated, String[] seatNameState, String[] seatIdState, int[] seatTierState) {
        switch (type) {
            case BracketInterface.SINGLE_ELIM: return new BracketSE(name, dateCreated, seatNameState, seatIdState, seatTierState, new PlanterSE());
            //case BracketInterface.DOUBLE_ELIM: return new BracketDE(name, dateCreated, seatNameState, seatIdState, seatTierState, new PlanterDE());
            default: return null;
        }
    }
}

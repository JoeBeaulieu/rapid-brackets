package com.joebeaulieu.rapidbrackets.bracketinterface;

import android.content.Context;
import android.widget.LinearLayout;

import com.joebeaulieu.rapidbrackets.bracketds.BracketFactory;
import com.joebeaulieu.rapidbrackets.exceptions.BracketNotCreatedException;
import com.joebeaulieu.rapidbrackets.exceptions.InvalidElimTypeException;

import java.util.ArrayList;

/**
 * The class that the client code uses to interact with the {@code Bracket}. This is an adapter class
 * at the top level of the underlying command design pattern. It is between the client code and the
 * {@code Invoker}, and in some cases the client code and the {@code BracketFactory}. This class further
 * decouples the client from the underlying structure.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @see Invoker Invoker
 * @see com.joebeaulieu.rapidbrackets.bracketds.BracketFactory BracketFactory
 * @since 1.0.0
 */
public class BracketInterface {
    /**
     * The {@code Invoker} the {@code BracketInterface} class uses to invoke {@code Command}s.
     */
    private static Invoker invoker = new Invoker();

    /**
     * Used to denote a single elimination {@code Bracket}.
     */
    public static final int SINGLE_ELIM = 0;

    /**
     * Used to denote a double elimination {@code Bracket}.
     */
    public static final int DOUBLE_ELIM = 1;

    /**
     * Used to denote {@code Player}s in the Winners' {@code Bracket} tier.
     */
    public static final int WINNERS_BRACKET = 0;

    /**
     * Used to denote {@code Player}s in the Losers' {@code Bracket} tier.
     */
    public static final int LOSERS_BRACKET = 1;

    /**
     * Used to denote {@code Player}s in the Finalists' sub-{@code Bracket} tier.
      */
    public static final int FINALISTS_SUBBRACKET = 2;

    /**
     * Creates a new {@code Invoker} instance which is passed a new {@code Bracket}, created by the
     * {@code BracketFactory}, as a parameter.
     *
     * @param type    the elimination type of the Bracket. Either BracketInterface.SINGLE_ELIM or
     *                BracketInterface.DOUBLE_ELIM
     * @param name    a String representation of the name of the Bracket
     * @param players a String[] containing the names of all the Players in the Bracket. Does not
     *                include Byes
     * @param seeds   an Integer[] containing the seeds of each Player in the Bracket (excluding
     *                Byes). The index of each seed is the index of its corresponding Player in the
     *                players array. The seeds array may contain null values. null values represent
     *                a Player that doesn't have a seed
     * @throws InvalidElimTypeException thrown when the elimination type is not found in the
     *         BracketInterface class
     * @see #SINGLE_ELIM SINGLE_ELIM
     * @see #DOUBLE_ELIM DOUBLE_ELIM
     */
    public static void createBracket(int type, String name, String[] players, Integer[] seeds) throws InvalidElimTypeException{
        switch (type) {
            case SINGLE_ELIM: invoker = new Invoker(BracketFactory.getBracket(SINGLE_ELIM, name, players, seeds)); break;
            // case DOUBLE_ELIM: invoker = new Invoker(BracketFactory.getBracket(DOUBLE_ELIM, name, players, seeds)); break;
            default: throw new InvalidElimTypeException(type + "is not a valid bracket elimination type");
        }
    }

    /**
     * Creates a new {@code Invoker} instance which is passed a new {@code Bracket}, recreated by the
     * {@code BracketFactory}, as a parameter.
     *
     * @param type          the elimination type of the Bracket. Either BracketInterface.SINGLE_ELIM
     *                      or BracketInterface.DOUBLE_ELIM
     * @param name          a String representation of the name of the Bracket
     * @param dateCreated   a String representation of the date the Bracket was created; of the form:
     *                      MM/DD/YYYY
     * @param seatNameState a String[] which represents the currently saved state of the Bracket. It
     *                      contains a Seat name for each slot that is occupied in the Bracket. This
     *                      array does not contain information on the empty places in the Bracket
     * @param seatIdState   a String[] which represents the ids of each Seat in the Bracket. The ids
     *                      are of the format: L##, where L is a letter denoting the Seat type, and
     *                      ## are the column and row numbers respectively. L can either be "p", "r",
     *                      or "b" for Player, Remnant, or Bye respectively. The index of each id is
     *                      the index of its corresponding Seat in the playerState array
     * @param seatTierState an int[] which represents the sub-Bracket where each Seat is located. The
     *                      values can be either BracketInterface.WINNERS_BRACKET,
     *                      BracketInterface.LOSERS_BRACKET, or BracketInterface.FINALISTS_SUBBRACKET.
     *                      The index of each tier is the index of its corresponding Seat in the
     *                      playerState array
     * @throws InvalidElimTypeException thrown when the elimination type is not found in the
     *         BracketInterface class
     * @see #SINGLE_ELIM          SINGLE_ELIM
     * @see #DOUBLE_ELIM          DOUBLE_ELIM
     * @see #WINNERS_BRACKET      WINNERS_BRACKET
     * @see #LOSERS_BRACKET       LOSERS_BRACKET
     * @see #FINALISTS_SUBBRACKET FINALISTS_SUBBRACKET
     */
    public static void recreateBracket(int type, String name, String dateCreated, String[] seatNameState, String[] seatIdState, int[] seatTierState) throws InvalidElimTypeException{
        switch (type) {
            case SINGLE_ELIM: invoker = new Invoker(BracketFactory.getBracket(SINGLE_ELIM, name, dateCreated, seatNameState, seatIdState, seatTierState)); break;
            // case DOUBLE_ELIM: invoker = new Invoker(BracketFactory.getBracket(DOUBLE_ELIM, name, dateCreated, seatNameState, seatIdState, seatTierState)); break;
            default: throw new InvalidElimTypeException(type + "is not a valid bracket elimination type");
        }
    }

    /**
     * Creates the {@code Bracket} UI, adds it to the target {@code LinearLayout}, and displays it
     * on-screen.
     *
     * @param context the Context from which this method was called
     * @param layout  the LinearLayout in which this Bracket UI is to be created
     * @throws BracketNotCreatedException thrown if this method is called before the Bracket is
     *         Created
     */
    public static void buildBracketUI(Context context, LinearLayout layout) throws BracketNotCreatedException{
        invoker.buildBracketUI(context, layout);
    }

    /**
     * Moves a {@code Player} in the {@code Bracket} forwards and re-colors the on-screen nodes.
     * This method moves the {@code Player} both on-screen, and in the underlying {@code Bracket}
     * data structure.
     *
     * @param context the Context from which this method was called
     * @param layout  the LinearLayout in which this Player is to be moved
     * @param id      a String representation of the ID of the calling View and subsequently its
     *                corresponding Player's ID in the Bracket (not including the Player ID's
     *                leading Seat type identifier)
     * @throws BracketNotCreatedException thrown if this method is called before the Bracket is
     *         Created
     */
    public static void moveForward(Context context, LinearLayout layout, String id) throws BracketNotCreatedException{
        invoker.moveForward(context, layout, id);
    }

    /**
     * Moves a {@code Player} in the {@code Bracket} backwards and re-colors the on-screen nodes.
     * This method moves the {@code Player} both on-screen, and in the underlying {@code Bracket}
     * data structure.
     *
     * @param context the Context from which this method was called
     * @param layout  the LinearLayout in which this Player is to be moved
     * @param id      a String representation of the ID of the calling View and subsequently its
     *                corresponding Player's ID in the Bracket (not including the Player ID's
     *                leading Seat type identifier)
     * @throws BracketNotCreatedException thrown if this method is called before the Bracket is
     *         Created
     */
    public static void moveBack(Context context, LinearLayout layout, String id) throws BracketNotCreatedException{
        invoker.moveBack(context, layout, id);
    }

    /**
     * Deconstructs the {@code Bracket} and stores it in the {@code SQLiteDatabase}.
     *
     * @param context the Context from which this method was called
     * @throws BracketNotCreatedException thrown if this method is called before the Bracket is
     *         Created
     */
    public static void storeBracket(Context context) throws BracketNotCreatedException{
        invoker.storeBracket(context);
    }

    /**
     * Deletes the target {@code Bracket} from the {@code SQLiteDatabase}.
     *
     * @param context   the Context from which this method was called
     * @param bracketId the ID of the Bracket to be deleted
     */
    public static void deleteBracket(Context context, int bracketId) {
        invoker.deleteBracket(context, bracketId);
    }

    /**
     * Returns the current state of the {@code Bracket} from the underlying {@code Bracket} data
     * structure as an {@code ArrayList<Seat>} upcasted to an {@code ArrayList<Object>}. This
     * {@code ArrayList} does not include empty slots in the {@code Bracket}.
     *
     * @return the current state of the Bracket
     * @throws BracketNotCreatedException thrown if this method is called before the Bracket is
     *         Created
     */
    public static ArrayList getBracketState() throws BracketNotCreatedException {
        return (ArrayList)invoker.getBracketState();
    }
}

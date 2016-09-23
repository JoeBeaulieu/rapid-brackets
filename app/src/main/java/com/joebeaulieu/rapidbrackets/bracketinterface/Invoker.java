package com.joebeaulieu.rapidbrackets.bracketinterface;

import android.content.Context;
import android.widget.LinearLayout;

import com.joebeaulieu.rapidbrackets.bracketds.Bracket;
import com.joebeaulieu.rapidbrackets.exceptions.BracketNotCreatedException;

/**
 * Directly invokes all of the {@code Command}s for the {@code BracketInterface}.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @see BracketInterface BracketInterface
 * @see Command          Command
 * @since 1.0.0
 */
public class Invoker {
    /**
     * The {@code Aggregator} for the command design pattern.
     */
    private Aggregator agg;

    /**
     * The default constructor for the {@code Invoker} class.
     */
    public Invoker() {
    }

    /**
     * A parameterized constructor for the {@code Invoker} class. Passes the {@code Bracket} that is
     * currently being used by the application to the {@code Aggregator}.
     *
     * @param bracket the Bracket that is currently being used by the application
     */
    public Invoker(Bracket bracket) {
        agg = new Aggregator(bracket);
    }

    /**
     * Creates the {@code Bracket} UI, adds it to the target {@code LinearLayout}, and displays it
     * on-screen.
     *
     * @param context the Context from which this Command sequence was initiated
     * @param layout  the LinearLayout in which this Bracket UI is to be created
     * @throws BracketNotCreatedException thrown if this method is called before the Bracket is
     *         Created
     */
    public void buildBracketUI(Context context, LinearLayout layout) throws BracketNotCreatedException{
        if (agg == null) {
            throw new BracketNotCreatedException();
        } else {
            CMDBuildBracketUI cmd = new CMDBuildBracketUI(agg, context, layout);
            cmd.execute();
        }
    }

    /**
     * Moves a {@code Player} in the {@code Bracket} forwards and re-colors the on-screen nodes.
     * This method moves the {@code Player} both on-screen, and in the underlying {@code Bracket}
     * data structure.
     *
     * @param context the Context from which this Command sequence was initiated
     * @param layout  the LinearLayout in which this Player is to be moved
     * @param id      a String representation of the ID of the calling View and subsequently its
     *                corresponding Player's ID in the Bracket (not including the Player ID's
     *                leading Seat type identifier)
     * @throws BracketNotCreatedException thrown if this method is called before the Bracket is
     *         Created
     */
    public void moveForward(Context context, LinearLayout layout, String id) throws BracketNotCreatedException{
        if (agg == null) {
            throw new BracketNotCreatedException();
        } else {
            Bracket bracket = agg.getBracket();
            int intID = Integer.parseInt(id);
            int column = intID / 100;
            int row = intID % 100;

            // if not at end of bracket
            // && there exists an opponent for activated player on current level
            // && the match between activated player and opponent has not yet been decided
            if (column < agg.getBracket().size() - 1
                    && ((row % 2 == 0 && bracket.get(column, row + 1) != null)
                            || (row % 2 == 1 && bracket.get(column, row - 1) != null))
                    && bracket.get(column + 1, row / 2) == null) {
                CMDMoveForward cmd = new CMDMoveForward(agg, context, layout, id);
                cmd.execute();
            }
        }
    }

    /**
     * Moves a {@code Player} in the {@code Bracket} backwards and re-colors the on-screen nodes.
     * This method moves the {@code Player} both on-screen, and in the underlying {@code Bracket}
     * data structure.
     *
     * @param context the Context from which this Command sequence was initiated
     * @param layout  the LinearLayout in which this Player is to be moved
     * @param id      a String representation of the ID of the calling View and subsequently its
     *                corresponding Player's ID in the Bracket (not including the Player ID's
     *                leading Seat type identifier)
     * @throws BracketNotCreatedException thrown if this method is called before the Bracket is
     *         Created
     */
    public void moveBack(Context context, LinearLayout layout, String id) throws BracketNotCreatedException{
        if (agg == null) {
            throw new BracketNotCreatedException();
        } else {
            Bracket bracket = agg.getBracket();
            int intID = Integer.parseInt(id);
            int column = intID / 100;
            int row = intID % 100;

            if (column == agg.getBracket().size() - 1 ||
                    column > 0 && bracket.get(column + 1, row / 2) == null) {
                CMDMoveBack cmd = new CMDMoveBack(agg, context, layout, id);
                cmd.execute();
            }
        }
    }

    /**
     * Deconstructs the {@code Bracket} and stores it in the {@code SQLiteDatabase}.
     *
     * @param context the Context from which this command sequence was initiated
     * @throws BracketNotCreatedException thrown if this method is called before the Bracket is
     *         Created
     */
    public void storeBracket(Context context) throws BracketNotCreatedException{
        if (agg == null) {
            throw new BracketNotCreatedException();
        } else {
            CMDStoreBracket cmd = new CMDStoreBracket(agg, context);
            cmd.execute();
        }
    }

    /**
     * Deletes the target {@code Bracket} from the {@code SQLiteDatabase}.
     *
     * @param context   the Context from which this command sequence was initiated
     * @param bracketId the ID of the Bracket to be Deleted
     */
    public void deleteBracket(Context context, int bracketId) {
        CMDDeleteBracket cmd = new CMDDeleteBracket(agg, context, bracketId);
        cmd.execute();
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
    public Object getBracketState() throws BracketNotCreatedException{
        if (agg == null) {
            throw new BracketNotCreatedException();
        } else {
            return new CMDGetBracketState(agg).execute();
        }
    }
}

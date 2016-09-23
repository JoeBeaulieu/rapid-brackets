package com.joebeaulieu.rapidbrackets.bracketinterface;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.LinearLayout;

import com.joebeaulieu.rapidbrackets.bracketds.Bracket;
import com.joebeaulieu.rapidbrackets.activities.R;
import com.joebeaulieu.rapidbrackets.seats.Seat;

/**
 * Moves the target {@code Player} backwards in the {@code Bracket} both on-screen (via the given
 * {@code LinearLayout}) and in the underlying {@code Bracket} data structure (via the {@code Aggregator}).
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @see Aggregator Aggregator
 * @since 1.0.0
 */
public class CMDMoveBack extends Command{
    /**
     * The {@code Context} from which this {@code Command} sequence was initiated.
     */
    private Context context;

    /**
     * The {@code LinearLayout} in which the {@code Player} is to be moved.
     */
    private LinearLayout layout;

    /**
     * A {@code String} representation of ID of the calling {@code View} and subsequently its
     * corresponding {@code Player}'s ID in the {@code Bracket} (not including the {@code Player}
     * ID's leading {@code Seat} type identifier).
     */
    private String id;

    /**
     * The sole constructor for the {@code CMDMoveBack} class. Initializes all class variables and
     * passes the {@code Aggregator} to the {@code Command} superclass.
     *
     * @param agg     the Aggregator for the command design pattern
     * @param context the Context from which this Command sequence was initiated
     * @param layout  the LinearLayout in which the Player is to be moved
     * @param id      a String representation of the ID of the calling View and subsequently its
     *                corresponding Player's ID in the Bracket (not including the Player ID's
     *                leading Seat type identifier)
     */
    public CMDMoveBack(Aggregator agg, Context context, LinearLayout layout, String id) {
        super(agg);
        this.context = context;
        this.id = id;
        this.layout = layout;
    }

    /**
     * Moves a {@code Player} in the {@code Bracket} backwards and re-colors the on-screen nodes.
     * This method moves the {@code Bracket} both on-screen, and in the underlying {@code Bracket}
     * data structure.
     *
     * @return returns null
     */
    @Override
    public Object execute() {
        Bracket bracket = super.agg.getBracket();
        int idInt = Integer.parseInt(id);
        int column = idInt / 100;
        int row = idInt % 100;
        int prevId, prevLoserId, prevRow;
        int prevColumn = column - 1;
        Seat current = bracket.get(column, row);

        // Determines the correct row to move the Player to
        // by finding the Remnant in the previous row pair.
        // There are only two possible choices: top or bottom.
        if (bracket.get(prevColumn, row * 2).getID().startsWith("r")) {
            prevRow = row * 2;
        } else {
            prevRow = row * 2 + 1;
        }
        prevId = prevColumn * 100 + prevRow;

        try {
            // re-arrange bracket array
            bracket.set(prevColumn, prevRow, current);
            String id = "";
            id += prevColumn;
            id += prevRow;
            current.setID((id));
            bracket.set(column, row, null);

            // clear selected node
            Button node = ((Button) layout.findViewById(idInt));
            node.setText("");
            node.setOnClickListener(null);
            node.setOnLongClickListener(null);

            // set nodes from previous match
            Button prevNode = ((Button) layout.findViewById(prevId));
            Button prevLoser;
            if (prevId % 2 == 0) {
                prevLoserId = prevId + 1;
                prevLoser = ((Button) layout.findViewById(prevLoserId));
            } else {
                prevLoserId = prevId - 1;
                prevLoser = ((Button) layout.findViewById(prevLoserId));
            }
            if (!bracket.get(prevLoserId / 100, prevLoserId % 100).getID().startsWith("b")) {
                prevLoser.setTextColor(ContextCompat.getColor(context, R.color.active_node_text));
            }
            prevNode.setTextColor(ContextCompat.getColor(context, R.color.active_node_text));
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }
}

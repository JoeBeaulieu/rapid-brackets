package com.joebeaulieu.rapidbrackets.bracketinterface;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.joebeaulieu.rapidbrackets.bracketds.Bracket;
import com.joebeaulieu.rapidbrackets.activities.R;
import com.joebeaulieu.rapidbrackets.prompts.PromptError;
import com.joebeaulieu.rapidbrackets.exceptions.BracketNotCreatedException;
import com.joebeaulieu.rapidbrackets.seats.Player;
import com.joebeaulieu.rapidbrackets.seats.Remnant;
import com.joebeaulieu.rapidbrackets.seats.Seat;

/**
 * Moves the target {@code Player} forwards in the {@code Bracket} both on-screen (via the given
 * {@code LinearLayout}) and in the underlying {@code Bracket} data structure (via the {@code Aggregator}).
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @see Aggregator Aggregator
 * @since 1.0.0
 */
public class CMDMoveForward extends Command{
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
     * The sole constructor for the {@code CMDMoveForward} class. Initializes all class variables
     * and passes the {@code Aggregator} to the {@code Command} superclass.
     *
     * @param agg     the Aggregator for the command design pattern
     * @param context the Context from which this Command sequence was initiated
     * @param layout  the LinearLayout in which the Player is to be moved
     * @param id      a String representation of the ID of the calling View and subsequently its
     *                corresponding Player's ID in the Bracket (not including the Player ID's
     *                leading Seat type identifier)
     */
    public CMDMoveForward(Aggregator agg, Context context, LinearLayout layout, String id) {
        super(agg);
        this.context = context;
        this.id = id;
        this.layout = layout;
    }

    /**
     * Moves a {@code Player} in the {@code Bracket} forwards and re-colors the on-screen nodes.
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
        String nextID = "";
        nextID += column + 1;
        nextID += row / 2;
        Seat current = bracket.get(column, row);

        // does not advance a Seat if it is a Bye
        if (current.getID().startsWith("b")) {
            Toast advanceByeErr = Toast.makeText(context, R.string.toast_advance_bye, Toast.LENGTH_SHORT);
            advanceByeErr.show();
        } else {
            try {
                // re-arrange bracket array
                bracket.set(column + 1, row / 2, current);
                Remnant remnant = new Remnant((Player) current);
                current.setID(nextID);
                remnant.setID(id);
                bracket.set(column, row, remnant);

                // modify "forward" node
                Button node = ((Button) layout.findViewById((column + 1) * 100 + (row / 2)));
                node.setText(((Button) layout.findViewById(idInt)).getText());
                node.setTextColor(ContextCompat.getColor(context, R.color.active_node_text));
                node.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String id = "";
                            id += v.getId();
                            BracketInterface.moveForward(context, layout, id);
                        } catch (BracketNotCreatedException e) {
                            System.out.println(e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
                node.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View v) {
                        try {
                            String id = "";
                            id += v.getId();
                            BracketInterface.moveBack(context, layout, id);
                        } catch (BracketNotCreatedException e) {
                            System.out.println(e.getMessage());
                            e.printStackTrace();
                        }
                        return true;
                    }
                });

                // "grey" nodes from previous match
                Button remNode = ((Button) layout.findViewById(idInt));
                Button loser;
                if (idInt % 2 == 0) {
                    loser = ((Button) layout.findViewById(idInt + 1));
                } else {
                    loser = ((Button) layout.findViewById(idInt - 1));
                }
                remNode.setTextColor(ContextCompat.getColor(context, R.color.remnant_and_bye_text));
                loser.setTextColor(ContextCompat.getColor(context, R.color.remnant_and_bye_text));

                // display congratulations dialog
                if (column + 1 == bracket.size() - 1) {
                    DialogFragment dialog = PromptError.newInstance(
                            context.getResources().getString(R.string.prompt_title_congratulations),
                            bracket.get(column + 1, row / 2).getName() + " "
                                    + context.getResources().getString(R.string.prompt_new_champion));
                    dialog.show(((Activity) context).getFragmentManager(), "congratulations dialog");
                }
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

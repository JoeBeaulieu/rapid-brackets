package com.joebeaulieu.rapidbrackets.prompts;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface;
import com.joebeaulieu.rapidbrackets.activities.LoadBracket;
import com.joebeaulieu.rapidbrackets.activities.R;

/**
 * A prompt, in the form of an {@code AlertDialog}, which asks the user whether or not they want to
 * delete the selected {@code Bracket} from the {@code SQLiteDatabase}, then performs the requested
 * action. Deletion is handled via {@code BracketInterface.deleteBracket(Context, int)}.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#deleteBracket(Context, int)  BracketInterface.deleteBracket(Context, int)
 * @since 1.0.0
 */
public class PromptDelete extends DialogFragment{

    /**
     * Returns a new instance of the {@code PromptDelete} class.
     *
     * @param title     a String representation of the title for this prompt
     * @param msg       a String representation of the message to be displayed by this prompt
     * @param bracketId the ID of the Bracket to be deleted
     * @param rowId     the ID of the currently selected row
     * @return          returns a new instance of the PromptDelete class
     */
    public static PromptDelete newInstance(String title, String msg, int bracketId, int rowId) {
        PromptDelete prompt = new PromptDelete();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", msg);
        args.putInt("bracketId", bracketId);
        args.putInt("rowId", rowId);
        prompt.setArguments(args);
        return prompt;
    }

    /**
     * Creates and returns the {@code PromptDelete} {@code AlertDialog}. Adds the title and message,
     * and creates the {@code Buttons}, adding functionality. Is responsible for deleting the {@code Bracket}.
     *
     * @param savedInstanceState the Bundle in which unique information for this prompt is stored
     * @return                   returns the newly constructed AlertDialog
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#deleteBracket(Context, int)  BracketInterface.deleteBracket(Context, int)
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity activity = getActivity();
        String title = getArguments().getString("title");
        String msg = getArguments().getString("message");
        final int bracketId = getArguments().getInt("bracketId");
        final int rowId = getArguments().getInt("rowId");
        return new AlertDialog.Builder(getActivity(), R.style.UserPrompts)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BracketInterface.deleteBracket(activity, bracketId);
                        ((LoadBracket) activity).removeRow(rowId);
                        ((LoadBracket) activity).addAdditionalRows();
                        ((LoadBracket) activity).resetRowColors();
                        Toast bracketSaved = Toast.makeText(activity, R.string.toast_bracket_deleted, Toast.LENGTH_SHORT);
                        bracketSaved.show();
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((LoadBracket) activity).resetRowColors();
                    }
                })
                .create();
    }
}

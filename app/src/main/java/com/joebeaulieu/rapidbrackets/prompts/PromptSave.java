package com.joebeaulieu.rapidbrackets.prompts;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.widget.Toast;

import com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface;
import com.joebeaulieu.rapidbrackets.activities.R;
import com.joebeaulieu.rapidbrackets.exceptions.BracketNotCreatedException;

/**
 * A prompt, in the form of an {@code AlertDialog}, which asks the user whether or not they want to
 * save the active {@code Bracket} before leaving the current screen. Save functionality is provided
 * via {@code BracketInterface.storeBracket(Context)}.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#storeBracket(Context) BracketInterface.storeBracket(Context)
 * @since 1.0.0
 */
public class PromptSave extends DialogFragment{

    /**
     * Returns a new instance of the {@code PromptSave} class.
     *
     * @param title a String representation of the title for this prompt
     * @param msg   a String representation of the message to be displayed by this prompt
     * @return      returns a new instance of the PromptSave class
     */
    public static PromptSave newInstance(String title, String msg) {
        PromptSave prompt = new PromptSave();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", msg);
        prompt.setArguments(args);
        return prompt;
    }

    /**
     * Creates and returns the {@code PromptSave} {@code AlertDialog}. Adds the title and message,
     * and creates the {@code Buttons}, adding functionality. Is responsible for saving the {@code Bracket}
     * and back-navigation.
     *
     * @param savedInstanceState the Bundle in which unique information for this prompt is stored
     * @return                   returns the newly constructed AlertDialog
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#storeBracket(Context) BracketInterface.storeBracket(Context)
     */
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity activity = getActivity();
        String title = getArguments().getString("title");
        String msg = getArguments().getString("message");
        return new AlertDialog.Builder(getActivity(), R.style.UserPrompts)
                .setTitle(title)
                .setMessage(msg)
                .setNeutralButton(R.string.button_dont_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavUtils.navigateUpFromSameTask(activity);
                    }
                })
                .setPositiveButton(R.string.button_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            BracketInterface.storeBracket(activity);
                        } catch (BracketNotCreatedException e) {
                            System.out.println(e.getMessage());
                            e.printStackTrace();
                        }
                        Toast bracketSaved = Toast.makeText(activity, R.string.toast_bracket_saved, Toast.LENGTH_SHORT);
                        bracketSaved.show();
                        NavUtils.navigateUpFromSameTask(activity);
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dialog closes
                    }
                }).create();
    }
}

package com.joebeaulieu.rapidbrackets.prompts;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;

import com.joebeaulieu.rapidbrackets.activities.R;

/**
 * A prompt, in the form of an {@code AlertDialog}, which asks the user whether or not they want to
 * leave the current screen. Screen back-navigation is handled directly via
 * {@code NavUtils.navigateUpFromSameTask(Activity)}.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @see <a href="https://developer.android.com/reference/android/support/v4/app/NavUtils.html#navigateUpFromSameTask(android.app.Activity)">NavUtils.navigateUpFromSameTask(Activity)</a>
 * @since 1.0.0
 */
public class PromptLeaveScreen extends DialogFragment {

    /**
     * Returns a new instance of the {@code PromptLeaveScreen} class.
     *
     * @param title a String representation of the title for this prompt
     * @param msg   a String representation of the message to be displayed by this prompt
     * @return      returns a new instance of the PromptLeaveScreen class
     */
    public static PromptLeaveScreen newInstance(String title, String msg) {
        PromptLeaveScreen prompt = new PromptLeaveScreen();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", msg);
        prompt.setArguments(args);
        return prompt;
    }

    /**
     * Creates and returns the {@code PromptLeaveScreen} {@code AlertDialog}. Adds the title and
     * message, and creates the {@code Buttons}, adding functionality. Is responsible for back-navigation,
     * which is handled directly via {@code NavUtils.navigateUpFromSameTask(Activity)}.
     *
     * @param savedInstanceState the Bundle in which unique information for this prompt is stored
     * @return                   returns the newly constructed AlertDialog
     * @see <a href="https://developer.android.com/reference/android/support/v4/app/NavUtils.html#navigateUpFromSameTask(android.app.Activity)">NavUtils.navigateUpFromSameTask(Activity)</a>
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity activity = getActivity();
        String title = getArguments().getString("title");
        String msg = getArguments().getString("message");
        return new AlertDialog.Builder(getActivity(), R.style.UserPrompts)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavUtils.navigateUpFromSameTask(activity);
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dialog closes
                    }
                })
                .create();
    }
}

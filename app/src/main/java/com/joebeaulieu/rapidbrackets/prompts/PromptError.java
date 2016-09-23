package com.joebeaulieu.rapidbrackets.prompts;

import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.joebeaulieu.rapidbrackets.activities.R;

/**
 * A prompt, in the form of an {@code AlertDialog}, which notifies the user of an error. These errors
 * only deal with user entered text.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @since 1.0.0
 */
public class PromptError extends DialogFragment{

    /**
     * Returns a new instance of the {@code PromptError} class.
     *
     * @param title a String representation of the title for this prompt
     * @param msg   a String representation of the message to be displayed by this prompt
     * @return      returns a new instance of the PromptError class
     */
    public static PromptError newInstance(String title, String msg) {
        PromptError prompt = new PromptError();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", msg);
        prompt.setArguments(args);
        return prompt;
    }

    /**
     * Creates and returns the {@code PromptError} {@code AlertDialog}. Adds the title and message,
     * and creates the {@code Buttons}, adding functionality.
     *
     * @param savedInstanceState the Bundle in which unique information for this prompt is stored
     * @return                   returns the newly constructed AlertDialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String msg = getArguments().getString("message");
        return new AlertDialog.Builder(getActivity(), R.style.UserPrompts)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dialog closes
                    }
                })
                .create();
    }
}

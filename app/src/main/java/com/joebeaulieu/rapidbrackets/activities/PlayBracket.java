package com.joebeaulieu.rapidbrackets.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface;
import com.joebeaulieu.rapidbrackets.exceptions.BracketNotCreatedException;
import com.joebeaulieu.rapidbrackets.exceptions.InvalidElimTypeException;
import com.joebeaulieu.rapidbrackets.prompts.PromptSave;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is the {@code AppCompatActivity} for the application's "Load" screen. This is where the
 * {@code Bracket} is displayed on screen and played. The {@code Bracket} is also saved here, and
 * only here, but only if the user explicitly tells the application to do it via the save {@code Button},
 * or prompts in the form of {@code AlertDialog}s.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @since 1.0.0
 */
public class PlayBracket extends AppCompatActivity {
    /**
     * Keeps track of whether or not the {@code AppCompatActivity} was destroyed due to screen rotation.
     */
    private boolean wasScreenRotated;

    /**
     * Creates the {@code NewBracket} {@code AppCompatActivity}. This is where the user "plays through"
     * and saves the {@code Bracket}.
     *
     * @param savedInstanceState the Bundle that is associated with this AppCompatActivity. The only
     *                           information stored in this Bundle is a boolean value representing
     *                           whether or not the screen was rotated.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_bracket);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // creates or recreates the Bracket based on where the class was started
        // from, or whether or not the screen was rotated
        try {
            Intent intent = getIntent();
            String bracketName = intent.getStringExtra("bracketName");
            int elimType = intent.getIntExtra("elimType", -1);
            if (elimType == -1) {
                throw new InvalidElimTypeException("Possible values: 0, 1\tFound: -1\nelimType variable not initialized or stored");
            }
            try {
                wasScreenRotated = savedInstanceState.getBoolean("wasScreenRotated");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            if (wasScreenRotated){
                BracketInterface.recreateBracket(
                        elimType,
                        bracketName,
                        savedInstanceState.getString("bracketDateCreated"),
                        savedInstanceState.getStringArray("seatNameState"),
                        savedInstanceState.getStringArray("seatIdState"),
                        savedInstanceState.getIntArray("seatTierState"));
                wasScreenRotated = false;
            } else if (getCallingActivity() != null
                    && getCallingActivity().getClassName().equals(NewBracket.class.getName())) {
                // this lengthy conversion of the Serializable Extra "seeds" is to
                // deal with a bug in Android 4.X.X where directly casting an Integer[]
                // in the following manner: (Integer[]) intent.getSerializableExtra(string)
                // will throw a ClassCastException
                Object[] seedsObj = (Object[]) intent.getSerializableExtra("seeds");
                Integer[] seeds = Arrays.copyOf(seedsObj, seedsObj.length, Integer[].class);
                BracketInterface.createBracket(
                        elimType,
                        bracketName,
                        (String[]) intent.getSerializableExtra("names"),
                        seeds);
            } else if (getCallingActivity() != null
                    && getCallingActivity().getClassName().equals(LoadBracket.class.getName())) {
                BracketInterface.recreateBracket(
                        elimType,
                        bracketName,
                        "",
                        (String[]) intent.getSerializableExtra("seatNameState"),
                        (String[]) intent.getSerializableExtra("seatIdState"),
                        (int[]) intent.getSerializableExtra("seatTierState"));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (InvalidElimTypeException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        // build the Bracket on-screen
        try {
            BracketInterface.buildBracketUI(this, (LinearLayout) findViewById(R.id.comp_bracket));
        } catch (BracketNotCreatedException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Inflates the {@code Menu} for this {@code AppCompatActivity}.
     *
     * @param menu the Menu to be inflated
     * @return     a boolean value which tells the system whether or not to display the Menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_play_bracket, menu);
        return true;
    }

    /**
     * Handles clicks on the {@code MenuItem}s in the {@code Menu}. Creates and shows a {@code PromptSave}
     * instance if the home {@code Button} is pressed, and saves the {@code Bracket} if the save
     * {@code Button} is pressed.
     *
     * @param  item the MenuItem the user selected
     * @return      returns false from the superclass's onOptionsItemSelected(MenuItem) method to
     *              allow normal Menu processing to proceed, or true if the superclass consumed it
     *              there
     * @see <a href="https://developer.android.com/reference/android/app/Activity.html#onOptionsItemSelected(android.view.MenuItem)">Activity.onOptionsItemSelected(MenuItem)</a>
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                DialogFragment prompt = PromptSave.newInstance(
                        this.getString(R.string.prompt_title_wait),
                        this.getString(R.string.prompt_save_on_exit));
                prompt.show(getFragmentManager(), "save prompt");
                return true;
            case R.id.action_save:
                saveBracket();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Saves the {@code Bracket} via the {@code BracketInterface}. Displays a {@code Toast} after the
     * {@code Bracket} has been saved, and a {@code Toast} if it failed to.
     */
    public void saveBracket() {
        try {
            BracketInterface.storeBracket(this);
            Toast bracketSaved = Toast.makeText(this, R.string.toast_bracket_saved, Toast.LENGTH_SHORT);
            bracketSaved.show();
        } catch (BracketNotCreatedException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            Toast bracketNotSaved = Toast.makeText(this, R.string.toast_bracket_save_error, Toast.LENGTH_SHORT);
            bracketNotSaved.show();
        }
    }

    /**
     * Handles the device's back button being pressed. A prompt is displayed in the form of an
     * {@code AlertDialog} which warns the user that all information will be lost of they leave the
     * screen by navigating back to the {@code MainActivity}, and asks if they would like to save
     * the {@code Bracket} first.
     *
     * Note: {@code super.onBackPressed()} is never called because it destroys the {@code AppCompatActivity}.
     * {@code PromptSave} handles the "back" action per-se by calling the home/up action via
     * {@code NavUtils.navigateUpFromSameTask(Activity)}.
     *
     * @see <a href="https://developer.android.com/reference/android/app/Activity.html#onBackPressed()">Activity.onBackPressed()</a>
     * @see <a href="https://developer.android.com/reference/android/support/v4/app/NavUtils.html#navigateUpFromSameTask(android.app.Activity)">NavUtils.navigateUpFromSameTask(Activity)</a>
     */
    @Override
    public void onBackPressed() {
        DialogFragment prompt = PromptSave.newInstance(
                this.getString(R.string.prompt_title_wait),
                this.getString(R.string.prompt_save_on_exit));
        prompt.show(getFragmentManager(), "save prompt");
    }

    /**
     * Stores all the information required to recreate the on-screen {@code Bracket} when the
     * {@code AppCompatActivity} is destroyed due to system constraints.
     *
     * ex. screen rotation, the system needs to clear up resources, etc.
     *
     * @param savedInstanceState the Bundle which stores dynamic data from the AppCompatActivity
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        try {
            ArrayList bracketState = BracketInterface.getBracketState();
            String dateCreated = "";
            String[] seatNameState = new String[bracketState.size()];
            String[] seatIdState = new String[bracketState.size()];
            int[] seatTierState = new int[bracketState.size()];
            for (int i = 0; i < bracketState.size(); i++) {
                Object current = bracketState.get(i);
                if (current instanceof Object[]) {
                    if (i == 0) {
                        dateCreated = (String) (((Object[]) current)[0]);
                    } else {
                        seatNameState[i] = (String) (((Object[]) current)[0]);
                        seatIdState[i] = (String) (((Object[]) current)[1]);
                        seatTierState[i] = (int) (((Object[]) current)[2]);
                    }
                }
            }
            savedInstanceState.putString("bracketDateCreated", dateCreated);
            savedInstanceState.putStringArray("seatNameState", seatNameState);
            savedInstanceState.putStringArray("seatIdState", seatIdState);
            savedInstanceState.putIntArray("seatTierState", seatTierState);
            savedInstanceState.putBoolean("wasScreenRotated", true);
        } catch (BracketNotCreatedException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}

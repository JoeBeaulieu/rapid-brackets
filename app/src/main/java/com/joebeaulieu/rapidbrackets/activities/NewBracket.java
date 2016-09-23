package com.joebeaulieu.rapidbrackets.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.joebeaulieu.rapidbrackets.bracketinterface.BracketDbHelper;
import com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface;
import com.joebeaulieu.rapidbrackets.prompts.PromptError;
import com.joebeaulieu.rapidbrackets.prompts.PromptLeaveScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This is the {@code AppCompatActivity} for the application's "Create" screen where new {@code Bracket}s
 * are created. Here the user will enter a {@code Bracket} name, choose the elimination type and how
 * many {@code Player}s to include, and enter {@code Player} names and seeds. The {@code Bracket} is
 * not saved on this page. This is to avoid having a bloated table in the Load Bracket page with
 * {@code Bracket}s the user never wanted to save.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @since 1.0.0
 */
public class NewBracket extends AppCompatActivity {
    /**
     * Keeps track of the last position the numPlayersEntry {@code Spinner} was at before the
     * {@code AppCompatActivity} was destroyed due to screen rotation, etc.
     */
    private int lastPos;

    /**
     * The {@code LayoutParams} used by most of the {@code NewBracket}'s on-screen components.
     */
    private LinearLayout.LayoutParams componentParams;
    /**
     * The {@code LinearLayout} for the {@code Player} list, which includes the information entered
     * into the {@code Player} name {@code EditText}s and {@code Player} seed {@code Spinner}s.
     */
    private LinearLayout playerEntryLayout;

    /**
     * Creates the {@code NewBracket} {@code AppCompatActivity}. All class variables are initialized
     * here.
     *
     * @param savedInstanceState the Bundle that is associated with this activity. The only
     *                           information stored in this Bundle is the selectionId, which is
     *                           stored in onSaveInstanceState(Bundle) when the screen is rotated
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // gets overridden in onRestoreInstanceState(Bundle)
        lastPos = 0;
        componentParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        setContentView(R.layout.activity_new_bracket);
        playerEntryLayout = (LinearLayout) findViewById(R.id.comp_player_info_entry);
        makeSpinners(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Inflates the {@code Menu} for the {@code NewBracket} {@code AppCompatActivity}.
     *
     * @param menu the Menu to be inflated
     * @return     a boolean value which tells the system whether or not to display the Menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_bracket, menu);
        return true;
    }

    /**
     * Handles clicks on the {@code MenuItem}s in the {@code Menu}.
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
        switch(id) {
            case android.R.id.home:
                // prompt user as to whether or not they want to
                // save before exiting the screen
                DialogFragment prompt = PromptLeaveScreen.newInstance(
                        this.getString(R.string.prompt_title_wait),
                        this.getString(R.string.prompt_confirm_leave_screen));
                prompt.show(getFragmentManager(), "confirm leave screen prompt");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Cancels creation of the {@code Bracket} and opens the {@code MainActivity} {@code AppCompatActivity}.
     * A prompt is displayed in the form of an {@code AlertDialog} which warns the user that all
     * information will be lost if they leave the screen by navigating back to the {@code MainActivity}.
     *
     * @param view the View from which this method was called
     */
    public void cancelBracket(View view) {
        DialogFragment prompt = PromptLeaveScreen.newInstance(
                this.getString(R.string.prompt_title_wait),
                this.getString(R.string.prompt_confirm_leave_screen));
        prompt.show(getFragmentManager(), "confirm leave screen prompt");
    }

    /**
     * Parses and organizes the user-entered information from the screen and passes it to the
     * {@code PlayBracket} {@code AppCompatActivity} for actual {@code Bracket} creation. Performs
     * error checking to make sure that:
     * <p>
     * (a) all fields are filled
     * (b) all user-entered information contains only valid characters
     * (c) {@code Player} names and seeds are unique in the context of the {@code Bracket}
     * (d) the {@code Bracket} name is unique
     *
     * @param view the View from which this method was called
     */
    public void createBracket(View view) {
        int childCount = playerEntryLayout.getChildCount();
        View editBracketName = findViewById(R.id.edit_bracket_name);
        String bracketName = ((EditText) editBracketName).getText().toString();
        ArrayList<String> playerNames = new ArrayList<>();
        ArrayList<String> seedsSelected = new ArrayList<>();
        boolean dialogPopped = false;
        BracketDbHelper db = new BracketDbHelper(this);

        // fills playerNames and seedsSelected ArrayLists to check
        // for name and seed uniqueness
        for (int i = 0; i < childCount; i++) {
            LinearLayout playerComp = (LinearLayout) playerEntryLayout.getChildAt(i);
            int subChildCount = playerComp.getChildCount();
            for (int j = 0; j < subChildCount; j++) {
                View temp = playerComp.getChildAt(j);
                if (temp instanceof EditText) {
                    playerNames.add(((EditText) temp).getText().toString());
                } else if (temp instanceof Spinner) {
                    if (!((Spinner) temp).getSelectedItem().toString().equals("No Seed")) {
                        seedsSelected.add(((Spinner) temp).getSelectedItem().toString());
                    }
                }
            }
        }

        // error check the Bracket name, and check to see if it already exists
        if (bracketName.equals("")) {
            popEmptyNameDialog(getString(R.string.error_text_no_bracket_name));
            dialogPopped = true;
        } else if (!checkCharValidity(bracketName)) {
            popEmptyNameDialog(getString(R.string.error_text_invalid_char_bracket));
            dialogPopped = true;
        } else if (db.doesBracketExist(bracketName)) {
            popEmptyNameDialog(getString(R.string.error_text_duplicate_bracket_name));
            dialogPopped = true;
        } else {
            // check that player names are filled out
            for (int i = 0; i < childCount; i++) {
                LinearLayout playerComp = (LinearLayout) playerEntryLayout.getChildAt(i);
                int subChildCount = playerComp.getChildCount();
                for (int j = 0; j < subChildCount; j++) {
                    View temp = playerComp.getChildAt(j);
                    if (temp instanceof EditText) {
                        if (((EditText) temp).getText().toString().equals("")) {
                            dialogPopped = true;
                            popEmptyNameDialog(getString(R.string.error_text_player_names));
                            break;
                        } else if (!checkCharValidity(((EditText) temp).getText().toString())) {
                            dialogPopped = true;
                            popEmptyNameDialog(getString(R.string.error_text_invalid_char_player));
                            break;
                        }
                    }
                }
                if (dialogPopped) {
                    break;
                }
            }

            // check that player names and seeds are unique
            if (!dialogPopped) {
                HashMap<String, Integer> nameMap = new HashMap<>();
                HashMap<String, Integer> seedMap = new HashMap<>();

                for (int i = 0; i < playerNames.size(); i++) {
                    nameMap.put(playerNames.get(i), i);
                }
                for (int i = 0; i < seedsSelected.size(); i++) {
                    seedMap.put(seedsSelected.get(i), i);
                }
                if (nameMap.size() != playerNames.size()) {
                    dialogPopped = true;
                    popEmptyNameDialog(getString(R.string.error_text_non_unique_player_name));
                } else if (seedMap.size() != seedsSelected.size()) {
                    dialogPopped = true;
                    popEmptyNameDialog(getString(R.string.error_text_non_unique_seed));
                }
            }
        }

        // parse the user-entered information and pass it to the NewBracket class
        if (!dialogPopped) {
            String[] names = new String[childCount];
            Integer[] seeds = new Integer[childCount];
            String elimTypeSelection = ((Spinner) findViewById(R.id.spinner_elimination)).getSelectedItem().toString();
            int elimTypeInt;
            if (elimTypeSelection.equals("Single")) {
                elimTypeInt = BracketInterface.SINGLE_ELIM;
            } else {
                elimTypeInt = BracketInterface.DOUBLE_ELIM;
            }

            for (int i = 0; i < childCount; i++) {
                LinearLayout playerComp = (LinearLayout) playerEntryLayout.getChildAt(i);
                int subChildCount = playerComp.getChildCount();
                for (int j = 0; j < subChildCount; j++) {
                    View temp = playerComp.getChildAt(j);
                    if (temp instanceof EditText) {
                        String text = ((EditText) temp).getText().toString();
                        names[i] = text.trim();
                    } else if (temp instanceof Spinner) {
                        String text = ((Spinner) temp).getSelectedItem().toString();
                        try {
                            seeds[i] = Integer.parseInt(text);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            Intent intent = new Intent(this, PlayBracket.class);
            intent.putExtra("bracketName", bracketName);
            intent.putExtra("elimType", elimTypeInt);
            intent.putExtra("names", names);
            intent.putExtra("seeds", seeds);
            // the int value passed here is arbitrary as startActivityForResult()
            // is only being called over startActivity() so that getCallingActivity()
            // will not return null
            startActivityForResult(intent, 0);
        }
    }

    /**
     * Checks the validity of the target {@code String} and returns a {@code boolean} value representing
     * whether or not all of the {@code Character}s were contained in the valid {@code Character} list.
     *
     * @param name the String to be checked for Character validity
     * @return     a boolean value representing whether or not all of the Characters were contained
     *             in the valid Character list
     */
    // TODO replace hard-coded string with a string from R
    public boolean checkCharValidity(String name) {
        String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890 ";

        for (int i = 0; i < name.length(); i++) {
            if (!validChars.contains(String.valueOf(name.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Creates the "Elimination Type" and "Players" {@code Spinner}s. When creating the "Players"
     * {@code Spinner}, the {@code EditText}s and {@code Spinner}s for {@code Player} name and seed
     * entry are subsequently created.
     *
     * @param savedInstanceState a Bundle containing the names of all the Players and all of their
     *                           corresponding seeds. Used if the AppCompatActivity is destroyed.
     */
    public void makeSpinners(final Bundle savedInstanceState) {
        final Spinner elimTypeEntry = (Spinner) findViewById(R.id.spinner_elimination);
        final ArrayAdapter<CharSequence> elimTypeValues = ArrayAdapter.createFromResource(this,
                R.array.spinner_elimination_types, android.R.layout.simple_spinner_item);
        elimTypeValues.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        elimTypeEntry.setAdapter(elimTypeValues);
        AdapterView.OnItemSelectedListener elimListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == elimTypeValues.getPosition("Double")) {
                    DialogFragment prompt = PromptError.newInstance(
                            getBaseContext().getString(R.string.error_title),
                            getBaseContext().getString(R.string.error_double_elim_coming_soon));
                    prompt.show(getFragmentManager(), "error dialog");
                    elimTypeEntry.setSelection(elimTypeValues.getPosition("Single"));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
        elimTypeEntry.setOnItemSelectedListener(elimListener);

        Spinner numPlayersEntry = (Spinner) findViewById(R.id.spinner_num_players);
        ArrayAdapter<CharSequence> numPlayersValues = ArrayAdapter.createFromResource(this,
                R.array.spinner_num_players_arr, android.R.layout.simple_spinner_item);
        numPlayersValues.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numPlayersEntry.setAdapter(numPlayersValues);
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer selection = Integer.parseInt(parent.getItemAtPosition(position).toString());
                removePlayerList(playerEntryLayout);
                makePlayerList(playerEntryLayout, selection);
                // recreates the player name and seed lists if the Activity has
                // been destroyed and recreated
                if (position == lastPos) {
                    restorePlayerComp(savedInstanceState);
                }
                lastPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
        numPlayersEntry.setOnItemSelectedListener(listener);
    }

    /**
     * Removes the current {@code Player} list. This list is comprised of {@code EditText}s and
     * {@code Spinner}s containing the names and seeds of all the {@code Player}s to be included in
     * the {@code Bracket}.
     *
     * @param playerEntryLayout the LinearLayout from which the Player list is to be removed
     */
    public void removePlayerList(LinearLayout playerEntryLayout) {
        playerEntryLayout.removeAllViews();
    }

    /**
     * Creates the {@code Player} list. This list is comprised of {@code EditText}s and {@code Spinner}s
     * containing the names and seeds of {@code Player}s.
     *
     * @param playerList the LinearLayout from which the Player list is to be removed
     * @param numPlayers the number of Players to be in the Bracket
     */
    public void makePlayerList(LinearLayout playerList, int numPlayers) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        for(int i = 0; i < numPlayers; i++) {
            // initialize Player list row
            LinearLayout playerListRow = new LinearLayout(this);
            playerListRow.setLayoutParams(layoutParams);
            playerListRow.setOrientation(LinearLayout.HORIZONTAL);

            // fill Player list row
            EditText nameEntry = new EditText(this);
            nameEntry.setLayoutParams(componentParams);
            nameEntry.setMaxEms(R.integer.text_ems);
            nameEntry.setHint(R.string.hint_enter_name);
            nameEntry.setId(100 + i);

            Spinner seedEntry = new Spinner(this);
            seedEntry.setLayoutParams(componentParams);
            seedEntry.setId(200 + i);
            ArrayList<CharSequence> seedValues = new ArrayList<>(Arrays.asList(
                    (CharSequence[])getResources().getStringArray(R.array.spinner_seed_num)));
            ArrayAdapter<CharSequence> seedEntryAd = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    seedValues);
            seedEntryAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            seedEntry.setAdapter(seedEntryAd);
            populateSeedSpinner(seedEntryAd, numPlayers);

            // add row to Player list
            playerListRow.addView(nameEntry);
            playerListRow.addView(seedEntry);
            playerList.addView(playerListRow);
        }
    }

    /**
     * Populates the "Seed" {@code Spinner}s for every {@code Player}. Each {@code Spinner} is created
     * for a 2 {@code Player} {@code Bracket} by default. This method adds seed selections for more
     * than 2 {@code Player}s if more than two {@code Player}s exist.
     *
     * @param adapter   the ArrayAdapter of the target Spinner
     * @param selection the number of Players to be in the Bracket
     */
    public void populateSeedSpinner(ArrayAdapter<CharSequence> adapter, int selection) {
        for (int i = 3; i <= selection ; i++) {
            adapter.add(i + "");
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Displays an error message to the user in the form of an {@code AlertDialog}.
     *
     * @param msg a String representation of the message to be displayed by the AlertDialog
     */
    public void popEmptyNameDialog(String msg) {
        DialogFragment prompt = PromptError.newInstance(this.getString(R.string.error_title), msg);
        prompt.show(getFragmentManager(), "error dialog");
    }

    /**
     * Handles the back button being pressed. A prompt is displayed in the form of an {@code AlertDialog}
     * which warns the user that all information will be lost if they leave the screen by navigating
     * back to the {@code MainActivity} {@code AppCompatActivity}.
     *
     * Note: super.onBackPressed() is never called because it destroys the {@code AppCompatActivity}.
     * {@code PromptSave} handles the "back" action per-se by calling the home/up action via
     * {@code NavUtils.navigateUpFromSameTask(Activity)}.
     *
     * @see <a href="https://developer.android.com/reference/android/app/Activity.html#onBackPressed()">Activity.onBackPressed()</a>
     * @see <a href="https://developer.android.com/reference/android/support/v4/app/NavUtils.html#navigateUpFromSameTask(android.app.Activity)">NavUtils.navigateUpFromSameTask(Activity)</a>
     */
    @Override
    public void onBackPressed() {
        DialogFragment prompt = PromptLeaveScreen.newInstance(
                this.getString(R.string.prompt_title_wait),
                this.getString(R.string.prompt_confirm_leave_screen));
        prompt.show(getFragmentManager(), "leave screen confirmation prompt");
    }

    /**
     * Stores the current state of the {@code Player} list, which includes the information entered
     * into the {@code Player} name {@code EditText}s and {@code Player} seed {@code Spinner}s. Also
     * stores the last position the "Players" {@code Spinner} was on, which determines the number of
     * {@code Player}s in the {@code Bracket}.
     *
     * @param savedInstanceState the Bundle to which the Player list state will be stored
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("lastPos", lastPos);
        LinearLayout playerEntryLayout = (LinearLayout) findViewById(R.id.comp_player_info_entry);
        int childCount = playerEntryLayout.getChildCount();
        Integer textKey = 100;
        Integer spinnerKey = 200;
        for (int i = 0; i < childCount; i++) {
            LinearLayout playerComp = (LinearLayout) playerEntryLayout.getChildAt(i);
            int subChildCount = playerComp.getChildCount();
            for(int j = 0; j < subChildCount; j++) {
                View temp = playerComp.getChildAt(j);
                if (temp instanceof EditText) {
                    savedInstanceState.putString(textKey.toString(), ((EditText) temp).getText().toString());
                    textKey++;
                } else if (temp instanceof Spinner) {
                    savedInstanceState.putString(spinnerKey.toString(), ((Spinner) temp).getSelectedItem().toString());
                    spinnerKey++;
                }
            }
        }
    }

    /**
     * Restores the last position of the "Players" {@code Spinner}, which determines the number of
     * {@code Player}s in the {@code Bracket}.
     *
     * @param savedInstanceState the Bundle from which the last position of the "Players" Spinner is
     *                           extracted
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastPos = savedInstanceState.getInt("lastPos");
    }

    /**
     * Restores the previous state of the {@code Player} list should the {@code AppCompatActivity}
     * be destroyed. The {@code Player} list includes the information entered into the {@code Player}
     * name {@code EditText}s and {@code Player} seed {@code Spinner}s.
     * <p>
     * Note: {@code @SuppressWarnings("unchecked")} is used to suppress a {@code warning} on line:
     * <p>
     * {@code (Spinner) temp).setSelection(((ArrayAdapter) ((Spinner) temp).getAdapter()).getPosition(text));}
     * <p>
     * There is no way to check type {@code T} of {@code ArrayAdapter<T>} at runtime, as well as no
     * {@code Exception} to catch for it, therefore {@code @SuppressWarnings("unchecked")} is necessary
     * to get rid of the {@code warning}.
     *
     * @param savedInstanceState the Bundle from which the Player list information is extracted
     */
    @SuppressWarnings("unchecked")
    public void restorePlayerComp(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            LinearLayout playerEntryLayout = (LinearLayout) findViewById(R.id.comp_player_info_entry);
            int childCount = playerEntryLayout.getChildCount();
            Integer textKey = 100;
            Integer spinnerKey = 200;
            for (int i = 0; i < childCount; i++) {
                LinearLayout playerComp = (LinearLayout) playerEntryLayout.getChildAt(i);
                int subChildCount = playerComp.getChildCount();
                for (int j = 0; j < subChildCount; j++) {
                    View temp = playerComp.getChildAt(j);
                    if (temp instanceof EditText) {
                        String text = savedInstanceState.getString(textKey.toString());
                        ((EditText) temp).setText(text);
                        textKey++;
                    } else if (temp instanceof Spinner) {
                        String text = savedInstanceState.getString(spinnerKey.toString());
                        // false as second parameter in getPosition() caused graphical bug on Spinner
                        ((Spinner) temp).setSelection(((ArrayAdapter) ((Spinner) temp).getAdapter()).getPosition(text));
                        spinnerKey++;
                    }
                }
            }
        }
    }
}

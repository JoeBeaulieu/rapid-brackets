package com.joebeaulieu.rapidbrackets.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.joebeaulieu.rapidbrackets.bracketinterface.BracketDbHelper;
import com.joebeaulieu.rapidbrackets.prompts.PromptDelete;
import com.joebeaulieu.rapidbrackets.prompts.PromptError;

import java.util.HashMap;

/**
 * This is the {@code AppCompatActivity} for the application's "Load" screen. It contains a table
 * for selecting {@code Bracket}s to load, as well as "Cancel", "Delete", and "Load" {@code Button}s.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @since 1.0.0
 */
public class LoadBracket extends AppCompatActivity {
    /**
     * The background color for each column in the dark rows in the {@code Bracket} selection
     * table. Table row background colors alternate between dark and light, with dark being first.
     */
    private int columnDark;

    /**
     * The background color for each column in the light rows in the {@code Bracket} selection
     * table. Table row background colors alternate between dark and light, with dark being first.
     */
    private int columnLight;

    /**
     * The background color for each column in a user-selected (highlighted) row in the
     * {@code Bracket} selection table. Only one row can be selected at any given time.
     */
    private int columnHighlight;

    /**
     * The ID of the currently selected row. This ID directly corresponds to the ID of the
     * {@code Bracket} which information it's displaying.
     */
    private int selectionId;

    /**
     * The ID of the text {@code style} for all of the rows in the {@code Bracket} selection table.
     * This {@code style} is taken from {@code R.style}.
     */
    private int rowTextStyleId;

    /**
     * A {@code HashMap<Character, Integer>} in which the {@code Integer} value represents the size,
     * in pixels, of its corresponding {@code Character} key's representative glyph as displayed
     * on-screen given the current text {@code style}.
     */
    private HashMap<Character, Integer> charSizes;

    /**
     * A {@code HashMap<Character, Integer>} in which the {@code Integer} value represents the size,
     * in pixels, of the padding the Android system adds to its corresponding {@code Character}
     * key's representative glyph when displaying it on-screen given the current text {@code style}.
     */
    private HashMap<Character, Integer> charPadding;

    /**
     * Creates the {@code LoadBracket} {@code AppCompatActivity}. All text {@code color}s and
     * {@code style}s are initialized here. This is also where the on-screen text size calculation
     * and table creation methods are called.
     *
     * @param savedInstanceState the Bundle that is associated with the LoadBracket AppCompatActivity.
     *                           The only information stored in this Bundle is the selectionId class
     *                           variable, which is stored in onSaveInstanceState(Bundle) when the
     *                           screen is rotated
     * @see #onSaveInstanceState(Bundle) onSaveInstanceState(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            selectionId = savedInstanceState.getInt("selectionId");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_load_bracket);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        columnDark = R.color.table_column_dark;
        columnLight = R.color.table_column_light;
        columnHighlight = R.color.table_column_highlight;
        rowTextStyleId = R.style.MediumText;
        charSizes = getCharSizes();
        charPadding = getCharPadding();
        createTable();
    }

    /**
     * Inflates the {@code Menu} for the {@code LoadBracket} {@code AppCompatActivity}, initializing
     * its contents.
     *
     * @param menu the Menu to be inflated
     * @return     a boolean value which tells the system whether or not to display the Menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_load_bracket, menu);
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
        return super.onOptionsItemSelected(item);
    }

    // TODO refactor
    /**
     * Creates the {@code Bracket} selection table. This table displays information pertaining to
     * each {@code Bracket} contained in the {@code SQLiteDatabase}. The user can select a
     * {@code Bracket} and delete or load it with either the "Delete" or "Load" {@code Button}.
     */
    private void createTable() {
        final TableLayout tableHeader = (TableLayout) findViewById(R.id.table_header);
        TableLayout layout = (TableLayout) findViewById(R.id.table);
        BracketDbHelper db = new BracketDbHelper(this);
        int[] bracketIds = db.getAllBracketIds();
        final String[] bracketNames = db.getAllBracketNames();
        String[] bracketDates = db.getAllBracketDates();
        int[] bracketNumPlayers = db.getAllNumPlayers();
        int rowColor, rowColorType;

        // create column names
        TableRow columnNames = new TableRow(this);
        TextView bracketNameCol = new TextView(this);
        bracketNameCol.setBackgroundColor(ContextCompat.getColor(this, R.color.table_column_name));
        bracketNameCol.setGravity(Gravity.CENTER);
        TextViewCompat.setTextAppearance(bracketNameCol, R.style.LargeText);
        bracketNameCol.setText(R.string.load_bracket_name_column);
        bracketNameCol.setTextColor(ContextCompat.getColor(this, R.color.table_column_name_text));
        TableRow.LayoutParams nameColParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        nameColParams.setMargins(0, 0, 1, 0);
        bracketNameCol.setLayoutParams(nameColParams);
        columnNames.addView(bracketNameCol);

        TextView bracketPlayerCol = new TextView(this);
        bracketPlayerCol.setBackgroundColor(ContextCompat.getColor(this, R.color.table_column_name));
        bracketPlayerCol.setGravity(Gravity.CENTER);
        TextViewCompat.setTextAppearance(bracketPlayerCol, R.style.LargeText);
        bracketPlayerCol.setText(R.string.load_bracket_players_column);
        bracketPlayerCol.setTextColor(ContextCompat.getColor(this, R.color.table_column_name_text));
        TableRow.LayoutParams playerColParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        playerColParams.setMargins(0, 0, 1, 0);
        bracketPlayerCol.setLayoutParams(playerColParams);
        columnNames.addView(bracketPlayerCol);

        TextView bracketDateCol = new TextView(this);
        bracketDateCol.setBackgroundColor(ContextCompat.getColor(this, R.color.table_column_name));
        bracketDateCol.setGravity(Gravity.CENTER);
        TextViewCompat.setTextAppearance(bracketDateCol, R.style.LargeText);
        bracketDateCol.setText(R.string.load_bracket_date_column);
        bracketDateCol.setTextColor(ContextCompat.getColor(this, R.color.table_column_name_text));
        TableRow.LayoutParams dateColParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        bracketDateCol.setLayoutParams(dateColParams);
        columnNames.addView(bracketDateCol);
        tableHeader.addView(columnNames);

        // fill rows
        for (int i = 0; i < bracketIds.length; i++) {
            if (i % 2 == 0) {
                rowColor = columnDark;
                rowColorType = 1;
            } else {
                rowColor = columnLight;
                rowColorType = 2;
            }
            final TableRow row = new TableRow(this);
            row.setId(Integer.parseInt(String.valueOf(rowColorType) + String.valueOf(bracketIds[i])));

            final TextView name = new TextView(this);
            name.setBackgroundColor(ContextCompat.getColor(this, rowColor));
            TextViewCompat.setTextAppearance(name, rowTextStyleId);
            final int iCopy = i;
            name.post(new Runnable() {
                @Override
                public void run() {
                    name.setWidth(((TableRow) tableHeader.getChildAt(0)).getChildAt(0).getWidth());
                    name.setText(getCroppedNodeText(name, bracketNames[iCopy], ((TableRow) tableHeader.getChildAt(0)).getChildAt(0).getWidth()));
                }
            });
            name.setPadding(5, 0, 0, 0);
            TableRow.LayoutParams nameParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            nameParams.setMargins(0, 0, 1, 0);
            name.setLayoutParams(nameParams);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TableRow currentRow = (TableRow) v.getParent();
                    for (int j = 0; j < currentRow.getChildCount(); j++) {
                        currentRow.getChildAt(j).setBackgroundColor(ContextCompat.getColor(v.getContext(), columnHighlight));
                    }
                    if (currentRow.getId() != selectionId && selectionId > 0) {
                        TableRow prevRow = (TableRow) v.getRootView().findViewById(selectionId);
                        for (int j = 0; j < prevRow.getChildCount(); j++) {
                            if (String.valueOf(selectionId).substring(0, 1).equals("1")) {
                                prevRow.getChildAt(j).setBackgroundColor(ContextCompat.getColor(v.getContext(), columnDark));
                            } else {
                                prevRow.getChildAt(j).setBackgroundColor(ContextCompat.getColor(v.getContext(), columnLight));
                            }
                        }
                    }
                    selectionId = currentRow.getId();
                }
            });
            row.addView(name);

            final TextView numPlayers = new TextView(this);
            numPlayers.setBackgroundColor(ContextCompat.getColor(this, rowColor));
            TextViewCompat.setTextAppearance(numPlayers, rowTextStyleId);
            numPlayers.setText(String.valueOf(bracketNumPlayers[i]));
            numPlayers.setGravity(Gravity.CENTER_HORIZONTAL);
            numPlayers.post(new Runnable() {
                @Override
                public void run() {
                    numPlayers.setWidth(((TableRow) tableHeader.getChildAt(0)).getChildAt(1).getWidth());
                }
            });
            numPlayers.setPadding(5, 0, 0, 0);
            TableRow.LayoutParams numPlayersParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            numPlayersParams.setMargins(0, 0, 1, 0);
            numPlayers.setLayoutParams(numPlayersParams);
            numPlayers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TableRow currentRow = (TableRow) v.getParent();
                    for (int j = 0; j < currentRow.getChildCount(); j++) {
                        currentRow.getChildAt(j).setBackgroundColor(ContextCompat.getColor(v.getContext(), columnHighlight));
                    }
                    if (currentRow.getId() != selectionId && selectionId > 0) {
                        TableRow prevRow = (TableRow) v.getRootView().findViewById(selectionId);
                        for (int j = 0; j < prevRow.getChildCount(); j++) {
                            if (String.valueOf(selectionId).substring(0, 1).equals("1")) {
                                prevRow.getChildAt(j).setBackgroundColor(ContextCompat.getColor(v.getContext(), columnDark));
                            } else {
                                prevRow.getChildAt(j).setBackgroundColor(ContextCompat.getColor(v.getContext(), columnLight));
                            }
                        }
                    }
                    selectionId = currentRow.getId();
                }
            });
            row.addView(numPlayers);

            final TextView date = new TextView(this);
            date.setBackgroundColor(ContextCompat.getColor(this, rowColor));
            TextViewCompat.setTextAppearance(date, rowTextStyleId);
            date.setText(bracketDates[i]);
            date.setGravity(Gravity.CENTER_HORIZONTAL);
            date.post(new Runnable() {
                @Override
                public void run() {
                    date.setWidth(((TableRow) tableHeader.getChildAt(0)).getChildAt(2).getWidth());
                }
            });
            date.setPadding(5, 0, 0, 0);
            TableRow.LayoutParams dateParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            date.setLayoutParams(dateParams);
            row.addView(date);
            date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TableRow currentRow = (TableRow) v.getParent();
                    for (int j = 0; j < currentRow.getChildCount(); j++) {
                        currentRow.getChildAt(j).setBackgroundColor(ContextCompat.getColor(v.getContext(), columnHighlight));
                    }
                    if (currentRow.getId() != selectionId && selectionId > 0) {
                        TableRow prevRow = (TableRow) v.getRootView().findViewById(selectionId);
                        for (int j = 0; j < prevRow.getChildCount(); j++) {
                            if (String.valueOf(selectionId).substring(0, 1).equals("1")) {
                                prevRow.getChildAt(j).setBackgroundColor(ContextCompat.getColor(v.getContext(), columnDark));
                            } else {
                                prevRow.getChildAt(j).setBackgroundColor(ContextCompat.getColor(v.getContext(), columnLight));
                            }
                        }
                    }
                    selectionId = currentRow.getId();
                }
            });
            layout.addView(row);
        }
        addAdditionalRows();
        // re-highlights rows if the activity has been destroyed
        // and a row was already highlighted beforehand
        if (selectionId != 0) {
            rehighlightRows();
        }
    }

    // TODO call from onCreate() if screen has been rotated
    /**
     * Re-highlights the rows in the {@code Bracket} selection table. This method is called from the
     * {@code LoadBracket} class's {@code createTable()} method to ensure that the selected row is
     * re-highlighted after the {@code AppCompatActivity} is destroyed and re-created after screen
     * rotation.
     *
     * @see #createTable() createTable()
     */
    private void rehighlightRows() {
        if (selectionId != 0) {
            TableLayout layout = (TableLayout) findViewById(R.id.table);
            TableRow row = (TableRow) layout.findViewById(selectionId);
            for (int i = 0; i < row.getChildCount(); i++) {
                row.getChildAt(i).setBackgroundColor(ContextCompat.getColor(this, columnHighlight));
            }
        }
    }

    /**
     * Removes the selected row from the {@code Bracket} selection table after the corresponding
     * {@code Bracket} has been deleted from the {@code SQLiteDatabase}.
     *
     * @param id the ID of the row to be removed from the Bracket selection table
     */
    public void removeRow(int id) {
        TableLayout layout = (TableLayout) findViewById(R.id.table);
        layout.removeView(findViewById(id));
    }

    /**
     * Adds additional rows to the {@code Bracket} selection table, if necessary, to make it contain
     * a minimum number of rows. The current minimum is 7.
     */
    public void addAdditionalRows() {
        final TableLayout tableHeader = (TableLayout) findViewById(R.id.table_header);
        final TableLayout table = (TableLayout) findViewById(R.id.table);
        int numRows = table.getChildCount();

        if (numRows < 7) {
            for (int i = 0; i < 7 - numRows; i++) {
                int color;
                if ((numRows + i) % 2 == 0) {
                    color = R.color.table_column_dark;
                } else {
                    color = R.color.table_column_light;
                }
                //create row
                TableRow row = new TableRow(this);
                row.setId(0);

                // fill row
                final TextView name = new TextView(this);
                name.setBackgroundColor(ContextCompat.getColor(this, color));
                TextViewCompat.setTextAppearance(name, rowTextStyleId);
                name.setText("");
                name.post(new Runnable() {
                    @Override
                    public void run() {
                        name.setWidth(((TableRow) tableHeader.getChildAt(0)).getChildAt(0).getWidth());
                    }
                });
                name.setPadding(5, 0, 0, 0);
                TableRow.LayoutParams nameParams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                nameParams.setMargins(0, 0, 1, 0);
                name.setLayoutParams(nameParams);
                row.addView(name);

                final TextView numPlayers = new TextView(this);
                numPlayers.setBackgroundColor(ContextCompat.getColor(this, color));
                TextViewCompat.setTextAppearance(numPlayers, rowTextStyleId);
                numPlayers.setText("");
                name.post(new Runnable() {
                    @Override
                    public void run() {
                        numPlayers.setWidth(((TableRow) tableHeader.getChildAt(0)).getChildAt(1).getWidth());
                    }
                });
                numPlayers.setPadding(5, 0, 0, 0);
                TableRow.LayoutParams numPlayersParams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                numPlayersParams.setMargins(0, 0, 1, 0);
                numPlayers.setLayoutParams(numPlayersParams);
                row.addView(numPlayers);

                final TextView date = new TextView(this);
                date.setBackgroundColor(ContextCompat.getColor(this, color));
                TextViewCompat.setTextAppearance(date, rowTextStyleId);
                date.setText("");
                name.post(new Runnable() {
                    @Override
                    public void run() {
                        date.setWidth(((TableRow) tableHeader.getChildAt(0)).getChildAt(2).getWidth());
                    }
                });
                date.setPadding(5, 0, 0, 0);
                TableRow.LayoutParams dateParams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                date.setLayoutParams(dateParams);
                row.addView(date);
                table.addView(row);
            }
        }
    }

    /**
     * Re-colors all the rows in the {@code Bracket} selection table after a row has either been
     * deleted, or a deletion has been canceled. In the case of a canceled deletion, this method is
     * called in order to un-highlight the selected row.
     */
    public void resetRowColors() {
        TableLayout layout = (TableLayout) findViewById(R.id.table);
        int childCount = layout.getChildCount();

        for (int i = 0; i < childCount; i++) {
            TableRow row = (TableRow) layout.getChildAt(i);
            int rowChildCount = row.getChildCount();
            for (int j = 0; j < rowChildCount; j++) {
                if (i % 2 == 0) {
                    row.getChildAt(j).setBackgroundColor(ContextCompat.getColor(this, columnDark));
                } else {
                    row.getChildAt(j).setBackgroundColor(ContextCompat.getColor(this, columnLight));
                }
            }
        }
        selectionId = 0;
    }

    /**
     * Loads the {@code MainActivity} {@code AppCompatActivity}. Used by the "Cancel" {@code Button}.
     *
     * @param view the View from which this method was called
     */
    public void cancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * An intermediate method for the deletion of a {@code Bracket} from the {@code SQLiteDatabase}.
     * Creates an instance of the {@code PromptDelete} class to handle the actual deletion. If no
     * table row is selected, a new {@code PromptError} instance is created to notify the user.
     * <p>
     * Note: this intermediate method is necessary as the "Delete" {@code Button} is contained in
     * this {@code AppCompatActivity}'s XML layout file, and XML files can only have one
     * {@code Context} from which to call methods.
     *
     * @param view the View from which this method was called
     */
    public void delete(View view) {
        if (selectionId != 0) {
            TableLayout layout = (TableLayout) findViewById(R.id.table);
            TableRow row = (TableRow) layout.findViewById(selectionId);
            // the first element in this string is the row's color id
            // and the string's length can change based on the amount
            // of brackets the player has created, hence the usage of
            // substring rather than modulo to separate the bracket id
            int bracketId = Integer.parseInt((String.valueOf(row.getId()).substring(1)));
            DialogFragment prompt = PromptDelete.newInstance(
                    this.getString(R.string.prompt_title_wait),
                    this.getString(R.string.prompt_confirm_bracket_deletion), bracketId, selectionId);
            prompt.show(getFragmentManager(), "confirm bracket deletion");
            selectionId = 0;
        } else {
            DialogFragment prompt = PromptError.newInstance(
                    this.getString(R.string.error_title),
                    this.getString(R.string.error_no_bracket_selected_delete));
            prompt.show(getFragmentManager(), "bracket deletion error");
        }
    }

    /**
     * Loads a {@code Bracket} from the {@code SQLiteDatabase}. Queries the {@code SQLiteDatabase}
     * for {@code Bracket} information necessary to recreate the {@code Bracket} and passes it to the
     * {@code PlayBracket} class via an {@code Intent}. If no table row is selected, a new
     * {@code PromptError} instance is created to notify the user.
     *
     * @param view the View from which this method was called
     */
    public void load(View view) {
        if (selectionId != 0) {
            BracketDbHelper db = new BracketDbHelper(this);
            TableLayout layout = (TableLayout) findViewById(R.id.table);
            TableRow row = (TableRow) layout.findViewById(selectionId);
            // the first element in this string is the row's color id
            // and the string's length can change based on the amount
            // of brackets the player has created, hence the usage of
            // substring rather than modulo to separate the bracket id
            int bracketId = Integer.parseInt((String.valueOf(row.getId()).substring(1)));
            String bracketName = db.getBracketName(bracketId);
            String[] seatNameState = db.getSeatNameState(bracketId);
            String[] seatIdState = db.getSeatIdState(bracketId);
            int[] seatTierState = db.getSeatTierState(bracketId);
            int elimType = db.getBracketElimType(bracketId);

            Intent intent = new Intent(this, PlayBracket.class);
            intent.putExtra("bracketName", bracketName);
            intent.putExtra("elimType", elimType);
            intent.putExtra("seatNameState", seatNameState);
            intent.putExtra("seatIdState", seatIdState);
            intent.putExtra("seatTierState", seatTierState);
            startActivityForResult(intent, 1);
        } else {
            DialogFragment prompt = PromptError.newInstance(
                    this.getString(R.string.error_title),
                    this.getString(R.string.error_no_bracket_selected_load));
            prompt.show(getFragmentManager(), "load bracket error");
        }
    }

    /**
     * Receives a {@code Seat} name and calculates the max pixel size of the text that can fit
     * within its node, cuts off {@code Character}s to make room for, and adds, an ellipsis (...),
     * then returns the new {@code String}. This method takes the boarder size of the node as well
     * as extra padding into consideration while calculating. All measurements made are in pixels.
     * Called from within the {@code LoadBracket} class's {@code createTable()} method.
     *
     * @param column the TextView inside the column to crop if necessary
     * @param name   the original text to be placed inside the TextView and potentially be cropped
     * @param width  the width of the corresponding column title; used to calculate the max width of
     *               the resulting cropped String
     * @return       the potentially cropped text to be placed inside the calling TextView
     * @see #createTable() createTable()
     */
    private String getCroppedNodeText(TextView column, String name, int width) {
        int maxStringWidth, padding, stringWidth, ellipsisWidth;
        String ellipsis = "...";

        //get pixel width of string as displayed on TextView
        column.setText(name);
        Rect bounds = new Rect();
        Paint textPaint = column.getPaint();
        textPaint.getTextBounds(column.getText().toString(), 0, column.getText().length(), bounds);
        stringWidth = bounds.width();
        padding = (int) (10 * this.getResources().getDisplayMetrics().density);
        maxStringWidth = width - padding;

        if (stringWidth > maxStringWidth) {
            // get pixel width of ellipsis as displayed on-screen
            TextView container = new TextView(this);
            TextViewCompat.setTextAppearance(container, rowTextStyleId);
            container.setText(ellipsis);
            bounds = new Rect();
            Paint textPaint2 = container.getPaint();
            textPaint2.getTextBounds(container.getText().toString(), 0, container.getText().length(), bounds);
            ellipsisWidth = bounds.width();

            // trim button text
            int index = 0;
            int trimWidth = 0;
            int trimLength = 0;
            String textTrimmed;
            while (trimWidth <= maxStringWidth) {
                if (index > 0) {
                    trimWidth += charPadding.get(name.charAt(index));
                }
                trimWidth += charSizes.get(name.charAt(index));
                trimLength++;
                index++;
            }
            textTrimmed = name.substring(0, trimLength - 1);

            // trim further to add ellipsis and add it
            index = textTrimmed.length() - 1;
            trimWidth = 0;
            trimLength = 0;
            while (trimWidth < ellipsisWidth) {
                trimWidth += charSizes.get(textTrimmed.charAt(index)) + charPadding.get(textTrimmed.charAt(index));
                trimLength++;
                index--;
            }
            return name.substring(0, textTrimmed.length() - trimLength) + ellipsis;
        }
        return name;
    }

    /**
     * Returns a {@code HashMap<Character, Integer>} in which the {@code Integer} value represents
     * the size, in pixels, of its corresponding {@code Character} key's representative glyph as
     * displayed on-screen given the current text {@code style}.
     *
     * @return a {@literal HashMap<Character, Integer>} in which the Integer value represents the
     *         size, in pixels, of its corresponding Character key as displayed on-screen given the
     *         current text style
     */
    private HashMap<Character, Integer> getCharSizes() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890 ";
        HashMap<Character, Integer> alphabetMap = new HashMap<>();
        TextView container = new TextView(this);
        char currentChar;
        Rect bounds;
        Paint textPaint;

        TextViewCompat.setTextAppearance(container, rowTextStyleId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            container.setAllCaps(false);
        }

        for (int i = 0; i < alphabet.length(); i++) {
            currentChar = alphabet.charAt(i);
            container.setText(String.valueOf(currentChar));
            bounds = new Rect();
            textPaint = container.getPaint();
            textPaint.getTextBounds(container.getText().toString(), 0, container.getText().length(), bounds);
            alphabetMap.put(currentChar, bounds.width());
        }
        return alphabetMap;
    }

    /**
     * Returns a {@code HashMap<Character, Integer>} in which the {@code Integer} value represents
     * the size, in pixels, of the padding the Android system adds to its corresponding
     * {@code Character} key's representative glyph when displaying it on-screen given the current
     * text {@code style}. This padding is only added when 2 or more {@code Character}s are displayed.
     * These calculations represent padding added between 2 {@code Character}s that are exactly the
     * same. Actual padding varies slightly when mixing {@code Character}s.
     * <p>
     * The {@code noinspection} {@code AndroidLintSetTextI18n} statement is included to suppress the
     * {@code lint} {@code warning} thrown by the system regarding concatenating {@code String}s
     * within the {@code Button}'s {@code TextView} superclass's {@code setText(CharSequence)} method.
     * This {@code warning} refers to translation difficulty with concatenated {@code String}s. This
     * is irrelevant, however, because the text is user-entered.
     *
     * @return a {@literal HashMap<Character, Integer>} in which the Integer value represents the
     *         size, in pixels, of the padding the Android system adds to its corresponding Character
     *         key's representative glyph when displaying it on-screen given the current text style
     * @see <a href="https://developer.android.com/reference/android/widget/TextView.html#setText(java.lang.CharSequence)">TextView.setText(CharSequence)</a>
     */
    private HashMap<Character, Integer> getCharPadding() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890 ";
        HashMap<Character, Integer> alphabetMap = new HashMap<>();
        TextView container = new TextView(this);
        char currentChar;
        Rect bounds;
        Paint textPaint;

        TextViewCompat.setTextAppearance(container, rowTextStyleId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            container.setAllCaps(false);
        }

        for (int i = 0; i < alphabet.length(); i++) {
            currentChar = alphabet.charAt(i);
            // container is never displayed and
            // therefore will never need translating
            // noinspection AndroidLintSetTextI18n
            container.setText(String.valueOf(currentChar) + "" + String.valueOf(currentChar));
            bounds = new Rect();
            textPaint = container.getPaint();
            textPaint.getTextBounds(container.getText().toString(), 0, container.getText().length(), bounds);
            alphabetMap.put(currentChar, bounds.width() - (2 * charSizes.get(currentChar)));
        }
        return alphabetMap;
    }

    /**
     * Stores the ID of the currently selected row when the {@code AppCompatActivity} is destroyed
     * due to system constraints.
     * <p>
     * For example: screen rotation, the system needs to clear up resources, etc.
     *
     * @param savedInstanceState the Bundle which stores dynamic data from the
     *                           AppCompatActivity
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("selectionId", selectionId);
    }
}

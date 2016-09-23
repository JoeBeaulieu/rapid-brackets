package com.joebeaulieu.rapidbrackets.bracketinterface;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.joebeaulieu.rapidbrackets.bracketds.Bracket;
import com.joebeaulieu.rapidbrackets.activities.R;
import com.joebeaulieu.rapidbrackets.exceptions.BracketNotCreatedException;
import com.joebeaulieu.rapidbrackets.seats.Bye;
import com.joebeaulieu.rapidbrackets.seats.Remnant;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Creates the {@code Bracket} UI and adds it to the target {@code LinearLayout} to be displayed
 * on-screen.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @since 1.0.0
 */
public class CMDBuildBracketUI extends Command{
    /**
     * The {@code Context} from which this {@code Command} sequence was initiated.
     */
    private Context context;

    /**
     * The {@code LinearLayout} in which the {@code Bracket} UI is to be created and displayed.
     */
    private LinearLayout layout;

    /**
     * The {@code LayoutParams} used by all components of the {@code Bracket} UI.
     */
    private LinearLayout.LayoutParams bracketParams;

    /**
     * The text {@code style} used by all applicable components of the {@code Bracket} UI.
     */
    private int textStyleId;

    /**
     * The {@code HashMap<Character, Integer>} containing {@code Character} sizes, in pixels, for
     * all valid {@code Character}s given the current text {@code style}.
     */
    private HashMap<Character, Integer> charSizes;

    /**
     * The {@code HashMap<Character, Integer>} containing the Android system's automatically
     * generated {@code Character} padding sizes, in pixels, for all valid {@code Character}s given
     * the current text {@code style}.
     */
    private HashMap<Character, Integer> charPadding;

    /**
     * The sole constructor for the {@code CMDBuildBracketUI} class. Initializes all class variables
     * and passes the {@code Aggregator} to the {@code Command} superclass.
     *
     * @param agg     the Aggregator for the command design pattern
     * @param context the Context from which this Command sequence was initiated
     * @param layout  the LinearLayout in which the Bracket UI is to be created and displayed
     */
    public CMDBuildBracketUI(Aggregator agg, Context context, LinearLayout layout) {
        super(agg);
        this.context = context;
        this.layout = layout;
        bracketParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textStyleId = R.style.LargeText;
        charSizes = getCharSizes();
        charPadding = getCharPadding();
    }

    /**
     * Creates the {@code Bracket} UI, adds it to the target {@code LinearLayout}, and displays it
     * on-screen.
     *
     * @return returns null
     */
    @Override
    public Object execute() {
        final Bracket bracket = super.agg.getBracket();
        int padding = 0;
        int halfConnHeight = 0;

        for(int i = 0; i < bracket.size(); i++) {
            switch (i) {
                case 0: padding = 1; break;
                case 1: padding = 2; break;
                default: padding = (2 * padding) + 1;
            }
            switch (i) {
                case 0: halfConnHeight = 2;break;
                case 1: halfConnHeight = 3;break;
                default: halfConnHeight *= 2;
            }

            LinearLayout nodeColumn = new LinearLayout(context);
            nodeColumn.setLayoutParams(bracketParams);
            nodeColumn.setOrientation(LinearLayout.VERTICAL);

            LinearLayout connColumn = new LinearLayout(context);
            connColumn.setLayoutParams(bracketParams);
            connColumn.setOrientation(LinearLayout.VERTICAL);

            for(int j = 0; j < bracket.get(i).length; j++) {
                final Button node = new Button(context);
                node.setLayoutParams(bracketParams);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    node.setAllCaps(false);
                }
                String id = "";
                id += i;
                // make sure the id retains the form crr where c = column# and r = row#
                if (j < 10) {
                    id += 0;
                }
                id += j;
                // Views can only take int values for setID(),
                // therefore Seats in the first column will
                // have the 0 (first number of index) cut off
                node.setId(Integer.parseInt(id));
                TextViewCompat.setTextAppearance(node, textStyleId);
                // add functionality to the current node if
                // it is to be filled with a Seat and fill it
                if (bracket.get(i, j) != null) {
                    // "grey" Byes, Remnants, and non-current nodes
                    if ((bracket.get(i, j) instanceof Bye || bracket.get(i, j) instanceof Remnant)
                            || !isNodeCurrent(i, j)) {
                        node.setTextColor(ContextCompat.getColor(context, R.color.remnant_and_bye_text));
                    }
                    final int iCopy = i;
                    final int jCopy = j;
                    node.post(new Runnable() {
                        @Override
                        public void run() {
                            node.setText(getCroppedNodeText(node, bracket.get(iCopy, jCopy).getName()));
                        }
                    });
                    node.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                int viewID = v.getId();
                                String id = "";
                                // this deals with the problem of setID()
                                // only excepting ints and therefore cutting
                                // off the first digit of ids for Seats in
                                // the first column
                                if (String.valueOf(viewID).length() == 1) {
                                    id += "00";
                                } else if (String.valueOf(viewID).length() == 2) {
                                    id += "0";
                                }
                                id += viewID;
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
                                int viewID = v.getId();
                                String id = "";
                                // this deals with the problem of setID()
                                // only excepting ints and therefore cutting
                                // off the first digit of ids for Seats in
                                // the first column
                                if (String.valueOf(viewID).length() == 1) {
                                    id += "00";
                                } else if (String.valueOf(viewID).length() == 2) {
                                    id += "0";
                                }
                                id += viewID;
                                BracketInterface.moveBack(context, layout, id);
                            } catch (BracketNotCreatedException e) {
                                System.out.println(e.getMessage());
                                e.printStackTrace();
                            }
                            return true;
                        }
                    });
                    node.setHapticFeedbackEnabled(true);
                }
                // Adds the appropriate space above each node. Since competing
                // nodes in the first column are grouped directly next to each
                // other in pairs, only the top node in each pair in the first
                // column gets "top space"
                if ((i == 0 && j % 2 == 0) || i > 0) {
                    ArrayList<ImageView> topSpaces = getSpaces(padding);
                    for (int k = 0; k < topSpaces.size(); k++) {
                        nodeColumn.addView(topSpaces.get(k));
                    }
                }
                // sets the background image of the current node
                if (i == 0) {
                    node.setBackgroundResource(R.drawable.node_start);
                } else if (i == bracket.size() - 1) {
                    node.setBackgroundResource(R.drawable.node_end);
                } else {
                    node.setBackgroundResource(R.drawable.node_intermediate);
                }
                nodeColumn.addView(node);
                // Adds the appropriate space below each node. Since competing
                // nodes in the first column are grouped directly next to each
                // other in pairs, only the bottom node in each pair in the first
                // column gets "bottom space"
                if ((i == 0 && j % 2 != 0) || i > 0) {
                    ArrayList<ImageView> bottomSpaces = getSpaces(padding);
                    for (int m = 0; m < bottomSpaces.size(); m++) {
                        nodeColumn.addView(bottomSpaces.get(m));
                    }
                }
                // Creates the connector column. The last node column in the
                // Bracket does not get a corresponding connector column
                if (i != bracket.size() - 1) {
                    // creates the top half of the connector column
                    if (j % 2 == 0) {
                        // adds appropriate space above the connector column
                        ArrayList<ImageView> connTopSpaces = getSpaces(padding);
                        for (int n = 0; n < connTopSpaces.size(); n++) {
                            connColumn.addView(connTopSpaces.get(n));
                        }
                        for (int p = halfConnHeight; p > 0; p--) {
                            ImageView connector = new ImageView(context);
                            connector.setLayoutParams(bracketParams);
                            if (i == 0 && p == halfConnHeight) {
                                connector.setImageResource(R.drawable.conn_start_top);
                                p--;
                            } else if (p == halfConnHeight) {
                                connector.setImageResource(R.drawable.conn_inter_top);
                                p--;
                            } else if (p % 2 == 0) {
                                connector.setImageResource((R.drawable.conn_inter_double));
                                p--;
                            } else {
                                connector.setImageResource((R.drawable.conn_inter_single));
                            }
                            connColumn.addView(connector);
                        }
                        // adds a "full" middle connector if not currently processing the first column
                        if (i != 0) {
                            ImageView connector = new ImageView(context);
                            connector.setLayoutParams(bracketParams);
                            connector.setImageResource(R.drawable.conn_inter_mid);
                            connColumn.addView(connector);
                        }
                    } else {
                        // creates the bottom half of the connector column
                        for (int p = halfConnHeight; p > 0; p--) {
                            ImageView connector = new ImageView(context);
                            connector.setLayoutParams(bracketParams);
                            if (i == 0 && p / 2 == 1) {
                                connector.setImageResource(R.drawable.conn_start_bot);
                                p--;
                            } else if (p % 2 != 0) {
                                connector.setImageResource((R.drawable.conn_inter_single));
                            } else if (p / 2 == 1) {
                                connector.setImageResource(R.drawable.conn_inter_bot);
                                p--;
                            } else {
                                connector.setImageResource((R.drawable.conn_inter_double));
                                p--;
                            }
                            connColumn.addView(connector);
                        }
                        // adds appropriate space below the connector column
                        ArrayList<ImageView> connBotSpaces = getSpaces(padding);
                        for (int n = 0; n < connBotSpaces.size(); n++) {
                            connColumn.addView(connBotSpaces.get(n));
                        }
                    }
                }
            }
            layout.addView(nodeColumn);
            layout.addView(connColumn);
        }
        return null;
    }

    /**
     * Returns an {@code ArrayList<ImageView>} containing appropriate amount of space to be added
     * above or below the current {@code Seat} node or connector column in the {@code Bracket}. This
     * space is comprised of either single or double space images contained in {@code ImageView}s.
     * A single space represents one unit and is exactly half the height of a {@code Seat} node.
     * A double space is exactly twice the size of a single space.
     *
     * @param padding the amount of space units to be converted into single/double spaces.
     *                1 unit = 1 single space = 1/2 height of a Seat node.
     *                1 double space = 2 * single space
     * @return        an ArrayList containing the spaces to be added to the {@code Bracket} UI in the
     *                form of ImageViews
     */
    private ArrayList<ImageView> getSpaces(int padding) {
        ArrayList<ImageView> spaces = new ArrayList<>();
        for(int i = padding; i > 0; i--) {
            ImageView space = new ImageView(context);
            space.setLayoutParams(bracketParams);
            if (i > 1) {
                space.setImageResource(R.drawable.space_double);
                i--;
            } else {
                space.setImageResource(R.drawable.space_single);
            }
            spaces.add(space);
        }
        return spaces;
    }

    /**
     * Receives a {@code Seat} name and calculates the max pixel size of the text that can fit within
     * its node, cuts off {@code Character}s to make room for, and adds, an ellipsis (...), then
     * returns the new {@code String}. This method takes the boarder size of the node as well as
     * extra padding into consideration while calculating. All measurements made are in pixels.
     * Called from within this class's {@code execute()} method.
     *
     * @param button the Button inside the column whose text will be cropped if necessary
     * @param name   a String representation of the original text to be placed inside the Button
     *               and potentially cropped
     * @return       a String representation of the potentially cropped text to be placed inside the
     *               calling Button
     * @see #execute() execute()
     */
    private String getCroppedNodeText(Button button, String name) {
        int buttonWidth = button.getWidth();
        // this ratio represents the amount of inner space
        // of a Seat node image compared to its entire size
        double ratio = 0.875;
        int maxStringWidth, padding, stringWidth, ellipsisWidth;
        String ellipsis = "...";

        //get pixel width of String as displayed on button
        button.setText(name);
        Rect bounds = new Rect();
        Paint textPaint = button.getPaint();
        textPaint.getTextBounds(button.getText().toString(), 0, button.getText().length(), bounds);
        stringWidth = bounds.width();
        padding = (int) (22 * context.getResources().getDisplayMetrics().density);
        maxStringWidth = (int) ((buttonWidth * ratio) - (padding));

        if (stringWidth > maxStringWidth) {
            // get pixel width of ellipsis as displayed on-screen
            Button container = new Button(context);
            TextViewCompat.setTextAppearance(container, textStyleId);
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

            // trim further to add ellipsis
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
     * Returns a {@code HashMap<Character, Integer>} containing the size, in pixels, of each valid
     * {@code Character} as displayed on-screen given the current text {@code style}.
     *
     * @return a {@literal HashMap<Character, Integer>} in which the Integer represents the
     *         size, in pixels, of the corresponding Character as displayed on-screen given the
     *         current text style
     */
    private HashMap<Character, Integer> getCharSizes() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890 ";
        HashMap<Character, Integer> alphabetMap = new HashMap<>();
        Button container = new Button(context);
        char currentChar;
        Rect bounds;
        Paint textPaint;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            container.setAllCaps(false);
        }
        TextViewCompat.setTextAppearance(container, textStyleId);

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
     * Returns a {@code HashMap<Character, Integer>} containing the padding, in pixels, the Android
     * system adds to each {@code Character} when displaying it on-screen given the current text
     * {@code style}. This padding is only added when 2 or more {@code Character}s are displayed.
     * These calculations represent padding added between 2 {@code Character}s that are exactly the
     * same. Actual padding varies slightly when mixing {@code Character}s.
     * <p>
     * Note: The {@code noinspection AndroidLintSetTextI18n} statement is included to suppress the
     * {@code lint} {@code warning} thrown by the system regarding concatenating {@code String}s
     * within {@code TextView}'s {@code setText()} method. This {@code warning} refers to translation
     * difficulty with concatenated {@code String}s. This is irrelevant, however, because the text
     * is user entered.
     *
     * @return a {@literal HashMap<Character, Integer>} in which the Integer represents the size,
     *         in pixels, of the padding the Android system adds to each character when displaying
     *         it on-screen given the current text style
     * @see <a href="https://developer.android.com/reference/android/widget/TextView.html#setText(java.lang.CharSequence)">TextView.setText(CharSequence)</a>
     */
    private HashMap<Character, Integer> getCharPadding() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890 ";
        HashMap<Character, Integer> alphabetMap = new HashMap<>();
        Button container = new Button(context);
        char currentChar;
        Rect bounds;
        Paint textPaint;

        TextViewCompat.setTextAppearance(container, textStyleId);
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
     * Returns a {@code boolean} value representing whether or not the given {@code Seat} position
     * corresponds to the most current {@code Seat} in its corresponding {@code Bracket} lane.
     * <p>
     * For example: if the {@code Bracket} is halfway completed, with each lane having been progressed
     * the same amount, none of the {@code Seat}s in the first column will be current.
     *
     * @param column the column of the target Seat
     * @param row    the row of the target Seat
     * @return       a boolean value representing whether or not the given Seat position corresponds
     *               to the most current Seat in its corresponding Bracket lane
     */
    private boolean isNodeCurrent(int column, int row) {
        Bracket bracket = agg.getBracket();
        return column >= bracket.size() - 1 || bracket.get(column + 1, row / 2) == null;
    }
}

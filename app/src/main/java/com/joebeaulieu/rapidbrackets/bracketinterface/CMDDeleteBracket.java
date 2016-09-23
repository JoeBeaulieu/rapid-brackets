package com.joebeaulieu.rapidbrackets.bracketinterface;

import android.content.Context;

/**
 * Deletes a given {@code Bracket} from the {@code SQLiteDatabase} via the {@code BracketDbHelper}.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @see BracketDbHelper BracketDbHelper
 * @since 1.0.0
 */
public class CMDDeleteBracket extends Command{
    /**
     * The {@code Context} from which this {@code Command} sequence was initiated.
     */
    private Context context;

    /**
     * The ID of the target {@code Bracket}.
     */
    private int bracketId;

    /**
     * The sole constructor for the {@code CMDDeleteBracket} class. Initializes all class variables
     * and passes the {@code Aggregator} to the {@code Command} superclass.
     *
     * @param agg       the Aggregator for the command design pattern
     * @param context   the Context from which this Command sequence was initiated
     * @param bracketId the ID of the target Bracket
     */
    public CMDDeleteBracket(Aggregator agg, Context context, int bracketId) {
        super(agg);
        this.context = context;
        this.bracketId = bracketId;
    }

    /**
     * Deletes the target {@code Bracket} from the {@code SQLiteDatabase}.
     *
     * @return returns null
     */
    public Object execute() {
        BracketDbHelper db = new BracketDbHelper(context);
        db.deleteBracket(bracketId);
        return null;
    }
}

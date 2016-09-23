package com.joebeaulieu.rapidbrackets.bracketinterface;

import android.content.Context;

import com.joebeaulieu.rapidbrackets.bracketds.Bracket;
import com.joebeaulieu.rapidbrackets.seats.Seat;

/**
 * Stores the {@code Bracket} currently being used by the application in the {@code SQLiteDatabase}
 * via the {@code BracketDbHelper}.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @see BracketDbHelper BracketDbHelper
 * @since 1.0.0
 */
public class CMDStoreBracket extends Command{
    /**
     * The {@code Context} from which this {@code Command} sequence was initiated.
     */
    private Context context;

    /**
     * The sole constructor for the {@code CMDMoveForward} class. Initializes all class variables
     * and passes the {@code Aggregator} to the {@code Command} superclass.
     *
     * @param agg     the Aggregator for the command design pattern
     * @param context the Context from which this Command sequence was initiated
     */
    public CMDStoreBracket(Aggregator agg, Context context) {
        super(agg);
        this.context = context;
    }

    /**
     * Deconstructs the {@code Bracket} and stores it in the {@code SQLiteDatabase}.
     *
     * @return returns null
     */
    @Override
    public Object execute() {
        Bracket bracket = agg.getBracket();
        String bracketName = bracket.getName();
        String dateCreated = bracket.getDateCreated();
        int elimType = bracket.getElimType();
        int numPlayers = bracket.getNumPlayers();
        BracketDbHelper db = new BracketDbHelper(context);

        // if the Bracket does not yet exist in the SQLiteDatabase,
        // a new spot for it is created, else, delete the previously
        // saved Bracket state
        if (!db.doesBracketExist(bracketName)) {
            db.insertBracketName(bracketName, elimType, numPlayers, dateCreated);
        } else {
            db.deleteBracketState(bracketName);
        }
        // store the Bracket state in the SQLiteDatabase
        for (int i = 0; i < bracket.size(); i++) {
            for (int j = 0; j < bracket.get(i).length; j++) {
                Seat current = bracket.get(i, j);
                if (current != null) {
                    db.insertPlayer(bracketName, current.getID(), current.getName(), current.getTier());
                }
            }
        }
        return null;
    }
}

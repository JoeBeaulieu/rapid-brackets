package com.joebeaulieu.rapidbrackets.bracketinterface;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

/**
 * The helper class that defines and contains all methods relating to the {@code SQLiteDatabase}.
 * This class gets all of the table and column names for the {@code SQLiteDatabase} from
 * {@code BracketDbContract.BracketNames} and {@code BracketDbContract.BracketState}.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @see BracketDbContract.BracketNames BracketDbContract.BracketNames
 * @see BracketDbContract.BracketState BracketDbContract.BracketState
 * @since 1.0.0
 */
public class BracketDbHelper extends SQLiteOpenHelper{
    /**
     * The current version of the {@code SQLiteDatabase}.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * A pre-defined String for SQL's TEXT type.
     */
    private static final String TEXT_TYPE = " TEXT";

    /**
     * A pre-defined String for SQL's INTEGER type.
     */
    private static final String INTEGER_TYPE = " INTEGER";

    /**
     * A pre-defined String for SQL's NOT NULL type.
      */
    private static final String NOT_NULL = " NOT NULL";

    /**
     * A pre-defined String for SQL's comma separator.
      */
    private static final String COMMA_SEP = ",";

    /**
     * An SQL statement as a pre-defined String which creates the {@code BracketNames} table.
     */
    private static final String SQL_CREATE_BRACKET_NAME_TABLE =
            "CREATE TABLE " + BracketDbContract.BracketNames.TABLE_NAME + " (" +
                              BracketDbContract.BracketNames.COLUMN_BRACKET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                              BracketDbContract.BracketNames.COLUMN_BRACKET_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                              BracketDbContract.BracketNames.COLUMN_ELIM_TYPE + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                              BracketDbContract.BracketNames.COLUMN_NUM_PLAYERS + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                              BracketDbContract.BracketNames.COLUMN_DATE_CREATED + TEXT_TYPE + NOT_NULL +
            " )";

    /**
     * An SQL statement as a pre-defined String which creates the {@code BracketState} table.
     */
    private static final String SQL_CREATE_BRACKET_STATE_TABLE =
            "CREATE TABLE "  + BracketDbContract.BracketState.TABLE_NAME + " (" +
                               BracketDbContract.BracketState.COLUMN_PLAYER_ID + TEXT_TYPE + COMMA_SEP +
                               BracketDbContract.BracketState.COLUMN_PLAYER_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                               BracketDbContract.BracketState.COLUMN_BRACKET_ID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                               BracketDbContract.BracketState.COLUMN_TIER + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
            " FOREIGN KEY (" + BracketDbContract.BracketState.COLUMN_BRACKET_ID + ")" +
            " REFERENCES "   + BracketDbContract.BracketNames.TABLE_NAME + "(" + BracketDbContract.BracketNames.COLUMN_BRACKET_ID + ")" +
            " )";

    /**
     * An SQL statement as a pre-defined String which deletes the {@code BracketNames} table if it
     * exists.
     */
    private static final String SQL_DELETE_BRACKET_NAMES_TABLE =
            "DELETE IF EXISTS " + BracketDbContract.BracketNames.TABLE_NAME;

    /**
     * An SQL statement as a pre-defined String which deletes the {@code BracketState} table if it
     * exists.
     */
    private static final String SQL_DELETE_BRACKET_STATE_TABLE =
            "DELETE IF EXISTS " + BracketDbContract.BracketState.TABLE_NAME;

    /**
     * The sole constructor of the {@code BracketDbHelper} class. Sends the calling class's
     * {@code Context}, database name ({@code BracketDbContract.DATABASE_NAME}), and database version
     * ({@code DATABASE_VERSION})to the {@code SQLiteOpenHelper} class.
     *
     * @param context the context of the calling Activity
     */
    public BracketDbHelper(Context context) {
        super(context, BracketDbContract.DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creates the {@code SQLiteDatabase} if it does not already exist.
     *
     * @param db the SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_BRACKET_NAME_TABLE);
        db.execSQL(SQL_CREATE_BRACKET_STATE_TABLE);
    }

    /**
     * Upgrades the {@code SQLiteDatabase}. This implementation simply wipes the {@code SQLiteDatabase}
     * and creates a new one. Code will need to be added to migrate the date when a new version of
     * the {@code SQLiteDatabase} is coded.
     *
     * @param db         the SQLiteDatabase
     * @param oldVersion the old SQLiteDatabase version number
     * @param newVersion the new SQLiteDatabase version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_BRACKET_NAMES_TABLE);
        db.execSQL(SQL_DELETE_BRACKET_STATE_TABLE);
        onCreate(db);
    }

    /**
     * Called when the {@code SQLiteDatabase} is opened. If the {@code SQLiteDatabase} is not read-only,
     * foreign key constraints are turned on with {@code PRAGMA foreign_keys = ON}.
     *
     * @param db the SQLiteDatabase
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys = ON");
        }
    }

    /**
     * Checks to see if a {@code Bracket} with the given name exists in the {@code SQLiteDatabase}.
     * Returns {@code true} if it does, and {@code false} if it does not.
     *
     * @param bracketName a String representation of the Bracket's name
     * @return            a boolean value representing whether or not the Bracket exists
     */
    public boolean doesBracketExist(String bracketName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery(
                "SELECT " + BracketDbContract.BracketNames.COLUMN_BRACKET_NAME +
                " FROM "  + BracketDbContract.BracketNames.TABLE_NAME +
                " WHERE " + BracketDbContract.BracketNames.COLUMN_BRACKET_NAME +
                " = ?"    , new String[]{bracketName});
        int resCount = result.getCount();
        result.close();
        return (resCount != 0);
    }

    /**
     * Deletes the {@code Bracket} from the {@code SQLiteDatabase} with the given ID. Uses the
     * {@code deleteBracketState(int)} method to delete the {@code Bracket} state from the
     * {@code BracketState} table, and then deletes the {@code Bracket} name, and other associated
     * meta-data from the {@code BracketNames} table.
     *
     * @param bracketId the Bracket's ID
     * @see #deleteBracketState(int) deleteBracketState(int)
     */
    public void deleteBracket(int bracketId) {
        SQLiteDatabase db = this.getWritableDatabase();
        deleteBracketState(bracketId);
        db.execSQL(
                "DELETE FROM " + BracketDbContract.BracketNames.TABLE_NAME +
                " WHERE "      + BracketDbContract.BracketNames.COLUMN_BRACKET_ID +
                " = "          + bracketId);
    }

    /**
     * Deletes the state of the {@code Bracket} from the {@code BracketState} table.
     *
     * @param bracketId the Bracket's ID
     */
    public void deleteBracketState(int bracketId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(
                "DELETE FROM " + BracketDbContract.BracketState.TABLE_NAME +
                " WHERE "      + BracketDbContract.BracketState.COLUMN_BRACKET_ID +
                " = "          + bracketId);
    }

    /**
     * Deletes the state of the {@code Bracket} from the {@code BracketState} table.
     *
     * Note: The {@code NewBracket} class's {@code createBracket(View)} method ensures that no two
     * {@code Bracket}s are created with the same name.
     *
     * @param bracketName a String representation of the Bracket's name
     * @see com.joebeaulieu.rapidbrackets.activities.NewBracket#createBracket(View) NewBracket.createBracket(View)
     */
    public void deleteBracketState(String bracketName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(
                "DELETE FROM " + BracketDbContract.BracketState.TABLE_NAME +
                " WHERE "      + BracketDbContract.BracketState.COLUMN_BRACKET_ID +
                " = (SELECT "  + BracketDbContract.BracketNames.COLUMN_BRACKET_ID +
                " FROM "       + BracketDbContract.BracketNames.TABLE_NAME +
                " WHERE "      + BracketDbContract.BracketNames.COLUMN_BRACKET_NAME +
                " = ? )"       , new String[]{bracketName});
    }

    /**
     * Creates a new {@code Bracket}, excluding state, in the {@code BracketNames} table along with
     * its associated meta-data.
     *
     * @param bracketName a String representation of the Bracket's name
     * @param elimType    the elimination type. Either BracketInterface.SINGLE_ELIM or
     *                    BracketInterface.DOUBLE_ELIM
     * @param numPlayers  the number of Players in the Bracket, not including Byes
     * @param dateCreated a String representation of the date the Bracket was created
     * @see BracketInterface#SINGLE_ELIM BracketInterface.SINGLE_ELIM
     * @see BracketInterface#DOUBLE_ELIM BracketInterface.DOUBLE_ELIM
     */
    public void insertBracketName(String bracketName, int elimType, int numPlayers, String dateCreated) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(
                "INSERT INTO " + BracketDbContract.BracketNames.TABLE_NAME + " (" +
                                 BracketDbContract.BracketNames.COLUMN_BRACKET_NAME + COMMA_SEP +
                                 BracketDbContract.BracketNames.COLUMN_ELIM_TYPE + COMMA_SEP +
                                 BracketDbContract.BracketNames.COLUMN_NUM_PLAYERS + COMMA_SEP +
                                 BracketDbContract.BracketNames.COLUMN_DATE_CREATED + ") " +
                "VALUES (?, " + elimType + ", " + numPlayers + ", ?)", new String[]{bracketName, dateCreated});
    }

    /**
     * Inserts a deconstructed {@code Seat} {@code Object} into the {@code SQLiteDatabase}.
     *
     * @param bracketName a String representation of the Bracket's name
     * @param id          a String representation of the Player's ID
     * @param name        a String representation of the Player's name
     * @param tier        the tier of the Bracket the given Player currently resides in
     */
    public void insertPlayer(String bracketName, String id, String name, int tier) {
        SQLiteDatabase db = this.getWritableDatabase();

        // get Bracket ID
        Cursor result = db.rawQuery(
                "SELECT " + BracketDbContract.BracketNames.COLUMN_BRACKET_ID +
                " FROM "  + BracketDbContract.BracketNames.TABLE_NAME +
                " WHERE " + BracketDbContract.BracketNames.COLUMN_BRACKET_NAME +
                " = ?"    , new String[]{bracketName});
        result.moveToFirst();
        int bracketID = result.getInt(0);

        // insert Player into Bracket
        db.execSQL(
                "INSERT INTO " + BracketDbContract.BracketState.TABLE_NAME + " (" +
                                 BracketDbContract.BracketState.COLUMN_PLAYER_ID + COMMA_SEP +
                                 BracketDbContract.BracketState.COLUMN_PLAYER_NAME + COMMA_SEP +
                                 BracketDbContract.BracketState.COLUMN_BRACKET_ID + COMMA_SEP +
                                 BracketDbContract.BracketState.COLUMN_TIER + ") " +
                "VALUES (?, ?, " + bracketID + COMMA_SEP + tier + ")", new String[] {id, name});
        result.close();
    }

    /**
     * Returns a {@code String[]} containing the saved state of the {@code Bracket} in terms of
     * {@code Player} names.
     *
     * @param bracketId the Bracket's ID
     * @return          a String[] containing the state of the Bracket in terms of Player names
     */
    public String[] getSeatNameState(int bracketId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery(
                "SELECT "     + BracketDbContract.BracketState.COLUMN_PLAYER_NAME +
                " FROM "      + BracketDbContract.BracketState.TABLE_NAME +
                " WHERE "     + BracketDbContract.BracketState.COLUMN_BRACKET_ID +
                " = "         + bracketId, null);
        String[] playerStates = new String[result.getCount()];
        int count = 0;
        while (result.moveToNext()) {
            playerStates[count] = result.getString(0);
            count++;
        }
        result.close();
        return playerStates;
    }

    /**
     * Returns a {@code String[]} containing the saved state of the {@code Bracket} in terms of
     * {@code Player} IDs.
     *
     * @param bracketId the Bracket's ID
     * @return          a String[] containing the state of the Bracket in terms of Player IDs
     */
    public String[] getSeatIdState(int bracketId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery(
                "SELECT "     + BracketDbContract.BracketState.COLUMN_PLAYER_ID +
                " FROM "      + BracketDbContract.BracketState.TABLE_NAME +
                " WHERE "     + BracketDbContract.BracketState.COLUMN_BRACKET_ID +
                " = "         + bracketId, null);
        String[] idStates = new String[result.getCount()];
        int count = 0;
        while (result.moveToNext()) {
            idStates[count] = result.getString(0);
            count++;
        }
        result.close();
        return idStates;
    }

    /**
     * Returns an {@code int[]} containing the saved state of the {@code Bracket} in terms of
     * {@code Player} tiers.
     *
     * @param bracketId the Bracket's ID
     * @return          an int[] containing the state of the Bracket in terms of Player tiers
     */
    public int[] getSeatTierState(int bracketId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery(
                "SELECT "     + BracketDbContract.BracketState.COLUMN_TIER +
                " FROM "      + BracketDbContract.BracketState.TABLE_NAME +
                " WHERE "     + BracketDbContract.BracketState.COLUMN_BRACKET_ID +
                " = "         + bracketId, null);
        int[] idStates = new int[result.getCount()];
        int count = 0;
        while (result.moveToNext()) {
            idStates[count] = result.getInt(0);
            count++;
        }
        result.close();
        return idStates;
    }

    /**
     * Returns an {@code int[]} containing all of the {@code Bracket} IDs in the {@code SQLiteDatabase}.
     *
     * @return an int[] containing all of the Bracket IDs in the SQLiteDatabase
     */
    public int[] getAllBracketIds() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery(
                "SELECT " + BracketDbContract.BracketNames.COLUMN_BRACKET_ID +
                " FROM "  + BracketDbContract.BracketNames.TABLE_NAME, null);
        int[] bracketIds = new int[result.getCount()];
        int count = 0;
        while (result.moveToNext()) {
            bracketIds[count] = result.getInt(0);
            count++;
        }
        result.close();
        return bracketIds;
    }

    /**
     * Returns a {@code String[]} containing all of the {@code Bracket} names in the {@code SQLiteDatabase}.
     *
     * @return a String[] containing all of the Bracket names in the SQLiteDatabase
     */
    public String[] getAllBracketNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery(
                "SELECT " + BracketDbContract.BracketNames.COLUMN_BRACKET_NAME +
                " FROM "  + BracketDbContract.BracketNames.TABLE_NAME, null);
        String[] bracketNames = new String[result.getCount()];
        int count = 0;
        while (result.moveToNext()) {
            bracketNames[count] = result.getString(0);
            count++;
        }
        result.close();
        return bracketNames;
    }

    /**
     * Returns a {@code String} representation of the {@code Bracket} name associated with the given
     * {@code Bracket} ID.
     *
     * @param bracketId the Bracket's ID
     * @return          a String representation of the Bracket's name
     */
    public String getBracketName(int bracketId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery(
                "SELECT " + BracketDbContract.BracketNames.COLUMN_BRACKET_NAME +
                " FROM "  + BracketDbContract.BracketNames.TABLE_NAME +
                " WHERE " + BracketDbContract.BracketNames.COLUMN_BRACKET_ID +
                " = "     + bracketId, null);
        result.moveToFirst();
        String bracketName = result.getString(0);
        result.close();
        return bracketName;
    }

    /**
     * Returns an {@code int[]} containing the number of {@code Player}s, not including {@code Bye}s,
     * for every {@code Bracket} in the {@code SQLiteDatabase}.
     *
     * @return an int[] containing the number of Players, not including Byes, for every Bracket in
     *         the SQLiteDatabase
     */
    public int[] getAllNumPlayers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery(
                "SELECT " + BracketDbContract.BracketNames.COLUMN_NUM_PLAYERS +
                " FROM "  + BracketDbContract.BracketNames.TABLE_NAME, null);
        int[] allNumPlayers = new int[result.getCount()];
        int count = 0;
        while (result.moveToNext()) {
            allNumPlayers[count] = result.getInt(0);
            count++;
        }
        result.close();
        return allNumPlayers;
    }

    /**
     * Returns a {@code String[]} containing the date created for every {@code Bracket} in the
     * {@code SQLiteDatabase}.
     *
     * @return a String[] containing the date created for every Bracket in the SQLiteDatabase
     */
    public String[] getAllBracketDates() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery(
                "SELECT " + BracketDbContract.BracketNames.COLUMN_DATE_CREATED +
                " FROM "  + BracketDbContract.BracketNames.TABLE_NAME, null);
        String[] bracketDates = new String[result.getCount()];
        int count = 0;
        while (result.moveToNext()) {
            bracketDates[count] = result.getString(0);
            count++;
        }
        result.close();
        return bracketDates;
    }

    /**
     * Returns the elimination type for the {@code Bracket} associated with the given {@code Bracket}
     * ID. Can be either {@code BracketInterface.SINGLE_ELIM} or {@code BracketInterface.DOUBLE_ELIM}.
     *
     * @param bracketId the Bracket's ID
     * @return          the elimination type for the Bracket associated with the given ID
     * @see BracketInterface#SINGLE_ELIM BracketInterface.SINGLE_ELIM
     * @see BracketInterface#DOUBLE_ELIM BracketInterface.DOUBLE_ELIM
     */
    public int getBracketElimType(int bracketId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery(
                "SELECT " + BracketDbContract.BracketNames.COLUMN_ELIM_TYPE +
                " FROM "  + BracketDbContract.BracketNames.TABLE_NAME +
                " WHERE " + BracketDbContract.BracketNames.COLUMN_BRACKET_ID +
                " = "     + bracketId, null);
        result.moveToFirst();
        int elimType = result.getInt(0);
        result.close();
        return elimType;
    }
}

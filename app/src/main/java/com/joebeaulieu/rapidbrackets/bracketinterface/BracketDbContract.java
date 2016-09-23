package com.joebeaulieu.rapidbrackets.bracketinterface;

/**
 * The contract for the {@code SQLiteDatabase}. This class enforces the structure of the
 * {@code SQLiteDatabase} while allowing it to be easily modified, and is utilized by the
 * {@code BracketDbHelper} class.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @see BracketDbHelper BracketDbHelper
 * @since 1.0.0
 */
public final class BracketDbContract {
    /**
     * The name of the {@code SQLiteDatabase}.
     */
    public static final String DATABASE_NAME = "Bracket.db";

    /**
     * The sole, and default constructor for the {@code BracketDbContract} class.
     */
    public BracketDbContract() {
    }

    /**
     * Structure for the {@code BracketNames} table. This table contains all the information about
     * the {@code Bracket}, excluding its current state. {@code COLUMN_BRACKET_ID} is the primary
     * key.
     */
    public static abstract class BracketNames {
        /**
         * A String representation of the {@code bracket_names} table name.
         */
        public static final String TABLE_NAME = "bracket_names";

        /**
         * A String representation of the {@code bracket_id} column. The {@code BracketState}'s
         * {@code bracket_id} variable links to this variable via foreign key.
         */
        public static final String COLUMN_BRACKET_ID = "bracket_id";

        /**
         * A String representation of the {@code bracket_name} column.
         */
        public static final String COLUMN_BRACKET_NAME = "bracket_name";

        /**
         * A String representation of the {@code elim_type} column.
         */
        public static final String COLUMN_ELIM_TYPE = "elim_type";

        /**
         * A String representation of the {@code num_players} column.
         */
        public static final String COLUMN_NUM_PLAYERS = "num_players";

        /**
         * A String representation of the {@code date_created} column.
         */
        public static final String COLUMN_DATE_CREATED = "date_created";
    }

    /**
     * Structure for the {@code BracketState} table. This table contains all the information about
     * the {@code Bracket} state. {@code COLUMN_BRACKET_ID} is a foreign key which corresponds to
     * {@code COLUMN_BRACKET_ID} in the {@code BracketNames} table.
     */
    public static abstract class BracketState {
        /**
         * A String representation of the {@code bracket_state} table name.
         */
        public static final String TABLE_NAME = "bracket_state";

        /**
         * A String representation of the {@code player_id} column.
         */
        public static final String COLUMN_PLAYER_ID = "player_id";

        /**
         * A String representation of the {@code player_name} column.
         */
        public static final String COLUMN_PLAYER_NAME = "player_name";

        /**
         * A String representation of the {@code bracket_id} column. Is a foreign key to {@code bracket_id}
         * in the {@code BracketNames} table.
         */
        public static final String COLUMN_BRACKET_ID = "bracket_id";

        /**
         * A String representation of the {@code tier} column.
         */
        public static final String COLUMN_TIER = "tier";
    }
}

package com.joebeaulieu.rapidbrackets.bracketds;

import java.util.Calendar;
import java.util.Random;

import com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface;
import com.joebeaulieu.rapidbrackets.seats.Bye;
import com.joebeaulieu.rapidbrackets.seats.Player;
import com.joebeaulieu.rapidbrackets.seats.Remnant;
import com.joebeaulieu.rapidbrackets.seats.Seat;

/**
 * This class creates and stores a single instance of a single elimination {@code Bracket}s. It is
 * stored internally as a {@code Seat[][]}. Arranging the starting lineup is not done here, but rather
 * in the class's {@code PlanterSE} {@code Object}.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @see PlanterSE                                PlanterSE
 * @see com.joebeaulieu.rapidbrackets.seats.Seat Seat
 * @since 1.0.0
 */
public class BracketSE implements Bracket{
    /**
     * A String representation of the name of the {@code Bracket}.
     */
    private String name;

    /**
     * A String representation of the date the {@code Bracket} was created; of the form: MM/DD/YYYY.
     */
    private String dateCreated;

    /**
     * The elimination type of the {@code Bracket}. Since {@code BracketSE} is a single elimination
     * {@code Bracket}, this value will always be {@code BracketInterface.SINGLE_ELIM}.
     *
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#SINGLE_ELIM BracketInterface.SINGLE_ELIM
     */
    private int elimType;

    /**
     * The number of {@code Player}s in the {@code Bracket}, not including {@code Bye}s.
     */
    private int numPlayers;

    /**
     * The {@code Bracket} as a {@code Seat[]}.
     */
    private Seat[] bracket;

    /**
     * The {@code Bracket} as it is used throughout the application, as a {@code Seat[][]}.
     */
    private Seat[][] grid;

    /**
     * The {@code PlanterSE} which creates the starting lineup for the {@code Bracket}.
     */
    private PlanterSE planter;

    /**
     * Gives random seeds to non-seeded {@code Player}s; seeds created here are always greater than
     * the largest user defined seed, but smaller than seeds automatically assigned to {@code Bye}s.
     */
    private Random rand;

    /**
     * The minimum value of the range from which this class's {@code Random} number generator
     * randomly creates seeds for non-seeded {@code Player}s.
     */
    private int randRangeMin;

    /**
     * The tier of a sub-{@code Bracket} in the overarching structure of the {@code Bracket}. This
     * value is assigned to each {@code Seat} in the {@code Bracket}. It is used for {@code Bracket}
     * partitioning (double elimination) and {@code Bracket} recreation. Since {@code BracketSE} is
     * a single elimination {@code Bracket}, this value is constant throughout the entire
     * {@code Bracket}. Its value will always be {@code BracketInterface.WINNERS_BRACKET}.
     *
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#WINNERS_BRACKET BracketInterface.WINNERS_BRACKET
     */
    private int tier;

    /**
     * The constructor that is used for the initial creation of a {@code Bracket}. Class variables
     * representing {@code Bracket} metadata are initialized here.
     *
     * @param name    a String representation of the name of the Bracket
     * @param players a String[] containing all Player names; does not include Byes
     * @param seeds   an Integer[] containing seeds for every player, should they have a seed. The
     *                index of the seed corresponds to the index of the representative Player in the
     *                players array. Players with no seed will have a corresponding null value in
     *                this array
     * @param planter the PlanterSE Object for the given instance of this class. The PlanterSE
     *                is responsible for determining the starting lineup based on seeds, and
     *                planting the Players in the Bracket accordingly
     */
    public BracketSE(String name, String[] players, Integer[] seeds, PlanterSE planter) {
        rand = new Random();
        randRangeMin = seeds.length + 1;
        this.name = name;
        numPlayers = players.length;
        elimType = BracketInterface.SINGLE_ELIM;
        tier = BracketInterface.WINNERS_BRACKET;
        Calendar calendar = Calendar.getInstance();
        // 1 must be added to the MONTH as Calendar.MONTH is zero-based
        dateCreated = (calendar.get(Calendar.MONTH) + 1) + "/" +
                calendar.get(Calendar.DATE) + "/" +
                calendar.get(Calendar.YEAR);
        this.planter = planter;
        makeBracket(players, seeds);
    }

    /**
     * The constructor that is used for {@code Bracket} recreation. The {@code Bracket} will either
     * be recreated when the {@code PlayBracket} {@code AppCompatActivity} is destroyed and recreated,
     * or when it is rebuild from the {@code SQLiteDatabase} via the {@code LoadBracket}
     * {@code AppCompatActivity}.
     *
     * @param name          the name of the Bracket
     * @param dateCreated   the date the Bracket was originally created
     * @param seatNameState a String[] which represents the currently saved state of the Bracket in
     *                      terms of Seats. This array does not contain information on the empty
     *                      places in the Bracket
     * @param seatIdState   a String[] which represents the ids of each Seat in the Bracket. The ids
     *                      are of the format: L###, where L is a letter denoting the Seat type,
     *                      and ### are the column and row numbers, with the first digit being the
     *                      column, and the last two being the row. L can either be "p", "r", or "b"
     *                      for Player, Remnant, or Bye respectively. The index of each id is the
     *                      index of its corresponding Seat in the seatNameState array
     * @param seatTierState an int[] which represents the sub-Bracket where each Seat is located. Since
     *                      {@code BracketSE} is a single elimination {@code Bracket}, this value is
     *                      constant throughout the entire {@code Bracket}. Its value will always be
     *                      {@code BracketInterface.WINNERS_BRACKET}. The index of each tier is the
     *                      index of its corresponding Seat in the seatNameState array
     * @param planter       the PlanterSE Object for the given instance of this class. The PlanterSE
     *                      is responsible for determining the starting lineup based on seeds, and
     *                      planting the Players in the Bracket accordingly
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#WINNERS_BRACKET BracketInterface.WINNERS_BRACKET
     */
    public BracketSE(String name, String dateCreated, String[] seatNameState, String[] seatIdState, int[] seatTierState, PlanterSE planter) {
        this.name = name;
        this.dateCreated = dateCreated;
        elimType = BracketInterface.SINGLE_ELIM;
        tier = BracketInterface.WINNERS_BRACKET;
        this.planter = planter;
        rand = null;
        randRangeMin = -1;
        reconstructBracket(seatNameState, seatIdState, seatTierState);
    }

    /**
     * This method ultimately creates the {@code Bracket}, utilizing {@code numByes(String[])} to
     * help determine the amount of leaves for the {@code Bracket} tree, this {@code Object} instance's
     * {@code PlanterSE}'s {@code plant(Seat[], Seat[], int)} method to plant the {@code Player}s
     * into their correct starting position in the {@code Bracket}, and {@code makeGrid()} to get
     * the finalized {@code Bracket} as a {@code Seat[][]}. {@code Bye}s are also generated here.
     *
     * @param players a String[] containing all of the Player names for the Bracket. Does not
     *                include Byes
     * @param seeds   an Integer[] containing seeds for every player, should they have a seed.
     *                The index of the seed corresponds to the index of the representative Player
     *                in the players array. Players with no seed will have a corresponding null
     *                value in this array
     * @see #numByes(String[])                   numByes(String[])
     * @see #makeGrid()                          makeGrid()
     * @see PlanterSE#plant(Seat[], Seat[], int) PlanterSE.plant(Seat[], Seat[], int)
     */
    private void makeBracket(String[] players, Integer[] seeds) {
        int leaves = numByes(players) + players.length;
        int nodes = (2 * leaves) - 1;
        int empties = nodes - leaves;
        Seat[] allLeaves = new Seat[leaves];
        bracket = new Seat[nodes];

        // create Seats and place them in the allLeaves array
        for (int i = 0; i < allLeaves.length; i++) {
            if (i < players.length) {
                if (seeds[i] == null){
                    allLeaves[i] = new Player(players[i], rand.nextInt(999 - randRangeMin) + randRangeMin);
                } else {
                    allLeaves[i] = new Player(players[i], seeds[i]);
                }
            } else {
                allLeaves[i] = new Bye();
            }
        }
        // sort all the Seats and plant them in the bracket Seat[]
        planter.plant(bracket, allLeaves, empties);
        makeGrid();
    }

    /**
     * This method ultimately recreates the {@code Bracket}, utilizing
     * {@code reconstructGrid(String[], String[], int[])} to get the finalized {@code Bracket} as a
     * {@code Seat[][]}.
     *
     * @param seatNameState a String[] which represents the currently saved state of the Bracket in
     *                      terms of Seat names. This array does not contain information on the empty
     *                      places in the Bracket
     * @param seatIdState   a String[] which represents the IDs of each Seat in the Bracket. The ids
     *                      are of the format: L###, where L is a letter denoting the Seat type,
     *                      and ### are the column and row numbers, with the first digit being the
     *                      column, and the last two being the row. L can either be "p", "r", or "b"
     *                      for Player, Remnant, or Bye respectively. The index of each ID is the
     *                      index of its corresponding Seat in the seatNameState array
     * @param seatTierState an int[] which represents the sub-Bracket where each Seat is located. Since
     *                      {@code BracketSE} is a single elimination {@code Bracket}, this value is
     *                      constant throughout the entire {@code Bracket}. Its value will always be
     *                      {@code BracketInterface.WINNERS_BRACKET}. The index of each tier is the
     *                      index of its corresponding Seat in the seatNameState array
     * @see #reconstructGrid(String[], String[], int[])                                     reconstructGrid(String[], String[], int[])
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#WINNERS_BRACKET BracketInterface.WINNERS_BRACKET
     */
    private void reconstructBracket(String[] seatNameState, String[] seatIdState, int[] seatTierState) {
        int leaves = 0;
        int byes = 0;
        int nodes;

        // determine the amount of leaves in the playerState array
        // leaves have a corresponding ID of L0## (L can be any valid Seat identification prefix)
        for (String id : seatIdState) {
            if (id.substring(1).startsWith("0")) {
                leaves++;
                if (id.startsWith("b")){
                    byes++;
                }
            } else {
                break;
            }
        }
        numPlayers = leaves - byes;
        nodes = (2 * leaves) - 1;
        bracket = new Seat[nodes];
        reconstructGrid(seatNameState, seatIdState, seatTierState);
    }

    /**
     * Calculates and returns the number of {@code Bye}s to be used in the {@code Bracket} as an
     * {@code int}.
     *
     * @param players a String[] containing the names of all the players
     * @return        the number of Byes to be used in the Bracket as an int
     */
    private int numByes(String[] players) {
        int size = players.length;
        if (isPower2(players)) {
            return 0;
        } else if (size == 1) {
            return 1;
        } else {
            int num = 1;
            // calculates the amount of total leaves given the amount of players
            while (size > num) {
                num *= 2;
            }
            // subtracts players from total leaves, the remainder is amount of Byes
            return (num - size);
        }
    }

    /**
     * Takes the raw {@code Bracket} {@code Seat[]} and segments it into columns that represent each
     * "level" of the {@code Bracket} to later be displayed on-screen via the {@code CMDBuildBracketUI}
     * class.
     * <p>
     * Visually, it looks like:
     * <pre>
     *      [[P1, P2, P3, P4], [null, null], [null]]
     * </pre>
     * This appears on-screen like this ({@code null} values are shown as empty text):
     * <pre>
     *      P1
     *         > null
     *      P2
     *                > null
     *      P3
     *         > null
     *      P4
     * </pre>
     */
    @Override
    public void makeGrid() {
        int height = log2(bracket.length) + 1;
        grid = new Seat[height][];
        
        //creates empty grid
        grid[0] = new Seat[(bracket.length / 2) + 1];
        int colSize = grid[0].length;
        for (int i = 1; i < height; i++){
            colSize = colSize / 2;
            grid[i] = new Seat[colSize];
        }
        
        //fills grid
        int start = (bracket.length / 2) + 1; // think of better name
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = bracket[start - 1 + j];
                if (grid[i][j] != null) { // WILL NEED TO BE CHANGED TO instanceof Seat IF TBD not used
                    String id = "";
                    id += i;
                    // make sure the id retains the form crr where c = column# and rr = row#
                    if (j < 10) {
                        id += 0;
                    }
                    id += j;
                    grid[i][j].setID(id);
                    grid[i][j].setTier(tier);
                }
            }
            start /= 2;
        }
    }

    /**
     * Takes the current state of the {@code Bracket} and recreates it. The {@code Bracket}'s state
     * is comprised of {@code Seat}s from all levels of the {@code Bracket}, along with their
     * corresponding IDs and tier levels. This state is used to rebuild the original {@code Seat[][]}
     * of the {@code Bracket}, which is segmented into columns that represent each "level" of the
     * {@code Bracket} to later be displayed on-screen via the {@code CMDBuildBracketUI} class.
     * <p>
     * Visually, it looks like:
     * <pre>
     *      [[P1, P2, P3, P4], [null, null], [null]]
     * </pre>
     * This appears on-screen like this ({@code null} values are shown as empty text):
     * <pre>
     *      P1
     *         > null
     *      P2
     *                > null
     *      P3
     *         > null
     *      P4
     * </pre>
     *
     * @param seatNameState a String[] which represents the currently saved state of the Bracket in
     *                      terms of Seat names. This array does not contain information on the empty
     *                      places in the Bracket
     * @param seatIdState   a String[] which represents the IDs of each Seat in the Bracket. The ids
     *                      are of the format: L###, where L is a letter denoting the Seat type,
     *                      and ### are the column and row numbers, with the first digit being the
     *                      column, and the last two being the row. L can either be "p", "r", or "b"
     *                      for Player, Remnant, or Bye respectively. The index of each ID is the
     *                      index of its corresponding Seat in the seatNameState array
     * @param seatTierState an int[] which represents the sub-Bracket where each Seat is located. Since
     *                      {@code BracketSE} is a single elimination {@code Bracket}, this value is
     *                      constant throughout the entire {@code Bracket}. Its value will always be
     *                      {@code BracketInterface.WINNERS_BRACKET}. The index of each tier is the
     *                      index of its corresponding Seat in the seatNameState array
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#WINNERS_BRACKET BracketInterface.WINNERS_BRACKET
     */
    @Override
    public void reconstructGrid(String[] seatNameState, String[] seatIdState, int[] seatTierState) {
        int height = log2(bracket.length) + 1;
        grid = new Seat[height][];

        //creates empty grid
        grid[0] = new Seat[(bracket.length / 2) + 1];
        int colSize = grid[0].length;
        for (int i = 1; i < height; i++){
            colSize = colSize / 2;
            grid[i] = new Seat[colSize];
        }

        //fills grid
        for (int i = 0; i < seatNameState.length; i++) {
            String seatType = seatIdState[i].substring(0, 1);
            int column = Integer.parseInt(seatIdState[i].substring(1, 2));
            int row = Integer.parseInt(seatIdState[i].substring(2));
            switch (seatType) {
                case "p": grid[column][row] = new Player(seatNameState[i], seatIdState[i], seatTierState[i]); break;
                case "r": grid[column][row] = new Remnant(seatNameState[i], seatIdState[i], seatTierState[i]); break;
                case "b": grid[column][row] = new Bye(seatIdState[i], seatTierState[i]);
            }
        }
    }

    /**
     * Returns the name of the {@code Bracket}.
     *
     * @return the name of the Bracket
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns an entire column of the {@code Bracket} in the form of a {@code Seat[]}.
     *
     * @param i the index of the column to be returned
     * @return  the desired column as a Seat[]
     */
    @Override
    public Seat[] get(int i) {
        return grid[i];
    }

    /**
     * Returns an individual {@code Seat} from the {@code Bracket}.
     *
     * @param i the index of the row in which the desired Seat is located
     * @param j the index of the Seat to be returned
     * @return  the desired Seat
     */
    @Override
    public Seat get(int i, int j) {
        return grid[i][j];
    }

    /**
     * Returns the date when the {@code Bracket} was created; of the form: MM/DD/YYYY.
     *
     * @return a String representation of the date when the Bracket was created; of the form: MM/DD/YYYY
     */
    @Override
    public String getDateCreated() {
        return dateCreated;
    }

    /**
     * Returns the elimination type of the {@code Bracket}. Since {@code BracketSE} is a single
     * elimination {@code Bracket}, this value will always be {@code BracketInterface.SINGLE_ELIM}.
     *
     * @return the elimination type of the Bracket
     * @see com.joebeaulieu.rapidbrackets.bracketinterface.BracketInterface#SINGLE_ELIM BracketInterface.SINGLE_ELIM
     */
    @Override
    public int getElimType() {
        return elimType;
    }

    /**
     * Returns the number of unique {@code Player}s in the {@code Bracket}. This number does not
     * include {@code Bye}s.
     *
     * @return the number of unique Players in the Bracket, not including Byes
     */
    @Override
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * "Places" a {@code Seat} at the given index in the {@code Bracket} {@code Seat[][]}.
     *
     * @param i    the target column to place the Seat
     * @param j    the index within the target column to place the Seat
     * @param seat the Seat to be placed in the Bracket
     */
    @Override
    public void set(int i, int j, Seat seat) {
        grid[i][j] = seat;
    }

    /**
     * Returns the number of columns in the {@code Bracket}, which is representative of the number
     * of {@code Seat[]}s in the first dimension of the {@code Bracket}'s {@code Seat[][]}.
     *
     * @return the number of columns in the Bracket
     */
    @Override
    public int size() {
        return grid.length;
    }

    /**
     * Determines whether or not the amount of players is a power of two. This is necessary in
     * determining the number of Byes for the {@code Bracket} as every {@code Bracket} must have an
     * amount of Seats that is a power of two, as a {@code Bracket} is a full Binary Tree by nature.
     * <p>
     * Note: although 1 is a power of 2, this method does not treat it as such because a tournament
     * of 1 person will still need 1 {@code Bye}.
     *
     * @param players a String[] containing the name of all Players in the Bracket, not including
     *                Byes
     * @return        a boolean value representing whether or not the number of Players is a power
     *                of two. true = yes, false = no
     */
    private boolean isPower2(String[] players) {
        int x = players.length;
        if (x < 2) {
            return false;
        } else {
            while((x % 2 == 0) && (x > 1)) {
                x /= 2;
            }
        }
        return (x == 1);
    }

    /**
     * Calculates and returns log base 2 of the given {@code int}.
     *
     * @param leaves the number of leaves in the Bracket Seat[]
     * @return       log base 2 of the given int
     */
    private int log2(int leaves) {
        return ((int) (Math.log10(leaves) / Math.log10(2)));
    }
}
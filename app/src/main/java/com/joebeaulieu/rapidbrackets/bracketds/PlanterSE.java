package com.joebeaulieu.rapidbrackets.bracketds;

import com.joebeaulieu.rapidbrackets.seats.Seat;

/**
 * The {@code Planter} is used to create the starting {@code Seat} lineup for the {@code Bracket}.
 * This class sorts and plants all the {@code Seat}s ({@code Player}s and {@code Bye}s; {@code Remnant}s
 * do not yet exist) into the beginning of the {@code Bracket} in their proper positions.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @see com.joebeaulieu.rapidbrackets.seats.Seat Seat
 * @since 1.0.0
 */
public class PlanterSE implements Planter {

    /**
     * The sole, and default constructor for the {@code PlanerSE} class.
     */
    public PlanterSE() {
    }

    /**
     * Plants the {@code Seat}s in the {@code Bracket} in their proper positions. Utilizes
     * {@code sortBySeed(Seat[])} to sort the {@code Seat}s by their seeds.
     * <p>
     * Note: {@code @ManualArrayCopy} is suppressed because the array in question cannot be copied
     * by the {@code System}'s {@code arrayCopy(Object, int Object, int, int)} method in the way that
     * it needs to be. This suppression pertains to the second for loop, which plants the {@code Seat}s
     * in the {@code Bracket} {@code Seat[]}.
     *
     * @param bracket   the Bracket as a Seat[]
     * @param allLeaves a Seat[] containing all of the leaves of the Bracket tree
     * @param empties   the number of empty nodes in the Bracket tree
     * @see #sortBySeed(Seat[]) sortBySeed(Seat[])
     * @see <a href="https://docs.oracle.com/javase/7/docs/api/java/lang/System.html#arraycopy(java.lang.Object,%20int,%20java.lang.Object,%20int,%20int)">System.arraycopy(Object, int, Object, int, int)</a>
     */
    @Override
    @SuppressWarnings("ManualArrayCopy")
    public void plant(Seat[] bracket, Seat[] allLeaves, int empties) {
        Seat[] startingLineup = new Seat[allLeaves.length];
        int[] order = getOrder(allLeaves.length);
        sortBySeed(allLeaves);

        for (int i = 0; i < allLeaves.length; i++) {
            startingLineup[i] = allLeaves[order[i] - 1];
        }
        // starts at the first non-empty spot in the tree
        for (int j = empties; j < bracket.length; j++) {
            bracket[j] = startingLineup[j - empties];
        }
    }

    /**
     * Sorts the {@code Seat}s in ascending order by seed. This is a modified insertion sort method.
     *
     * @param seats a Seat[] containing all the Seats in the Bracket
     */
    private void sortBySeed(Seat[] seats) {
        int i, j;
        Seat temp;
        for (i = 1; i < seats.length; i++) {
            temp = seats[i];
            j = i;
            while (j > 0 && seats[j - 1].getSeed() > temp.getSeed()) {
                seats[j] = seats[j - 1];
                j--;
            }
            seats[j] = temp;
        } 
    }

    /**
     * Determines the seeded order of the {@code Seat}s in the {@code Bracket}. {@code Seat}s are
     * ordered such that {@code Player}s of similar skill are spaced as far as possible from each
     * other (all {@code Bye} seeds are automatically greater than all {@code Player} seeds). This
     * balances the {@code Bracket} in a way such that the two best {@code Player}s will not be able
     * to meet until the end of the tournament, and so forth, tunneling down for {@code Player}s with
     * a greater seed. The lesser the seed, the better the {@code Player}.
     * <p>
     * Example output:
     * <pre>
     *      original  - [1, 2, 3, 4]
     *      output    - [1, 4, 2, 3]
     * </pre>
     * This appears on-screen like:
     * <pre>
     *      1
     *        > 1
     *      4
     *            > 1
     *      2
     *        > 2
     *      3
     * </pre>
     *
     * @param numSeats the amount of Seats in the starting lineup of the Bracket
     * @return         an int[] which represents the seeded order of the Bracket
     */
    private int[] getOrder(int numSeats) {
        int numRounds = numRounds(numSeats) - 1;
        int[] arr = {1, 2};
        
        for(int i = 0; i < numRounds; i++) {
            arr = expand(arr);
        }
        return arr;
    }

    /**
     * Expands the input array, matching each {@code Seat} with its corresponding opponent given the
     * current array size and seeding rules.
     *
     * @param arr an int[] that represents half of the current Seats, represented by seeds
     * @return    an int[] that contains the seeded order for a Bracket of its size
     */
    private int[] expand(int[] arr) {
        int newSize = arr.length * 2;
        int[] newArr = new int[newSize];
        int index = 0;
        
        for(int i = 0; i < newArr.length; i++) {
            if (i % 2 == 0) {
                newArr[i] = arr[index];
                index++;
            } else {
                // length minus (prev number - 1) Rank 1 gets matched with 8; 8 - (1 - 1)
                newArr[i] = newSize - (newArr[i - 1] - 1);
            }
        }
        return newArr;
    }

    /**
     * Determines the number of rounds for a {@code Bracket} with the given amount of {@code Seat}s.
     *
     * @param numSeats the number of Seats in the Bracket
     * @return         the number of rounds for the Bracket
     */
    private int numRounds(int numSeats) {
        if (numSeats == 1) {
            return 0;
        } else {
            return 1 + numRounds(numSeats / 2);
        }
    }
}

/**
 * Contains all the {@code Seat}s used by the {@code Bracket}. Seats can be either {@code Player}s,
 * {@code Remnant}s, or {@code Bye}s. In short, {@code Player}s are simply the "real" players in
 * the {@code Bracket}, {@code Remnant}s are used to show a player's path through the {@code Bracket}
 * (with "greyed-out" player names), and {@code Bye}s are used to enforce the rule that all
 * {@code Bracket}s have seats equal to a power of 2. This is because, by nature, all {@code Bracket}s
 * are full binary trees.
 * <p>
 * For example, if a {@code Bracket} has 3 "real" players, a single {@code Bye} must be added to so
 * that each "real" player has an opponent. Being placed against a {@code Bye} results in an
 * automatic win for that round. Therefore, {@code Bye}s are distributed to players in order of
 * ranking, with the best players getting {@code Bye}s first.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @since 1.0.0
 */
package com.joebeaulieu.rapidbrackets.seats;
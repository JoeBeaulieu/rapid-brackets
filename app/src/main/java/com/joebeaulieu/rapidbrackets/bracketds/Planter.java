package com.joebeaulieu.rapidbrackets.bracketds;

import com.joebeaulieu.rapidbrackets.seats.Seat;

/**
 * The interface for the different types of {@code Planter}s. The {@code Planter} is used to create
 * the starting {@code Seat} lineup for the {@code Bracket}. This class sorts and plants all the
 * {@code Seat}s ({@code Player}s and {@code Bye}s; {@code Remnant}s do not yet exist) into the
 * beginning of the {@code Bracket} in their proper positions. As of now, {@code PlanterSE} is the
 * only implemented subclass.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @see com.joebeaulieu.rapidbrackets.seats.Seat Seat
 * @since 1.0.0
 */
public interface Planter {

    /**
     * Plants the {@code Seat}s ({@code Player}s and {@code Bye}s; {@code Remnant}s do not yet exist)
     * into the beginning of the {@code Bracket} in their proper positions.
     *
     * @param bracket   the Bracket as a Seat[]
     * @param allLeaves a Seat[] containing all of the leaves of the Bracket tree
     * @param empties   the number of empty nodes in the Bracket tree
     */
    void plant(Seat[] bracket, Seat[] allLeaves, int empties);
}

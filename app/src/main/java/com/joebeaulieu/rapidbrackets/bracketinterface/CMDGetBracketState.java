package com.joebeaulieu.rapidbrackets.bracketinterface;

import com.joebeaulieu.rapidbrackets.bracketds.Bracket;
import com.joebeaulieu.rapidbrackets.seats.Seat;

import java.util.ArrayList;

/**
 * Gets the state of the target {@code Bracket} from the {@code SQLiteDatabase} via the {@code Aggregator}.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @see Aggregator Aggregator
 * @since 1.0.0
 */
public class CMDGetBracketState extends Command{

    /**
     * The sole constructor for the {@code CMDGetBracketState} class. Passes the {@code Aggregator}
     * to the {@code Command} superclass.
     *
     * @param agg the Aggregator for the command design pattern
     */
    public CMDGetBracketState(Aggregator agg) {
        super(agg);
    }

    /**
     * Returns the current state of the {@code Bracket} from the underlying {@code Bracket} data
     * structure as an {@code ArrayList<Seat>} upcasted to an {@code ArrayList<Object>}. This
     * {@code ArrayList} does not include empty slots in the {@code Bracket}.
     *
     * @return the current state of the Bracket
     */
    @Override
    public Object execute() {
        Bracket bracket = agg.getBracket();
        ArrayList<Object[]> bracketState = new ArrayList<>();
        bracketState.add(new Object[] {bracket.getDateCreated()});
        for (int i = 0; i < bracket.size(); i++) {
            for (int j = 0; j < bracket.get(i).length; j++) {
                Seat current = bracket.get(i, j);
                if (current != null) {
                    bracketState.add(new Object[] {current.getName(), current.getID(), current.getTier()});
                }
            }
        }
        return bracketState;
    }
}

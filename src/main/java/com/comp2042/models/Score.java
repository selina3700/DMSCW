package com.comp2042.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Responsible for tracking the player's score using a {@code IntegerProperty}.
 * <p>
 *     Using a property ensures that the score display in the GUI automatically updates whenever the
 *     {@code add} or {@code reset} methods are called.
 * </p>
 */
public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);

    /**
     * Returns the object representing the score.
     * <p>
     *     This method is used by the {@code GuiController} to bind the score
     *     label text to the current score value.
     * </p>
     * @return The {@code IntegerProperty} containing the score.
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Adds the specified integer value to the current score
     * @param i The amount to add to the score
     */
    public void add(int i){
        score.setValue(score.getValue() + i);
    }

    /**
     * Resets the current score value back to zero
     */
    public void reset() {
        score.setValue(0);
    }
}

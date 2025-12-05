package com.comp2042.models;

import com.comp2042.game.MatrixOperations;

/**
 * Holds the projected shape position index resulting from the next
 * potential rotation of a brick
 * <p>
 *     Allows the {@code SimpleBoard} to check for collisions with the rotated shape
 *     before committing the change to the brick's state.
 * </p>
 */
public final class NextShapeInfo {

    private final int[][] shape;
    private final int position;

    /**
     * Constructs a new NextShapeInfo object
     * @param shape The 2D array representing the structure of the brick in its next rotational state.
     * @param position The integer index corresponding to the calculated next rotation (0 to N-1).
     */
    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    /**
     * Gets a deep copy of the 2D array (matrix) representing the rotated shape
     * <p>
     * Returning a copy ensures the internal matrix reference remains protected.
     *
     * @return A deep copy of the shape matrix for the next rotation.
     */
    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    /**
     * Gets the index of the next rotational state
     *
     * @return The integer position index (0 to N-1).
     */
    public int getPosition() {
        return position;
    }
}

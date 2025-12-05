package com.comp2042;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.models.NextShapeInfo;

public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    /**
     * Calculates the properties of the brick's next rotational state.
     * <p>
     *     Increments the current rotational index and uses a % to ensure the
     *     index wraps back to 0 after the last rotation.
     * </p>
     * @return {@code NextShapeInfo} object containing the matric data and the index of the next
     * rotation.
     */
    public NextShapeInfo getNextShape() {
        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    /**
     * Retrieves the 2D matrix representing the current rotational state of the brick.
     * <p>
     *     Matrix defines the configuration of blocks for the piece at the index
     *     specified by {@code currentShape}
     * </p>
     *
     * @return The 2D matrix representing the shape of the brick.
     */
    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    /**
     * Sets the rotational index of the brick
     * <p>
     *     Determines which matrix from the shape list will be returned by
     *     {@code getCurrentShape()}
     * </p>
     * @param currentShape The index of the desired rotational state.
     */
    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    /**
     * Assigns a new brick to the rotator.
     * <p>
     *     When a brick is set, the {@code currentShape} index is automatically reset to 0,
     *     this ensures that the brick ALWAYS starts at its initial position.
     * </p>
     * @param brick New brick object managed by this rotator.
     */
    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }
}
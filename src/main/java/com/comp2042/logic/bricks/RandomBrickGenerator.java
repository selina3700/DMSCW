package com.comp2042.logic.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * Implements the 7-Bag System for generating Tetris bricks.
 * <p>
 *     This system ensures that all 7 tetris bricks are generated exactly once in random order
 *     before the sequence is repeated.
 *     This prevents long droughts of a particular brick type.
 * </p>
 * <p>
 *     Maintains 2 queues:
 *     The {@code bag} for shuffling the current set, and {@code nextBricks} to merge
 *     a short preview queue for the game view.
 * </p>
 */
public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickTypes;
    private final Deque<Brick> bag = new ArrayDeque<>();
    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    /**
     * Initializes the generator by defining all seven brick types,
     * filling the first random bag, and populating the initial preview queue
     * {@code nextBrick} with two bricks.
     */
    public RandomBrickGenerator() {
        brickTypes = new ArrayList<>();
        brickTypes.add(new IBrick());
        brickTypes.add(new JBrick());
        brickTypes.add(new LBrick());
        brickTypes.add(new OBrick());
        brickTypes.add(new SBrick());
        brickTypes.add(new TBrick());
        brickTypes.add(new ZBrick());

        refillBag();

        //Initialize queue with 2 bricks
        nextBricks.add(drawFromBag());
        nextBricks.add(drawFromBag());
    }

    /**
     * Fills the internal {@code bag} with one copy of the 7 brick types in a newly
     * shuffled random order.
     */
    private void refillBag() {
        List<Brick> newBag = new ArrayList<>();
        for (Brick b : brickTypes) {
            newBag.add(b.copy());
        }
        Collections.shuffle(newBag);
        bag.addAll(newBag);
    }

    /**
     * Draws the next available brick from the {@code bag}
     *
     * If the bag is empty, it triggers a refill before drawing
     * @return The next {@code Brick} object from the bag.
     */
    private Brick drawFromBag() {
        if (bag.isEmpty()) {
            refillBag();
        }
        return bag.poll();
    }

    /**
     * Retrieve the next brick for the player to use from the head of the preview queue.
     * <p>
     *     After retrieving a brick, a new brick is drawn from the bag and added to the tail of the
     *     preview queue to maintain the required preview size.
     * </p>
     * @return The next {@code Brick} object to fall on the board.
     */
    @Override
    public Brick getBrick() {
        while (nextBricks.size() < 2) {
            nextBricks.add(drawFromBag());
        }

        Brick brick = nextBricks.poll();
        nextBricks.add(drawFromBag());

        return brick;
    }

    /**
     * Peeks at the next brick in the queue.
     *
     * Used to display the "Next Brick" preview in the GUI
     * @return The {@code Brick} object that will be returned by {@code getBrick()}
     */
    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }
}

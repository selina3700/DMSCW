package com.comp2042.logic.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickTypes;
    private final Deque<Brick> bag = new ArrayDeque<>();
    private final Deque<Brick> nextBricks = new ArrayDeque<>();

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

        // Initialize next queue with 2 bricks
        nextBricks.add(drawFromBag());
        nextBricks.add(drawFromBag());
    }

    /** Fills and shuffles the 7-bag */
    private void refillBag() {
        List<Brick> newBag = new ArrayList<>();
        for (Brick b : brickTypes) {
            newBag.add(b.copy()); // important!
        }
        Collections.shuffle(newBag);
        bag.addAll(newBag);
    }

    /** Draws the next brick from the bag, refilling if empty */
    private Brick drawFromBag() {
        if (bag.isEmpty()) {
            refillBag();
        }
        return bag.poll();
    }

    @Override
    public Brick getBrick() {
        // Always keep at least 2 bricks predicted
        while (nextBricks.size() < 2) {
            nextBricks.add(drawFromBag());
        }

        Brick brick = nextBricks.poll();
        nextBricks.add(drawFromBag()); // push next piece into queue

        return brick;
    }

    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }
}

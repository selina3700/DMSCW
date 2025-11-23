package com.comp2042.logic.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;
    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    public RandomBrickGenerator() {
        brickList = new ArrayList<>();
        brickList.add(new IBrick());
        brickList.add(new JBrick());
        brickList.add(new LBrick());
        brickList.add(new OBrick());
        brickList.add(new SBrick());
        brickList.add(new TBrick());
        brickList.add(new ZBrick());

        Brick first = getRandomBrick();
        Brick second = getRandomBrick();
        nextBricks.add(first);
        nextBricks.add(second);

        System.out.println("=== BRICK GENERATOR INITIALIZED ===");
        System.out.println("Queue: [" + first.getClass().getSimpleName() + ", " + second.getClass().getSimpleName() + "]");
    }

    private Brick getRandomBrick() {
        return brickList.get(ThreadLocalRandom.current().nextInt(brickList.size()));
    }

    @Override
    public Brick getBrick() {
        while (nextBricks.size() < 2) {
            nextBricks.add(getRandomBrick());
        }
        Brick brick = nextBricks.poll();

        // DEBUG: Print stack trace to see WHO is calling getBrick()
        System.out.println("=== getBrick() called, returning: " + brick.getClass().getSimpleName() + " ===");
        Thread.dumpStack();

        return brick;
    }

    @Override
    public Brick getNextBrick() {
        Brick next = nextBricks.peek();
        System.out.println("getNextBrick() called, peeking: " + (next != null ? next.getClass().getSimpleName() : "null"));
        return next;
    }
}
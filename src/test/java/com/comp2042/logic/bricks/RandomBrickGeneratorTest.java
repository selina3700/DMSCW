package com.comp2042.logic.bricks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RandomBrickGeneratorTest {

    private RandomBrickGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new RandomBrickGenerator();
    }

    @Test
    void getBrick() {
        Brick brick = generator.getBrick();
        assertNotNull(brick);
        assertNotNull(brick.getShapeMatrix());
    }

    @Test
    void getNextBrick() {
        Brick nextBrick = generator.getNextBrick();
        assertNotNull(nextBrick);
        assertNotNull(nextBrick.getShapeMatrix());
    }
}
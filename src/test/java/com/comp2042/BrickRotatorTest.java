package com.comp2042;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.models.NextShapeInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BrickRotatorTest {

    private BrickRotator rotator;
    private TestBrick testBrick;

    private static class TestBrick implements Brick {
        private List<int[][]> shapes;

        public TestBrick() {
            shapes = new ArrayList<>();
            //4 rotation states
            shapes.add(new int[][]{{1, 1, 1, 1}});
            shapes.add(new int[][]{{1}, {1}, {1}, {1}});
            shapes.add(new int[][]{{1, 1, 1, 1}});
            shapes.add(new int[][]{{1}, {1}, {1}, {1}});
        }

        @Override
        public List<int[][]> getShapeMatrix() {
            return shapes;
        }

        @Override
        public Brick copy() {
            return new TestBrick();
        }
    }

    @BeforeEach
    void setUp() {
        rotator = new BrickRotator();
        testBrick = new TestBrick();
    }

    @Test
    void getNextShape() {
        rotator.setBrick(testBrick);

        //Get next shape from position 0
        NextShapeInfo nextShape = rotator.getNextShape();
        assertEquals(1, nextShape.getPosition());
        assertNotNull(nextShape.getShape());

        //Verify shape matrix is correct
        int[][] shape = nextShape.getShape();
        assertEquals(4, shape.length);
        assertEquals(1, shape[0].length);

        //Get next from position 1
        rotator.setCurrentShape(1);
        NextShapeInfo nextShape2 = rotator.getNextShape();
        assertEquals(2, nextShape2.getPosition());

        //Test wrapping
        rotator.setCurrentShape(3);
        NextShapeInfo wrappedShape = rotator.getNextShape();
        assertEquals(0, wrappedShape.getPosition());

        //Verify wrapped shape is correct
        int[][] wrappedMatrix = wrappedShape.getShape();
        assertEquals(1, wrappedMatrix.length);
        assertEquals(4, wrappedMatrix[0].length);
    }

    @Test
    void getCurrentShape() {
        rotator.setBrick(testBrick);

        //Get shape at initial position
        int[][] shape = rotator.getCurrentShape();
        assertNotNull(shape);
        assertEquals(1, shape.length);
        assertEquals(4, shape[0].length);
        assertEquals(1, shape[0][0]);

        //Change to position 1 and verify
        rotator.setCurrentShape(1);
        int[][] shape2 = rotator.getCurrentShape();
        assertEquals(4, shape2.length);
        assertEquals(1, shape2[0].length);

        //Verify position 2
        rotator.setCurrentShape(2);
        int[][] shape3 = rotator.getCurrentShape();
        assertEquals(1, shape3.length);
        assertEquals(4, shape3[0].length);

        //Verify position 3
        rotator.setCurrentShape(3);
        int[][] shape4 = rotator.getCurrentShape();
        assertEquals(4, shape4.length);
        assertEquals(1, shape4[0].length);
    }

    @Test
    void setCurrentShape() {
        rotator.setBrick(testBrick);

        //Set to position 0
        rotator.setCurrentShape(0);
        int[][] shape = rotator.getCurrentShape();
        assertEquals(1, shape.length);
        assertEquals(4, shape[0].length);

        //Set to position 2
        rotator.setCurrentShape(2);
        int[][] shape2 = rotator.getCurrentShape();
        assertEquals(1, shape2.length);

        //Verify getNextShape uses the updated position
        rotator.setCurrentShape(2);
        NextShapeInfo next = rotator.getNextShape();
        assertEquals(3, next.getPosition());

        //Set to last position and verify next wraps
        rotator.setCurrentShape(3);
        NextShapeInfo nextWrapped = rotator.getNextShape();
        assertEquals(0, nextWrapped.getPosition());
    }

    @Test
    void setBrick() {
        rotator.setBrick(testBrick);

        //Set position to non-zero
        rotator.setCurrentShape(2);
        assertEquals(1, rotator.getCurrentShape().length);

        //Set brick again - should reset to position 0
        TestBrick newBrick = new TestBrick();
        rotator.setBrick(newBrick);

        int[][] shape = rotator.getCurrentShape();
        assertEquals(1, shape.length);
        assertEquals(4, shape[0].length);

        //Verify next shape starts from position 0
        NextShapeInfo next = rotator.getNextShape();
        assertEquals(1, next.getPosition());

        //Set brick when at position 3, verify reset
        rotator.setCurrentShape(3);
        rotator.setBrick(testBrick);

        int[][] resetShape = rotator.getCurrentShape();
        assertEquals(1, resetShape.length);
        assertEquals(4, resetShape[0].length);

        //Verify position is truly 0
        NextShapeInfo nextAfterReset = rotator.getNextShape();
        assertEquals(1, nextAfterReset.getPosition());
    }

    @Test
    void rotationCycleIntegration() {
        //Full rotation cycle
        rotator.setBrick(testBrick);

        //Cycle through all positions
        for (int i = 0; i < 4; i++) {
            rotator.setCurrentShape(i);
            NextShapeInfo next = rotator.getNextShape();
            int expectedNext = (i + 1) % 4;
            assertEquals(expectedNext, next.getPosition());
        }
    }

    @Test
    void getNextShapeDoesNotModifyCurrentShape() {
        rotator.setBrick(testBrick);
        rotator.setCurrentShape(1);

        rotator.getNextShape();
        rotator.getNextShape();
        rotator.getNextShape();

        int[][] currentShape = rotator.getCurrentShape();
        assertEquals(4, currentShape.length);
        assertEquals(1, currentShape[0].length);
    }
}
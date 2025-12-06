package com.comp2042.logic.bricks;

import java.util.List;

/**
 * Defines All Tetris Brick Shapes
 */
public interface Brick {

    List<int[][]> getShapeMatrix();
    Brick copy();
}

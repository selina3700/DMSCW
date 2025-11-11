package com.comp2042;

import com.comp2042.logic.bricks.Brick;

public class BrickRotator {

    private Brick brick;
    private int currentShape = 0; //Current rotation of the brick

    //Calculates the next rotation of the brick
    public NextShapeInfo getNextShape() {
        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    //Sets a new brick for rotation
    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }


}

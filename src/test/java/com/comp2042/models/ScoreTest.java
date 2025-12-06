package com.comp2042.models;

import javafx.beans.property.IntegerProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {
    private Score score;

    @BeforeEach
    void setUp() {
        score = new Score();
    }

    @Test
    void scoreProperty() {
        IntegerProperty property = score.scoreProperty();
        assertNotNull(property);
        assertEquals(0, property.get());
    }

    @Test
    void add() {
        score.add(100);
        assertEquals(100, score.scoreProperty().get());
        score.add(50);
        assertEquals(150, score.scoreProperty().get());
    }

    @Test
    void reset() {
        score.add(500);
        assertEquals(500, score.scoreProperty().get());
        score.reset();
        assertEquals(0, score.scoreProperty().get());
    }
}
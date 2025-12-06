package com.comp2042.events;

/**
 * Defines the possible types of actions or movements that can be triggered in the game
 * <p>
 *     Enumeration is used within the {@code MoveEvent} class to instruct the {@code GameController}
 *     what operation to execute on the falling brick.
 * </p>
 */
public enum EventType {
    DOWN, LEFT, RIGHT, ROTATE
}

package com.comp2042.events;

/**
 * Defines the possible origins of a game event
 * Enumeration is crucial for the {@code GameController} to differentiate between events triggered
 * by user input and events triggered automatically by the game loop (such as moving bricks down).
 */
public enum EventSource {
    USER, THREAD
}

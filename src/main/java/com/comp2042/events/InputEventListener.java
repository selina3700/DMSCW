package com.comp2042.events;

import com.comp2042.models.DownData;
import com.comp2042.models.ViewData;

/**
 * Defines the contract for all classes that handle core game input and control events
 * <p>
 *     This ensures tha the game logic is triggered consistently regardless of how the input
 *     was originated.
 * </p>
 */
public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);
    ViewData onLeftEvent(MoveEvent event);
    ViewData onRightEvent(MoveEvent event);
    ViewData onRotateEvent(MoveEvent event);
    void createNewGame();
    void onHoldEvent();
}

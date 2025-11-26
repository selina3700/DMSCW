package com.comp2042.events;

import com.comp2042.models.DownData;
import com.comp2042.models.ViewData;

public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    void createNewGame();
    void onHoldEvent();
}

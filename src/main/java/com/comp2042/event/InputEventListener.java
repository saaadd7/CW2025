package com.comp2042.event;

import com.comp2042.event.DownData;
import com.comp2042.event.MoveEvent;
import com.comp2042.event.ViewData;

public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    DownData onHardDropEvent(MoveEvent event);

    void onBackToMenuEvent();

    void createNewGame();

   // void onLevelUp(int newLevel);
}

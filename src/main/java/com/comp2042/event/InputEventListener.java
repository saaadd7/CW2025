package com.comp2042.event;

import com.comp2042.event.DownData;
import com.comp2042.event.MoveEvent;
import com.comp2042.event.ViewData;

public interface InputEventListener {

    Object onGameEvent(GameEvent event);

}

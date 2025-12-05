package com.comp2042.event;

import com.comp2042.event.DownData;
import com.comp2042.event.MoveEvent;
import com.comp2042.event.ViewData;

/**
 * Defines the contract for an event listener that responds to user input
 * and game events related to brick movements and game state changes.
 */
public interface InputEventListener {

    Object onGameEvent(GameEvent event);

}

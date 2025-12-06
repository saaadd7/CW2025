package com.comp2042.event;

import com.comp2042.event.DownData;
import com.comp2042.event.MoveEvent;
import com.comp2042.event.ViewData;

/**
 * Defines the contract for an event listener that responds to user input
 * and game events related to brick movements and game state changes.
 */
public interface InputEventListener {

    /**
     * Handles a game event.
     *
     * @param event The game event to handle.
     * @return An object representing the result of the event, or null if there is no result.
     */
    Object onGameEvent(GameEvent event);

}

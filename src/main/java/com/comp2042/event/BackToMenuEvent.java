package com.comp2042.event;

/**
 * Represents a game event for returning to the main menu.
 */
public class BackToMenuEvent extends GameEvent {

    /**
     * Constructs a new BackToMenuEvent.
     */
    public BackToMenuEvent() {
        super(EventType.BACK_TO_MENU);
    }
}

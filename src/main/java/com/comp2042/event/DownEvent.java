package com.comp2042.event;

/**
 * Represents a game event for moving the current brick down.
 */
public class DownEvent extends GameEvent {

    /**
     * Constructs a new DownEvent.
     */
    public DownEvent() {
        super(EventType.DOWN);
    }
}

package com.comp2042.event;

/**
 * Represents a game event for moving the current brick to the left.
 */
public class LeftEvent extends GameEvent {

    /**
     * Constructs a new LeftEvent.
     */
    public LeftEvent() {
        super(EventType.LEFT);
    }
}

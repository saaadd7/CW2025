package com.comp2042.event;

/**
 * Represents a game event for moving the current brick to the right.
 */
public class RightEvent extends GameEvent {

    /**
     * Constructs a new RightEvent.
     */
    public RightEvent() {
        super(EventType.RIGHT);
    }
}

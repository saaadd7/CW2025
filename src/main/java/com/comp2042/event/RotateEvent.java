package com.comp2042.event;

/**
 * Represents a game event for rotating the current brick.
 */
public class RotateEvent extends GameEvent {

    /**
     * Constructs a new RotateEvent.
     */
    public RotateEvent() {
        super(EventType.ROTATE);
    }
}

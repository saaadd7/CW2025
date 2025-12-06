package com.comp2042.event;

/**
 * Represents a game event for performing a "hard drop" on the current brick.
 */
public class HardDropEvent extends GameEvent {

    /**
     * Constructs a new HardDropEvent.
     */
    public HardDropEvent() {
        super(EventType.HARD_DROP);
    }
}

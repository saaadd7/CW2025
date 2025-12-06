package com.comp2042.event;

/**
 * Represents a generic game event.
 */
public class GameEvent {

    private final EventType type;

    /**
     * Constructs a new GameEvent.
     *
     * @param type The type of the event.
     */
    public GameEvent(EventType type) {
        this.type = type;
    }

    /**
     * Returns the type of the event.
     *
     * @return The event type.
     */
    public EventType getType() {
        return type;
    }
}

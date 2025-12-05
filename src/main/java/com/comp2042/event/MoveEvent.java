package com.comp2042.event;

import com.comp2042.event.EventType;
import com.comp2042.event.EventSource;

/**
 * An immutable data class that encapsulates a movement event in the game.
 * It contains information about the type of movement and its source (user or system).
 */
public final class MoveEvent {
    private final EventType eventType;
    private final EventSource eventSource;

    /**
     * Constructs a MoveEvent object.
     *
     * @param eventType The {@link EventType} representing the kind of movement (e.g., DOWN, LEFT, ROTATE).
     * @param eventSource The {@link EventSource} indicating whether the event was user-initiated or from a game thread.
     */
    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    /**
     * Returns the type of this movement event.
     * @return The {@link EventType} of the movement.
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Returns the source of this movement event.
     * @return The {@link EventSource} of the movement.
     */
    public EventSource getEventSource() {
        return eventSource;
    }
}

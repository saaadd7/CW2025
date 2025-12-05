package com.comp2042.event;

public class GameEvent {

    private final EventType type;

    public GameEvent(EventType type) {
        this.type = type;
    }

    public EventType getType() {
        return type;
    }
}

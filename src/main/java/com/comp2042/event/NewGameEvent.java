package com.comp2042.event;

/**
 * Represents a game event for starting a new game.
 */
public class NewGameEvent extends GameEvent {

    /**
     * Constructs a new NewGameEvent.
     */
    public NewGameEvent() {
        super(EventType.NEW_GAME);
    }
}

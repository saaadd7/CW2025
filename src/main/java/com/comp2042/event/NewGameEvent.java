package com.comp2042.event;

public class NewGameEvent extends GameEvent {

    public NewGameEvent() {
        super(EventType.NEW_GAME);
    }
}

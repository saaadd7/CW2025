package com.comp2042.event;

/**
 * Defines the possible sources of a game event.
 * Events can originate from user input or from internal game threads (e.g., automatic brick drops).
 */
public enum EventSource {
    /** Indicates that the event was triggered by direct user interaction. */
    USER,
    /** Indicates that the event was triggered by an internal game thread (e.g., timed brick movement). */
    THREAD
}

package com.comp2042.event;

/**
 * Defines the types of movement events that can occur in the game.
 * These events correspond to actions like moving a brick down, left, right, rotating it, or performing a hard drop.
 */
public enum EventType {
    /** Represents a downward movement event. */
    DOWN,
    /** Represents a leftward movement event. */
    LEFT,
    /** Represents a rightward movement event. */
    RIGHT,
    /** Represents a rotation movement event. */
    ROTATE,
    /** Represents an immediate drop to the bottom event. */
    HARD_DROP,
    /** Represents a new game event. */
    NEW_GAME,
    /** Represents an event to go back to the main menu. */
    BACK_TO_MENU
}

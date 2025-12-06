package com.comp2042;

/**
 * An enumeration of the available game modes.
 */
public enum GameMode {
    /**
     * Classic Tetris mode. Play until the board fills up.
     */
    CLASSIC("Classic", "Play until game over"),
    /**
     * Sprint mode. Clear 30 lines as fast as possible.
     */
    SPRINT("Sprint", "Clear 30 lines as fast as possible"),
    /**
     * Ultra mode. Score as many points as possible in 2 minutes.
     */
    ULTRA("Ultra", "Score as much as possible in 2 minutes");

    private final String displayName;
    private final String description;

    /**
     * Constructs a new GameMode.
     *
     * @param displayName The name to display for the game mode.
     * @param description A short description of the game mode.
     */
    GameMode(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Returns the display name of the game mode.
     *
     * @return The display name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the description of the game mode.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }
}
package com.comp2042;

public enum GameMode {
    CLASSIC("Classic", "Play until game over"),
    SPRINT("Sprint", "Clear 30 lines as fast as possible"),
    ULTRA("Ultra", "Score as much as possible in 2 minutes");

    private final String displayName;
    private final String description;

    GameMode(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
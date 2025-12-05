package com.comp2042.event;

import com.comp2042.event.ClearRow;
import com.comp2042.event.ViewData;

/**
 * An immutable data class that encapsulates the result of a brick's downward movement,
 * potentially including information about cleared rows and updated view data.
 */
public final class DownData {
    private final ClearRow clearRow;
    private final ViewData viewData;

    /**
     * Constructs a DownData object.
     *
     * @param clearRow An optional {@link ClearRow} object if lines were cleared, or null otherwise.
     * @param viewData The updated {@link ViewData} reflecting the current state of the game board and brick.
     */
    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    /**
     * Returns the {@link ClearRow} object if any lines were cleared as a result of the downward movement.
     * @return The {@link ClearRow} object, or null if no lines were cleared.
     */
    public ClearRow getClearRow() {
        return clearRow;
    }

    /**
     * Returns the {@link ViewData} object that represents the current visual state of the game
     * after the downward movement.
     * @return The {@link ViewData} object.
     */
    public ViewData getViewData() {
        return viewData;
    }
}

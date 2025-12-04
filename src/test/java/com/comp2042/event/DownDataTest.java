package com.comp2042.event;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DownDataTest {

    @Test
    void testDownDataStoresValues() {
        // Arrange
        // We can pass null here because we are testing the CONTAINER,
        // not the objects inside it. This keeps the test fast and isolated.
        ClearRow dummyRow = null;
        ViewData dummyView = null;

        // Act
        DownData event = new DownData(dummyRow, dummyView);

        // Assert
        assertNull(event.getClearRow(), "Should return the ClearRow object passed in");
        assertNull(event.getViewData(), "Should return the ViewData object passed in");
    }
}
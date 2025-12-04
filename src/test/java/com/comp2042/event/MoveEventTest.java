package com.comp2042.event;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MoveEventTest {

    @Test
    void testMoveEventStoresValues() {
        // Arrange
        // We use null here to strictly test that the class stores 'whatever' is given to it.
        // You can replace these with real values like EventType.DOWN if you prefer.
        EventType type = null;
        EventSource source = null;

        // Act
        MoveEvent event = new MoveEvent(type, source);

        // Assert
        // We expect exactly what we put in (null) to come back out.
        assertNull(event.getEventType(), "Should return the EventType passed in");
        assertNull(event.getEventSource(), "Should return the EventSource passed in");
    }
}
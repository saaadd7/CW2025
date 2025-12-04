package com.comp2042.core.logic.bricks;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RandomBrickGeneratorTest {

    @Test
    void testInitialization() {
        RandomBrickGenerator generator = new RandomBrickGenerator();

        // The generator should start with a filled queue (size 4 based on your code)
        assertNotNull(generator.getNextBrick(), "Generator should have bricks ready immediately");
        assertEquals(4, generator.getNextBricks(10).size(), "Should initially have exactly 4 bricks in the queue");
    }

    @Test
    void testPeekVsPoll() {
        RandomBrickGenerator generator = new RandomBrickGenerator();

        // 1. Peek at the next brick
        Brick peekedBrick = generator.getNextBrick();

        // 2. Poll (take) the next brick
        Brick polledBrick = generator.getBrick();

        // 3. They should be the exact same object
        assertSame(peekedBrick, polledBrick, "getNextBrick() (peek) should match getBrick() (poll)");
    }

    @Test
    void testQueueRefillsAutomatically() {
        RandomBrickGenerator generator = new RandomBrickGenerator();

        // Take 100 bricks. The generator should never run out.
        for (int i = 0; i < 100; i++) {
            Brick b = generator.getBrick();
            assertNotNull(b, "Generator should never return null");
        }
    }

    @Test
    void testGetNextBricksList() {
        RandomBrickGenerator generator = new RandomBrickGenerator();

        // 1. Ask for the next 3 upcoming bricks
        List<Brick> futureBricks = generator.getNextBricks(3);

        // 2. Verify we got 3
        assertEquals(3, futureBricks.size());

        // 3. Verify that taking bricks one by one matches this list
        assertSame(futureBricks.get(0), generator.getBrick(), "First brick should match preview index 0");
        assertSame(futureBricks.get(1), generator.getBrick(), "Second brick should match preview index 1");
        assertSame(futureBricks.get(2), generator.getBrick(), "Third brick should match preview index 2");
    }
}
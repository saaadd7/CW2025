package com.comp2042.core.logic.bricks;

import com.comp2042.core.logic.bricks.Brick;
import com.comp2042.core.logic.bricks.BrickGenerator;
import com.comp2042.core.logic.bricks.IBrick;
import com.comp2042.core.logic.bricks.JBrick;
import com.comp2042.core.logic.bricks.LBrick;
import com.comp2042.core.logic.bricks.OBrick;
import com.comp2042.core.logic.bricks.SBrick;
import com.comp2042.core.logic.bricks.TBrick;
import com.comp2042.core.logic.bricks.ZBrick;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implements the {@link BrickGenerator} interface to provide a random sequence of Tetris bricks.
 * It maintains a queue of upcoming bricks to allow for preview functionality.
 */
public class RandomBrickGenerator implements BrickGenerator {

    private static final int INITIAL_QUEUE_SIZE = 4;

    private final List<BrickType> brickTypeList;

    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    /**
     * Constructs a RandomBrickGenerator. Initializes the list of available brick types
     * and populates the initial queue of next bricks randomly.
     */
    public RandomBrickGenerator() {
        brickTypeList = Arrays.asList(BrickType.values());
        while (nextBricks.size() < INITIAL_QUEUE_SIZE) {
            nextBricks.add(randomBrick());
        }
    }

    /**
     * Retrieves the next brick from the queue and adds a new random brick to the end of the queue.
     * @return The next {@link Brick} to be used in the game.
     */
    @Override
    public Brick getBrick() {
        Brick next = nextBricks.poll();
        nextBricks.add(randomBrick());
        return next;
    }

    /**
     * Peeks at the next brick in the queue without removing it.
     * This is useful for displaying the "next piece" preview in the UI.
     * @return The {@link Brick} that will be returned by the next call to {@code getBrick()}.
     */
    @Override
    public Brick getNextBrick() {
        // The first upcoming brick
        return nextBricks.peek();
    }

    /**
     * Returns a list of upcoming bricks for UI preview purposes.
     *
     * @param count The number of upcoming bricks to retrieve.
     * @return A {@link List} of {@link Brick} objects representing the next few bricks.
     */
    // Optional convenience for UIs that want to show multiple next pieces (not part of interface)
    public List<Brick> getNextBricks(int count) {
        List<Brick> list = new ArrayList<>();
        int i = 0;
        for (Brick b : nextBricks) {
            if (i++ >= count) break;
            list.add(b);
        }
        return list;
    }

    /**
     * Generates a random brick from the {@code brickList}.
     * @return A randomly selected {@link Brick} instance.
     */
    private Brick randomBrick() {
        BrickType randomType = brickTypeList.get(ThreadLocalRandom.current().nextInt(brickTypeList.size()));
        return BrickFactory.createBrick(randomType);
    }
}
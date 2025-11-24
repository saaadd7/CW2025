package com.comp2042.logic.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomBrickGenerator implements BrickGenerator {

    private static final int INITIAL_QUEUE_SIZE = 4;

    private final List<Brick> brickList;

    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    public RandomBrickGenerator() {
        brickList = new ArrayList<>();
        brickList.add(new IBrick());
        brickList.add(new JBrick());
        brickList.add(new LBrick());
        brickList.add(new OBrick());
        brickList.add(new SBrick());
        brickList.add(new TBrick());
        brickList.add(new ZBrick());
        while (nextBricks.size() < INITIAL_QUEUE_SIZE) {
            nextBricks.add(randomBrick());
        }
    }

    @Override
    public Brick getBrick() {
        Brick next = nextBricks.poll();
        nextBricks.add(randomBrick());
        return next;
    }

    @Override
    public Brick getNextBrick() {
        // The first upcoming brick
        return nextBricks.peek();
    }

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

    private Brick randomBrick() {
        return brickList.get(ThreadLocalRandom.current().nextInt(brickList.size()));
    }
}
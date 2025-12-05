package com.comp2042.core.logic.bricks;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BrickFactoryTest {

    @Test
    void testCreateIBrick() {
        Brick brick = BrickFactory.createBrick(BrickType.I);
        assertTrue(brick instanceof IBrick);
    }

    @Test
    void testCreateJBrick() {
        Brick brick = BrickFactory.createBrick(BrickType.J);
        assertTrue(brick instanceof JBrick);
    }

    @Test
    void testCreateLBrick() {
        Brick brick = BrickFactory.createBrick(BrickType.L);
        assertTrue(brick instanceof LBrick);
    }

    @Test
    void testCreateOBrick() {
        Brick brick = BrickFactory.createBrick(BrickType.O);
        assertTrue(brick instanceof OBrick);
    }

    @Test
    void testCreateSBrick() {
        Brick brick = BrickFactory.createBrick(BrickType.S);
        assertTrue(brick instanceof SBrick);
    }

    @Test
    void testCreateTBrick() {
        Brick brick = BrickFactory.createBrick(BrickType.T);
        assertTrue(brick instanceof TBrick);
    }

    @Test
    void testCreateZBrick() {
        Brick brick = BrickFactory.createBrick(BrickType.Z);
        assertTrue(brick instanceof ZBrick);
    }
}

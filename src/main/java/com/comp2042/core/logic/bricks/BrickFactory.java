package com.comp2042.core.logic.bricks;

/**
 * A factory class for creating different types of Tetris bricks.
 * This class provides a static method to create instances of {@link Brick} based on a given {@link BrickType}.
 */
public class BrickFactory {

    /**
     * Constructs a new BrickFactory instance.
     * This class is a utility class and should not be instantiated.
     */
    private BrickFactory() {
        // Private constructor to prevent instantiation
    }

    /**
     * Creates a new {@link Brick} instance of the specified type.
     *
     * @param type The type of brick to create.
     * @return A new {@link Brick} instance.
     */
    public static Brick createBrick(BrickType type) {
        return switch (type) {
            case I -> new IBrick();
            case J -> new JBrick();
            case L -> new LBrick();
            case O -> new OBrick();
            case S -> new SBrick();
            case T -> new TBrick();
            case Z -> new ZBrick();
        };
    }
}

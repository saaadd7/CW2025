package com.comp2042.core.logic.bricks;

public class BrickFactory {

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

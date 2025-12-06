package com.comp2042.core.logic.command;

import com.comp2042.core.Board;

/**
 * A command that performs a "hard drop" on the current brick, moving it down as far as possible.
 */
public class HardDropCommand implements Command {

    private final Board board;

    /**
     * Constructs a new HardDropCommand.
     *
     * @param board The game board.
     */
    public HardDropCommand(Board board) {
        this.board = board;
    }

    /**
     * Executes the command, performing the hard drop.
     */
    @Override
    public void execute() {
        boolean canMove;
        do {
            canMove = board.moveBrickDown();
        } while (canMove);
    }
}

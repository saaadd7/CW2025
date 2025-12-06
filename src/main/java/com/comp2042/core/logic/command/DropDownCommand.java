package com.comp2042.core.logic.command;

import com.comp2042.core.Board;

/**
 * A command that moves the current brick on the board down by one row.
 */
public class DropDownCommand implements Command {

    private final Board board;

    /**
     * Constructs a new DropDownCommand.
     *
     * @param board The game board.
     */
    public DropDownCommand(Board board) {
        this.board = board;
    }

    /**
     * Executes the command, moving the brick down.
     */
    @Override
    public void execute() {
        board.moveBrickDown();
    }
}

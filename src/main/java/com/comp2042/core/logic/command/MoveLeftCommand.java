package com.comp2042.core.logic.command;

import com.comp2042.core.Board;

/**
 * A command that moves the current brick on the board to the left by one column.
 */
public class MoveLeftCommand implements Command {

    private final Board board;

    /**
     * Constructs a new MoveLeftCommand.
     *
     * @param board The game board.
     */
    public MoveLeftCommand(Board board) {
        this.board = board;
    }

    /**
     * Executes the command, moving the brick to the left.
     */
    @Override
    public void execute() {
        board.moveBrickLeft();
    }
}

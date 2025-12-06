package com.comp2042.core.logic.command;

import com.comp2042.core.Board;

/**
 * A command that moves the current brick on the board to the right by one column.
 */
public class MoveRightCommand implements Command {

    private final Board board;

    /**
     * Constructs a new MoveRightCommand.
     *
     * @param board The game board.
     */
    public MoveRightCommand(Board board) {
        this.board = board;
    }

    /**
     * Executes the command, moving the brick to the right.
     */
    @Override
    public void execute() {
        board.moveBrickRight();
    }
}

package com.comp2042.core.logic.command;

import com.comp2042.core.Board;

/**
 * A command that rotates the current brick on the board.
 */
public class RotateCommand implements Command {

    private final Board board;

    /**
     * Constructs a new RotateCommand.
     *
     * @param board The game board.
     */
    public RotateCommand(Board board) {
        this.board = board;
    }

    /**
     * Executes the command, rotating the brick.
     */
    @Override
    public void execute() {
        board.rotateLeftBrick();
    }
}

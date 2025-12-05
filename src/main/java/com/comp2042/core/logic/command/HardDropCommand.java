package com.comp2042.core.logic.command;

import com.comp2042.core.Board;

public class HardDropCommand implements Command {

    private final Board board;

    public HardDropCommand(Board board) {
        this.board = board;
    }

    @Override
    public void execute() {
        boolean canMove;
        do {
            canMove = board.moveBrickDown();
        } while (canMove);
    }
}

package com.comp2042.core.logic.command;

import com.comp2042.core.Board;

public class DropDownCommand implements Command {

    private final Board board;

    public DropDownCommand(Board board) {
        this.board = board;
    }

    @Override
    public void execute() {
        board.moveBrickDown();
    }
}

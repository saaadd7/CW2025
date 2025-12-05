package com.comp2042.core.logic.command;

import com.comp2042.core.Board;

public class MoveLeftCommand implements Command {

    private final Board board;

    public MoveLeftCommand(Board board) {
        this.board = board;
    }

    @Override
    public void execute() {
        board.moveBrickLeft();
    }
}

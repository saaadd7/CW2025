package com.comp2042.core.logic.command;

import com.comp2042.core.Board;

public class MoveRightCommand implements Command {

    private final Board board;

    public MoveRightCommand(Board board) {
        this.board = board;
    }

    @Override
    public void execute() {
        board.moveBrickRight();
    }
}

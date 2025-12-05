package com.comp2042.core.logic.command;

import com.comp2042.core.Board;

public class RotateCommand implements Command {

    private final Board board;

    public RotateCommand(Board board) {
        this.board = board;
    }

    @Override
    public void execute() {
        board.rotateLeftBrick();
    }
}

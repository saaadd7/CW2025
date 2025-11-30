package com.comp2042.ui;

import com.comp2042.EventSource;
import com.comp2042.EventType;
import com.comp2042.InputEventListener;
import com.comp2042.MoveEvent;
import javafx.scene.input.KeyEvent;

public class InputHandler {

    private InputEventListener eventListener;

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }
    private final GameFlowController gameFlowController;
    private final GameBoardRenderer gameBoardRenderer;

    public InputHandler(GameFlowController gameFlowController, GameBoardRenderer gameBoardRenderer) {
        this.gameFlowController = gameFlowController;
        this.gameBoardRenderer = gameBoardRenderer;
    }

    public void handleKeyPress(KeyEvent keyEvent) {
        if (!gameFlowController.isPaused() && !gameFlowController.isGameOver()) {
            switch (keyEvent.getCode()) {
                case LEFT:
                case A:
                    gameBoardRenderer.refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                    return;
                case RIGHT:
                case D:
                    gameBoardRenderer.refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                    return;
                case UP:
                case W:
                    gameBoardRenderer.refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                    return;
                case DOWN:
                case S:
                    gameFlowController.handleDropResult(eventListener.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.USER)));
                    return;
                case SPACE:
                    gameFlowController.handleDropResult(eventListener.onHardDropEvent(new MoveEvent(EventType.HARD_DROP, EventSource.USER)));
                    return;
            }
        }

        switch (keyEvent.getCode()) {
            case N:
                gameFlowController.newGame();
                break;
            case P:
                gameFlowController.pauseGame();
                break;
        }
    }
}

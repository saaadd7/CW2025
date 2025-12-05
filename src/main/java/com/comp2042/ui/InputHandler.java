package com.comp2042.ui;

import com.comp2042.event.GameEvent;
import com.comp2042.event.InputEventListener;
import com.comp2042.event.MoveEvent;
import javafx.scene.input.KeyEvent;
import com.comp2042.ui.GameFlowController;
import com.comp2042.ui.GameBoardRenderer;

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
        if (eventListener == null) return;

        if (!gameFlowController.isPaused() && !gameFlowController.isGameOver()) {
            GameEvent event = null;
            switch (keyEvent.getCode()) {
                case LEFT:
                case A:
                    event = new com.comp2042.event.LeftEvent();
                    break;
                case RIGHT:
                case D:
                    event = new com.comp2042.event.RightEvent();
                    break;
                case UP:
                case W:
                    event = new com.comp2042.event.RotateEvent();
                    break;
                case DOWN:
                case S:
                    event = new com.comp2042.event.DownEvent();
                    break;
                case SPACE:
                    event = new com.comp2042.event.HardDropEvent();
                    break;
            }

            if (event != null) {
                Object result = eventListener.onGameEvent(event);
                if (result instanceof com.comp2042.event.DownData) {
                    gameFlowController.handleDropResult((com.comp2042.event.DownData) result);
                } else if (result instanceof com.comp2042.event.ViewData) {
                    gameBoardRenderer.refreshBrick((com.comp2042.event.ViewData) result);
                }
            }
        }

        // Handle global keys regardless of game state
        switch (keyEvent.getCode()) {
            case N:
                eventListener.onGameEvent(new com.comp2042.event.NewGameEvent());
                break;
            case P:
                gameFlowController.pauseGame();
                break;
        }
    }
}

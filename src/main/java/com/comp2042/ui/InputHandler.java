package com.comp2042.ui;

import com.comp2042.event.GameEvent;
import com.comp2042.event.InputEventListener;
import com.comp2042.event.MoveEvent;
import javafx.scene.input.KeyEvent;
import com.comp2042.ui.GameFlowController;
import com.comp2042.ui.GameBoardRenderer;

/**
 * Handles user input from keyboard events and translates them into game actions.
 * This class interacts with {@link GameFlowController} and {@link InputEventListener}
 * to control the game state and update the game board.
 */
public class InputHandler {

    private InputEventListener eventListener;

    /**
     * Sets the event listener that will handle game input events.
     * @param eventListener The listener to be set.
     */
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }
    private final GameFlowController gameFlowController;
    private final GameBoardRenderer gameBoardRenderer;

    /**
     * Constructs an InputHandler with references to the game flow controller and game board renderer.
     *
     * @param gameFlowController The controller responsible for managing the game's overall flow.
     * @param gameBoardRenderer The renderer responsible for updating the visual game board.
     */
    public InputHandler(GameFlowController gameFlowController, GameBoardRenderer gameBoardRenderer) {
        this.gameFlowController = gameFlowController;
        this.gameBoardRenderer = gameBoardRenderer;
    }

    /**
     * Handles keyboard key press events.
     * Translates key presses into game movements (left, right, down, rotate, hard drop),
     * and game control actions (new game, pause).
     *
     * @param keyEvent The KeyEvent triggered by the user's key press.
     */
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
            case R:
                eventListener.onGameEvent(new com.comp2042.event.NewGameEvent());
                break;
            case P:
                gameFlowController.pauseGame();
                break;
        }
    }
}

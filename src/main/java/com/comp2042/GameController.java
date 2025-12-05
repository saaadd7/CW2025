package com.comp2042;

import com.comp2042.core.Board;
import com.comp2042.core.SimpleBoard;
import com.comp2042.event.*;
import com.comp2042.sounds.SoundManager;
import com.comp2042.ui.GameBoardRenderer;
import com.comp2042.ui.GuiController;
import javafx.stage.Stage;

/**
 * Acts as the main controller for the game logic, implementing {@link InputEventListener}.
 * It bridges the gap between user interface actions and the core game board mechanics.
 * This class manages brick movements, scoring, game state (e.g., game over), and sound effects.
 */
public class GameController implements InputEventListener {

    private final Board board;

    private final GuiController viewGuiController;
    private final GameBoardRenderer gameBoardRenderer;


    private final SoundManager soundManager;
    private final Main mainApp;

    /**
     * Constructs a GameController.
     *
     * @param c The {@link GuiController} for managing UI interactions.
     * @param gameBoardRenderer The {@link GameBoardRenderer} for drawing the game board.
     * @param soundManager The {@link SoundManager} for handling game sounds.
     * @param mainApp The main application class to switch between scenes.
     * @param boardWidth The width of the game board.
     * @param boardHeight The height of the game board.
     */
    public GameController(GuiController c, GameBoardRenderer gameBoardRenderer, SoundManager soundManager, Main mainApp, int boardWidth, int boardHeight) {
        this.board = new SimpleBoard(boardWidth, boardHeight);
        this.viewGuiController = c;
        this.gameBoardRenderer = gameBoardRenderer;
        this.soundManager = soundManager;
        this.mainApp = mainApp;

        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
    }

    /**
     * Handles a downward movement event for the current brick.
     * If the brick cannot move down further, it calls {@link #handleBrickLanded()} to process the landing.
     * @param event The {@link MoveEvent} indicating a downward movement.
     * @return {@link DownData} containing information about the movement and any cleared rows.
     */
    @Override
    public Object onGameEvent(GameEvent event) {
        switch (event.getType()) {
            case DOWN:
                boolean canMove = board.moveBrickDown();
                ClearRow clearRow = null;
                if (!canMove) {
                    clearRow = handleBrickLanded();
                }
                return new DownData(clearRow, board.getViewData());
            case LEFT:
                board.moveBrickLeft();
                return board.getViewData();
            case RIGHT:
                board.moveBrickRight();
                return board.getViewData();
            case ROTATE:
                board.rotateLeftBrick();
                return board.getViewData();
            case HARD_DROP:
                boolean canMoveDrop;
                do {
                    canMoveDrop = board.moveBrickDown();
                } while (canMoveDrop);
                ClearRow clearRowDrop = handleBrickLanded();
                return new DownData(clearRowDrop, board.getViewData());
            case NEW_GAME:
                board.newGame();
                viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
                gameBoardRenderer.refreshGameBackground(board.getBoardMatrix());
                return null;
            case BACK_TO_MENU:
                viewGuiController.gameOver();
                if (mainApp != null) {
                    try {
                        Stage currentStage = (Stage) viewGuiController.getViewRoot().getScene().getWindow();
                        mainApp.showMainMenu(currentStage);
                    } catch (Exception e) {
                        System.err.println("Failed to switch back to Main Menu.");
                        e.printStackTrace();
                    }
                }
                return null;
        }
<<<<<<< HEAD
        return null;
    }
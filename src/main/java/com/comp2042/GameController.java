package com.comp2042;

import com.comp2042.core.Board;
import com.comp2042.core.SimpleBoard;
import com.comp2042.event.*;
import com.comp2042.sounds.SoundManager;
import com.comp2042.ui.GameBoardRenderer;
import com.comp2042.ui.GameFlowController;
import com.comp2042.ui.GuiController;
import javafx.stage.Stage;

/**
 * The main controller for the game logic. It acts as a bridge between the user interface (GUI) and the game's core logic (Board).
 * It handles user input events, updates the game state, and manages the overall game flow.
 */
public class GameController implements InputEventListener {

    private final Board board;
    private final GuiController viewGuiController;
    private final GameBoardRenderer gameBoardRenderer;
    private final GameFlowController gameFlowController;
    private final SoundManager soundManager;
    private final Main mainApp;

    private boolean isProcessingNewGame = false;

    /**
     * Constructs a new GameController.
     *
     * @param c                  The GUI controller.
     * @param gameBoardRenderer  The renderer for the game board.
     * @param gameFlowController The controller for the game flow.
     * @param soundManager       The manager for sound effects.
     * @param mainApp            The main application class.
     * @param boardWidth         The width of the game board.
     * @param boardHeight        The height of the game board.
     */
    public GameController(GuiController c, GameBoardRenderer gameBoardRenderer, GameFlowController gameFlowController, SoundManager soundManager, Main mainApp, int boardWidth, int boardHeight) {

        this.board = new SimpleBoard(boardWidth, boardHeight);

        this.viewGuiController = c;
        this.gameBoardRenderer = gameBoardRenderer;
        this.gameFlowController = gameFlowController;
        this.soundManager = soundManager;
        this.mainApp = mainApp;


        viewGuiController.setEventListener(this);
    }

    /**
     * Initializes the game with a specific game mode.
     * This method sets up the game board, resets the timer, and updates the UI for the new game.
     *
     * @param mode The game mode to initialize (e.g., Classic, Sprint, Ultra).
     */
    public void initGame(GameMode mode) {

        board.setGameMode(mode);


        board.newGame();


        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
        viewGuiController.updateModeStatus(mode.toString(), board.getGameModeStatus());


        gameBoardRenderer.refreshGameBackground(board.getBoardMatrix());
    }

    /**
     * Handles incoming game events from the user interface.
     * This method is the central point for processing user actions like moving or rotating bricks.
     *
     * @param event The game event to process.
     * @return An object containing data for the UI to update, or null if no update is needed.
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
                viewGuiController.updateModeStatus(board.getGameMode().toString(), board.getGameModeStatus());

                if (board.isGameModeComplete()) {
                    handleVictory();
                    return null;
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
                if (isProcessingNewGame) return null;
                isProcessingNewGame = true;
                try {
                    board.newGame();
                    gameFlowController.newGame(board.getGameMode());
                    viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
                    gameBoardRenderer.refreshGameBackground(board.getBoardMatrix());
                } finally {
                    isProcessingNewGame = false;
                }
                return null;
            case BACK_TO_MENU:
                viewGuiController.gameOver();
                if (mainApp != null) {
                    try {
                        Stage currentStage = (Stage) viewGuiController.getViewRoot().getScene().getWindow();
                        mainApp.showMainMenu(currentStage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
        }
        return null;
    }

    /**
     * Handles the logic when a brick has landed.
     * This includes merging the brick to the board, checking for cleared lines, and checking for game over conditions.
     *
     * @return A ClearRow object containing information about cleared lines.
     */
    private ClearRow handleBrickLanded() {
        if (soundManager != null) soundManager.playThudSound();

        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();

        if (clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
            if (soundManager != null) soundManager.playSwooshSound();
        }


        if (board.isGameModeComplete()) {
            handleVictory();
            return clearRow;
        }


        if (board.createNewBrick()) {
            viewGuiController.gameOver();
        }

        gameBoardRenderer.refreshGameBackground(board.getBoardMatrix());
        return clearRow;
    }

    /**
     * Handles the victory condition.
     * This stops the game and displays a victory message.
     */
    private void handleVictory() {

        viewGuiController.gameOver();



        System.out.println("VICTORY! Mode: " + board.getGameMode());


        // soundManager.playWinSound();
    }
}
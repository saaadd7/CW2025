package com.comp2042;

import com.comp2042.core.Board;
import com.comp2042.core.SimpleBoard;
import com.comp2042.event.*;
import com.comp2042.sounds.SoundManager;
import com.comp2042.ui.GameBoardRenderer;
import com.comp2042.ui.GameFlowController;
import com.comp2042.ui.GuiController;
import javafx.stage.Stage;

public class GameController implements InputEventListener {

    private final Board board;
    private final GuiController viewGuiController;
    private final GameBoardRenderer gameBoardRenderer;
    private final GameFlowController gameFlowController;
    private final SoundManager soundManager;
    private final Main mainApp;

    private boolean isProcessingNewGame = false;

    public GameController(GuiController c, GameBoardRenderer gameBoardRenderer, GameFlowController gameFlowController, SoundManager soundManager, Main mainApp, int boardWidth, int boardHeight) {
        // 1. We create the board here, default to Classic for now
        this.board = new SimpleBoard(boardWidth, boardHeight);

        this.viewGuiController = c;
        this.gameBoardRenderer = gameBoardRenderer;
        this.gameFlowController = gameFlowController;
        this.soundManager = soundManager;
        this.mainApp = mainApp;

        // NOTE: We do NOT start the game here anymore.
        // We wait for initGame() to be called by the GameModeController.
        viewGuiController.setEventListener(this);
    }

    /**
     * NEW METHOD: Initializes the game with the specific mode (Classic/Sprint/Ultra)
     */
    public void initGame(GameMode mode) {
        // 1. Set the mode
        board.setGameMode(mode);

        // 2. Reset board and timer
        board.newGame();

        // 3. Update UI
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
        viewGuiController.updateModeStatus(mode.toString(), board.getGameModeStatus());

        // 4. Force a refresh so the board isn't empty
        gameBoardRenderer.refreshGameBackground(board.getBoardMatrix());
    }

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
                // CHECK FOR VICTORY (Needed for ULTRA mode - Time limit)
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
                viewGuiController.gameOver(); // Stop the timer
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

    private ClearRow handleBrickLanded() {
        if (soundManager != null) soundManager.playThudSound();

        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();

        if (clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
            if (soundManager != null) soundManager.playSwooshSound();
        }

        // CHECK FOR VICTORY (Needed for SPRINT mode - Line count)
        if (board.isGameModeComplete()) {
            handleVictory();
            return clearRow;
        }

        // Check for Loss (Blockout)
        if (board.createNewBrick()) {
            viewGuiController.gameOver();
        }

        gameBoardRenderer.refreshGameBackground(board.getBoardMatrix());
        return clearRow;
    }

    /**
     * NEW METHOD: Handles what happens when the player wins
     */
    private void handleVictory() {
        // Stop the game loop
        viewGuiController.gameOver();

        // Show a victory message
        // You might want to create a specific viewGuiController.showVictory() later
        System.out.println("VICTORY! Mode: " + board.getGameMode());

        // Ideally, play a win sound here
        // soundManager.playWinSound();
    }
}
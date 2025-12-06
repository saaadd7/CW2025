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

    // Add a flag to prevent recursive calls
    private boolean isProcessingNewGame = false;

    public GameController(GuiController c, GameBoardRenderer gameBoardRenderer, GameFlowController gameFlowController, SoundManager soundManager, Main mainApp, int boardWidth, int boardHeight) {
        this.board = new SimpleBoard(boardWidth, boardHeight);
        this.viewGuiController = c;
        this.gameBoardRenderer = gameBoardRenderer;
        this.gameFlowController = gameFlowController;
        this.soundManager = soundManager;
        this.mainApp = mainApp;

        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
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
                // FIX: Prevent recursive calls
                if (isProcessingNewGame) {
                    return null;
                }

                isProcessingNewGame = true;
                try {
                    // 1. Reset the board state
                    board.newGame();

                    // 2. Update UI (this should NOT fire another NewGameEvent)
                    gameFlowController.newGame();

                    // 3. Refresh the view
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
                        System.err.println("Failed to switch back to Main Menu.");
                        e.printStackTrace();
                    }
                }
                return null;
        }
        return null;
    }

    private ClearRow handleBrickLanded() {
        if (soundManager != null) {
            soundManager.playThudSound();
        }

        board.mergeBrickToBackground();

        ClearRow clearRow = board.clearRows();
        if (clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());

            if (soundManager != null) {
                soundManager.playSwooshSound();
            }
        }

        if (board.createNewBrick()) {
            viewGuiController.gameOver();
        }

        gameBoardRenderer.refreshGameBackground(board.getBoardMatrix());
        return clearRow;
    }
}
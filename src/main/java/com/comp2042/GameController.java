package com.comp2042;

import com.comp2042.core.Board;
import com.comp2042.core.SimpleBoard;
import com.comp2042.event.*;
import com.comp2042.sounds.SoundManager;
import com.comp2042.ui.GameBoardRenderer;
import com.comp2042.ui.GuiController;
import javafx.stage.Stage;

public class GameController implements InputEventListener {

    private final Board board;

    private final GuiController viewGuiController;
    private final GameBoardRenderer gameBoardRenderer;


    private final SoundManager soundManager;
    private final Main mainApp;

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

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        if (!canMove) {
            clearRow = handleBrickLanded();
        } else {
            // This makes scoring dependent only on line clears (classic behavior).
        }

        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }


    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        gameBoardRenderer.refreshGameBackground(board.getBoardMatrix());
    }

    @Override
    public DownData onHardDropEvent(MoveEvent event) {
        int dropDistance = 0;
        boolean canMove;

        // Move the current brick down until it can't move anymore
        do {
            canMove = board.moveBrickDown();
            if (canMove) {
                dropDistance++;
            }
        } while (canMove);

        ClearRow clearRow = handleBrickLanded();

        return new DownData(clearRow, board.getViewData());
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


    @Override
    public void onBackToMenuEvent() {
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
    }
}
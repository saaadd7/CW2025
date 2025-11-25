package com.comp2042;

import com.comp2042.sounds.SoundManager;
import javafx.stage.Stage;

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(25, 10);

    private final GuiController viewGuiController;


    // 1. Add a final field for the SoundManager
    private final SoundManager soundManager;
    private final Main mainApp;

    // 2. Implement the two-argument constructor
    public GameController(GuiController c, SoundManager soundManager, Main mainApp) {
        this.viewGuiController = c;
        this.soundManager = soundManager;
        this.mainApp = mainApp; // Store the Main app reference!

        board.createNewBrick();
        viewGuiController.setEventListener(this);
        // CRITICAL FIX: The initial call to initGameView should happen here,
        // not inside the constructor, but we'll leave it as is for now
        // since setEventListener is being used to trigger the start.
        // We will focus the fix in createNewGame and setEventListener.
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        if (!canMove) {

            // 1. ADD THUD SOUND HERE (Piece Locked)
            if (soundManager != null) {
                soundManager.playThudSound();
            }

            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());

                // 2. ADD SWOOSH SOUND HERE (Line Cleared)
                if (soundManager != null) {
                    soundManager.playSwooshSound();
                }
            }
            if (board.createNewBrick()) {
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());

        } else {
            //  DO NOT award +1 for user soft drops anymore.
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

        // CRITICAL FIX: The initial view setup (initGameView) MUST be called
        // to create the GameBoardView/displayMatrix before we attempt to refresh it.
        // Since initGameView is designed to handle the initial piece spawn,
        // we call it here to ensure the UI is drawn before the background is refreshed.
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());

        // Refreshing the background is now optional here, as initGameView should do it,
        // but we keep it for redundancy, now that the view is initialized.
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }

    @Override
    public DownData onHardDropEvent(MoveEvent event) {
        int dropDistance = 0;
        boolean canMove;
        ClearRow clearRow = null;

        // Move the current brick down until it can't move anymore
        do {
            canMove = board.moveBrickDown();
            if (canMove) {
                dropDistance++;
            }
        } while (canMove);

        // 🔊 1. Play THUD sound when the brick locks
        if (soundManager != null) {
            soundManager.playThudSound();
        }

        // Brick has locked – merge into background
        board.mergeBrickToBackground();

        // Clear any full rows
        clearRow = board.clearRows();
        if (clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());

            // 🔊 2. Play SWOOSH sound if lines were cleared
            if (soundManager != null) {
                soundManager.playSwooshSound();
            }
        }

        // Hard-drop bonus: I didnt add it because it was not in the original game
        if (dropDistance > 0) {
            ;
        }

        // Spawn a new brick or end game
        if (board.createNewBrick()) {
            viewGuiController.gameOver();
        }

        // Redraw background with the locked piece
        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        return new DownData(clearRow, board.getViewData());
    }

    // GameController.java (New method implementation)

    @Override
    public void onBackToMenuEvent() {
        // 1. CRITICAL: Stop the game loop/timeline
        // The game loop (Timeline) is managed by the GuiController/FlowManager.
        viewGuiController.gameOver(); // Stopping the game loop is part of the gameOver method.

        if (mainApp != null) {
            try {
                // 2. Access the current Stage/Window
                Stage currentStage = (Stage) viewGuiController.getViewRoot().getScene().getWindow();

                // 3. Switch the scene back to the main menu
                mainApp.showMainMenu(currentStage);
            } catch (Exception e) {
                System.err.println("Failed to switch back to Main Menu.");
                e.printStackTrace();
            }
        }
    }


}
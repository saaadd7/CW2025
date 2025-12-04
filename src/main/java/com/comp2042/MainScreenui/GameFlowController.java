package com.comp2042.ui;

import com.comp2042.DownData;
import com.comp2042.EventType;
import com.comp2042.EventSource;
import com.comp2042.InputEventListener;
import com.comp2042.MoveEvent;
import com.comp2042.NotificationPanel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.ParallelTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class GameFlowController {

    private Timeline timeLine;
    private boolean gameStarted = false;
    private final BooleanProperty isPause = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    private InputEventListener eventListener;
    private ParticleEffect particleEffect; // NEW: Particle effect system

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void setParticleEffect(ParticleEffect particleEffect) {
        this.particleEffect = particleEffect;
    }
    private final GameBoardRenderer gameBoardRenderer;
    private final GameInfoPanelController gameInfoPanelController;
    private final StackPane groupNotification;
    private final Button pauseButton;
    private final com.comp2042.GameOverPanel gameOverPanel;

    private int level = 1;
    private int totalLinesCleared = 0;
    private static final int LINES_PER_LEVEL = 5;

    public GameFlowController(GameBoardRenderer gameBoardRenderer,
                              GameInfoPanelController gameInfoPanelController, StackPane groupNotification,
                              Button pauseButton, com.comp2042.GameOverPanel gameOverPanel) {
        this.gameBoardRenderer = gameBoardRenderer;
        this.gameInfoPanelController = gameInfoPanelController;
        this.groupNotification = groupNotification;
        this.pauseButton = pauseButton;
        this.gameOverPanel = gameOverPanel;
    }

    public void start() {
        System.out.println("GameFlowController.start() called");

        // Stop any existing timeline first
        if (timeLine != null) {
            timeLine.stop();
        }

        // CRITICAL: Set pause to false and game over to false
        isPause.set(false);
        isGameOver.set(false);

        System.out.println("After setting: isPause = " + isPause.get() + ", isGameOver = " + isGameOver.get());

        // Update button text - DON'T trigger events
        pauseButton.setText("Pause");

        // Start the game timeline
        updateGameSpeed();

        // CRITICAL FIX: Add a small delay before marking game as started
        // This prevents any spurious button events during initialization
        javafx.application.Platform.runLater(() -> {
            gameStarted = true;
            System.out.println("GameFlowController.start() completed - game now active");
        });
    }

    private void moveDown(MoveEvent event) {
        if (!isPause.getValue() && !isGameOver.getValue()) {
            if(eventListener != null) {
                handleDropResult(eventListener.onDownEvent(event));
            }
        }
    }

    public void handleDropResult(DownData data) {
        if (data.getClearRow() != null && data.getClearRow().getLinesRemoved() > 0) {
            int linesRemoved = data.getClearRow().getLinesRemoved();
            totalLinesCleared += linesRemoved;

            // Show score notification for each line cleared
            NotificationPanel scoreNotification = new NotificationPanel("+" + data.getClearRow().getScoreBonus());
            groupNotification.getChildren().add(scoreNotification);
            scoreNotification.showScore(groupNotification.getChildren());

            // Trigger particle effect!
            if (particleEffect != null && data.getClearRow().getClearedRows() != null) {
                particleEffect.createLineClearExplosion(
                        data.getClearRow().getClearedRows(),
                        linesRemoved
                );
            }

            // Check if level should increase
            int newLevel = (totalLinesCleared / LINES_PER_LEVEL) + 1;
            if (newLevel > level) {
                level = newLevel;
                gameInfoPanelController.setLevel(level);
                updateGameSpeed();

                // Show level up notification with a slight delay
                Timeline levelUpDelay = new Timeline(new KeyFrame(Duration.millis(500), e -> {
                    NotificationPanel levelUpNotification = new NotificationPanel("LEVEL " + level + "!");
                    groupNotification.getChildren().add(levelUpNotification);
                    levelUpNotification.showScore(groupNotification.getChildren());
                }));
                levelUpDelay.play();
            }
        }

        gameBoardRenderer.refreshBrick(data.getViewData());
        gameInfoPanelController.updatePreviews(data.getViewData());
    }

    public void gameOver() {
        if (timeLine != null) {
            timeLine.stop();
        }

        // Show dramatic Game Over notification
        NotificationPanel gameOverNotification = new NotificationPanel("GAME OVER");
        groupNotification.getChildren().add(gameOverNotification);

        // Dramatic animation: scale up, fade in, and pulse effect
        gameOverNotification.setOpacity(0);
        gameOverNotification.setScaleX(0.5);
        gameOverNotification.setScaleY(0.5);

        // Fade and scale animation
        FadeTransition ft = new FadeTransition(Duration.millis(600), gameOverNotification);
        ft.setFromValue(0);
        ft.setToValue(1);

        // Scale up animation
        Timeline scaleTimeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new javafx.animation.KeyValue(gameOverNotification.scaleXProperty(), 0.5),
                        new javafx.animation.KeyValue(gameOverNotification.scaleYProperty(), 0.5)
                ),
                new KeyFrame(Duration.millis(600),
                        new javafx.animation.KeyValue(gameOverNotification.scaleXProperty(), 1.1),
                        new javafx.animation.KeyValue(gameOverNotification.scaleYProperty(), 1.1)
                ),
                new KeyFrame(Duration.millis(800),
                        new javafx.animation.KeyValue(gameOverNotification.scaleXProperty(), 1.0),
                        new javafx.animation.KeyValue(gameOverNotification.scaleYProperty(), 1.0)
                )
        );

        // Combine animations
        ParallelTransition entrance = new ParallelTransition(ft);
        entrance.play();
        scaleTimeline.play();

        // Add a subtle pulse effect that repeats
        Timeline pulse = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new javafx.animation.KeyValue(gameOverNotification.scaleXProperty(), 1.0),
                        new javafx.animation.KeyValue(gameOverNotification.scaleYProperty(), 1.0)
                ),
                new KeyFrame(Duration.millis(1000),
                        new javafx.animation.KeyValue(gameOverNotification.scaleXProperty(), 1.05),
                        new javafx.animation.KeyValue(gameOverNotification.scaleYProperty(), 1.05)
                ),
                new KeyFrame(Duration.millis(2000),
                        new javafx.animation.KeyValue(gameOverNotification.scaleXProperty(), 1.0),
                        new javafx.animation.KeyValue(gameOverNotification.scaleYProperty(), 1.0)
                )
        );
        pulse.setCycleCount(Timeline.INDEFINITE);
        pulse.setDelay(Duration.millis(800));
        pulse.play();

        isGameOver.setValue(true);
    }

    public void newGame() {
        System.out.println("GameFlowController.newGame() called");

        // Stop existing timeline
        if (timeLine != null) {
            timeLine.stop();
        }

        // Clear any existing notifications
        groupNotification.getChildren().clear();

        gameOverPanel.setVisible(false);
        eventListener.createNewGame();

        // Reset state explicitly
        isPause.set(false);
        isGameOver.set(false);
        pauseButton.setText("Pause");

        // Mark as started
        gameStarted = true;

        System.out.println("After newGame: isPause = " + isPause.get() + ", isGameOver = " + isGameOver.get());

        level = 1;
        totalLinesCleared = 0;
        gameInfoPanelController.setLevel(1);

        // Create and start new timeline
        updateGameSpeed();
    }

    public void pauseGame() {
        System.out.println("pauseGame() called - gameStarted: " + gameStarted + ", isPause before: " + isPause.get());

        // CRITICAL FIX: Don't allow pause until game has actually started
        if (!gameStarted || isGameOver.getValue() || timeLine == null) {
            System.out.println("pauseGame() blocked - game not ready");
            return;
        }

        if (isPause.get()) {
            // Currently paused, so resume
            timeLine.play();
            pauseButton.setText("Pause");
            isPause.set(false);
        } else {
            // Currently playing, so pause
            timeLine.pause();
            pauseButton.setText("Resume");
            isPause.set(true);
        }

        System.out.println("pauseGame() called - isPause after: " + isPause.get());
    }

    private void updateGameSpeed() {
        // Always stop old timeline first
        if (timeLine != null) {
            timeLine.stop();
        }

        // Create new timeline
        timeLine = new Timeline(new KeyFrame(Duration.millis(getDropSpeedForLevel()),
                e -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))));
        timeLine.setCycleCount(Timeline.INDEFINITE);

        // Always play - don't check pause state here
        timeLine.play();

        System.out.println("Timeline created and started");
    }

    private int getDropSpeedForLevel() {
        int baseSpeed = 400;
        int speedDecrease = 40;
        int minSpeed = 100;

        return Math.max(minSpeed, baseSpeed - (level - 1) * speedDecrease);
    }

    public boolean isPaused() {
        boolean paused = isPause.get();
        System.out.println("isPaused() called - returning: " + paused);
        return paused;
    }

    public boolean isGameOver() {
        return isGameOver.get();
    }
}
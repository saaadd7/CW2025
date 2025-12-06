package com.comp2042.ui;

import com.comp2042.event.DownData;
import com.comp2042.event.InputEventListener;
import com.comp2042.ui.GameOverPanel;
import com.comp2042.ui.NotificationPanel;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class GameFlowController {

    private Timeline timeLine;
    private boolean gameStarted = false;
    private final BooleanProperty isPause = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    private InputEventListener eventListener;
    private ParticleEffect particleEffect;

    private final GameBoardRenderer gameBoardRenderer;
    private final GameInfoPanelController gameInfoPanelController;
    private final StackPane groupNotification;
    private final Button pauseButton;
    private final GameOverPanel gameOverPanel;

    private int level = 1;
    private int totalLinesCleared = 0;
    private static final int LINES_PER_LEVEL = 5;

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void setParticleEffect(ParticleEffect particleEffect) {
        this.particleEffect = particleEffect;
    }

    public GameFlowController(GameBoardRenderer gameBoardRenderer,
                              GameInfoPanelController gameInfoPanelController, StackPane groupNotification,
                              Button pauseButton, GameOverPanel gameOverPanel) {
        this.gameBoardRenderer = gameBoardRenderer;
        this.gameInfoPanelController = gameInfoPanelController;
        this.groupNotification = groupNotification;
        this.pauseButton = pauseButton;
        this.gameOverPanel = gameOverPanel;
    }

    /**
     * Helper method to clean up the UI (hide Game Over text, etc)
     */
    public void resetUI() {
        // 1. Clear the "GAME OVER" text added to the notification group
        groupNotification.getChildren().clear();

        // 2. Hide the static Game Over panel (if used)
        if (gameOverPanel != null) {
            gameOverPanel.setVisible(false);
        }

        // 3. Reset Pause button text
        pauseButton.setText("Pause");

        // 4. Reset Flags
        isPause.set(false);
        isGameOver.set(false);
    }

    public void start() {
        if (timeLine != null) {
            timeLine.stop();
        }

        // Call resetUI to remove the badge
        resetUI();

        updateGameSpeed();

        javafx.application.Platform.runLater(() -> {
            gameStarted = true;
        });
    }

    public void newGame() {
        // 1. Stop the old timeline so the game loop doesn't overlap
        if (timeLine != null) {
            timeLine.stop();
        }

        // 2. Clean up UI (Game Over text, etc.)
        resetUI();

        // 3. REMOVED: Don't fire NewGameEvent here - this was causing the infinite loop!
        // The GameController already handles board initialization when it receives
        // the NEW_GAME event that triggered this method.

        // Reset level and line counters
        level = 1;
        totalLinesCleared = 0;

        // 4. Update the View to show Level 1
        gameInfoPanelController.setLevel(1);

        // 5. Start the game logic
        gameStarted = true;
        updateGameSpeed(); // This will now correctly calculate speed for Level 1
    }

    private void moveDown() {
        if (!isPause.getValue() && !isGameOver.getValue()) {
            if(eventListener != null) {
                Object result = eventListener.onGameEvent(new com.comp2042.event.DownEvent());
                if (result instanceof DownData) {
                    handleDropResult((DownData) result);
                }
            }
        }
    }

    public void handleDropResult(DownData data) {
        if (data.getClearRow() != null && data.getClearRow().getLinesRemoved() > 0) {
            int linesRemoved = data.getClearRow().getLinesRemoved();
            totalLinesCleared += linesRemoved;

            NotificationPanel scoreNotification = new NotificationPanel("+" + data.getClearRow().getScoreBonus());
            groupNotification.getChildren().add(scoreNotification);
            scoreNotification.showScore(groupNotification.getChildren());

            if (particleEffect != null && data.getClearRow().getClearedRows() != null) {
                particleEffect.createLineClearExplosion(
                        data.getClearRow().getClearedRows(),
                        linesRemoved
                );
            }

            int newLevel = (totalLinesCleared / LINES_PER_LEVEL) + 1;
            if (newLevel > level) {
                level = newLevel;
                gameInfoPanelController.setLevel(level);
                updateGameSpeed();

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

        NotificationPanel gameOverNotification = new NotificationPanel("GAME OVER");
        groupNotification.getChildren().add(gameOverNotification);

        gameOverNotification.setOpacity(0);
        gameOverNotification.setScaleX(0.5);
        gameOverNotification.setScaleY(0.5);

        FadeTransition ft = new FadeTransition(Duration.millis(600), gameOverNotification);
        ft.setFromValue(0);
        ft.setToValue(1);

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

        ParallelTransition entrance = new ParallelTransition(ft);
        entrance.play();
        scaleTimeline.play();

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

    public void pauseGame() {
        if (!gameStarted || isGameOver.getValue() || timeLine == null) {
            return;
        }

        if (isPause.get()) {
            timeLine.play();
            pauseButton.setText("PAUSE");
            isPause.set(false);
        } else {
            timeLine.pause();
            pauseButton.setText("RESUME");
            isPause.set(true);
        }
    }

    private void updateGameSpeed() {
        if (timeLine != null) {
            timeLine.stop();
        }

        timeLine = new Timeline(new KeyFrame(Duration.millis(getDropSpeedForLevel()),
                e -> moveDown()));
        timeLine.setCycleCount(Timeline.INDEFINITE);

        timeLine.play();
    }

    private int getDropSpeedForLevel() {
        int baseSpeed = 400;
        int speedDecrease = 40;
        int minSpeed = 100;

        return Math.max(minSpeed, baseSpeed - (level - 1) * speedDecrease);
    }

    public boolean isPaused() {
        return isPause.get();
    }

    public boolean isGameOver() {
        return isGameOver.get();
    }
}
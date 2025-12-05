package com.comp2042.ui;

import com.comp2042.event.DownData;
import com.comp2042.event.GameEvent;
import com.comp2042.event.InputEventListener;
import com.comp2042.event.MoveEvent;
import com.comp2042.ui.GameOverPanel;
import com.comp2042.ui.NotificationPanel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;

import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * Controls the flow and state of the game, including starting/stopping, pausing,
 * handling game over conditions, and managing game speed based on level.
 * It orchestrates interactions between the game board, info panel, and event listeners.
 */
public class GameFlowController {

    private Timeline timeLine;
    private boolean gameStarted = false;
    private final BooleanProperty isPause = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    private InputEventListener eventListener;
    private ParticleEffect particleEffect; // NEW: Particle effect system

    /**
     * Sets the event listener for game input and actions.
     * @param eventListener The listener to be set.
     */
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    /**
     * Sets the particle effect system to be used for visual effects.
     * @param particleEffect The ParticleEffect instance.
     */
    public void setParticleEffect(ParticleEffect particleEffect) {
        this.particleEffect = particleEffect;
    }
    private final GameBoardRenderer gameBoardRenderer;
    private final GameInfoPanelController gameInfoPanelController;
    private final StackPane groupNotification;
    private final Button pauseButton;
    private final GameOverPanel gameOverPanel;

    private int level = 1;
    private int totalLinesCleared = 0;
    private static final int LINES_PER_LEVEL = 5;

    /**
     * Constructs a GameFlowController.
     *
     * @param gameBoardRenderer The renderer for the game board.
     * @param gameInfoPanelController The controller for the game information panel.
     * @param groupNotification The StackPane for displaying notifications.
     * @param pauseButton The button used to pause/resume the game.
     * @param gameOverPanel The panel displayed when the game is over.
     */
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
     * Starts the game. Initializes game state, resets pause/game over flags,
     * updates game speed, and sets gameStarted to true.
     */
    public void start() {
        if (timeLine != null) {
            timeLine.stop();
        }

        isPause.set(false);
        isGameOver.set(false);

        pauseButton.setText("Pause");

        updateGameSpeed();

        javafx.application.Platform.runLater(() -> {
            gameStarted = true;
        });
    }

    /**
     * Moves the current brick down. This method is typically called by the game's timeline
     * and respects the game's pause and game over states. It triggers a DownEvent internally.
     */
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

    /**
     * Handles the result of a brick dropping, including clearing lines,
     * updating score, triggering particle effects, and checking for level-ups.
     *
     * @param data The {@link DownData} containing information about the drop result.
     */
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

    /**
     * Ends the current game session. Stops the game timer, displays a "GAME OVER" notification,
     * and sets the game over flag.
     */
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

    /**
     * Starts a new game. Stops any active game, clears notifications, hides game over panel,
     * and resets game state and level.
     */
    public void newGame() {
        if (timeLine != null) {
            timeLine.stop();
        }

        groupNotification.getChildren().clear();

        gameOverPanel.setVisible(false);
        if (eventListener != null) {
            eventListener.onGameEvent(new com.comp2042.event.NewGameEvent());
        }

        isPause.set(false);
        isGameOver.set(false);
        pauseButton.setText("Pause");

        gameStarted = true;

        level = 1;
        totalLinesCleared = 0;
        gameInfoPanelController.setLevel(1);

        updateGameSpeed();
    }

    /**
     * Pauses or resumes the game. Toggles the `isPause` state and updates the pause button text.
     */
    public void pauseGame() {
        if (!gameStarted || isGameOver.getValue() || timeLine == null) {
            return;
        }

        if (isPause.get()) {
            timeLine.play();
            pauseButton.setText("Pause");
            isPause.set(false);
        } else {
            timeLine.pause();
            pauseButton.setText("Resume");
            isPause.set(true);
        }
    }

    /**
     * Updates the game speed based on the current level.
     * Stops the existing game timer and creates a new one with the adjusted speed.
     */
    private void updateGameSpeed() {
        if (timeLine != null) {
            timeLine.stop();
        }

        timeLine = new Timeline(new KeyFrame(Duration.millis(getDropSpeedForLevel()),
                e -> moveDown()));
        timeLine.setCycleCount(Timeline.INDEFINITE);

        timeLine.play();
    }

    /**
     * Calculates the drop speed of bricks based on the current game level.
     * The speed increases with higher levels.
     *
     * @return The drop speed in milliseconds.
     */
    private int getDropSpeedForLevel() {
        int baseSpeed = 400;
        int speedDecrease = 40;
        int minSpeed = 100;

        return Math.max(minSpeed, baseSpeed - (level - 1) * speedDecrease);
    }

    /**
     * Checks if the game is currently paused.
     *
     * @return true if the game is paused, false otherwise.
     */
    public boolean isPaused() {
        boolean paused = isPause.get();
        return paused;
    }

    /**
     * Checks if the game is currently over.
     *
     * @return true if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return isGameOver.get();
    }
}
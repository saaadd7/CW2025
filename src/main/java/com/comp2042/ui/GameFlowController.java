package com.comp2042.ui;

import com.comp2042.event.DownData;
import com.comp2042.event.EventType;
import com.comp2042.event.EventSource;
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
    private final GameOverPanel gameOverPanel;

    private int level = 1;
    private int totalLinesCleared = 0;
    private static final int LINES_PER_LEVEL = 5;

    public GameFlowController(GameBoardRenderer gameBoardRenderer,
                              GameInfoPanelController gameInfoPanelController, StackPane groupNotification,
                              Button pauseButton, GameOverPanel gameOverPanel) {
        this.gameBoardRenderer = gameBoardRenderer;
        this.gameInfoPanelController = gameInfoPanelController;
        this.groupNotification = groupNotification;
        this.pauseButton = pauseButton;
        this.gameOverPanel = gameOverPanel;
    }

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

    public void newGame() {
        if (timeLine != null) {
            timeLine.stop();
        }

        groupNotification.getChildren().clear();

        gameOverPanel.setVisible(false);
        eventListener.createNewGame();

        isPause.set(false);
        isGameOver.set(false);
        pauseButton.setText("Pause");

        gameStarted = true;

        level = 1;
        totalLinesCleared = 0;
        gameInfoPanelController.setLevel(1);

        updateGameSpeed();
    }

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

    private void updateGameSpeed() {
        if (timeLine != null) {
            timeLine.stop();
        }

        timeLine = new Timeline(new KeyFrame(Duration.millis(getDropSpeedForLevel()),
                e -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))));
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
        boolean paused = isPause.get();
        return paused;
    }

    public boolean isGameOver() {
        return isGameOver.get();
    }
}
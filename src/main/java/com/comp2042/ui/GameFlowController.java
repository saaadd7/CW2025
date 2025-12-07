package com.comp2042.ui;

import com.comp2042.event.DownData;
import com.comp2042.event.InputEventListener;
import com.comp2042.GameMode; // Ensure this import exists!
import javafx.animation.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * Manages the game flow, including the main game loop, timers, scoring, and game state transitions.
 */
public class GameFlowController {

    // --- Core Game Variables ---
    private Timeline gameLoop;
    private Timeline ultraTimer;
    private boolean gameStarted = false;
    private final BooleanProperty isPause = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    // --- Mode Variables ---
    private GameMode currentMode = GameMode.CLASSIC;
    private int linesToClearGoal = 0;
    private int secondsRemaining = 120;

    // --- Dependencies ---
    private InputEventListener eventListener;
    private ParticleEffect particleEffect;
    private final GameBoardRenderer gameBoardRenderer;
    private final GameInfoPanelController gameInfoPanelController;
    private final StackPane groupNotification;
    private final Button pauseButton;
    private final GameOverPanel gameOverPanel;

    private int level = 1;
    private int totalLinesCleared = 0;
    private static final int LINES_PER_LEVEL = 7;

    /**
     * Constructs a new GameFlowController.
     *
     * @param gameBoardRenderer       The renderer for the game board.
     * @param gameInfoPanelController The controller for the game info panel.
     * @param groupNotification       The container for notifications.
     * @param pauseButton             The pause button.
     * @param gameOverPanel           The game over panel.
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
     * Sets the event listener for game events.
     *
     * @param eventListener The event listener.
     */
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    /**
     * Sets the particle effect for line clearing.
     *
     * @param particleEffect The particle effect.
     */
    public void setParticleEffect(ParticleEffect particleEffect) {
        this.particleEffect = particleEffect;
    }

    /**
     * Starts a new game with the specific mode rules.
     *
     * @param mode The game mode to start.
     */
    public void newGame(GameMode mode) {
        this.currentMode = mode;

        if (gameLoop != null) gameLoop.stop();
        if (ultraTimer != null) ultraTimer.stop();

        resetUI();
        level = 1;
        totalLinesCleared = 0;
        gameInfoPanelController.setLevel(1);
        gameStarted = true;

        // --- Mode Logic ---
        if (currentMode == GameMode.ULTRA) {
            startUltraTimer(120); // 2 Minutes
        } else if (currentMode == GameMode.SPRINT) {
            linesToClearGoal = 30;
        }

        updateGameSpeed();
    }

    /**
     * Default start method, defaults to Classic mode.
     */
    public void start() {
        newGame(GameMode.CLASSIC);
    }

    private void startUltraTimer(int seconds) {
        this.secondsRemaining = seconds;
        gameInfoPanelController.updateTime(secondsRemaining);
        ultraTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (!isPause.get() && !isGameOver.get()) {
                secondsRemaining--;
                gameInfoPanelController.updateTime(secondsRemaining);
                if (secondsRemaining <= 0) {
                    gameOver("VICTORY!");
                }
            }
        }));
        ultraTimer.setCycleCount(Timeline.INDEFINITE);
        ultraTimer.play();
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

    /**
     * Handles the result of a drop action.
     *
     * @param data The data from the drop action.
     */
    public void handleDropResult(DownData data) {
        if (data.getClearRow() != null && data.getClearRow().getLinesRemoved() > 0) {
            int linesRemoved = data.getClearRow().getLinesRemoved();
            totalLinesCleared += linesRemoved;

            NotificationPanel scoreNotification = new NotificationPanel("+" + data.getClearRow().getScoreBonus());
            groupNotification.getChildren().add(scoreNotification);
            scoreNotification.showScore(groupNotification.getChildren());

            if (particleEffect != null && data.getClearRow().getClearedRows() != null) {
                particleEffect.createLineClearExplosion(data.getClearRow().getClearedRows(), linesRemoved);
            }

            // --- Sprint Win Condition ---
            if (currentMode == GameMode.SPRINT) {
                if ((linesToClearGoal - totalLinesCleared) <= 0) {
                    gameOver("VICTORY!");
                    return;
                }
            }

            // --- Classic Leveling ---
            if (currentMode != GameMode.ULTRA) {
                int newLevel = (totalLinesCleared / LINES_PER_LEVEL) + 1;
                if (newLevel > level) {
                    level = newLevel;
                    gameInfoPanelController.setLevel(level);
                    updateGameSpeed();
                    showLevelUpNotification();
                }
            }
        }
        gameBoardRenderer.refreshBrick(data.getViewData());
        gameInfoPanelController.updatePreviews(data.getViewData());
    }

    private void showLevelUpNotification() {
        Timeline levelUpDelay = new Timeline(new KeyFrame(Duration.millis(500), e -> {
            NotificationPanel levelUpNotification = new NotificationPanel("LEVEL " + level + "!");
            groupNotification.getChildren().add(levelUpNotification);
            levelUpNotification.showScore(groupNotification.getChildren());
        }));
        levelUpDelay.play();
    }

    /**
     * Ends the game with a specific message.
     *
     * @param message The message to display.
     */
    public void gameOver(String message) {
        if (gameLoop != null) gameLoop.stop();
        if (ultraTimer != null) ultraTimer.stop();

        NotificationPanel gameOverNotification = new NotificationPanel(message);
        groupNotification.getChildren().add(gameOverNotification);
        animateGameOver(gameOverNotification);
        isGameOver.setValue(true);
    }

    /**
     * Ends the game with the default "GAME OVER" message.
     */
    public void gameOver() {
        gameOver("GAME OVER");
    }

    private void animateGameOver(NotificationPanel panel) {

        panel.setOpacity(0);
        panel.setScaleX(0.0);
        panel.setScaleY(0.0);



        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), panel);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);


        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(500), panel);
        scaleUp.setFromX(0.0);
        scaleUp.setFromY(0.0);
        scaleUp.setToX(1.0);
        scaleUp.setToY(1.0);

        scaleUp.setInterpolator(Interpolator.EASE_OUT);


        ParallelTransition entrance = new ParallelTransition(fadeIn, scaleUp);


        ScaleTransition pulse = new ScaleTransition(Duration.millis(900), panel);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.15);
        pulse.setToY(1.15);
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.setAutoReverse(true);


        entrance.setOnFinished(e -> pulse.play());
        entrance.play();
    }

    /**
     * Pauses or resumes the game.
     */
    public void pauseGame() {
        if (!gameStarted || isGameOver.getValue() || gameLoop == null) return;

        if (isPause.get()) {
            gameLoop.play();
            if(ultraTimer != null && currentMode == GameMode.ULTRA) ultraTimer.play();
            pauseButton.setText("PAUSE");
            isPause.set(false);
        } else {
            gameLoop.pause();
            if(ultraTimer != null) ultraTimer.pause();
            pauseButton.setText("RESUME");
            isPause.set(true);
        }
    }

    private void updateGameSpeed() {
        if (gameLoop != null) gameLoop.stop();
        gameLoop = new Timeline(new KeyFrame(Duration.millis(getDropSpeedForLevel()), e -> moveDown()));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    private int getDropSpeedForLevel() {
        int baseSpeed = 400;
        int speedDecrease = 40;
        int minSpeed = 100;
        return Math.max(minSpeed, baseSpeed - (level - 1) * speedDecrease);
    }

    /**
     * Resets the UI to its initial state.
     */
    public void resetUI() {
        groupNotification.getChildren().clear();
        if (gameOverPanel != null) gameOverPanel.setVisible(false);
        pauseButton.setText("PAUSE");
        isPause.set(false);
        isGameOver.set(false);
    }

    /**
     * Checks if the game is paused.
     *
     * @return true if the game is paused, false otherwise.
     */
    public boolean isPaused() { return isPause.get(); }

    /**
     * Checks if the game is over.
     *
     * @return true if the game is over, false otherwise.
     */
    public boolean isGameOver() { return isGameOver.get(); }
}
package com.comp2042.ui;

import com.comp2042.event.GameEvent;
import com.comp2042.event.InputEventListener;
import com.comp2042.event.ViewData;
import com.comp2042.sounds.ISoundManager;
import com.comp2042.sounds.SoundManager;
import com.comp2042.ui.GameOverPanel;
import com.comp2042.ui.NotificationPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.beans.property.IntegerProperty;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.layout.Pane;
import com.comp2042.ui.ParticleEffect;
import javafx.scene.control.Label;
import javafx.application.Platform;
import com.comp2042.GameMode;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The main controller for the Graphical User Interface (GUI).
 * It manages all UI elements, handles user interactions, and communicates with the game logic controllers.
 */
public class GuiController implements Initializable {


    private GameMode currentMode = GameMode.CLASSIC;

    private ISoundManager soundManager;
    private InputEventListener eventListener;

    private GameBoardRenderer gameBoardRenderer;
    private GameInfoPanelController gameInfoPanelController;
    private GameFlowController gameFlowController;
    private InputHandler inputHandler;

    @FXML private Pane particlePane;
    private ParticleEffect particleEffect;
    @FXML private GridPane gamePanel;
    @FXML private StackPane groupNotification;
    @FXML private GameOverPanel gameOverPanel;
    @FXML private Label scoreLabel;
    @FXML private Button pauseButton;
    @FXML private Label levelLabel;
    @FXML private Button startButton;
    @FXML private Button settingsButton;
    @FXML private Button helpButton;
    @FXML private GridPane nextGrid;
    @FXML private Parent viewRoot;
    @FXML private Label modeNameLabel;
    @FXML private Label modeDetailsLabel;
    @FXML
    private Button modeButton;


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateModeDisplay();



        Button[] allButtons = {startButton, settingsButton, helpButton, pauseButton, modeButton};


        for (Button btn : allButtons) {
            if (btn != null) {

                btn.setFocusTraversable(false);


                btn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    soundManager.playClickSound();
                });
            }
        }
        if (soundManager == null) {
            soundManager = SoundManager.getInstance();
        }


        if (particlePane != null) {
            particleEffect = new ParticleEffect(particlePane);
        }

        for (Button btn : allButtons) {
            if (btn != null) {
                btn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    soundManager.playClickSound();
                });
            }
        }
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);

        gameBoardRenderer = new GameBoardRenderer(gamePanel);
        gameInfoPanelController = new GameInfoPanelController(scoreLabel, levelLabel, nextGrid);
        gameFlowController = new GameFlowController(
                gameBoardRenderer, gameInfoPanelController, groupNotification, pauseButton, gameOverPanel);
        inputHandler = new InputHandler(gameFlowController, gameBoardRenderer);
        particleEffect = new ParticleEffect(gamePanel);
        gameFlowController.setParticleEffect(particleEffect);

        // Game Panel UI setup
        gamePanel.setHgap(0);
        gamePanel.setVgap(0);
        StackPane.setAlignment(gamePanel, Pos.CENTER);
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(gamePanel.widthProperty());
        clip.heightProperty().bind(gamePanel.heightProperty());
        gamePanel.setClip(clip);

        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(inputHandler::handleKeyPress);

        pauseButton.setFocusTraversable(false);
        pauseButton.setMnemonicParsing(false);

        gameOverPanel.setVisible(false);

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    /**
     * Initializes the game view with the board matrix and the first brick.
     *
     * @param boardMatrix The game board matrix.
     * @param brick       The initial brick data.
     */
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        gameOverPanel.setVisible(false);
        gameBoardRenderer.initGameView(boardMatrix);
        gameInfoPanelController.updatePreviews(brick);
        gameFlowController.start();
        gameFlowController.newGame(this.currentMode);
        gamePanel.requestFocus();
    }

    /**
     * Sets the game mode. Called by the Main Menu or Tests before the game starts.
     * @param mode The game mode to set.
     */
    public void setGameMode(GameMode mode) {
        this.currentMode = mode;

        // Optional: Update labels immediately if they exist
        Platform.runLater(() -> {
            if (modeNameLabel != null) {
                modeNameLabel.setText("Mode: " + mode);
            }
            if (modeDetailsLabel != null) {
                if (mode == GameMode.SPRINT) modeDetailsLabel.setText("Clear 30 Lines");
                else if (mode == GameMode.ULTRA) modeDetailsLabel.setText("2 Minute Timer");
                else modeDetailsLabel.setText("Endless");
            }
        });
    }

    /**
     * Sets the event listener for game events.
     *
     * @param eventListener The event listener to set.
     */
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
        gameFlowController.setEventListener(eventListener);
        inputHandler.setEventListener(eventListener);
    }

    /**
     * Binds the score property to the score label.
     *
     * @param scoreProp The integer property representing the score.
     */
    public void bindScore(IntegerProperty scoreProp) {
        gameInfoPanelController.bindScore(scoreProp);
    }

    /**
     * Updates the two-line mode status display.
     *
     * @param modeName The name of the mode (e.g., "Sprint", "Classic").
     * @param details  The specific stats (e.g., "Lines: 1/30", "Score: 1500").
     */
    public void updateModeStatus(String modeName, String details) {
        Platform.runLater(() -> {


            if (modeNameLabel != null) {
                modeNameLabel.setText("Mode: " + modeName);
            }


            if (modeDetailsLabel != null) {

                if (modeName.equalsIgnoreCase("Classic")) {
                    modeDetailsLabel.setText("");
                } else {

                    modeDetailsLabel.setText(details);
                }
            }


            if (scoreLabel != null) {
                scoreLabel.setVisible(true);
            }
        });
    }

    /**
     * Signals the game over state to the UI.
     */
    public void gameOver() {
        gameFlowController.gameOver();
    }

    /**
     * Starts a new game.
     *
     * @param e The action event.
     */
    @FXML
    public void newGame(ActionEvent e) {
        gameOverPanel.setVisible(false);
        gameFlowController.newGame(this.currentMode);;

        if (eventListener != null) {
            eventListener.onGameEvent(new com.comp2042.event.NewGameEvent());
        }
        gamePanel.requestFocus();
    }

    /**
     * Pauses the game.
     *
     * @param e The action event.
     */
    @FXML
    public void pauseGame(ActionEvent e) {
        soundManager.playClickSound();
        gameFlowController.pauseGame();
        gamePanel.requestFocus();
    }

    /**
     * Handles the settings button click event.
     *
     * @param e The action event.
     */
    @FXML
    public void onSettingsClicked(ActionEvent e) {
        soundManager.playClickSound();
    }

    /**
     * Handles the help button click event.
     *
     * @param e The action event.
     */
    @FXML
    public void onHelpClicked(ActionEvent e) {
        soundManager.playClickSound();
    }

    /**
     * Navigates back to the main menu.
     */
    @FXML
    private void backToMainMenu() {
        if (eventListener != null) {
            eventListener.onGameEvent(new com.comp2042.event.BackToMenuEvent());
        }
    }


    /**
     * Switches the game mode.
     */
    @FXML
    public void switchMode() {

        switch (currentMode) {
            case CLASSIC:
                currentMode = GameMode.SPRINT;
                break;
            case SPRINT:
                currentMode = GameMode.ULTRA;
                break;
            case ULTRA:
                currentMode = GameMode.CLASSIC;
                break;
        }


        updateModeDisplay();


        newGame(null);
    }

    private void updateModeDisplay() {

        modeNameLabel.setText("Mode: " + currentMode.toString());


        switch (currentMode) {
            case CLASSIC:
                modeDetailsLabel.setText("Endless Marathon");
                break;
            case SPRINT:
                modeDetailsLabel.setText("Clear 30 Lines");
                break;
            case ULTRA:
                modeDetailsLabel.setText("2 Minute Timer");
                break;
        }
    }

    /**
     * Returns the root node of the view.
     *
     * @return The root parent node.
     */
    public Parent getViewRoot() {
        return viewRoot;
    }

    /**
     * Returns the game board renderer.
     *
     * @return The game board renderer.
     */
    public GameBoardRenderer getGameBoardRenderer() {
        return gameBoardRenderer;
    }

    /**
     * Returns the game info panel controller.
     *
     * @return The game info panel controller.
     */
    public GameInfoPanelController getGameInfoPanelController() {
        return gameInfoPanelController;
    }

    /**
     * Returns the game flow controller.
     *
     * @return The game flow controller.
     */
    public GameFlowController getGameFlowController() {
        return gameFlowController;
    }


    /**
     * Signals the victory state to the UI.
     */
    public void victory() {
        gameFlowController.victory();
    }
}
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

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The main UI controller for the Tetris game.
 * This class manages the interaction between the game logic and the JavaFX user interface.
 * It initializes and orchestrates various UI components like the game board, info panel,
 * and handles user input.
 */
public class GuiController implements Initializable {

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


    /**
     * Called to initialize a controller after its root element has been completely processed.
     *
     * @param location The location used to resolve relative paths for the root object, or
     *                 {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (soundManager == null) {
            soundManager = SoundManager.getInstance();
        }
        Button[] allButtons = {startButton, settingsButton, helpButton, pauseButton};

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
     * Initializes the game view with the given board matrix and the current brick.
     *
     * @param boardMatrix The initial state of the game board.
     * @param brick The initial brick to display.
     */
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        gameBoardRenderer.initGameView(boardMatrix);
        gameInfoPanelController.updatePreviews(brick);
        gameFlowController.start();
        gamePanel.requestFocus();
    }

    /**
     * Sets the event listener for game input.
     *
     * @param eventListener The InputEventListener to be set.
     */
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
        gameFlowController.setEventListener(eventListener);
        inputHandler.setEventListener(eventListener);
    }

    /**
     * Binds the score property to the game info panel.
     *
     * @param scoreProp The IntegerProperty representing the current score.
     */
    public void bindScore(IntegerProperty scoreProp) {
        gameInfoPanelController.bindScore(scoreProp);
    }

    /**
     * Triggers the game over sequence in the game flow controller.
     */
    public void gameOver() {
        gameFlowController.gameOver();
    }

    /**
     * Handles the action when the new game button is clicked.
     *
     * @param e The ActionEvent generated by the button click.
     */
    @FXML
    public void newGame(ActionEvent e) {
        if (eventListener != null) {
            eventListener.onGameEvent(new com.comp2042.event.NewGameEvent());
        }
        gamePanel.requestFocus();
    }

    /**
     * Handles the action when the pause game button is clicked.
     *
     * @param e The ActionEvent generated by the button click.
     */
    @FXML
    public void pauseGame(ActionEvent e) {
        soundManager.playClickSound();
        gameFlowController.pauseGame();
        gamePanel.requestFocus();
    }

    /**
     * Handles the action when the settings button is clicked.
     *
     * @param e The ActionEvent generated by the button click.
     */
    @FXML
    public void onSettingsClicked(ActionEvent e) {
        soundManager.playClickSound();
    }

    /**
     * Handles the action when the help button is clicked.
     *
     * @param e The ActionEvent generated by the button click.
     */
    @FXML
    public void onHelpClicked(ActionEvent e) {
        soundManager.playClickSound();
    }

    /**
     * Handles the action to navigate back to the main menu.
     * This method is typically called from a UI element.
     */
    @FXML
    private void backToMainMenu() {
        if (eventListener != null) {
            eventListener.onGameEvent(new com.comp2042.event.BackToMenuEvent());
        }
    }

    /**
     * Returns the root node of the view.
     *
     * @return The Parent root node of the GUI.
     */
    public Parent getViewRoot() {
        return viewRoot;
    }

    /**
     * Returns the GameBoardRenderer instance.
     *
     * @return The GameBoardRenderer.
     */
    public GameBoardRenderer getGameBoardRenderer() {
        return gameBoardRenderer;
    }

    /**
     * Returns the GameInfoPanelController instance.
     *
     * @return The GameInfoPanelController.
     */
    public GameInfoPanelController getGameInfoPanelController() {
        return gameInfoPanelController;
    }
}
package com.comp2042.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

/**
 * A custom JavaFX {@link BorderPane} designed to display a Game Over or Victory message.
 */
public class GameOverPanel extends BorderPane {

    private final Label gameOverLabel;

    /**
     * Constructs a new GameOverPanel.
     * Initializes the UI elements for displaying game over or victory messages.
     */
    public GameOverPanel() {

        gameOverLabel = new Label("GAME OVER");


        gameOverLabel.getStyleClass().add("gameOverStyle");
        this.setStyle("-fx-background-color: transparent;");


        setCenter(gameOverLabel);


        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> gameOverLabel.setOpacity(1)),
                new KeyFrame(Duration.seconds(0.5), e -> gameOverLabel.setOpacity(0)),
                new KeyFrame(Duration.seconds(1), e -> gameOverLabel.setOpacity(1))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }


    /**
     * Sets the text to be displayed on the game over panel.
     * If the text contains "VICTORY" (case-insensitive), the text color will be set to light green.
     *
     * @param text The string message to display.
     */
    public void setVictoryText(String text) {
        gameOverLabel.setText(text);

        if (text.toUpperCase().contains("VICTORY")) {
            gameOverLabel.setStyle("-fx-text-fill: lightgreen; -fx-font-size: 50px; -fx-background-color: transparent;");
        } else {
            gameOverLabel.setStyle("-fx-text-fill: white; -fx-font-size: 50px; -fx-background-color: transparent;");
        }
    }
}
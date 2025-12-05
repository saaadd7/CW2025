package com.comp2042.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * A custom JavaFX {@link BorderPane} designed to display a "GAME OVER" message
 * with a pulsating visual effect.
 */
public class GameOverPanel extends BorderPane {

    /**
     * Constructs a new GameOverPanel.
     * Initializes the "GAME OVER" label, applies custom styling,
     * centers the label, and sets up a pulsating animation.
     */
    public GameOverPanel() {
        // Create the "GAME OVER" label
        final Label gameOverLabel = new Label("GAME OVER");

        // Add style class for custom styling (optional)
        gameOverLabel.getStyleClass().add("gameOverStyle");

        // Set the label in the center of the BorderPane
        setCenter(gameOverLabel);

        // Add pulsating effect using Timeline
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> gameOverLabel.setOpacity(1)),
                new KeyFrame(Duration.seconds(0.5), e -> gameOverLabel.setOpacity(0)),
                new KeyFrame(Duration.seconds(1), e -> gameOverLabel.setOpacity(1))
        );

        // Set the animation to loop indefinitely
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();  // Start the animation
    }
}

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


    public void setVictoryText(String text) {
        gameOverLabel.setText(text);

        if (text.toUpperCase().contains("VICTORY")) {
            gameOverLabel.setStyle("-fx-text-fill: lightgreen; -fx-font-size: 50px; -fx-background-color: transparent;");
        } else {
            gameOverLabel.setStyle("-fx-text-fill: white; -fx-font-size: 50px; -fx-background-color: transparent;");
        }
    }
}
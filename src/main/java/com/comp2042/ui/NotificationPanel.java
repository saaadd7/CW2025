package com.comp2042.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * A custom JavaFX {@link BorderPane} used to display transient notifications
 * such as score bonuses or "GAME OVER" messages.
 * It features animation for fading out and moving the notification.
 */
public class NotificationPanel extends BorderPane {

    /**
     * Constructs a NotificationPanel with a specified text.
     * Applies different styling and effects based on whether the text is "GAME OVER".
     *
     * @param text The text to be displayed in the notification.
     */
    public NotificationPanel(String text) {

        // Special styling for GAME OVER
        if (text.contains("GAME OVER")) {
            setMinHeight(80);
            setMinWidth(300);
            setMaxHeight(80);
            setMaxWidth(300);

            // Make background transparent
            setStyle("-fx-background-color: transparent;");

            final Label score = new Label(text);
            score.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-font-family: 'Arial Black'; -fx-background-color: transparent;");

            // Bright orange color
            score.setTextFill(Color.web("#FF8800"));

            // Add strong drop shadow for contrast
            DropShadow dropShadow = new DropShadow();
            dropShadow.setColor(Color.BLACK);
            dropShadow.setRadius(15);
            dropShadow.setSpread(0.8);

            DropShadow outerGlow = new DropShadow();
            outerGlow.setColor(Color.web("#FF8800")); // Orange glow
            outerGlow.setRadius(25);
            outerGlow.setSpread(0.6);
            outerGlow.setInput(dropShadow);

            Glow glow = new Glow(0.9);
            glow.setInput(outerGlow);
            score.setEffect(glow);

            setCenter(score);
        } else if (text.contains("VICTORY!")) {
            System.out.println("VICTORY! panel created");
            setMinHeight(80);
            setMinWidth(300);
            setMaxHeight(80);
            setMaxWidth(300);

            // Make background transparent
            setStyle("-fx-background-color: transparent;");

            final Label score = new Label(text);
            score.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-font-family: 'Arial Black'; -fx-background-color: transparent;");

            // Gold color for victory
            score.setTextFill(Color.web("#FFD700")); // Gold color

            // Add strong drop shadow for contrast
            DropShadow dropShadow = new DropShadow();
            dropShadow.setColor(Color.BLACK);
            dropShadow.setRadius(15);
            dropShadow.setSpread(0.8);

            DropShadow outerGlow = new DropShadow();
            outerGlow.setColor(Color.web("#FFD700")); // Gold glow
            outerGlow.setRadius(25);
            outerGlow.setSpread(0.6);
            outerGlow.setInput(dropShadow);

            Glow glow = new Glow(0.9);
            glow.setInput(outerGlow);
            score.setEffect(glow);

            setCenter(score);
        } else {
            setMinHeight(200);
            setMinWidth(220);
            final Label score = new Label(text);
            score.getStyleClass().add("bonusStyle");
            final Effect glow = new Glow(0.6);
            score.setEffect(glow);
            score.setTextFill(Color.WHITE);
            setCenter(score);
        }
    }

    /**
     * Displays the notification with a fade-out and translate animation.
     * The notification is removed from the parent's children list after the animation finishes.
     *
     * @param list The {@link ObservableList} of {@link Node}s (typically the parent's children)
     *             from which this notification panel will be removed after animation.
     */
    public void showScore(ObservableList<Node> list) {
        FadeTransition ft = new FadeTransition(Duration.millis(1000), this);
        TranslateTransition tt = new TranslateTransition(Duration.millis(700), this);
        tt.setByY(-40);
        ft.setFromValue(1);
        ft.setToValue(0);
        ParallelTransition transition = new ParallelTransition(tt, ft);
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                list.remove(NotificationPanel.this);
            }
        });
        transition.play();
    }
}
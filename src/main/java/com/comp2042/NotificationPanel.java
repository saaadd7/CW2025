package com.comp2042;

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

public class NotificationPanel extends BorderPane {

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

    public void showScore(ObservableList<Node> list) {
        FadeTransition ft = new FadeTransition(Duration.millis(2000), this);
        TranslateTransition tt = new TranslateTransition(Duration.millis(2500), this);
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
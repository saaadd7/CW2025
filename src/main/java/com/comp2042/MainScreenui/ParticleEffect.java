package com.comp2042.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleEffect {

    private final Pane container;
    private final Random random = new Random();
    private static final double CELL_SIZE = 20.0; // Size of each Tetris block

    public ParticleEffect(Pane container) {
        this.container = container;
    }

    /**
     * Creates a smooth horizontal block disintegration effect for cleared lines
     * @param clearedRows List of row indices that were cleared
     * @param numLines Number of lines cleared
     */
    public void createLineClearExplosion(List<Integer> clearedRows, int numLines) {
        if (clearedRows == null || clearedRows.isEmpty()) {
            return;
        }

        double containerWidth = container.getWidth();
        int blocksPerRow = (int)(containerWidth / CELL_SIZE);

        System.out.println("Creating horizontal disintegration for rows: " + clearedRows);

        // For each cleared row, create blocks that slide out horizontally
        for (Integer rowIndex : clearedRows) {
            // Convert row index to Y position (accounting for hidden top rows)
            double rowY = (rowIndex - 2) * CELL_SIZE;

            System.out.println("Row " + rowIndex + " at Y position: " + rowY);

            // Create blocks for each column in this row
            for (int col = 0; col < blocksPerRow; col++) {
                double blockX = col * CELL_SIZE;

                // Create the block rectangle
                Rectangle block = new Rectangle(blockX, rowY, CELL_SIZE - 1, CELL_SIZE - 1);

                // Color based on number of lines cleared
                Color blockColor = getColorForIntensity(numLines);
                block.setFill(blockColor);
                block.setStroke(Color.BLACK);
                block.setStrokeWidth(1);

                container.getChildren().add(block);

                // Animate blocks sliding out horizontally
                // Left half slides left, right half slides right
                boolean slideLeft = col < blocksPerRow / 2;
                animateHorizontalSlide(block, slideLeft, col * 20); // 20ms delay per block
            }
        }
    }

    private void animateHorizontalSlide(Rectangle block, boolean slideLeft, int delayMs) {
        // Slide distance
        double slideDistance = 150 + random.nextDouble() * 100;
        if (slideLeft) {
            slideDistance = -slideDistance; // Negative for left direction
        }

        double animationDuration = 600 + random.nextDouble() * 200;

        // Create horizontal slide animation
        TranslateTransition slide = new TranslateTransition(Duration.millis(animationDuration), block);
        slide.setByX(slideDistance);

        // Slight vertical movement for variety
        double verticalDrift = (random.nextDouble() - 0.5) * 20;
        slide.setByY(verticalDrift);

        // Create fade out animation
        FadeTransition fade = new FadeTransition(Duration.millis(animationDuration), block);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        // Combine animations
        ParallelTransition animation = new ParallelTransition(slide, fade);
        animation.setDelay(Duration.millis(delayMs));

        // Remove block when animation completes
        animation.setOnFinished(e -> container.getChildren().remove(block));

        animation.play();
    }

    private Color getColorForIntensity(int intensity) {
        switch (intensity) {
            case 4: // Tetris - Gold
                return Color.color(1.0, 0.843, 0.0);
            case 3: // Triple - Orange
                return Color.color(1.0, 0.5, 0.0);
            case 2: // Double - Cyan
                return Color.color(0.0, 1.0, 1.0);
            default: // Single - White
                return Color.color(1.0, 1.0, 1.0);
        }
    }
}
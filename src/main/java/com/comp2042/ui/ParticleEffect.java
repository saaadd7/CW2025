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

/**
 * Manages and renders particle effects within the game, specifically for line clear explosions.
 * It creates visual feedback when rows are cleared by animating individual blocks.
 */
public class ParticleEffect {

    private final Pane container;
    private final Random random = new Random();
    private static final double CELL_SIZE = 20.0; // Assuming each game block is 20x20 pixels

    /**
     * Constructs a ParticleEffect manager.
     * @param container The JavaFX {@link Pane} where particle effects will be rendered.
     */
    public ParticleEffect(Pane container) {
        this.container = container;
    }

    /**
     * Creates a smooth horizontal block disintegration effect for cleared lines.
     * Individual blocks from the cleared rows slide horizontally and fade out.
     *
     * @param clearedRows A {@link List} of row indices that were cleared.
     * @param numLines The number of lines cleared in this event, used to determine particle color intensity.
     */
    public void createLineClearExplosion(List<Integer> clearedRows, int numLines) {
        if (clearedRows == null || clearedRows.isEmpty()) {
            return;
        }

        double containerWidth = container.getWidth();
        int blocksPerRow = (int)(containerWidth / CELL_SIZE); // Calculate how many blocks fit horizontally

        for (Integer rowIndex : clearedRows) {
            // Adjust row index because the game board might have hidden rows
            double rowY = (rowIndex - 2) * CELL_SIZE;

            for (int col = 0; col < blocksPerRow; col++) {
                double blockX = col * CELL_SIZE;

                Rectangle block = new Rectangle(blockX, rowY, CELL_SIZE - 1, CELL_SIZE - 1); // -1 for a slight gap

                Color blockColor = getColorForIntensity(numLines);
                block.setFill(blockColor);
                block.setStroke(Color.BLACK);
                block.setStrokeWidth(1);

                container.getChildren().add(block); // Add block to the pane

                // Determine if the block should slide left or right
                boolean slideLeft = col < blocksPerRow / 2;
                animateHorizontalSlide(block, slideLeft, col * 20); // Apply animation
            }
        }
    }

    /**
     * Animates a single block to slide horizontally, drift vertically, and fade out.
     *
     * @param block The {@link Rectangle} representing the particle block.
     * @param slideLeft True if the block should slide left, false if it should slide right.
     * @param delayMs The delay in milliseconds before the animation starts.
     */
    private void animateHorizontalSlide(Rectangle block, boolean slideLeft, int delayMs) {
        // Randomize slide distance and duration for a more natural explosion
        double slideDistance = 150 + random.nextDouble() * 100;
        if (slideLeft) {
            slideDistance = -slideDistance;
        }

        double animationDuration = 600 + random.nextDouble() * 200;

        TranslateTransition slide = new TranslateTransition(Duration.millis(animationDuration), block);
        slide.setByX(slideDistance); // Horizontal movement

        // Add a slight random vertical drift
        double verticalDrift = (random.nextDouble() - 0.5) * 20; // -10 to +10 pixels
        slide.setByY(verticalDrift);

        FadeTransition fade = new FadeTransition(Duration.millis(animationDuration), block);
        fade.setFromValue(1.0); // Start fully opaque
        fade.setToValue(0.0);   // End fully transparent

        ParallelTransition animation = new ParallelTransition(slide, fade); // Run slide and fade simultaneously
        animation.setDelay(Duration.millis(delayMs)); // Stagger animations for a wave effect

        // Remove the block from the container once its animation is finished
        animation.setOnFinished(e -> container.getChildren().remove(block));

        animation.play();
    }

    /**
     * Returns a color based on the intensity (number of lines cleared).
     * This can be used to make multi-line clears visually more impactful.
     *
     * @param intensity The number of lines cleared.
     * @return A JavaFX {@link Color} for the particle.
     */
    private Color getColorForIntensity(int intensity) {
        switch (intensity) {
            case 4: // Tetris (4 lines)
                return Color.color(1.0, 0.843, 0.0); // Gold/Orange
            case 3: // Triple
                return Color.color(1.0, 0.5, 0.0);   // Orange
            case 2: // Double
                return Color.color(0.0, 1.0, 1.0);   // Cyan
            default: // Single or other
                return Color.color(1.0, 1.0, 1.0);   // White
        }
    }
}
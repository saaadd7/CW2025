package com.comp2042.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleEffect {

    private final Pane container;
    private final Random random = new Random();

    public ParticleEffect(Pane container) {
        this.container = container;
    }

    /**
     * Creates a DRAMATIC particle explosion effect for cleared lines
     * @param clearedRows List of row indices that were cleared
     * @param numLines Number of lines cleared (affects particle intensity)
     */
    public void createLineClearExplosion(List<Integer> clearedRows, int numLines) {
        if (clearedRows == null || clearedRows.isEmpty()) {
            return;
        }

        // MUCH MORE PARTICLES for dramatic effect
        int particleCount = 100 * numLines;
        List<Particle> particles = new ArrayList<>();

        double containerWidth = container.getWidth();
        double containerHeight = container.getHeight();

        System.out.println("Container size: " + containerWidth + "x" + containerHeight);
        System.out.println("Cleared rows: " + clearedRows);

        // For each cleared row, create a burst of particles
        for (Integer rowIndex : clearedRows) {
            // Convert row index to Y position
            // Assuming rows go from top (0) to bottom (max)
            // Each cell is approximately containerHeight / 20 pixels (20 rows visible)
            double rowY = (rowIndex - 2) * (containerHeight / 20.0); // -2 because top 2 rows are hidden

            System.out.println("Creating particles at row " + rowIndex + " -> Y: " + rowY);

            int particlesPerRow = particleCount / clearedRows.size();

            for (int i = 0; i < particlesPerRow; i++) {
                // Spread particles across the entire width
                double startX = random.nextDouble() * containerWidth;
                Particle particle = createParticle(startX, rowY, numLines);
                particles.add(particle);
                container.getChildren().add(particle.circle);
            }
        }

        // Animate particles
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(16), e -> {
            particles.removeIf(particle -> {
                particle.update();

                // Remove if faded out or way off screen
                if (particle.alpha <= 0 ||
                        particle.y > containerHeight + 100 ||
                        particle.y < -100 ||
                        particle.x < -50 ||
                        particle.x > containerWidth + 50) {
                    container.getChildren().remove(particle.circle);
                    return true;
                }
                return false;
            });
        }));
        animation.setCycleCount(200); // Run for ~3.2 seconds
        animation.setOnFinished(e -> {
            // Clean up any remaining particles
            particles.forEach(p -> container.getChildren().remove(p.circle));
        });
        animation.play();
    }

    private Particle createParticle(double startX, double startY, int intensity) {
        // DRAMATIC velocity - explode in all directions!
        double angle = random.nextDouble() * 2 * Math.PI;
        double speed = 5 + random.nextDouble() * 10; // Much faster!
        double velocityX = Math.cos(angle) * speed;
        double velocityY = Math.sin(angle) * speed - 3; // Upward bias

        // Larger particles for visibility
        double size = 3 + random.nextDouble() * 6;

        // Bright, dramatic colors based on intensity
        Color color;
        switch (intensity) {
            case 4: // Tetris! - BRILLIANT GOLD
                color = Color.color(1.0, 0.843, 0.0);
                size *= 1.5; // Bigger particles for Tetris!
                break;
            case 3: // Triple - BRIGHT ORANGE
                color = Color.color(1.0, 0.5, 0.0);
                size *= 1.3;
                break;
            case 2: // Double - ELECTRIC CYAN
                color = Color.color(0.0, 1.0, 1.0);
                size *= 1.2;
                break;
            default: // Single - BRIGHT WHITE
                color = Color.color(1.0, 1.0, 1.0);
                break;
        }

        // Add variety with different colors
        if (random.nextDouble() < 0.4) {
            Color[] colors = {
                    Color.color(1.0, 0.0, 0.5),  // Hot Pink
                    Color.color(0.5, 0.0, 1.0),  // Purple
                    Color.color(0.0, 1.0, 0.5),  // Lime Green
                    Color.color(1.0, 1.0, 0.0),  // Yellow
            };
            color = colors[random.nextInt(colors.length)];
        }

        return new Particle(startX, startY, velocityX, velocityY, size, color);
    }

    private class Particle {
        Circle circle;
        double x, y;
        double velocityX, velocityY;
        double alpha = 1.0;
        Color baseColor;
        double rotation = 0;

        Particle(double x, double y, double vx, double vy, double size, Color color) {
            this.x = x;
            this.y = y;
            this.velocityX = vx;
            this.velocityY = vy;
            this.baseColor = color;

            circle = new Circle(x, y, size);
            circle.setFill(color);
        }

        void update() {
            // Update position
            x += velocityX;
            y += velocityY;

            // Apply gravity
            velocityY += 0.4;

            // Apply air resistance
            velocityX *= 0.98;

            // Fade out slowly for longer visibility
            alpha -= 0.008;
            alpha = Math.max(0, alpha);

            // Rotation for visual effect
            rotation += 0.1;

            // Update circle
            circle.setCenterX(x);
            circle.setCenterY(y);
            circle.setRotate(rotation * 180 / Math.PI);
            circle.setFill(Color.color(
                    baseColor.getRed(),
                    baseColor.getGreen(),
                    baseColor.getBlue(),
                    alpha
            ));
        }
    }
}
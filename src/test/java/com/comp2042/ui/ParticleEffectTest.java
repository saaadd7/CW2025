package com.comp2042.ui;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class ParticleEffectTest {

    @BeforeAll
    static void initToolkit() {
        try { Platform.startup(() -> {}); } catch (IllegalStateException e) {}
    }

    @Test
    void testExplosionAddsParticles() {
        // Arrange
        Pane container = new Pane();
        // Give the pane a size so particles can calculate positions
        container.setPrefSize(200, 400);

        ParticleEffect effect = new ParticleEffect(container);

        Platform.runLater(() -> {
            // Act
            // Simulate clearing row 10 and 11
            effect.createLineClearExplosion(Arrays.asList(10, 11), 2);

            // Assert
            // We expect particles (Rectangles) to be added to the container
            assertFalse(container.getChildren().isEmpty(), "Particles should be added to the pane after explosion");
        });
    }

    @Test
    void testEmptyListDoesNothing() {
        Pane container = new Pane();
        ParticleEffect effect = new ParticleEffect(container);

        Platform.runLater(() -> {
            effect.createLineClearExplosion(null, 0);
            assertTrue(container.getChildren().isEmpty());
        });
    }
}
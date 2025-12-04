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
        ParticleEffect effect = new ParticleEffect(container);

        Platform.runLater(() -> {
            // FIX: Manually resize the container so getWidth() returns 200 instead of 0
            container.resize(200, 400);

            // Act
            effect.createLineClearExplosion(Arrays.asList(10, 11), 2);

            // Assert
            assertFalse(container.getChildren().isEmpty(), "Particles should be added to the pane after explosion");
        });
    }

    @Test
    void testEmptyListDoesNothing() {
        Pane container = new Pane();
        ParticleEffect effect = new ParticleEffect(container);

        Platform.runLater(() -> {
            container.resize(200, 400); // Good practice to set size here too
            effect.createLineClearExplosion(null, 0);

            assertTrue(container.getChildren().isEmpty(), "Should not add particles if row list is null");
        });
    }
}
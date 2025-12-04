package com.comp2042.ui;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameInfoPanelControllerTest {

    @BeforeAll
    static void initToolkit() {
        try { Platform.startup(() -> {}); } catch (IllegalStateException e) {}
    }

    @Test
    void testInitializationAndScoreBind() {
        // Arrange
        Label scoreLbl = new Label();
        Label levelLbl = new Label();
        GridPane nextGrid = new GridPane();

        Platform.runLater(() -> {
            // Act
            GameInfoPanelController controller = new GameInfoPanelController(scoreLbl, levelLbl, nextGrid);

            // Check Grid Init
            assertFalse(nextGrid.getChildren().isEmpty(), "Next Brick grid should be populated with Rectangles");
            assertTrue(nextGrid.getChildren().get(0) instanceof Rectangle);

            // Check Score Binding
            SimpleIntegerProperty scoreProp = new SimpleIntegerProperty(0);
            controller.bindScore(scoreProp);

            scoreProp.set(500);
            assertEquals("Score: 500", scoreLbl.getText());

            // Check Level Update
            controller.setLevel(5);
            assertEquals("5", levelLbl.getText());
        });
    }
}
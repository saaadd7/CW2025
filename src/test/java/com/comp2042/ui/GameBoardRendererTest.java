package com.comp2042.ui;

import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameBoardRendererTest {

    @BeforeAll
    static void initToolkit() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {}
    }

    @Test
    void testInitGameViewCreatesGrid() {
        // Arrange
        GridPane gridPane = new GridPane();
        GameBoardRenderer renderer = new GameBoardRenderer(gridPane);

        // Create a standard board matrix (22 rows, 10 cols)
        int[][] matrix = new int[22][10];

        // Act
        renderer.initGameView(matrix);

        // Assert
        // The renderer skips the first 2 "HIDDEN_ROWS", so it draws 20 rows.
        // 20 rows * 10 columns = 200 rectangles expected.
        int expectedRectangles = 20 * 10;

        assertEquals(expectedRectangles, gridPane.getChildren().size(),
                "Renderer should populate the GridPane with Rectangles");

        // Verify the type of the children
        assertTrue(gridPane.getChildren().get(0) instanceof Rectangle,
                "Children should be JavaFX Rectangles");
    }
}
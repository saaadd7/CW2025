package com.comp2042.ui;

import com.comp2042.event.ViewData;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * Controller for the game information panel, responsible for displaying the player's score,
 * current level, and a preview of the next falling brick.
 */
public class GameInfoPanelController {

    private static final int PREVIEW_BRICK_SIZE = 12;
    private static final int NEXT_GRID_SIZE = 4;

    private final Label scoreLabel;
    private final Label levelLabel;
    private final GridPane nextGrid;
    private final Rectangle[][] nextCells = new Rectangle[NEXT_GRID_SIZE][NEXT_GRID_SIZE];

    /**
     * Constructs a GameInfoPanelController.
     *
     * @param scoreLabel The JavaFX Label to display the score.
     * @param levelLabel The JavaFX Label to display the current level.
     * @param nextGrid The JavaFX GridPane to display the next brick preview.
     */
    public GameInfoPanelController(Label scoreLabel, Label levelLabel, GridPane nextGrid) {
        this.scoreLabel = scoreLabel;
        this.levelLabel = levelLabel;
        this.nextGrid = nextGrid;
        initNextGrid();
    }

    /**
     * Initializes the GridPane for the next brick preview.
     * Sets up its dimensions and creates a grid of transparent rectangles.
     */
    private void initNextGrid() {
        if (nextGrid == null) {
            return;
        }

        nextGrid.setHgap(0);
        nextGrid.setVgap(0);
        nextGrid.getChildren().clear();
        nextGrid.setPrefWidth(NEXT_GRID_SIZE * PREVIEW_BRICK_SIZE);
        nextGrid.setPrefHeight(NEXT_GRID_SIZE * PREVIEW_BRICK_SIZE);
        for (int row = 0; row < NEXT_GRID_SIZE; row++) {
            for (int col = 0; col < NEXT_GRID_SIZE; col++) {
                Rectangle r = new Rectangle(PREVIEW_BRICK_SIZE, PREVIEW_BRICK_SIZE);
                r.setFill(Color.TRANSPARENT);
                r.setStroke(Color.BLACK);
                r.setStrokeWidth(1);
                r.setStrokeType(StrokeType.INSIDE);

                nextCells[row][col] = r;
                nextGrid.add(r, col, row);
            }
        }
    }

    /**
     * Updates the preview of the next brick based on the provided {@link ViewData}.
     * Only the first next brick is currently displayed.
     *
     * @param brick The {@link ViewData} containing the data for the next brick.
     */
    public void updatePreviews(ViewData brick) {
        drawPreview(brick.getNextBrickData1());
    }

    /**
     * Draws the brick preview onto the {@code nextGrid}.
     *
     * @param data The 2D integer array representing the brick's shape to be displayed as a preview.
     */
    private void drawPreview(int[][] data) {
        if (nextGrid == null) return;

        for (int i = 0; i < NEXT_GRID_SIZE; i++) {
            for (int j = 0; j < NEXT_GRID_SIZE; j++) {
                int value = 0;
                if (data != null && i < data.length && j < data[i].length) {
                    value = data[i][j];
                }

                Rectangle r = nextCells[i][j];
                if (value != 0) {
                    r.setFill(getFillColor(value));
                    r.setStroke(Color.BLACK);
                    r.setStrokeWidth(0.5);
                } else {
                    r.setFill(Color.TRANSPARENT);
                    r.setStroke(Color.TRANSPARENT);
                    r.setStrokeWidth(1);
                }
            }
        }
    }

    /**
     * Binds the score label's text property to an {@link IntegerProperty}.
     * This allows the score display to update automatically when the score changes.
     *
     * @param scoreProp The {@link IntegerProperty} representing the game's score.
     */
    public void bindScore(IntegerProperty scoreProp) {
        scoreLabel.textProperty().bind(Bindings.format("Score: %d", scoreProp));
    }

    /**
     * Sets the text of the level label to display the current game level.
     *
     * @param level The current game level.
     */
    public void setLevel(int level) {
        levelLabel.setText(String.valueOf(level));
    }
    
    /**
     * Returns the appropriate fill color for a block based on its integer value.
     * This is a helper method to map brick types to their visual colors.
     *
     * @param i The integer value representing the block type.
     * @return The JavaFX {@link Color} for the block's fill.
     */
    private Color getFillColor(int i) {
        switch (i) {
            case 0: return Color.TRANSPARENT; // Empty space
            case 1: return Color.AQUA;
            case 2: return Color.BLUEVIOLET;
            case 3: return Color.DARKGREEN;
            case 4: return Color.YELLOW;
            case 5: return Color.RED;
            case 6: return Color.BEIGE;
            case 7: return Color.BURLYWOOD;
            default: return Color.WHITE; // Fallback for unknown values
        }
    }
    // Add this inside GameInfoPanelController
    public void updateTime(int secondsRemaining) {
        if (levelLabel != null) {
            // Calculate minutes and seconds
            int minutes = secondsRemaining / 60;
            int seconds = secondsRemaining % 60;

            // Format it nicely (e.g., "01:59")
            String timeString = String.format("%02d:%02d", minutes, seconds);

            // We temporarily use the level label to show time in Ultra mode
            // Or if you have a specific timeLabel, use that instead.
            levelLabel.setText(timeString);
        }
    }
}

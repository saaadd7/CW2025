package com.comp2042.ui;

import com.comp2042.ViewData;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class GameInfoPanelController {

    private static final int PREVIEW_BRICK_SIZE = 12;
    private static final int NEXT_GRID_SIZE = 4;

    private final Label scoreLabel;
    private final Label levelLabel;
    private final GridPane nextGrid;
    private final Rectangle[][] nextCells = new Rectangle[NEXT_GRID_SIZE][NEXT_GRID_SIZE];

    public GameInfoPanelController(Label scoreLabel, Label levelLabel, GridPane nextGrid) {
        this.scoreLabel = scoreLabel;
        this.levelLabel = levelLabel;
        this.nextGrid = nextGrid;
        initNextGrid();
    }

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

    public void updatePreviews(ViewData brick) {
        drawPreview(brick.getNextBrickData1());
    }

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

    public void bindScore(IntegerProperty scoreProp) {
        scoreLabel.textProperty().bind(Bindings.format("Score: %d", scoreProp));
    }

    public void setLevel(int level) {
        levelLabel.setText("Level: " + level);
    }
    
    private Color getFillColor(int i) {
        switch (i) {
            case 0: return Color.TRANSPARENT;
            case 1: return Color.AQUA;
            case 2: return Color.BLUEVIOLET;
            case 3: return Color.DARKGREEN;
            case 4: return Color.YELLOW;
            case 5: return Color.RED;
            case 6: return Color.BEIGE;
            case 7: return Color.BURLYWOOD;
            default: return Color.WHITE;
        }
    }
}

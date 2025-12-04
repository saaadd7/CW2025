package com.comp2042.ui;

import com.comp2042.ViewData;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.util.ArrayList;
import java.util.List;

public class GameBoardRenderer {

    private static final int BRICK_SIZE = 20;
    private static final int HIDDEN_ROWS = 2;

    private final GridPane gamePanel;
    private Rectangle[][] displayMatrix;
    private final List<Rectangle> fallingBrickNodes = new ArrayList<>();

    public GameBoardRenderer(GridPane gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void initGameView(int[][] boardMatrix) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];

        for (int row = HIDDEN_ROWS; row < boardMatrix.length; row++) {
            for (int col = 0; col < boardMatrix[row].length; col++) {
                Rectangle r = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                r.setFill(Color.TRANSPARENT);
                r.setStroke(Color.BLACK);
                r.setStrokeWidth(1);
                r.setStrokeType(StrokeType.INSIDE);

                displayMatrix[row][col] = r;
                gamePanel.add(r, col, row - HIDDEN_ROWS);
            }
        }
    }

    public void refreshGameBackground(int[][] board) {
        for (int row = HIDDEN_ROWS; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                displayMatrix[row][col].setFill(getFillColor(board[row][col]));
            }
        }
    }

    public void refreshBrick(ViewData brick) {
        clearGhost();
        gamePanel.getChildren().removeAll(fallingBrickNodes);
        fallingBrickNodes.clear();

        drawGhost(brick);

        int[][] brickData = brick.getBrickData();
        for (int row = 0; row < brickData.length; row++) {
            for (int col = 0; col < brickData[row].length; col++) {
                if (brickData[row][col] != 0) {
                    Rectangle r = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                    r.setFill(getFillColor(brickData[row][col]));
                    r.setStroke(Color.BLACK);
                    r.setStrokeWidth(0.25);
                    r.setStrokeType(StrokeType.INSIDE);

                    fallingBrickNodes.add(r);

                    int x = brick.getxPosition() + col;
                    int y = (brick.getyPosition() - HIDDEN_ROWS) + row;
                    if (y >= 0) {
                        gamePanel.add(r, x, y);
                    }
                }
            }
        }
    }

    private void clearGhost() {
        gamePanel.getChildren().removeIf(node -> node.getStyleClass().contains("ghost"));
    }

    private void drawGhost(ViewData view) {
        int[][] ghostData = view.getGhostData();
        if (ghostData == null) {
            return;
        }

        int x = view.getGhostX();
        int y = view.getGhostY();

        for (int row = 0; row < ghostData.length; row++) {
            for (int col = 0; col < ghostData[row].length; col++) {
                if (ghostData[row][col] == 0) continue;

                Rectangle ghost = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                ghost.setFill(getGhostColor(ghostData[row][col]));
                ghost.getStyleClass().add("ghost");
                ghost.setStroke(Color.BLACK);
                ghost.setStrokeWidth(0.25);
                ghost.setStrokeType(StrokeType.INSIDE);

                gamePanel.add(ghost, x + col, (y - HIDDEN_ROWS) + row);
            }
        }
    }

    private Paint getFillColor(int i) {
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

    private Paint getGhostColor(int value) {
        if (value == 0) return Color.TRANSPARENT;
        return Color.rgb(200, 200, 200, 0.3);
    }
}

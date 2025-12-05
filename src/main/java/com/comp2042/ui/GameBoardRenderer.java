package com.comp2042.ui;

import com.comp2042.event.ViewData;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.util.ArrayList;
import java.util.List;

/**
 * Renders the Tetris game board and the falling bricks onto a JavaFX {@link GridPane}.
 * It handles the visual representation of the game state, including static blocks,
 * the current falling brick, and the ghost piece.
 */
public class GameBoardRenderer {

    private static final int BRICK_SIZE = 20;
    private static final int HIDDEN_ROWS = 2; // Rows at the top of the board that are not visible initially

    private final GridPane gamePanel;
    private Rectangle[][] displayMatrix; // Represents the settled blocks on the board
    private final List<Rectangle> fallingBrickNodes = new ArrayList<>(); // Represents the currently falling brick

    /**
     * Constructs a GameBoardRenderer.
     * @param gamePanel The {@link GridPane} where the game board will be rendered.
     */
    public GameBoardRenderer(GridPane gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * Initializes the game view by clearing existing children from the game panel
     * and setting up the grid of transparent rectangles for the board background.
     * Hidden rows are accounted for when positioning elements in the GridPane.
     *
     * @param boardMatrix The initial 2D integer array representing the game board's dimensions.
     */
    public void initGameView(int[][] boardMatrix) {
        gamePanel.getChildren().clear();
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

    /**
     * Refreshes the visual representation of the settled blocks on the game board.
     * This method updates the colors of the background rectangles based on the provided board matrix.
     *
     * @param board The 2D integer array representing the current state of the game board.
     */
    public void refreshGameBackground(int[][] board) {
        for (int row = HIDDEN_ROWS; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                displayMatrix[row][col].setFill(getFillColor(board[row][col]));
            }
        }
    }

    /**
     * Refreshes the visual representation of the falling brick and its ghost piece.
     * This involves clearing old brick/ghost nodes and redrawing them based on the provided {@link ViewData}.
     *
     * @param brick The {@link ViewData} containing information about the current falling brick and ghost.
     */
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
                    if (y >= 0) { // Only add to display if it's not in the hidden rows
                        gamePanel.add(r, x, y);
                    }
                }
            }
        }
    }

    /**
     * Clears all ghost pieces from the game panel.
     */
    private void clearGhost() {
        gamePanel.getChildren().removeIf(node -> node.getStyleClass().contains("ghost"));
    }

    /**
     * Draws the ghost piece on the game panel based on the provided {@link ViewData}.
     * The ghost piece indicates where the current falling brick would land if instantly dropped.
     *
     * @param view The {@link ViewData} containing ghost piece information.
     */
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
                ghost.getStyleClass().add("ghost"); // Add a style class for easy identification and removal
                ghost.setStroke(Color.BLACK);
                ghost.setStrokeWidth(0.25);
                ghost.setStrokeType(StrokeType.INSIDE);

                // Adjust for hidden rows
                gamePanel.add(ghost, x + col, (y - HIDDEN_ROWS) + row);
            }
        }
    }

    /**
     * Returns the appropriate fill color for a block based on its integer value.
     * Different integer values correspond to different brick types/colors.
     *
     * @param i The integer value representing the block type.
     * @return The JavaFX {@link Paint} object for the block's color.
     */
    private Paint getFillColor(int i) {
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

    /**
     * Returns the fill color for the ghost piece.
     * @param value The integer value representing the block type (used to determine if it's a solid part).
     * @return The JavaFX {@link Paint} object for the ghost piece's color (semi-transparent gray).
     */
    private Paint getGhostColor(int value) {
        if (value == 0) return Color.TRANSPARENT;
        return Color.rgb(200, 200, 200, 0.3); // Semi-transparent gray
    }
}

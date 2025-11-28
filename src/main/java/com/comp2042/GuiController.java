package com.comp2042;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.util.Duration;
import java.util.List;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;


import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    // =========================================
    // CONSTANTS (Refactoring)
    // =========================================
    private static final int BRICK_SIZE = 20;
    private static final int PREVIEW_BRICK_SIZE = 12;
    private static final int HIDDEN_ROWS = 2;
    private static final int BRICK_Y_OFFSET = 180;
    private static final int X_FINE_TUNE_OFFSET = 12;
    private static final int PREVIEW_PIXEL_OFFSET = 6;
    private int level = 1;
    private int totalLinesCleared = 0;
    private static final int LINES_PER_LEVEL = 5;
    private static final int BOARD_OFFSET = 6;


    private double gridXBase = 0;

    // =========================================
    // FXML COMPONENTS
    // =========================================
    @FXML private GridPane gamePanel;
    //@FXML private GridPane groupNotification;
    @FXML private StackPane groupNotification;
    @FXML private GameOverPanel gameOverPanel;
    @FXML private javafx.scene.control.Label scoreLabel;
    @FXML private javafx.scene.control.Button pauseButton;
    @FXML private javafx.scene.control.Label levelLabel;

    // to show the next piece
    @FXML
    private GridPane nextGrid;


    @FXML
    private Parent viewRoot;

    @FXML
    private void backToMainMenu() {
        // This calls the method we added in Step 2 on the listener (GameController)
        if (eventListener != null) {
            eventListener.onBackToMenuEvent();
        }
    }

    // =========================================
    // STATE
    // =========================================
    private Rectangle[][] displayMatrix;
    private final List<Rectangle> fallingBrickNodes = new ArrayList<>();

    private InputEventListener eventListener;
    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();


    private static final int NEXT_GRID_SIZE = 4;

    private Rectangle[][] nextCells = new Rectangle[NEXT_GRID_SIZE][NEXT_GRID_SIZE];







    // =========================================
    // INITIALIZATION
    // =========================================
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);

        // Remove gaps to ensure tight grid
        gamePanel.setHgap(0);
        gamePanel.setVgap(0);

        // --- NEW LOGIC START ---

        // 1. Force the falling brick layer to start at Top-Left (0,0) instead of Center
        StackPane.setAlignment(gamePanel, Pos.CENTER);

        // 2. Create a "Clip" (Mask)
        // This ensures that if a piece is at Y=-2 (spawning), it is hidden
        // and doesn't draw over the Score label.
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(gamePanel.widthProperty());
        clip.heightProperty().bind(gamePanel.heightProperty());
        gamePanel.setClip(clip);

        // --- NEW LOGIC END ---

        initNextGrid(nextGrid, nextCells);

        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::handleKeyPress);

        pauseButton.setFocusTraversable(false);
        pauseButton.setMnemonicParsing(false);

        gameOverPanel.setVisible(false);

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }


    // =========================================
    // KEY INPUT HANDLER
    // =========================================
    private void handleKeyPress(KeyEvent keyEvent) {

        if (!isPause.getValue() && !isGameOver.getValue()) {

            switch (keyEvent.getCode()) {

                case LEFT:
                case A:
                    refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                    return;

                case RIGHT:
                case D:
                    refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                    return;

                case UP:
                case W:
                    refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                    return;

                case DOWN:
                case S:
                    moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                    return;

                case SPACE:
                    hardDrop(new MoveEvent(EventType.HARD_DROP, EventSource.USER));
                    return;
            }
        }

        if (keyEvent.getCode() == KeyCode.N) {
            newGame(null);
        }
    }


    // =========================================
    // INITIAL BOARD CONSTRUCTION
    // =========================================
    public void initGameView(int[][] boardMatrix, ViewData brick) {

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

        


        

        timeLine = new Timeline(new KeyFrame(Duration.millis(400),
                e -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();

        updatePreviews(brick);

    }


    // =========================================
    
    // =========================================
// NEXT PIECE GRIDS INITIALISATION
// =========================================
    private void initNextGrid(GridPane grid, Rectangle[][] cells) {
        if (grid == null) {
            return; // safety if FXML not wired yet
        }

        grid.setHgap(0);
        grid.setVgap(0);
        grid.getChildren().clear();
        grid.setPrefWidth(NEXT_GRID_SIZE * PREVIEW_BRICK_SIZE);
        grid.setPrefHeight(NEXT_GRID_SIZE * PREVIEW_BRICK_SIZE);
        for (int row = 0; row < NEXT_GRID_SIZE; row++) {
            for (int col = 0; col < NEXT_GRID_SIZE; col++) {
                Rectangle r = new Rectangle(PREVIEW_BRICK_SIZE, PREVIEW_BRICK_SIZE);
                r.setFill(Color.TRANSPARENT);

                // start fully invisible so empty cells don’t show grid lines
                r.setStroke(Color.BLACK);
                r.setStrokeWidth(1);
                r.setStrokeType(StrokeType.INSIDE);

                cells[row][col] = r;
                grid.add(r, col, row);
            }
        }
    }
    




    // =========================================
    // COLORS
    // =========================================
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
        return Color.rgb(200, 200, 200, 0.3); // light transparent grey
    }


    // =========================================
    // REFRESH BACKGROUND GRID
    // =========================================
    public void refreshGameBackground(int[][] board) {
        for (int row = HIDDEN_ROWS; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                displayMatrix[row][col].setFill(getFillColor(board[row][col]));
            }
        }
    }


    // =========================================
    // SHOW GHOST + REAL BRICKS
    // =========================================
    private void refreshBrick(ViewData brick) {
        if (isPause.getValue()) return;

        // Clear the old ghost and falling brick nodes
        clearGhost();
        gamePanel.getChildren().removeAll(fallingBrickNodes);
        fallingBrickNodes.clear();

        // Draw the new ghost piece
        drawGhost(brick);

        // Draw the new falling brick
        int[][] brickData = brick.getBrickData();
        for (int row = 0; row < brickData.length; row++) {
            for (int col = 0; col < brickData[row].length; col++) {
                if (brickData[row][col] != 0) {
                    Rectangle r = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                    r.setFill(getFillColor(brickData[row][col]));
                    r.setStroke(Color.BLACK);
                    r.setStrokeWidth(0.25);
                    r.setStrokeType(StrokeType.INSIDE);

                    // Add to our tracking list
                    fallingBrickNodes.add(r);

                    // Add to the grid pane at the correct logical position, only if visible
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



    // NEXT PIECES PREVIEW (3 boxes)
// =========================================
    private void updatePreviews(ViewData brick) {
        drawPreview(nextGrid, brick.getNextBrickData1());

    }
    private void drawPreview(GridPane panel, int[][] data) {
    if (panel == null) return;

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




    // =========================================
    // DROP HELPERS
    // =========================================
    private void handleDropResult(DownData data) {

        if (data.getClearRow() != null && data.getClearRow().getLinesRemoved() > 0) {
            int linesRemoved = data.getClearRow().getLinesRemoved();
            totalLinesCleared += linesRemoved;

            // Show score bonus notification first
            NotificationPanel np = new NotificationPanel("+" + data.getClearRow().getScoreBonus());
            groupNotification.getChildren().add(np);
            np.showScore(groupNotification.getChildren());

            // Check if we should level up
            int newLevel = (totalLinesCleared / LINES_PER_LEVEL) + 1;
            if (newLevel > level) {
                level = newLevel;
                levelLabel.setText(String.valueOf(level));
                updateGameSpeed(); // Speed up the game!

                // Show level up notification with a delay to avoid collision
                Timeline levelUpDelay = new Timeline(new KeyFrame(Duration.millis(500), e -> {
                    NotificationPanel levelUp = new NotificationPanel("LEVEL " + level + "!");
                    groupNotification.getChildren().add(levelUp);
                    levelUp.showScore(groupNotification.getChildren());
                }));
                levelUpDelay.play();
            }
        }

        refreshBrick(data.getViewData());
        updatePreviews(data.getViewData());
        gamePanel.requestFocus();
    }


    private void moveDown(MoveEvent event) {
        if (!isPause.getValue()) {
            handleDropResult(eventListener.onDownEvent(event));
        }
    }

    private void hardDrop(MoveEvent event) {
        if (!isPause.getValue()) {
            handleDropResult(eventListener.onHardDropEvent(event));
        }
    }


    // =========================================
    // UI EVENT HOOKS
    // =========================================
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty scoreProp) {
        scoreLabel.textProperty().bind(Bindings.format("Score: %d", scoreProp));
    }


    // =========================================
    // GAME STATE CONTROLS
    // =========================================
    public void gameOver() {
        timeLine.stop();
        gameOverPanel.setVisible(true);
        isGameOver.setValue(true);
    }

    public void newGame(ActionEvent e) {
        timeLine.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        isPause.setValue(false);
        isGameOver.setValue(false);
        pauseButton.setText("Pause");

        // Reset level system
        level = 1;
        totalLinesCleared = 0;
        levelLabel.setText("Level: 1");

        gamePanel.requestFocus();
        updateGameSpeed(); // Start with level 1 speed
    }


    public void pauseGame(ActionEvent e) {
        if (isGameOver.getValue()) return;

        if (isPause.getValue()) {
            timeLine.play();
            pauseButton.setText("Pause");
        } else {
            timeLine.pause();
            pauseButton.setText("Resume");
        }

        isPause.setValue(!isPause.getValue());
        gamePanel.requestFocus();
    }

    public Parent getViewRoot() {
        return viewRoot; // This must not be null!
    }

    private int getDropSpeedForLevel() {
        // Start at 400ms, decrease by 40ms per level
        int baseSpeed = 400;
        int speedDecrease = 40;
        int minSpeed = 100;

        return Math.max(minSpeed, baseSpeed - (level - 1) * speedDecrease);
    }

    private void updateGameSpeed() {
        timeLine.stop();
        timeLine = new Timeline(new KeyFrame(Duration.millis(getDropSpeedForLevel()),
                e -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }
}
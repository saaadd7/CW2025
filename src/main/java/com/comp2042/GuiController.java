package com.comp2042;

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


    private double gridXBase = 0;

    // =========================================
    // FXML COMPONENTS
    // =========================================
    @FXML private GridPane gamePanel;
    @FXML private Group groupNotification;
    @FXML private GridPane brickPanel;
    @FXML private GameOverPanel gameOverPanel;
    @FXML private javafx.scene.control.Label scoreLabel;
    @FXML private javafx.scene.control.Button pauseButton;
    // to show the next 3 pieces
    @FXML
    private GridPane nextGrid1;
    @FXML
    private GridPane nextGrid2;
    @FXML
    private GridPane nextGrid3;

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
    private Rectangle[][] rectangles;

    private InputEventListener eventListener;
    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();


    private static final int NEXT_GRID_SIZE = 2;

    private Rectangle[][] nextCells1 = new Rectangle[NEXT_GRID_SIZE][NEXT_GRID_SIZE];
    private Rectangle[][] nextCells2 = new Rectangle[NEXT_GRID_SIZE][NEXT_GRID_SIZE];
    private Rectangle[][] nextCells3 = new Rectangle[NEXT_GRID_SIZE][NEXT_GRID_SIZE];







    // =========================================
    // INITIALIZATION
    // =========================================
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);





        // make sure there are NO gaps; grid lines come from strokes
        gamePanel.setHgap(0);
        gamePanel.setVgap(0);
        brickPanel.setHgap(0);
        brickPanel.setVgap(0);

        initNextGrid(nextGrid1, nextCells1);
        initNextGrid(nextGrid2, nextCells2);
        initNextGrid(nextGrid3, nextCells3);


        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::handleKeyPress);

        pauseButton.setFocusTraversable(false); // so that it doesnt accidently pause when hard drop is being done
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

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];

        for (int row = 0; row < brick.getBrickData().length; row++) {
            for (int col = 0; col < brick.getBrickData()[row].length; col++) {

                Rectangle r = new Rectangle(BRICK_SIZE, BRICK_SIZE);

                int value = brick.getBrickData()[row][col];
                if (value == 0) {
                    // empty cell: invisible, no outline
                    r.setFill(Color.TRANSPARENT);
                    r.setStroke(Color.BLACK);
                } else {
                    // part of the tetromino
                    r.setFill(getFillColor(value));
                    r.setStroke(Color.BLACK);
                }

                r.setStrokeWidth(0.25);
                r.setStrokeType(StrokeType.INSIDE);

                rectangles[row][col] = r;
                brickPanel.add(r, col, row);
            }
        }
        gridXBase = gamePanel.getLayoutX();
        if (gridXBase < 1.0) {
            // 180 is the screen X coordinate where the first column (X=0) of the grid should start.
            gridXBase = 180.0;
        }


        updateBrickPanelPosition(brick);

        timeLine = new Timeline(new KeyFrame(Duration.millis(400),
                e -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();

        updatePreviews(brick);

    }


    // =========================================
    // UPDATE FALLING BRICK POSITION
    private void updateBrickPanelPosition(ViewData brick) {

        // X: Use the corrected gridXBase + logical X position * size
        brickPanel.setLayoutX(
                gridXBase
                        + brick.getxPosition() * BRICK_SIZE+ X_FINE_TUNE_OFFSET
        );

        // Y: This is the correct logical Y calculation (already fixed)
        brickPanel.setLayoutY(
                gamePanel.getLayoutY()
                        + (brick.getyPosition() - HIDDEN_ROWS) * BRICK_SIZE
                        + BRICK_Y_OFFSET
        );
    }


    // =========================================
// NEXT PIECE GRID INITIALISATION
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
        grid.setPrefWidth(NEXT_GRID_SIZE);
        grid.setPrefHeight(NEXT_GRID_SIZE);

        for (int row = 0; row < NEXT_GRID_SIZE; row++) {
            for (int col = 0; col < NEXT_GRID_SIZE; col++) {
                Rectangle r = new Rectangle(BRICK_SIZE, BRICK_SIZE);
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
    private void clearNextGrid(Rectangle[][] cells) {
        for (int row = 0; row < NEXT_GRID_SIZE; row++) {
            for (int col = 0; col < NEXT_GRID_SIZE; col++) {
                if (cells[row][col] != null) {
                    cells[row][col].setFill(Color.TRANSPARENT);
                    cells[row][col].setStroke(Color.TRANSPARENT);
                }
            }
        }
    }

    private void drawNextPiece(int[][] data, Rectangle[][] cells) {
        clearNextGrid(cells);
        if (data == null) return;

        for (int row = 0; row < NEXT_GRID_SIZE; row++) {
            for (int col = 0; col < NEXT_GRID_SIZE; col++) {
                int value = 0;
                if (row < data.length && col < data[row].length) {
                    value = data[row][col];
                }

                if (value == 0) {
                    // empty: invisible
                    cells[row][col].setFill(Color.TRANSPARENT);
                    cells[row][col].setStroke(Color.TRANSPARENT);
                } else {
                    // part of the piece
                    cells[row][col].setFill(getFillColor(value));
                    cells[row][col].setStroke(Color.BLACK);


                    cells[row][col].setStrokeWidth(0.5);
                    cells[row][col].setStrokeType(StrokeType.INSIDE);
                }
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

        clearGhost();
        drawGhost(brick);
        updateBrickPanelPosition(brick);

        for (int row = 0; row < brick.getBrickData().length; row++) {
            for (int col = 0; col < brick.getBrickData()[row].length; col++) {
                int value = brick.getBrickData()[row][col];

                if (value == 0) {
                    // empty: no fill, no outline
                    rectangles[row][col].setFill(Color.TRANSPARENT);
                    rectangles[row][col].setStroke(Color.TRANSPARENT);
                } else {
                    // block: fill + black outline
                    rectangles[row][col].setFill(getFillColor(value));
                    rectangles[row][col].setStroke(Color.BLACK);
                }

                // keep geometry consistent
                rectangles[row][col].setStrokeWidth(0.25);
                rectangles[row][col].setStrokeType(StrokeType.INSIDE);
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
        drawPreview(nextGrid1, brick.getNextBrickData1());
        drawPreview(nextGrid2, brick.getNextBrickData2());
        drawPreview(nextGrid3, brick.getNextBrickData3());
    }
    private void drawPreview(GridPane panel, int[][] data) {
        if (panel == null || data == null) return;
        panel.getChildren().clear();


        int rows = data.length;
        int cols = rows > 0 ? data[0].length : 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                Rectangle r = new Rectangle(PREVIEW_BRICK_SIZE, PREVIEW_BRICK_SIZE);
                r.setFill(getFillColor(data[i][j]));
                r.setArcHeight(6);
                r.setArcWidth(6);

                // Apply the pixel offset to shift the block right and down by 6 pixels
                r.setTranslateX(PREVIEW_PIXEL_OFFSET); // Shift right by 6 pixels
                r.setTranslateY(PREVIEW_PIXEL_OFFSET); // Shift down by 6 pixels

                // Add the block starting at (0, 0) of the grid, but the translate shifts the pixels
                panel.add(r, j, i); // <--- CHANGE: Only use j and i as grid indices
            }
        }
    }




    // =========================================
    // DROP HELPERS
    // =========================================
    private void handleDropResult(DownData data) {

        if (data.getClearRow() != null && data.getClearRow().getLinesRemoved() > 0) {
            NotificationPanel np = new NotificationPanel("+" + data.getClearRow().getScoreBonus());
            groupNotification.getChildren().add(np);
            np.showScore(groupNotification.getChildren());
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
        gamePanel.requestFocus();
        timeLine.play();
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
}
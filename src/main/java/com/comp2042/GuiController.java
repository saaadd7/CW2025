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

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    // =========================================
    // CONSTANTS (Refactoring)
    // =========================================
    private static final int BRICK_SIZE = 20;
    private static final int HIDDEN_ROWS = 2;
    private static final int BRICK_Y_OFFSET = -42;

    // =========================================
    // FXML COMPONENTS
    // =========================================
    @FXML private GridPane gamePanel;
    @FXML private Group groupNotification;
    @FXML private GridPane brickPanel;
    @FXML private GameOverPanel gameOverPanel;
    @FXML private javafx.scene.control.Label scoreLabel;
    @FXML private javafx.scene.control.Button pauseButton;
    @FXML private GridPane nextGrid; // to show the next piece

    // =========================================
    // STATE
    // =========================================
    private Rectangle[][] displayMatrix;
    private Rectangle[][] rectangles;

    private InputEventListener eventListener;
    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();


    private static final int NEXT_GRID_SIZE = 4;
    private static final int TILE_SIZE = 20;

    private Rectangle[][] nextCells = new Rectangle[NEXT_GRID_SIZE][NEXT_GRID_SIZE];


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

        initNextGrid();

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
                    r.setStroke(Color.TRANSPARENT);
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


        updateBrickPanelPosition(brick);

        timeLine = new Timeline(new KeyFrame(Duration.millis(400),
                e -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();

        refreshNextBrick(brick);

    }


    // =========================================
    // UPDATE FALLING BRICK POSITION
    // =========================================
    private void updateBrickPanelPosition(ViewData brick) {

        brickPanel.setLayoutX(
                gamePanel.getLayoutX()
                        + brick.getxPosition() * brickPanel.getVgap()
                        + brick.getxPosition() * BRICK_SIZE
        );

        brickPanel.setLayoutY(
                BRICK_Y_OFFSET
                        + gamePanel.getLayoutY()
                        + brick.getyPosition() * brickPanel.getHgap()
                        + brick.getyPosition() * BRICK_SIZE
        );
    }


    // =========================================
// NEXT PIECE GRID INITIALISATION
// =========================================
    private void initNextGrid() {
        if (nextGrid == null) {
            return; // safety if FXML not wired yet
        }

        nextGrid.setHgap(0);
        nextGrid.setVgap(0);
        nextGrid.getChildren().clear();
        nextGrid.setPrefWidth(BRICK_SIZE * NEXT_GRID_SIZE);
        nextGrid.setPrefHeight(BRICK_SIZE * NEXT_GRID_SIZE);

        for (int row = 0; row < NEXT_GRID_SIZE; row++) {
            for (int col = 0; col < NEXT_GRID_SIZE; col++) {
                Rectangle r = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                r.setFill(Color.TRANSPARENT);
                r.setStroke(Color.TRANSPARENT);
                r.setStrokeWidth(0.5);
                r.setStrokeType(StrokeType.INSIDE);

                nextCells[row][col] = r;
                nextGrid.add(r, col, row); // (col, row) in GridPane
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

    // =========================================
// NEXT PIECE PREVIEW
// =========================================
    private void refreshNextBrick(ViewData viewData) {
        if (nextGrid == null) return;

        int[][] nextData = viewData.getNextBrickData(); // uses ViewData's next piece

        if (nextData == null) {
            clearNextGrid();
            return;
        }

        for (int row = 0; row < NEXT_GRID_SIZE; row++) {
            for (int col = 0; col < NEXT_GRID_SIZE; col++) {
                int value = 0;
                if (row < nextData.length && col < nextData[row].length) {
                    value = nextData[row][col];
                }

                if (value == 0) {
                    // empty cell: completely invisible (no fill, no outline)
                    nextCells[row][col].setFill(Color.TRANSPARENT);
                    nextCells[row][col].setStroke(Color.TRANSPARENT);
                } else {
                    // part of the piece
                    nextCells[row][col].setFill(getFillColor(value));
                    nextCells[row][col].setStroke(Color.BLACK);
                }
                // geometry stays the same
                nextCells[row][col].setStrokeWidth(0.5);
                nextCells[row][col].setStrokeType(StrokeType.INSIDE);
            }
        }
    }

        private void clearNextGrid() {
        for (int row = 0; row < NEXT_GRID_SIZE; row++) {
            for (int col = 0; col < NEXT_GRID_SIZE; col++) {
                if (nextCells[row][col] != null) {
                    nextCells[row][col].setFill(Color.TRANSPARENT);
                }
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
        refreshNextBrick(data.getViewData());
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
}

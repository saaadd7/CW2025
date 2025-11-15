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
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    // ======================
    // CONSTANTS (Refactoring)
    // ======================
    private static final int BRICK_SIZE = 20;

    // Number of invisible rows above the visible grid
    private static final int HIDDEN_ROWS = 2;

    // Vertical offset to align falling brick visually
    private static final int BRICK_Y_OFFSET = -42;


    // ======================
    // FXML COMPONENTS
    // ======================
    @FXML private GridPane gamePanel;
    @FXML private Group groupNotification;
    @FXML private GridPane brickPanel;
    @FXML private GameOverPanel gameOverPanel;
    @FXML private javafx.scene.control.Label scoreLabel;
    @FXML private javafx.scene.control.Button pauseButton;


    // ======================
    // INTERNAL STATE
    // ======================
    private Rectangle[][] displayMatrix;
    private Rectangle[][] rectangles;

    private InputEventListener eventListener;
    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();



    // ======================
    // INITIALIZATION
    // ======================
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);

        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        gamePanel.setOnKeyPressed(this::handleKeyPress);

        gameOverPanel.setVisible(false);

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }



    // ======================
    // KEYBOARD HANDLER (Refactored)
    // ======================
    private void handleKeyPress(KeyEvent keyEvent) {

        if (!isPause.getValue() && !isGameOver.getValue()) {

            switch (keyEvent.getCode()) {

                case LEFT:
                case A:
                    refreshBrick(eventListener.onLeftEvent(
                            new MoveEvent(EventType.LEFT, EventSource.USER)));
                    keyEvent.consume();
                    return;

                case RIGHT:
                case D:
                    refreshBrick(eventListener.onRightEvent(
                            new MoveEvent(EventType.RIGHT, EventSource.USER)));
                    keyEvent.consume();
                    return;

                case UP:
                case W:
                    refreshBrick(eventListener.onRotateEvent(
                            new MoveEvent(EventType.ROTATE, EventSource.USER)));
                    keyEvent.consume();
                    return;

                case DOWN:
                case S:
                    moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                    keyEvent.consume();
                    return;

                // =====================
                // HARD DROP
                // =====================
                case SPACE:
                    hardDrop(new MoveEvent(EventType.HARD_DROP, EventSource.USER));
                    keyEvent.consume();
                    return;
            }
        }

        // NEW GAME (regardless of pause)
        if (keyEvent.getCode() == KeyCode.N) {
            newGame(null);
        }
    }



    // ======================
    // INITIAL GAME DRAW
    // ======================
    public void initGameView(int[][] boardMatrix, ViewData brick) {

        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];

        // ========== Refactored: replaced magic '2' with HIDDEN_ROWS ==========
        for (int row = HIDDEN_ROWS; row < boardMatrix.length; row++) {
            for (int col = 0; col < boardMatrix[row].length; col++) {

                Rectangle r = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                r.setFill(Color.TRANSPARENT);

                displayMatrix[row][col] = r;

                // Add to UI (subtract hidden rows)
                gamePanel.add(r, col, row - HIDDEN_ROWS);
            }
        }


        // ========== Active Brick Matrix ==========
        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];

        for (int row = 0; row < brick.getBrickData().length; row++) {
            for (int col = 0; col < brick.getBrickData()[row].length; col++) {

                Rectangle r = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                r.setFill(getFillColor(brick.getBrickData()[row][col]));

                rectangles[row][col] = r;
                brickPanel.add(r, col, row);
            }
        }

        updateBrickPanelPosition(brick);


        // Timeline for gravity (unchanged)
        timeLine = new Timeline(new KeyFrame(Duration.millis(400),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }



    // ======================
    // HELPER TO UPDATE BRICK POSITION (refactored)
    // ======================
    private void updateBrickPanelPosition(ViewData brick) {
        brickPanel.setLayoutX(
                gamePanel.getLayoutX() + brick.getxPosition() * BRICK_SIZE
        );

        brickPanel.setLayoutY(
                BRICK_Y_OFFSET
                        + gamePanel.getLayoutY()
                        + brick.getyPosition() * BRICK_SIZE
        );
    }



    // ======================
    // BRICK COLORING
    // ======================
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



    // ======================
    // REFRESH FALLING BRICK
    // ======================
    private void refreshBrick(ViewData brick) {
        if (!isPause.getValue()) {

            updateBrickPanelPosition(brick);

            for (int row = 0; row < brick.getBrickData().length; row++) {
                for (int col = 0; col < brick.getBrickData()[row].length; col++) {
                    setRectangleData(brick.getBrickData()[row][col], rectangles[row][col]);
                }
            }
        }
    }



    // ======================
    // REFRESH BACKGROUND GRID (refactored)
    // ======================
    public void refreshGameBackground(int[][] board) {

        for (int row = HIDDEN_ROWS; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                setRectangleData(board[row][col], displayMatrix[row][col]);
            }
        }
    }



    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }



    // ======================
    // NORMAL DROP
    // ======================
    private void moveDown(MoveEvent event) {

        if (!isPause.getValue()) {

            DownData data = eventListener.onDownEvent(event);

            if (data.getClearRow() != null && data.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel np =
                        new NotificationPanel("+" + data.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(np);
                np.showScore(groupNotification.getChildren());
            }

            refreshBrick(data.getViewData());
        }

        gamePanel.requestFocus();
    }



    // ======================
    // HARD DROP
    // ======================
    private void hardDrop(MoveEvent event) {

        if (!isPause.getValue()) {

            DownData data = eventListener.onHardDropEvent(event);

            if (data.getClearRow() != null && data.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel np =
                        new NotificationPanel("+" + data.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(np);
                np.showScore(groupNotification.getChildren());
            }

            refreshBrick(data.getViewData());
        }

        gamePanel.requestFocus();
    }



    // ======================
    // LINK CONTROLLER <-> GAME LOGIC
    // ======================
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }



    public void bindScore(IntegerProperty scoreProp) {
        scoreLabel.textProperty().bind(Bindings.format("Score: %d", scoreProp));
    }



    // ======================
    // GAME OVER / NEW GAME
    // ======================
    public void gameOver() {
        timeLine.stop();
        gameOverPanel.setVisible(true);
        isGameOver.setValue(true);
    }



    public void newGame(ActionEvent actionEvent) {

        timeLine.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();

        isPause.setValue(false);
        isGameOver.setValue(false);

        if (pauseButton != null)
            pauseButton.setText("Pause");

        gamePanel.requestFocus();
        timeLine.play();
    }



    // ======================
    // PAUSE / RESUME
    // ======================
    public void pauseGame(ActionEvent actionEvent) {

        if (isGameOver.getValue()) return;

        if (isPause.getValue()) {
            timeLine.play();
            isPause.setValue(false);
            pauseButton.setText("Pause");
        } else {
            timeLine.pause();
            isPause.setValue(true);
            pauseButton.setText("Resume");
        }

        gamePanel.requestFocus();
    }

}

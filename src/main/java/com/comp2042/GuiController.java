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

    // =========================================
    // STATE
    // =========================================
    private Rectangle[][] displayMatrix;
    private Rectangle[][] rectangles;

    private InputEventListener eventListener;
    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();


    // =========================================
    // INITIALIZATION
    // =========================================
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);

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

                displayMatrix[row][col] = r;

                gamePanel.add(r, col, row - HIDDEN_ROWS);
            }
        }

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

        timeLine = new Timeline(new KeyFrame(Duration.millis(400),
                e -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }


    // =========================================
    // UPDATE FALLING BRICK POSITION
    // =========================================
    private void updateBrickPanelPosition(ViewData brick) {
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(gamePanel.getLayoutY() + BRICK_Y_OFFSET + brick.getyPosition() * BRICK_SIZE);
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
                rectangles[row][col].setFill(getFillColor(brick.getBrickData()[row][col]));
            }
        }
    }


    private void clearGhost() {
        gamePanel.getChildren().removeIf(node -> node.getStyleClass().contains("ghost"));
    }

    private void drawGhost(ViewData view) {
        int[][] ghostData = view.getGhostData();

        // If ghost not set yet, do nothing
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

                gamePanel.add(ghost, x + col, (y - HIDDEN_ROWS) + row);
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

package com.comp2042.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SimpleBoardTest {

    // 1. Test Initialization
    @Test
    void testBoardInitialization() {
        // Arrange
        // (20 rows, 10 columns) based on your coordinate logic
        SimpleBoard board = new SimpleBoard(20, 10);

        // Assert
        assertNotNull(board.getBoardMatrix(), "Matrix should be created");
        assertEquals(20, board.getBoardMatrix().length, "Should have 20 rows (width)");
        assertEquals(10, board.getBoardMatrix()[0].length, "Should have 10 columns (height)");
        assertNotNull(board.getScore(), "Score object should be initialized");
    }

    // 2. Test Starting a New Game
    @Test
    void testNewGame() {
        SimpleBoard board = new SimpleBoard(20, 10);

        // Act
        board.newGame();

        // Assert
        assertNotNull(board.getViewData(), "ViewData should be generated");
        // We use .scoreProperty().get() because your Score class uses JavaFX properties
        assertEquals(0, board.getScore().scoreProperty().get(), "Score should be 0");
    }

    // 3. Test Movement Logic (Basic)
    @Test
    void testMoveBrickDown() {
        SimpleBoard board = new SimpleBoard(20, 10);
        board.newGame(); // Spawn a brick

        // Act
        boolean moved = board.moveBrickDown();

        // Assert
        // In an empty board, a new brick should always be able to move down
        assertTrue(moved, "Brick should move down successfully on an empty board");
    }

    // 4. Test Locking a Brick (Merge)
    @Test
    void testMergeBrickToBackground() {
        SimpleBoard board = new SimpleBoard(20, 10);
        board.newGame();

        // Act
        board.mergeBrickToBackground();

        // Assert
        boolean foundBlock = false;
        int[][] matrix = board.getBoardMatrix();

        for (int[] row : matrix) {
            for (int cell : row) {
                if (cell != 0) {
                    foundBlock = true;
                    break;
                }
            }
        }
        assertTrue(foundBlock, "Board matrix should contain blocks after merging");
    }

    // 5. Test Board Reset
    @Test
    void testBoardReset() {
        SimpleBoard board = new SimpleBoard(20, 10);
        board.newGame();

        // 1. Dirty the board manually
        // Note: Check if indices are within bounds (0-19, 0-9)
        board.getBoardMatrix()[19][0] = 1;

        // 2. Start a NEW game
        board.newGame();

        // 3. Assert the manual block is gone
        int[][] matrix = board.getBoardMatrix();
        assertEquals(0, matrix[19][0], "Board should be wiped clean on newGame()");
    }
}
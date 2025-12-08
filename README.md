# Tetris 

**Student Name:** Saad Amanulla
**Student ID:** 20713832

## GitHub
https://github.com/saaadd7/CW2025

## 1. Project Overview
This project involves the maintenance and extension of a legacy Tetris codebase. The objective was twofold:
1.  **Maintenance:** Refactor the existing code to improve architectural integrity, readability, and modularity without altering the core gameplay loop.
2.  **Extension:** Implement new features and assets to modernize the user experience and demonstrate technical proficiency.

## 2. Compilation Instructions
To compile and run the Tetris application, follow these steps:

1. Ensure you have the following prerequisites installed:
  - Java Development Kit (JDK) 23 or compatible version
  - Maven 3.6 or newer

2. Clone the repository:
   ```
   git clone https://github.com/saaadd7/CW2025
   cd CW2025
   ```

3. Compile the project using Maven:
   ```
   mvn clean compile
   ```

4. Run the application:
   ```
   mvn javafx:run
   ```

Alternatively, you can build an executable JAR file:
   ```
   mvn clean package
   java -jar target/CW2025-1.0-SNAPSHOT.jar
   ```


## Implemented and Working Properly
The following features have been successfully implemented and are functioning as expected:

1. **Core Tetris Gameplay**
   - Brick movement (left, right, down)
   - Brick rotation (with Wall Kick logic)
   - Smooth Soft Drop: Implemented Smooth Soft Drop
   - Hard drop functionality
   - **Ghost Piece:** A translucent outline indicating the current block's projected landing position.
   - Line clearing and scoring

2. **Progression & Mechanics**
   - **Next Piece Preview:** A dedicated UI panel displays the next incoming tetromino. I initially implemented a preview of the next 3 pieces, but I refined this to display only the single next piece to reduce visual clutter and improve the UI aesthetics.
   - **Leveling System:** Implemented a dynamic difficulty system where the game level increases for every **7 lines cleared**, progressively increasing the game speed.

3. **Visual Effects**
   - **Particle Animation:** A dynamic particle effect triggers whenever a row is cleared, providing satisfying visual feedback.
   - Refactored Game board rendering for smoother updates.
   - **Victory Screen:** A dynamic "VICTORY" overlay appears when completing Sprint or Ultra modes, utilizing parallel transitions for zoom and fade effects.

4. **Multiple Game Modes**
   - **Classic Mode:** Play until the board fills up.
   - **Sprint Mode:** Clear 30 lines as fast as possible.
   - **Ultra Mode:** Score as many points as possible in 2 minutes.

5. **User Interface**
   - Main menu with game mode selection.
   - Score display and Level tracking.
   - Game status information (Next Block panel).

6. **Audio System**
   - **Background Music:** A custom AI-generated track about "The Game of Tetris and Professor Tan" that loops during gameplay.
   - **Sound Effects:** Audio feedback for specific actions (brick landing, line clearing).

7. **Advanced Architecture**
   - **Command Pattern:** Encapsulated input handling.
   - **Factory Pattern:** Centralized brick creation.

## Implemented but Not Working Properly
The following features have been implemented but may have issues:


## Features Not Implemented
The following features were planned but have not been implemented:

1. **High Score Leaderboard**
   - A persistent leaderboard system to track top scores across sessions.
   - **Reason:** Prioritized the stability of the Refactored MVC architecture and the Command pattern implementation over file I/O operations.

2. **Multiplayer Mode**
   - A real-time versus mode allowing two players to compete simultaneously.
   - **Reason:** The complexity of implementing networking logic and state synchronization was deemed out of scope for this specific maintenance and extension assignment.

## New Java Classes
The following Java classes were introduced to extend the functionality of the original codebase:

### 1. Logic & Design Patterns
* **`BrickFactory.java`**: Implements the **Factory Pattern** to centralize `Brick` object creation.
* **`BrickRotator.java`**: Manages the rotation state and current shape of a Tetris brick.
* **`GameMode.java`**: Enum defining the game states (`CLASSIC`, `SPRINT`, `ULTRA`).
* **`Command.java` (Interface)**: Base interface for the **Command Pattern**.
* **`MoveLeftCommand.java`, `MoveRightCommand.java`, `RotateCommand.java`, `HardDropCommand.java`, `DropDownCommand.java`**: Concrete implementations of user inputs.

### 2. UI Refactoring (Separation of Concerns)
* **`GameFlowController.java`**: Manages high-level game states (Start, Pause, Game Over).
* **`GameBoardRenderer.java`**: Handles purely the drawing of the grid, blocks, and ghost piece.
* **`GameInfoPanelController.java`**: Controls the HUD (Score, Level, Next Block).
* **`MainMenuController.java`**: Manages the start screen and mode selection.
* **`GameModeController.java`**: Handles the game mode selection screen (Classic, Sprint, Ultra).
* **`SettingsController.java`**: Manages the settings screen.
* **`InputHandler.java`**: Routes raw keyboard events to the appropriate Commands.

### 3. Audio & Visuals
* **`SoundManager.java`**: Centralized audio loader and player.
* **`ParticleEffect.java`**: Manages the animation logic for row-clear effects.

## Modified Java Classes
The following classes from the original codebase were significantly modified:

1. **`GuiController.java`**
   - **Changes:** Stripped of rendering and game flow logic; now acts as a coordinator for the sub-controllers (`GameFlowController`, `GameBoardRenderer`, `GameInfoPanelController`).
   - **Reason:** The original class was a "God Object." Refactoring it into specialized controllers improves maintainability and separation of concerns.

2. **`SimpleBoard.java`**
   - **Changes:** Refactored internal logic for checking grid boundaries. Replaced hardcoded integer values ("magic numbers") with dynamic `width` and `height` constants.
   - **Reason:** To fix **Board Dimension Understanding** issues, ensuring collision detection works consistently regardless of the board size.

3. **`Main.java`**
   - **Changes:** Modified the `start()` method to load the `MainMenu` scene first instead of the game board.
   - **Reason:** Essential to support the "Game Mode" selection feature required for the extension.

4. **`GameController.java`**
   - **Changes:** Modified input handling logic to utilize the new `Command` objects instead of direct method calls.
   - **Reason:** Decouples input logic from game logic, allowing for easier key remapping and cleaner code.

## Unexpected Problems
During the development of this assignment, the following unexpected challenges were encountered:

1. **Gameplay Balancing (Hard Drop Scoring)**
   - **Problem:** I initially implemented bonus points (+1) for "Hard Drops." Playtesting revealed this was "spammy" and encouraged players to drop blocks instantly without strategy just to farm points.
   - **Resolution:** I removed the bonus points for hard drops to restore strategic balance.
   - **Learning:** I learned that mechanics which seem good on paper can negatively impact the "game feel" if they incentivize low-skill behaviors.

2. **UI Scalability & Hardcoded Coordinates**
   - **Problem:** The original game board relied on hardcoded X/Y coordinates, causing the UI to misalign if the window size changed.
   - **Resolution:** I refactored the layout logic to use JavaFX Layout Containers (`StackPane`, `BorderPane`) and relative positioning.
   - **Learning:** I learned the importance of "Resolution Independence" in UI design to ensure robustness across different environments.

3. **JavaFX Threading & Timers**
   - **Problem:** Updating the Timer Label directly from a standard `Timer` thread caused `NotOnFXApplicationThread` exceptions.
   - **Resolution:** I wrapped the UI updates inside `Platform.runLater()`.
   - **Learning:** Reinforced the rule that all JavaFX UI updates must happen on the Application Thread.
4. Soft Drop smoothness
   - **Problem:** relying on the operating system's KEY_PRESSED event caused the soft drop to be jerky (dependent on the OS key-repeat rate).
   - **Resolution:** Implemented a state-based system (isSoftDropping boolean) and a dedicated 50ms Timeline timer in GameFlowController.
   - **Learning:** Game loops should rely on internal timers (deltas) rather than raw input triggers for movement physics.

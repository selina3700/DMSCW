# COMP2042 CW - Tetris
### GitHub Link: https://github.com/selina3700/DMSCW

## Getting Started
This section will guide you through setting up and running the project on your local machine.

### **Prerequisites**

Ensure that you have the following installed:

* **Java Development Kit (JDK) 17 or Newer**
    
    You can check your version by running `java -version`


* **Maven**
  
    You can check your version by running `mvn -version`

### **1. Cloning the Repository**

Download the machine source code to your local computer using Git:

`git clone https://github.com/selina3700/DMSCW`

`cd DMSCW`

### **2. Building the Project**

This project uses **Maven** for dependency management and building. You can build the 
project using the included Maven Wrapper script:

`.\mvnw clean install`

This command removes any previous build artifacts and compiles the Java source files,
download necessary dependencies, run tests, and package the application into a runnable file.

### **3. Running the Application**

`mvn javafx:run`

This command executes the application without needing to manually locate or configure the final `.jar` file.

****
## Implemented and Working Properly
This section explains all the features that has been implemented into the game.
### Feature 1: Hard Drop
The hard drop feature provides a quick way to precisely place a falling brick.

**Functionality:** Hard drop instantly drops the current brick to the lowest possible position
on the board and locks it into place.

**Benefit:** Allows for fast, strategic play and immediate line clearing.

**Usage:** Press the **[SPACEBAR]**

### Feature 2: Ghost Piece
The Ghost Piece helps the player determine where the falling piece will land.

**Functionality:** Projects a translucent version of the falling brick on the board. The brick is rendered at the
exact position where the brick will settle if dropped.

**Benefit:** This improves the player's accuracy and speeds up the gameplay.

### Feature 3: Next Brick Preview
The Next Brick Preview allows the player to view the next incoming brick to plan their next steps.

**Functionality:** A small window or panel on the game screen displays the shape of the next piece in the sequence.

**Benefit:** Players can decide on the placement of the current piece based on the shape of the next piece.

### Feature 4: Hold Brick
The Hold Brick feature gives the player the ability to store a piece they don't immediately need and swap it for the 
next piece in the queue, adding a critical layer of strategy to the game.

**Functionality:** Stores a brick that the player doesn't want to use until it is swapped out with another brick.

**Benefit:** Allows players to remove an awkward brick from the game until it can be used at a better time.

### Feature 5: Level Progression/Speed Increase
**Functionality:** Level progression system for the game where the level increases after 10 lines are cleared. 
The speed also increases everytime the player levels up!

**Benefit:** Gradually increases the difficulty of the game, preventing the game from staying stagnant.

### Feature 6: BGM and SFX
**Functionality:** Implemented background music and auditory feedback for a better gaming experience.

**Benefit:** Auditory cues give the player immediate confirmation that a critical action (like clearing a line or 
placing a block) was successful as well as creating a more engaging and immersive gaming environment.

**Toggle Functionality:** Users can turn the BGM and SFX on or off via the "Options Menu".

### Feature 7: 7-Bag Randomization
**Functionality:** A system that guarantees a player receives each of the 7 pieces within a set of seven pieces.

**Benefit:** Keeps the randomization consistent and eliminates the chance of extreme good or bad luck sequences.
****
## Features Implemented but Not Working Properly
### Feature: Brick Spawn Location
**Description:** The game is designed to spawn new bricks at the top of the board, however, the piece doesn't always 
generate at the correct height. Particularly when starting a new game, the first piece appears 1 or 2 rows down from the
intended height.

**Impact:** Reduces the available time for the player to react to the piece, causing slight disadvantage at the start of 
the game.

****
## Features not Implemented
### Feature 1: High Score
**Description:** The game doesn't store the scores of the player and compares them to get the highest score.

**Goal:** Adds a competitive twist for players for wanting to beat the current high score.

### Feature 2: Combo System and Back-to-Back Bonuses
**Description:** The game doesn't currently track sequential line clears and award special bonuses for them.

**Goal:** Implement logic to award additional points for consecutive clears.

### Feature 3: Multiplayer Implementation
**Description:** The current game mode is single-player. Features such as a leaderboard can be implemented to encourage
competitive gameplay.

**Goal:** Develop a leaderboard that is updated whenever someone beats the current highest scorer.

### Feature 4: Resizing as Score Increases
**Description:** When the score exceeds a number of digits, it will transform to something like 12345... due to the lack 
of space.

**Goal:** Resize the layout accordingly to ensure that the entire score will always be properly displayed on the player's
screen.
****
## New Java Classes
### Handlers
| **Class Name**         | **Description**                                                                                                              |   
|------------------------|------------------------------------------------------------------------------------------------------------------------------|
| `HardDropHandler`      | Manages the instant downward movement of the brick and locking it at the bottom of the board when the [SPACEBAR] is pressed. |
| `MoveDownHandler`      | Controls the standard downward soft drop vertical movement of the falling brick.                                             |
| `KeyboardInputHandler` | Maps key presses to game actions.                                                                                            |

### Managers
| **Class Name**     | **Description**                                                      |
|--------------------|----------------------------------------------------------------------|
| `BgmManager`       | Controls the playback, looping and volume of the background music.   |
| `GameStateManager` | Central class for tracking and switching between game states.        |
| `MenuManager`      | Manages which menu is currently being displayed and menu navigation. |

### Menu

| **Class Name**  | **Description**                                                                                           |
|-----------------|-----------------------------------------------------------------------------------------------------------|
| `ControlsMenu`  | Displays the controls of the game. (e.g. "N" starts a new game)                                           |
| `MainMenu`      | The entry page for the user, allowing the user to start the game, open the options menu or quit the game. |
| `OptionsMenu`   | Allows users to adjust the setting such as muting and unmuting the BGM and SFX.                           |
| `PauseMenu`     | The menu displayed when the game is paused.                                                               |

### Models

| **Class Name**        | **Description**                                                           |
|-----------------------|---------------------------------------------------------------------------|
| `ClearRow`            | Logic representing a completed row on the board that needs to be cleared. |
| `DownData`            | Handles data relating to the brick's downward movement.                   |
| `LevelSelectorButton` | Starting the game at different level speeds.                              |

### Rendering

| **Class Name**  | **Description**                                                                       |
|-----------------|---------------------------------------------------------------------------------------|
| `BrickPreview`  | Renders the visual display of the next piece in the queue and the hold brick.         |
| `BrickRenderer` | Renders the active falling brick and the static placed blocks on the main game board. |
| `GhostPiece`    | Renders the transparent outline of the piece's projected landing spot.                |

### Sounds

| **Class Name** | **Description**                                               |
|----------------|---------------------------------------------------------------|
| `BrickLandSFX` | Plays the sound effect when a falling brick locks into place. |
| `ButtonSFX`    | Plays the sound effect when a buttons are clicked.            |
| `ClearLineSFX` | Plays the sound effect when one or more lines are cleared.    |
****
## Modified Java Classes
### Controllers
**GameController**

- Level and Speed System Added: Introduced level progression, starting level selection, and automatic speed
scaling/levelling up based on cleared lines.
- Line Tracking: Added `LinesCleared` counter to track total cleared lines.
- Dynamic Game Speed Adjustments: Game speed increases when the player levels up, making the game faster.
- Hold Mechanic: Implemented `onHoldEvent()` to allow storing and swapping of bricks.

**GuiController**

- Refactored into a more modular architecture
- Added proper game systems
- Complete audio system added
- Better UI Organization

### Game
**MatrixOperations**

- Coordinate system and bound checking fixed

**SimpleBoard**

- Hold Piece System
- Ghost Piece Logic
- Adjusted Brick Spawn Position
- isValidPosition() Utility Method

### Menu
**GameOverPanel**

- Uses an image-based UI
- Added buttons for navigation such as restart, main menu, quit and options
- Layout is responsive

### Models
**ClearRow**

- Updated with a cleaner structure

**DownData**

- Improved game logic clarity by bundling movement state together with `ClearRow` and `ViewData`

**ViewData**

- Added support for held bricks and ghost piece

### Main
- Added static fields for stage, scene, controller, root, current level, and game controller.
- Scene updated
- Added restart to start a new game dynamically
- Startup logic updated
****
## Unexpected Problems
**Time Management**

Juggling the implementation schedule for this project while simultaneous deadlines for other assignments and club
commitments led to conflicts and stress.
- Solution: Clearly segment tasks to ensure dedicated focus on high-priority tasks.

**Grid Alignment**

Falling bricks didn't align with the background grid lines, causing visual gaps and slight overlaps as pieces moved.
- Solution: Standardizes the cell size across both the logic and rendering engine.
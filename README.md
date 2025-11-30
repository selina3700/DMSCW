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
## Implemented but Not Working Properly

## Features not Implemented

## New Java Classes

## Modified Java Classes

## Unexpected Problems
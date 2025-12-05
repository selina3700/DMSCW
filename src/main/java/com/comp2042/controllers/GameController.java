package com.comp2042.controllers;

import com.comp2042.events.EventSource;
import com.comp2042.events.InputEventListener;
import com.comp2042.events.MoveEvent;
import com.comp2042.game.Board;
import com.comp2042.game.SimpleBoard;
import com.comp2042.models.ClearRow;
import com.comp2042.models.DownData;
import com.comp2042.models.ViewData;

/**
 * This is the primary controller class for the TetrisJFX game
 * <p>
 *     Responsibilities include:
 *     <ul>
 *         <li>Game setup and initialization such as levels, speed and scores.</li>
 *         <li>Processes all the move events and applying game mechanics such as
 *         collision, merging and line clearing.</li>
 *         <li>Manages score, level advancement and game speed adjustments.</li>
 *     </ul>
 * </p>
 */
public class GameController implements InputEventListener {

    private int level;
    private final int startingLevel;
    private int LinesCleared = 0;
    private final Board board = new SimpleBoard(25, 10);
    private final GuiController viewGuiController;
    private static final double BASE_SPEED = 400;

    /**
     * Constructs a new GameController and initializes the game environment.
     * <p>
     *     Sets up initial board, level, speed and connects the controller as the listener
     *     for input events from the GUI.
     * </p>
     * @param c GUI controller used for rendering the view.
     * @param initialLevel Starting level of the game.
     */
    public GameController(GuiController c, int initialLevel) {
        viewGuiController = c;

        this.startingLevel = initialLevel;
        this.level = initialLevel;
        // Start game with an initial brick
        board.createNewBrick();

        viewGuiController.setEventListener(this);
        double initialSpeed = calculateSpeed(level);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData(), initialSpeed);

        // Bind score label to the score value
        viewGuiController.bindScore(board.getScore().scoreProperty());

        // Update GUI labels
        viewGuiController.setLevel(level);
        viewGuiController.setLinesCleared(LinesCleared);
    }

    /**
     * Calculates the brick falling speed in milliseconds based on the current level.
     * The speed increases as the levels increase.
     * @param currentLevel Current level of the game.
     * @return The delay time in milliseconds between automatic falling bricks.
     */

    private double calculateSpeed(int currentLevel) {
        return Math.max(100, BASE_SPEED - (currentLevel - 1) * 20);
    }

    private void increaseGameSpeed() {
        double newSpeed = Math.max(100, BASE_SPEED - (level - 1) * 20);
        viewGuiController.setGameSpeed(newSpeed);
    }

    /**
     * Handles the move down event of the bricks automatically or using user input.
     * <p>
     *     If the brick cannot move any further, it's merged into the background and if
     *     the bricks fill an entire row, the row is cleared and the score is updated. This
     *     also checks the Game Over condition.
     * </p>
     * @param event Triggers downward movement
     * @return {@code DownData} containing information about cleared rows, current view data,
     * and whether the brick is successfully moved.
     */

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;

        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();

            if (clearRow.getLinesRemoved() > 0) {
                int lines = clearRow.getLinesRemoved();
                LinesCleared += lines;
                viewGuiController.setLinesCleared(LinesCleared);

                if (LinesCleared / 10 >= level) {
                    level++;
                    increaseGameSpeed();
                    viewGuiController.setLevel(level);
                }
                board.getScore().add(clearRow.getScoreBonus());
            }

            if (board.createNewBrick()) {
                viewGuiController.gameOver();
            }
            viewGuiController.refreshGameBackground(board.getBoardMatrix());

        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
        }
        return new DownData(clearRow, board.getViewData(), canMove);
    }

    /**
     * Handles the left movement of the actively falling brick.
     * @param event Triggers the left movement.
     * @return Updates the view data after the move attempt.
     */
    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    /**
     * Handles the right movement of the actively falling brick.
     * @param event Triggers the right movement.
     * @return Updates the view data after the move attempt.
     */
    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    /**
     * Handles the rotation of the actively falling brick.
     * @param event Triggers the rotation.
     * @return Updates the view data after the move attempt.
     */
    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    /**
     * Resets the entire game state.
     * <p>
     *     Clears the board, resets the level, score and lines cleared count,
     *     updates the GUI and adjusts the game speed.
     * </p>
     */
    @Override
    public void createNewGame() {
        board.newGame();
        level = startingLevel;
        LinesCleared = 0;
        viewGuiController.setLevel(level);
        viewGuiController.setLinesCleared(LinesCleared);
        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        //Adjust Speed based on Level
        double resetSpeed = calculateSpeed(level);
        viewGuiController.setGameSpeed(resetSpeed);
    }

    /**
     * Places the current brick into the hold slot and swapping it
     * with any previously held brick.
     */
    @Override
    public void onHoldEvent() {
        board.holdBrick();
        viewGuiController.refreshBrick(board.getViewData());
    }
}
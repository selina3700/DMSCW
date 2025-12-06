package com.comp2042.game;

import com.comp2042.models.ClearRow;
import com.comp2042.models.Score;
import com.comp2042.models.ViewData;

/**
 * Defines the contract for the core game board model.
 * <p>
 * Any implementation of this interface is responsible for managing the game state,
 * including the board matrix, the falling brick's position, movement/rotation logic,
 * collision detection, line clearing, scoring, and new game initialization.
 * </p>
 */
public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean createNewBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    ClearRow clearRows();

    Score getScore();

    void newGame();

    void holdBrick();
}

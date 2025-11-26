package com.comp2042.game;

import com.comp2042.models.ClearRow;
import com.comp2042.models.Score;
import com.comp2042.models.ViewData;

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

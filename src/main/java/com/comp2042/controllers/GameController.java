package com.comp2042.controllers;

import com.comp2042.events.EventSource;
import com.comp2042.events.InputEventListener;
import com.comp2042.events.MoveEvent;
import com.comp2042.game.Board;
import com.comp2042.game.SimpleBoard;
import com.comp2042.models.ClearRow;
import com.comp2042.models.DownData;
import com.comp2042.models.ViewData;

public class GameController implements InputEventListener {

    private int level = 1;
    private int LinesCleared = 0;
    private Board board = new SimpleBoard(25, 10);
    private final GuiController viewGuiController;

    public GameController(GuiController c) {
        viewGuiController = c;

        // Start game with an initial brick
        board.createNewBrick();

        // Connect input handling
        viewGuiController.setEventListener(this);

        // Calculate initial speed based on starting level
        double initialSpeed = Math.max(100, 400 - (level - 1) * 20);

        // Initial board and brick with speed
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData(), initialSpeed);

        // Bind score label to the score value
        viewGuiController.bindScore(board.getScore().scoreProperty());

        // Update GUI labels
        viewGuiController.setLevel(level);
        viewGuiController.setLinesCleared(LinesCleared);

        System.out.println("Starting at Level " + level + " - Speed: " + initialSpeed + "ms");
    }

    private void increaseGameSpeed() {
        double newSpeed = Math.max(100, 800 - (level - 1) * 20);
        viewGuiController.setGameSpeed(newSpeed);
    }

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

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    @Override
    public void createNewGame() {
        board.newGame();
        level = 1;
        LinesCleared = 0;
        viewGuiController.setLevel(level);
        viewGuiController.setLinesCleared(LinesCleared);
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        viewGuiController.setGameSpeed(400);
    }

    @Override
    public void onHoldEvent() {
        board.holdBrick();
        viewGuiController.refreshBrick(board.getViewData());
    }
}
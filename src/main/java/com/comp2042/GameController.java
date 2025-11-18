package com.comp2042;

public class GameController implements InputEventListener {

    //Scoring and level system
    private int level = 1;
    private int LinesCleared = 0;
    //Main game board
    private Board board = new SimpleBoard(25, 10);

    //GUI controller that handles user interface and visuals
    private final GuiController viewGuiController;

    public GameController(GuiController c) {
        viewGuiController = c;
        //Start game with an initial brick
        board.createNewBrick();
        //Connect input handling
        viewGuiController.setEventListener(this);

        // Calculate initial speed based on starting level
        double initialSpeed = Math.max(100, 400 - (level - 1) * 20);

        //Initial board and brick with speed
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData(), initialSpeed);
        //Bind score label to the score value
        viewGuiController.bindScore(board.getScore().scoreProperty());

        // Update GUI labels
        viewGuiController.setLevel(level);
        viewGuiController.setLinesCleared(LinesCleared);

        System.out.println("Starting at Level " + level + " - Speed: " + initialSpeed + "ms");
    }

    //Increases the game speed everytime the level increases
    private void increaseGameSpeed() {
        double newSpeed = Math.max(100, 800 - (level - 1) * 20);
        viewGuiController.setGameSpeed(newSpeed);
    }

    // Add this public method to GameController
    public boolean canPlaceBrickAtPosition(int[][] brickData, int x, int y) {
        if (board instanceof SimpleBoard) {
            return ((SimpleBoard) board).isValidPosition(brickData, x, y);
        }
        return false;
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        //Move brick down
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;

        //Stop moving because it hit the bottom or another brick
        if (!canMove) {
            //Merge brick to the board background
            board.mergeBrickToBackground();

            //Check if there are any full rows
            clearRow = board.clearRows();

            //Add Score when rows are cleared
            if (clearRow.getLinesRemoved() > 0) {
                int lines = clearRow.getLinesRemoved();
                LinesCleared += lines;

                //Update line count
                viewGuiController.setLinesCleared(LinesCleared);

                //Increase level every 10 lines
                if (LinesCleared / 10 >= level) {
                    level++;
                    increaseGameSpeed();
                    viewGuiController.setLevel(level); // ✅ Update GUI label
                }
                board.getScore().add(clearRow.getScoreBonus());
            }


            //If new brick cannot be created, board is full.
            if (board.createNewBrick()) {
                viewGuiController.gameOver();
            }
            //Update the GUI background after the brick merges and rows are cleared
            viewGuiController.refreshGameBackground(board.getBoardMatrix());

        } else {
            //Points increase by 1 when players click on down arrow
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
        //Reset level and lines cleared
        level = 1;
        LinesCleared = 0;
        //Update GUI
        viewGuiController.setLevel(level);
        viewGuiController.setLinesCleared(LinesCleared);
        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        viewGuiController.setGameSpeed(400);
    }
}
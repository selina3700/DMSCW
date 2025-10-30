package com.comp2042;

public class GameController implements InputEventListener {

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
        //Initial board and brick
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        //Bind score label to the score value
        viewGuiController.bindScore(board.getScore().scoreProperty());
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
        return new DownData(clearRow, board.getViewData());
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
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }
}

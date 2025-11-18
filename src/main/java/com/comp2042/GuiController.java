package com.comp2042;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    //Tetris block size (20 px)
    private static final int BRICK_SIZE = 22;

    @FXML private GridPane gamePanel;
    @FXML private Group groupNotification;
    @FXML private GridPane brickPanel;
    @FXML private GameOverPanel gameOverPanel;
    @FXML private Labeled scoreLabel;
    @FXML private Labeled levelLabel;
    @FXML private Labeled linesLabel;
    @FXML private Pane buttonGroup;
    @FXML private VBox nextBrickContainer;
    @FXML private Label nextBrickLabel;
    @FXML private GridPane nextBrickPanel;
    @FXML private StackPane pauseMenu;
    @FXML private StackPane MainMenu;
    @FXML private Pane originalGameView;


    //Visual layout of board and bricks
    private Rectangle[][] displayMatrix;
    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    //Game Loop
    private Timeline timeLine;

    //Game State
    private final BooleanProperty isPause = new SimpleBooleanProperty();
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    //BGM
    private MediaPlayer bgmPlayer;

    //Outline
    private Color getDarker;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Font.loadFont(getClass().getResourceAsStream("/PixelifySans.ttf"), 38);

        //Receive keyboard input & ensure grids have no gaps
        gamePanel.setPadding(Insets.EMPTY);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        brickPanel.setPadding(Insets.EMPTY);
        brickPanel.setManaged(false);
        brickPanel.setMouseTransparent(true);
        brickPanel.setPickOnBounds(false);
        brickPanel.setGridLinesVisible(false);

        //Key event handling
        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                //Only can move if the game is not paused or over
                if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                    if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                        refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                        refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                        refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.SPACE) {
                        hardDrop();
                        keyEvent.consume();
                    }
                }

                //Press "N" to start a new game
                if (keyEvent.getCode() == KeyCode.N) {
                    newGame(null);
                }
            }
        });

        //Hide the game over panel at the start of the game
        gameOverPanel.setVisible(false);

        //Add a reflection effect for text or panels (visual)
        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);

        setupPauseButton();

        initializeBackgroundMusic();
    }

    public void initGameView(int[][] boardMatrix, ViewData brick, double initialSpeed) {
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStrokeWidth(0.3);
                rectangle.setStrokeType(StrokeType.INSIDE);
                rectangle.setStroke(Color.WHITE);
                gamePanel.add(rectangle, j, i - 2);
            }
        }
        //Background grid
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        //Currently falling brick (invisible grid until cells filled)
        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setArcHeight(0);
                rectangle.setArcWidth(0);
                rectangle.setStrokeType(StrokeType.INSIDE);

                int colorCode = brick.getBrickData()[i][j];

                if (colorCode == 0) {
                    // EMPTY PART OF FALLING BRICK: INVISIBLE
                    rectangle.setFill(Color.TRANSPARENT);
                    rectangle.setStroke(Color.TRANSPARENT); // <--- THIS REMOVES THE GRID BEHIND BRICK
                } else {
                    // ACTUAL BRICK BLOCK
                    Color base = (Color) getFillColor(colorCode);
                    rectangle.setFill(base);
                    rectangle.setStroke(base.darker());
                    rectangle.setStrokeWidth(1.0);
                }

                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }

        //Position the brick at the correct location on the board

        brickPanel.setLayoutX(gamePanel.getLayoutX() + (brick.getxPosition() * (BRICK_SIZE)));
        brickPanel.setLayoutY(gamePanel.getLayoutY() + ((brick.getyPosition()-1) * (BRICK_SIZE)));

        //Moves the brick down automatically with the specified speed
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(initialSpeed),  // Use the passed speed instead of hardcoded 400
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();

        generateNextBrickPreview(brick.getNextBrickData());
    }

    //Color for each brick piece
    private Paint getFillColor(int i) {
        Paint returnPaint;
        switch (i) {
            case 0:
                returnPaint = Color.TRANSPARENT;
                break;
            case 1:
                returnPaint = Color.web("#D24447");
                break;
            case 2:
                returnPaint = Color.web("#EB8C4D");
                break;
            case 3:
                returnPaint = Color.web("#D174FF");
                break;
            case 4:
                returnPaint = Color.web("#5AC8FB");
                break;
            case 5:
                returnPaint = Color.web("#FFEE57");
                break;
            case 6:
                returnPaint = Color.web("#74FF64");
                break;
            case 7:
                returnPaint = Color.web("#FA74FF");
                break;
            default:
                returnPaint = Color.web("#FFFFFF");
                break;
        }
        return returnPaint;
    }

    //Update the visual position and appearance of the falling brick
    private void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * BRICK_SIZE);
            brickPanel.setLayoutY(gamePanel.getLayoutY() + (brick.getyPosition()-2) * BRICK_SIZE);

            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }

            if (brick.getNextBrickData() != null) {
                generateNextBrickPreview(brick.getNextBrickData());
            }

        }
    }

    //Refreshes the entire game background
    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    //Apply color and rounded corners to the rectangles
    private void setRectangleData(int color, Rectangle rectangle) {
    Color base = (Color) getFillColor(color);
        rectangle.setFill(base);
        rectangle.setStroke(getDarker(base));
        rectangle.setStrokeWidth(1.2);
        rectangle.setStrokeType(StrokeType.INSIDE);
    }

    //Handles the action when a brick moves down
    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);

            //If row was cleared, show floating score
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }

            //Update brick position
            refreshBrick(downData.getViewData());
        }

        //Keep the game window focused so keys continue to work
        gamePanel.requestFocus();
    }

    //Connect GUI controller with the InputEventListener
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty scoreProperty) {
        Font font = Font.loadFont(getClass().getResourceAsStream("/digital.ttf"), 38);
        scoreLabel.getStyleClass().add("bindScoreStyle");
        scoreLabel.setFont(font);
        scoreLabel.textProperty().bind(scoreProperty.asString("Score: %d"));
    }

    //Update Level
    public void setLevel(int level){
        levelLabel.setText("Level: "+ level);
    }

    // Update Lines Cleared
    public void setLinesCleared(int lines) {
        linesLabel.setText("Lines: " + lines);
    }

    //Displays game over screen and stop brick movement
    public void gameOver() {
        timeLine.stop();
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
        if (bgmPlayer != null) bgmPlayer.stop();
    }

    //Drops the brick immediately
    private void hardDrop() {
        if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
            boolean canMoveDown = true;
            MoveEvent downEvent = new MoveEvent(EventType.DOWN, EventSource.USER);
            DownData downData = null;

            // Drop until the brick can't move further
            while (canMoveDown) {
                downData = eventListener.onDownEvent(downEvent);
                if (!downData.isMoved()) {
                    canMoveDown = false;
                }
            }

            // Update the view one last time (locked position)
            if (downData != null) {
                refreshBrick(downData.getViewData());

                // Handle row clearing animation if needed
                if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                    NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                    groupNotification.getChildren().add(notificationPanel);
                    notificationPanel.showScore(groupNotification.getChildren());
                }
            }

            // Keep focus so keys continue to work
            gamePanel.requestFocus();
        }
    }

    //Reset the board and start a new game
    public void newGame(ActionEvent actionEvent) {
        timeLine.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        timeLine.play();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);

        if (bgmPlayer != null) {
            bgmPlayer.stop();
            bgmPlayer.play();
        }

    }

    //Pause the game
    public void pauseGame(ActionEvent actionEvent) {
        if (isPause.get()) {
            resumeGame();
            hidePauseMenu();
        } else {
            timeLine.pause();
            isPause.set(true);
            showPauseMenu();
            if (bgmPlayer != null) bgmPlayer.pause();
        }

        gamePanel.requestFocus();
    }

    private void setupPauseButton() {
        ButtonSFX pauseView = new ButtonSFX("/images/Pause_Button.png", "/images/Pause_After.png");
        pauseView.setFitWidth(55);
        pauseView.setFitHeight(55);

        pauseView.setLayoutX(5);
        pauseView.setLayoutY(10);
        pauseView.setOnMouseClicked(event -> pauseGame(null));

        //Display
        buttonGroup.getChildren().add(pauseView);
    }

    public void setGameSpeed(double newSpeed) {
        if (timeLine != null) {
            timeLine.stop();
            timeLine.getKeyFrames().setAll(
                    new KeyFrame(Duration.millis(newSpeed),
                            ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD)))
            );
            timeLine.play();
        }
    }

    //Brick Outline
    private Color getDarker(Color color) {
        return color.deriveColor(0, 1, 0.55, 1);
    }

    private void generateNextBrickPreview(int[][] nextBrickData) {
        nextBrickPanel.getChildren().clear();
        nextBrickPanel.getColumnConstraints().clear();
        nextBrickPanel.getRowConstraints().clear();

        int brickRows = nextBrickData.length;
        int brickCols = nextBrickData[0].length;

        int previewRows = (int) (nextBrickPanel.getPrefHeight() / BRICK_SIZE);
        int previewCols = (int) (nextBrickPanel.getPrefWidth() / BRICK_SIZE);

        // Offsets to center the brick
        int rowOffset = (previewRows - brickRows) / 2;
        int colOffset = (previewCols - brickCols) / 2;

        for (int i = 0; i < brickRows; i++) {
            for (int j = 0; j < brickCols; j++) {
                if (nextBrickData[i][j] != 0) {
                    Rectangle rect = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                    rect.setArcWidth(0);
                    rect.setArcHeight(0);

                    // Fill color
                    Color base = (Color) getFillColor(nextBrickData[i][j]);
                    rect.setFill(base);

                    // Outline color (darker)
                    rect.setStroke(getDarker(base));
                    rect.setStrokeWidth(1.0);
                    rect.setStrokeType(StrokeType.INSIDE);

                    // Add with offset so it's centered
                    nextBrickPanel.add(rect, j + colOffset, i + rowOffset);
                }
            }
        }
        nextBrickPanel.setVisible(true);
    }

    public void showPauseMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pauseMenu.fxml"));
            pauseMenu = loader.load();

            PauseMenu controller = loader.getController();
            controller.setGuiController(this);

            Pane root = (Pane) gamePanel.getScene().getRoot();

            pauseMenu.setPrefSize(root.getWidth(), root.getHeight());
            pauseMenu.prefWidthProperty().bind(root.widthProperty());
            pauseMenu.prefHeightProperty().bind(root.heightProperty());

            root.getChildren().add(pauseMenu);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMainMenu() {
        try {
            // Stop the game
            if (timeLine != null) {
                timeLine.stop();
            }

            // Hide pause menu if it's showing
            hidePauseMenu();

            // Load the main menu FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenu.fxml"));
            StackPane mainMenuPane = loader.load();

            // Set the controller
            MainMenu mainMenuController = loader.getController();
            mainMenuController.setPrimaryStage((Stage) gamePanel.getScene().getWindow());

            // Replace the scene root with main menu
            gamePanel.getScene().setRoot(mainMenuPane);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading main menu: " + e.getMessage());
        }
    }

    public void showOptionsMenu() {
        try {
            // Stop the game
            if (timeLine != null) {
                timeLine.stop();
            }

            // Hide pause menu if it's showing
            hidePauseMenu();

            // Load the main menu FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/optionsMenu.fxml"));
            StackPane mainMenuPane = loader.load();

            // Set the controller
            MainMenu mainMenuController = loader.getController();
            mainMenuController.setPrimaryStage((Stage) gamePanel.getScene().getWindow());

            // Replace the scene root with main menu
            gamePanel.getScene().setRoot(mainMenuPane);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading main menu: " + e.getMessage());
        }
    }

    public void hidePauseMenu() {
        if (pauseMenu != null) {
            ((Pane) gamePanel.getScene().getRoot()).getChildren().remove(pauseMenu);
            pauseMenu = null;
        }
    }
    public void hideMainMenu() {
        if (MainMenu != null) {
            ((Pane) gamePanel.getScene().getRoot()).getChildren().remove(MainMenu);
            MainMenu = null;
        }
    }

    public void resumeGame() {
        timeLine.play();
        isPause.set(false);
        if (bgmPlayer != null) bgmPlayer.play();
    }

    public GridPane getGamePanel() {
        return gamePanel;
    }


    //Background Music
    private void initializeBackgroundMusic() {
        try {
            Media sound = new Media(getClass().getResource("/sounds/bgm.mp3").toExternalForm());
            bgmPlayer = new MediaPlayer(sound);
            bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            bgmPlayer.setVolume(0.5);
            bgmPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading background music: " + e.getMessage());
        }
    }
}
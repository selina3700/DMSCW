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
import javafx.scene.media.AudioClip;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.stage.Window;
import javafx.scene.Scene;


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
    @FXML private GridPane holdBrickPanel;


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

    //SFX
    private AudioClip clearSoundPlayer;
    private ButtonSFX pauseButton;

    //Outline
    private Color getDarker;

    private Pane currentOptionsMenu;
    private Pane currentControlsMenu;

    private boolean isMusicMuted = false;
    private boolean isSFXMuted = false;
    private boolean isMainMenuOpen = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Font.loadFont(getClass().getResourceAsStream("/PixelifySans.ttf"), 38);
        Font font = Font.loadFont(getClass().getResourceAsStream("/PressStart.ttf"), 38);

        //Receive keyboard input & ensure grids have no gaps
        gamePanel.setPadding(Insets.EMPTY);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        brickPanel.setPadding(Insets.EMPTY);
        brickPanel.setManaged(false);
        brickPanel.setMouseTransparent(true);
        brickPanel.setPickOnBounds(false);
        brickPanel.setGridLinesVisible(false);

        Rectangle clipRect = new Rectangle();
        clipRect.widthProperty().bind(gamePanel.widthProperty());
        clipRect.heightProperty().bind(gamePanel.heightProperty());
        gamePanel.setClip(clipRect);

        //Clear row sfx
        try {
            String soundPath = getClass().getResource("/sounds/linecleared.mp3").toExternalForm();
            clearSoundPlayer = new AudioClip(soundPath);
            clearSoundPlayer.setVolume(0.3);
        } catch (Exception e) {
            System.out.println("Clear sound not found: " + e.getMessage());
        }

        //Key event handling
        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                //Only can move if the game is not paused or over
                if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                    if (keyEvent.getCode() == KeyCode.LEFT) {
                        refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.RIGHT) {
                        refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.UP) {
                        refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.DOWN) {
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.SPACE) {
                        hardDrop();
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.H) {
                        if (eventListener != null) {
                            eventListener.onHoldEvent();
                        }
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
                    rectangle.setStroke(Color.TRANSPARENT);
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
        brickPanel.setLayoutY(gamePanel.getLayoutY() + ((brick.getyPosition() - 2) * (BRICK_SIZE)));

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
    public void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * BRICK_SIZE);
            brickPanel.setLayoutY(gamePanel.getLayoutY() + (brick.getyPosition() - 2) * BRICK_SIZE);

            int boardWidth = 10;  // Number of columns in your board
            int boardHeight = 23; // Visible rows (25 - 2 hidden top rows)

            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    int cellX = brick.getxPosition() + j;
                    int cellY = brick.getyPosition() - 2 + i;

                    // Hide cells that are outside the visible board
                    if (cellX < 0 || cellX >= boardWidth || cellY < 0 || cellY >= boardHeight) {
                        rectangles[i][j].setFill(Color.TRANSPARENT);
                        rectangles[i][j].setStroke(Color.TRANSPARENT);
                    } else {
                        setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                    }
                }
            }

            if (brick.getNextBrickData() != null) {
                generateNextBrickPreview(brick.getNextBrickData());
            }

            if (brick.getHeldBrickData() != null) {
                generateHoldBrickPreview(brick.getHeldBrickData());
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
                if (clearSoundPlayer != null) {
                    clearSoundPlayer.play();
                }
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
        setPauseButtonEnabled(false);
        isGameOver.setValue(Boolean.TRUE);

        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }

        // Add gameOverPanel to root and make it fill the screen
        Pane root = (Pane) gamePanel.getScene().getRoot();

        if (!root.getChildren().contains(gameOverPanel)) {
            gameOverPanel.prefWidthProperty().bind(root.widthProperty());
            gameOverPanel.prefHeightProperty().bind(root.heightProperty());
            root.getChildren().add(gameOverPanel);
        }

        gameOverPanel.setGuiController(this);
        gameOverPanel.setVisible(true);
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
                    if (clearSoundPlayer != null) {
                        clearSoundPlayer.play();
                    }
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
        // 1. Swap Root back to GamePanel if needed
        if (gamePanel.getScene() == null && MainMenu != null && MainMenu.getScene() != null) {
            MainMenu.getScene().setRoot(gamePanel);
        }

        // 2. Reset state
        hideMainMenu();

        // 3. Standard reset logic
        timeLine.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        timeLine.play();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);

        // 4. RE-ENABLE the pause button
        setPauseButtonEnabled(true);

        // 5. Handle BGM - restart from beginning
        if (bgmPlayer != null) {
            bgmPlayer.stop();
            bgmPlayer.seek(bgmPlayer.getStartTime());
            bgmPlayer.play();
        }
    }

    //Pause the game
    public void pauseGame(ActionEvent actionEvent) {
        System.out.println("=== pauseGame() called, isPause=" + isPause.get() + " ===");

        if (isPause.get()) {
            // Already paused, so resume
            resumeGame();
            hidePauseMenu();
        } else {
            // Pause the game
            timeLine.pause();
            isPause.set(true);

            // Pause BGM
            if (bgmPlayer != null) {
                System.out.println("Pausing BGM, status BEFORE: " + bgmPlayer.getStatus());
                bgmPlayer.pause();
                System.out.println("BGM status AFTER pause: " + bgmPlayer.getStatus());
            } else {
                System.out.println("ERROR: bgmPlayer is NULL!");
            }

            showPauseMenu();
        }
        gamePanel.requestFocus();
    }

    private void setupPauseButton() {
        pauseButton = new ButtonSFX("/images/Pause_Button.png", "/images/Pause_After.png");
        pauseButton.setFitWidth(55);
        pauseButton.setFitHeight(55);

        pauseButton.setLayoutX(5);
        pauseButton.setLayoutY(10);

        // Modified: Check if game is over before allowing pause
        pauseButton.setOnMouseClicked(event -> {
            if (isGameOver.getValue() == Boolean.FALSE) {
                pauseGame(null);
            }
        });

        pauseButton.setSFXMuted(isSFXMuted);
        buttonGroup.getChildren().add(pauseButton);
    }

    public void setPauseButtonEnabled(boolean enabled) {
        if (pauseButton != null) {
            pauseButton.setDisable(!enabled);
            pauseButton.setOpacity(enabled ? 1.0 : 0.5);
        }
    }

    // Update the setSFXMute method to also update the pause button:
    public void setSFXMute(boolean mute) {
        this.isSFXMuted = mute;
        if (clearSoundPlayer != null) {
            clearSoundPlayer.setVolume(mute ? 0.0 : 0.3);
        }
        // Update pause button SFX state
        if (pauseButton != null) {
            pauseButton.setSFXMuted(mute);
        }
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

        int brickRows = nextBrickData.length;
        int brickCols = nextBrickData[0].length;

        // Calculate offsets to center the shape within the 4x4 grid
        int rowOffset = (4 - brickRows) / 2;
        int colOffset = (4 - brickCols) / 2;

        for (int i = 0; i < brickRows; i++) {
            for (int j = 0; j < brickCols; j++) {
                if (nextBrickData[i][j] != 0) {
                    Rectangle rect = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                    Color base = (Color) getFillColor(nextBrickData[i][j]);
                    rect.setFill(base);
                    rect.setStroke(getDarker(base));
                    rect.setStrokeWidth(1.0);
                    rect.setStrokeType(StrokeType.INSIDE);

                    // --- FIX: Center the rectangle inside the grid cell ---
                    GridPane.setHalignment(rect, HPos.CENTER);
                    GridPane.setValignment(rect, VPos.CENTER);
                    // ----------------------------------------------------

                    nextBrickPanel.add(rect, j + colOffset, i + rowOffset);
                }
            }
        }
    }

    private void generateHoldBrickPreview(int[][] heldBrickData) {
        if (holdBrickPanel == null) return;

        holdBrickPanel.getChildren().clear();

        int brickRows = heldBrickData.length;
        int brickCols = heldBrickData[0].length;

        int rowOffset = (4 - brickRows) / 2;
        int colOffset = (4 - brickCols) / 2;

        for (int i = 0; i < brickRows; i++) {
            for (int j = 0; j < brickCols; j++) {
                if (heldBrickData[i][j] != 0) {
                    Rectangle rect = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                    Color base = (Color) getFillColor(heldBrickData[i][j]);
                    rect.setFill(base);
                    rect.setStroke(getDarker(base));
                    rect.setStrokeWidth(1.0);
                    rect.setStrokeType(StrokeType.INSIDE);

                    // --- FIX: Center the rectangle inside the grid cell ---
                    GridPane.setHalignment(rect, HPos.CENTER);
                    GridPane.setValignment(rect, VPos.CENTER);
                    // ----------------------------------------------------

                    holdBrickPanel.add(rect, j + colOffset, i + rowOffset);
                }
            }
        }
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

            // BGM is already paused in pauseGame(), no need to pause again

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMainMenu() {
        try {
            if (timeLine != null) timeLine.stop();
            hidePauseMenu();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenu.fxml"));
            StackPane mainMenuPane = loader.load();

            MainMenu mainMenuController = loader.getController();
            mainMenuController.setPrimaryStage((Stage) gamePanel.getScene().getWindow());
            mainMenuController.setGuiController(this);

            gamePanel.getScene().setRoot(mainMenuPane);

            MainMenu = mainMenuPane;
            isMainMenuOpen = true;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading main menu: " + e.getMessage());
        }
    }


    public void hideMainMenu() {
        // Since MainMenu replaces the root, we don't "remove" it like a popup.
        // We just clear the reference.
        MainMenu = null;
        isMainMenuOpen = false;
    }

    // Getter for OptionsMenu to check
    public boolean isMainMenuOpen() {
        return isMainMenuOpen;
    }

    public void showOptionsMenu() {
        showOptionsMenu(null);
    }

    // 2. Specific method (used by Main Menu)
    public void showOptionsMenu(Pane specificRoot) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/optionsMenu.fxml"));
            Pane optionsPane = loader.load();

            OptionsMenu controller = loader.getController();
            controller.setGuiController(this);

            // DETERMINING THE PARENT ROOT
            javafx.scene.Parent rootParent = specificRoot;

            // If no specific root passed, try to find it automatically
            if (rootParent == null) {
                // A. Is Main Menu currently open? Use that reference.
                if (MainMenu != null && MainMenu.getScene() != null) {
                    rootParent = MainMenu;
                }
                // B. Try Game Panel
                else if (gamePanel != null && gamePanel.getScene() != null) {
                    rootParent = gamePanel.getScene().getRoot();
                }
                // C. Fallback: Find the active Window
                else {
                    for (Window window : Window.getWindows()) {
                        if (window.isShowing() && window instanceof Stage) {
                            Scene scene = ((Stage) window).getScene();
                            if (scene != null) {
                                rootParent = scene.getRoot();
                                break;
                            }
                        }
                    }
                }
            }

            if (rootParent == null) {
                System.out.println("ERROR: Could not find root to attach Options Menu.");
                return;
            }

            // Attach the menu
            Pane overlayParent;
            if (rootParent instanceof Pane) {
                overlayParent = (Pane) rootParent;
            } else {
                // Wrap if the root isn't a Pane (rare, but safe)
                StackPane wrapper = new StackPane(rootParent);
                rootParent.getScene().setRoot(wrapper);
                overlayParent = wrapper;
            }

            // Resize logic
            optionsPane.setPrefSize(overlayParent.getWidth(), overlayParent.getHeight());
            optionsPane.prefWidthProperty().bind(overlayParent.widthProperty());
            optionsPane.prefHeightProperty().bind(overlayParent.heightProperty());

            overlayParent.getChildren().add(optionsPane);
            currentOptionsMenu = optionsPane;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Also update hideOptionsMenu():
    public void hideOptionsMenu() {
        if (currentOptionsMenu != null && currentOptionsMenu.getParent() instanceof Pane) {
            ((Pane) currentOptionsMenu.getParent()).getChildren().remove(currentOptionsMenu);
            currentOptionsMenu = null;
            System.out.println("Options menu hidden");
        }
    }

    public void hidePauseMenu() {
        if (pauseMenu != null) {
            ((Pane) gamePanel.getScene().getRoot()).getChildren().remove(pauseMenu);
            pauseMenu = null;
        }
    }


    public GridPane getGamePanel() {
        return gamePanel;
    }


    //Background Music
    private void initializeBackgroundMusic() {
        System.out.println("=== initializeBackgroundMusic() called ===");
        try {
            if (bgmPlayer != null) {
                bgmPlayer.stop();
                bgmPlayer.dispose();
            }

            String bgmPath = getClass().getResource("/sounds/bgm.mp3").toExternalForm();
            System.out.println("BGM path: " + bgmPath);

            Media sound = new Media(bgmPath);
            bgmPlayer = new MediaPlayer(sound);
            bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            bgmPlayer.setVolume(0.5);
            bgmPlayer.setMute(isMusicMuted);

            System.out.println("bgmPlayer created successfully: " + (bgmPlayer != null));

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR loading background music: " + e.getMessage());
        }
    }

    public boolean isMusicMuted() {
        return isMusicMuted;
    }

    public boolean isSFXMuted() {
        return isSFXMuted;
    }

    public void setMusicMute(boolean mute) {
        System.out.println("=== GuiController.setMusicMute(" + mute + ") ===");
        this.isMusicMuted = mute;

        if (bgmPlayer != null) {
            System.out.println("bgmPlayer status BEFORE: " + bgmPlayer.getStatus());
            System.out.println("bgmPlayer.isMute() BEFORE: " + bgmPlayer.isMute());

            bgmPlayer.setMute(mute);

            System.out.println("bgmPlayer.isMute() AFTER: " + bgmPlayer.isMute());
            System.out.println("bgmPlayer status AFTER: " + bgmPlayer.getStatus());
        } else {
            System.out.println("ERROR: bgmPlayer is NULL!");
        }
    }

    public void resumeGame() {
        System.out.println("=== resumeGame() called ===");

        timeLine.play();
        isPause.set(false);

        // Resume BGM (setMute handles whether you hear it)
        if (bgmPlayer != null) {
            System.out.println("Resuming BGM, isMusicMuted=" + isMusicMuted);
            bgmPlayer.play();
            System.out.println("BGM status after play: " + bgmPlayer.getStatus());
        } else {
            System.out.println("ERROR: bgmPlayer is NULL in resumeGame!");
        }
    }

    public void showControlsMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/controlsMenu.fxml"));
            Pane controlsPane = loader.load();

            ControlsMenu controller = loader.getController();
            controller.setGuiController(this);

            // Logic to find the correct root to overlay on (same as your OptionsMenu logic)
            javafx.scene.Parent rootParent = null;

            // 1. Try gamePanel
            if (gamePanel != null && gamePanel.getScene() != null) {
                rootParent = gamePanel.getScene().getRoot();
            }

            // 2. Try focused window
            if (rootParent == null) {
                for (Window window : Window.getWindows()) {
                    if (window.isShowing() && window.isFocused() && window instanceof Stage) {
                        Stage stage = (Stage) window;
                        if (stage.getScene() != null) {
                            rootParent = stage.getScene().getRoot();
                            break;
                        }
                    }
                }
            }

            // 3. Fallback
            if (rootParent == null) {
                System.out.println("Could not find root for Controls Menu");
                return;
            }

            Pane overlayParent;
            if (rootParent instanceof Pane) {
                overlayParent = (Pane) rootParent;
            } else {
                StackPane wrapper = new StackPane();
                wrapper.getChildren().add(rootParent);
                Stage stage = (Stage) rootParent.getScene().getWindow();
                stage.getScene().setRoot(wrapper);
                overlayParent = wrapper;
            }

            // Bind size to fill screen
            controlsPane.setPrefSize(overlayParent.getWidth(), overlayParent.getHeight());
            controlsPane.prefWidthProperty().bind(overlayParent.widthProperty());
            controlsPane.prefHeightProperty().bind(overlayParent.heightProperty());

            overlayParent.getChildren().add(controlsPane);
            currentControlsMenu = controlsPane;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading Controls menu: " + e.getMessage());
        }
    }

    public void hideControlsMenu() {
        if (currentControlsMenu != null && currentControlsMenu.getParent() instanceof Pane) {
            ((Pane) currentControlsMenu.getParent()).getChildren().remove(currentControlsMenu);
            currentControlsMenu = null;
        }
    }

    public void onSceneLoaded() {
        System.out.println("=== onSceneLoaded() called ===");
        System.out.println("bgmPlayer is null BEFORE init? " + (bgmPlayer == null));

        if (bgmPlayer == null) {
            initializeBackgroundMusic();
        }

        System.out.println("bgmPlayer is null AFTER init? " + (bgmPlayer == null));

        if (bgmPlayer != null) {
            bgmPlayer.play();
            System.out.println("BGM started playing, status: " + bgmPlayer.getStatus());
        } else {
            System.out.println("ERROR: bgmPlayer is STILL NULL after initialization!");
        }
    }

    public void startBGM() {
        if (bgmPlayer != null && !isMusicMuted) {
            bgmPlayer.play();
        }
    }

    // 7. Add a method to stop BGM completely (for cleanup)
    public void stopBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }
    }

    public GameOverPanel getGameOverPanel() {
        return gameOverPanel;
    }

    public void showOptionsMenuFromGameOver() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/optionsMenu.fxml"));
            Pane optionsPane = loader.load();

            OptionsMenu controller = loader.getController();
            controller.setGuiController(this);
            controller.setOpenedFromGameOver(true);  // <-- Tell it where it came from

            Pane root = (Pane) gamePanel.getScene().getRoot();

            optionsPane.setPrefSize(root.getWidth(), root.getHeight());
            optionsPane.prefWidthProperty().bind(root.widthProperty());
            optionsPane.prefHeightProperty().bind(root.heightProperty());

            root.getChildren().add(optionsPane);
            currentOptionsMenu = optionsPane;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
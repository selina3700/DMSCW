package com.comp2042.controllers;

import com.comp2042.events.EventSource;
import com.comp2042.events.EventType;
import com.comp2042.events.InputEventListener;
import com.comp2042.events.MoveEvent;
import com.comp2042.handlers.HardDropHandler;
import com.comp2042.handlers.KeyboardInputHandler;
import com.comp2042.handlers.MoveDownHandler;
import com.comp2042.managers.GameStateManager;
import com.comp2042.managers.MenuManager;
import com.comp2042.menu.GameOverPanel;
import com.comp2042.managers.BgmManager;
import com.comp2042.models.NotificationPanel;
import com.comp2042.models.ViewData;
import com.comp2042.rendering.BrickRenderer;
import com.comp2042.sounds.BrickLandSFX;
import com.comp2042.sounds.ButtonSFX;
import com.comp2042.sounds.ClearLineSFX;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Labeled;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The main View Controller for TetrisJFX
 * <p>
 *     This class is the core component responsible for managing the user interface and delegating
 *     control between various handlers and managers.
 *     It implements the {@code Initializable} interface for JavaFX component setup and is responsible for:
 *
 *     <ul>
 *         <li>Initializing all FXML elements and visual components.</li>
 *         <li>Manages the game loop via the {@code Timeline} and controlling game speed.</li>
 *         <li>Delegating game state changes to the {@code GameStateManager}.</li>
 *     </ul>
 *
 *     It acts as the bridge between the purely logical game state and the visual representation.
 * </p>
 *
 */
public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 22;

    @FXML private GridPane gamePanel;
    @FXML private Group groupNotification;
    @FXML private GridPane brickPanel;
    @FXML private GameOverPanel gameOverPanel;
    @FXML private Labeled scoreLabel;
    @FXML private Labeled levelLabel;
    @FXML private Labeled linesLabel;
    @FXML private Pane buttonGroup;
    @FXML private GridPane nextBrickPanel;
    @FXML private GridPane holdBrickPanel;

    // Core components
    private InputEventListener eventListener;
    private Timeline timeLine;

    // Game State
    private final BooleanProperty isPause = new SimpleBooleanProperty();
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    // Audio
    private BgmManager bgm;
    private boolean isMusicMuted = false;
    private ClearLineSFX clearLineSFX;
    private ButtonSFX pauseButton;
    private boolean isSFXMuted = false;
    private BrickLandSFX brickLandSFX;

    // Managers and Handlers
    private MenuManager menuManager;
    private HardDropHandler hardDropHandler;
    private MoveDownHandler moveDownHandler;
    private KeyboardInputHandler keyboardInputHandler;
    private BrickRenderer brickRenderer;
    private GameStateManager gameStateManager;

    /**
     * Initialize the controller after its root element has been completely processed.
     * <p>
     *     Loads fonts, sets up initial FXML properties, hides the game over panel, initializes
     *     audio components, and sets up the initial state of the pause button and various handlers.
     * </p>
     * @param location Location used to resolve relative paths for the root object.
     * @param resources Resources used to localize the root object, or null if not located.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load fonts
        Font.loadFont(getClass().getResourceAsStream("/font/PixelifySans.ttf"), 38);
        Font.loadFont(getClass().getResourceAsStream("/font/PressStart.ttf"), 38);

        // Setup game panel
        gamePanel.setPadding(Insets.EMPTY);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        // Setup brick panel
        brickPanel.setPadding(Insets.EMPTY);
        brickPanel.setManaged(false);
        brickPanel.setMouseTransparent(true);
        brickPanel.setPickOnBounds(false);
        brickPanel.setGridLinesVisible(false);

        Rectangle clipRect = new Rectangle();
        clipRect.widthProperty().bind(gamePanel.widthProperty());
        clipRect.heightProperty().bind(gamePanel.heightProperty());
        gamePanel.setClip(clipRect);

        // Hide game over panel initially
        gameOverPanel.setVisible(false);

        setupPauseButton();

        // Initialize audio
        bgm = new BgmManager("/sounds/bgm.mp3", "/sounds/gameOver.mp3");
        brickLandSFX = new BrickLandSFX();
        clearLineSFX = new ClearLineSFX();

        moveDownHandler = new MoveDownHandler(
                null, this, isPause, clearLineSFX, brickLandSFX, groupNotification
        );

        keyboardInputHandler = new KeyboardInputHandler(
                null, this, isPause, isGameOver
        );

        brickRenderer = new BrickRenderer(gamePanel, brickPanel, isPause);


        menuManager = new MenuManager(this);
    }

    /**
     * Initializes the graphical representation of the game board and starts the game loop.
     * <p>
     *     Set up background grid, renders components, creates the game with the specified speed,
     *     and starts the automatic down movement events.
     * </p>
     * @param boardMatrix Initial state of the background grid.
     * @param brick The initial brick data to be rendered.
     * @param initialSpeed Starting speed for the game loop.
     */
    public void initGameView(int[][] boardMatrix, ViewData brick, double initialSpeed) {
        // Setup background grid visuals
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

        brickRenderer.initializeBackground(boardMatrix);
        brickRenderer.initializeFallingBrick(brick);
        brickRenderer.initializeBrickPreview(nextBrickPanel, holdBrickPanel);

        timeLine = new Timeline(new KeyFrame(
                Duration.millis(initialSpeed),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();

        gameStateManager = new GameStateManager(
                this, isPause, isGameOver, timeLine, bgm,
                gameOverPanel, gamePanel, eventListener, menuManager
        );
        gameStateManager.setPauseButton(pauseButton);

        brickRenderer.renderInitialNextBrick(brick.getNextBrickData());
    }

    /**
     * Updates the visual representation of the falling brick and its immediate surroundings.
     * @param brick The {@code ViewData} containing the current shape adata and preview data of the brick.
     */
    public void refreshBrick(ViewData brick) {
        brickRenderer.refreshBrick(brick);
    }

    /** Updates background grid visualization to reflect merged brick.
     * @param board 2D array representing the state of the merged background blocks.
     */
    public void refreshGameBackground(int[][] board) {
        brickRenderer.refreshGameBackground(board);
    }

    /**
     * Internal method to trigger a down movement event, delegating to the {@code MoveDownHandler}.
     * @param event The move event (from the game loop or user input).
     */
    private void moveDown(MoveEvent event) {
        if (moveDownHandler != null) {
            moveDownHandler.execute(event);
        }
    }

    /**
     * Public wrapper that calls the internal {@code moveDown} method.
     * Used by handlers like {@code KeyboardInputHandler}.
     * @param event The move event process.
     */
    public void moveDownPublic(MoveEvent event) {
        moveDown(event);
    }

    /**
     * Requests focus on the main game panel
     * Ensures keyboard input handlers remain active.
     */
    public void requestGamePanelFocus() {
        gamePanel.requestFocus();
    }

    /**
     * Internal methods to execute a hard drop, delegating to the {@code HardDropHandler}
     */
    private void hardDrop() {
        if (hardDropHandler != null) {
            hardDropHandler.execute(isPause.getValue(), isGameOver.getValue());
            gamePanel.requestFocus();
        }
    }

    /**
     * Public wrapper that calls the internal {@code hardDrop} method.
     * Used by handlers like {@code KeyboardInputHandler}.
     */
    public void hardDropPublic() {
        hardDrop();
    }

    /**
     * Sets the primary game event listener {@code GameController}
     * <p>
     *     This method is called during application startup to connect core logic with
     *     the GUI controller. It also initializes the necessary handlers using this listener,
     *     and sets up the keyboard input handlers on the game panel.
     * </p>
     * @param eventListener Instance of the {@code InputEventListener} (GameController).
     */
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;

        // Initialize handlers with eventListener
        this.hardDropHandler = new HardDropHandler(eventListener, this);
        this.moveDownHandler = new MoveDownHandler(
                eventListener, this, isPause, clearLineSFX, brickLandSFX, groupNotification
        );

        // Setup keyboard input
        this.keyboardInputHandler = new KeyboardInputHandler(
                eventListener, this, isPause, isGameOver
        );
        keyboardInputHandler.setupKeyEvents(gamePanel);
    }

    /**
     * Binds the score label's text property to the game's score property
     * This ensures that the score display automatically updates when the game score changes.
     * @param scoreProperty The {@code IntegerProperty} representing the current score.
     */
    public void bindScore(IntegerProperty scoreProperty) {
        Font font = Font.loadFont(getClass().getResourceAsStream("/font/digital.ttf"), 38);
        scoreLabel.getStyleClass().add("bindScoreStyle");
        scoreLabel.setFont(font);
        scoreLabel.textProperty().bind(scoreProperty.asString("Score: %d"));
    }

    /**
     * Sets the displayed level text in the GUI.
     * @param level Current game level.
     */
    public void setLevel(int level) {
        levelLabel.setText("Level: " + level);
    }

    /**
     * Sets the displayed number of lines cleared in the GUI
     * @param lines Total number of lines cleared so far.
     */
    public void setLinesCleared(int lines) {
        linesLabel.setText("Lines: " + lines);
    }

    /**
     * Displays a notification panel that is used to display the score after a line clears
     * @param notificationPanel The panel containing the notification text.
     */
    public void showNotification(NotificationPanel notificationPanel) {
        groupNotification.getChildren().add(notificationPanel);
        notificationPanel.showScore(groupNotification.getChildren());
    }

    /**
     * Triggers the Game Over sequence.
     * Involves stopping the game loop, changing the BGM, and showing the Game Over Panel.
     */
    public void gameOver() {
        gameStateManager.gameOver();
    }

    /**
     * Delegates control to the {@code GameStateManager} to start a new game.
     * @param actionEvent Event that triggered the action.
     */
    public void newGame(ActionEvent actionEvent) {
        gameStateManager.newGame(actionEvent);
    }

    /**
     * Delegates control to the {@code GameStateManager} to pause the game.
     * @param actionEvent Event that triggered the action.
     */
    public void pauseGame(ActionEvent actionEvent) {
        gameStateManager.pauseGame(actionEvent);
    }

    /**
     * Delegates control to the {@code GameStateManager} to resume the game from the PauseMenu.
    */
     public void resumeGame() {
        gameStateManager.resumeGame();
    }

    /**
     * Delegates to {@code MenuManager} to display the pause menu overlay.
     */
    public void showPauseMenu() {
        menuManager.showPauseMenu();
    }

    /**
     * Delegates to {@code MenuManager} to hide the pause menu overlay.
     */
    public void hidePauseMenu() {
        menuManager.hidePauseMenu();
    }

    /**
     * Delegates to {@code MenuManager} to display the main menu and stops the game loop.
     */
    public void showMainMenu() {
        if (timeLine != null) timeLine.stop();
        menuManager.showMainMenu();
    }

    /**
     * Check if the main menu is displayed
     * @return True if main menu is open and false otherwise.
     */
    public boolean isMainMenuOpen() {
        return menuManager.isMainMenuOpen();
    }

    /**
     * Delegates to {@code MenuManager} to display the option menu.
     */
    public void showOptionsMenu() {
        menuManager.showOptionsMenu();
    }

    /**
     * Delegates to {@code MenuManager} to show the options menu root.
     * @param specificRoot Parent pane to display the options menu within
     */
    public void showOptionsMenu(Pane specificRoot) {
        menuManager.showOptionsMenu(specificRoot);
    }

    /**
     * Delegates to {@code MenuManager} to hide the option menu.
     */
    public void hideOptionsMenu() {
        menuManager.hideOptionsMenu();
    }

    /**
     * Delegates to {@code MenuManager} to show the options menu when triggered from the Game Over screen.
     */
    public void showOptionsMenuFromGameOver() {
        menuManager.showOptionsMenuFromGameOver();
    }

    /**
     * Delegates to {@code MenuManager} to show the controls menu, returning to the parent menu.
     * @param parentMenu Stack pane of the calling parent menu.
     */
    public void showControlsMenuFromMenu(StackPane parentMenu) {
        menuManager.showControlsMenuFromMenu(parentMenu);
    }

    /**
     * Delegates to {@code MenuManager} to set the current controls menu pane
     * @param pane The pane containing the control layout
     */
    public void setCurrentControlsMenu(Pane pane) {
        menuManager.setCurrentControlsMenu(pane);
    }

    /**
     * Mutes and unmutes the SFX and updates all SFX handlers
     * @param mute Set true to mute and false to unmute
     */
    public void setSFXMute(boolean mute) {
        this.isSFXMuted = mute;
        if (clearLineSFX != null) clearLineSFX.setMuted(mute);
        if (pauseButton != null) pauseButton.setSFXMuted(mute);
        if (hardDropHandler != null) hardDropHandler.setSFXMuted(mute);
        if (brickLandSFX != null) brickLandSFX.setMuted(mute);
    }

    /**
     * Enables or disables BGM
     * @param mute Set true to mute and false to unmute
     */
    public void setMusicMuted(boolean mute) {
        isMusicMuted = mute;
        bgm.setMuted(mute);
    }

    /**
     * Checks the current muted state of the BGM
     * @return True if music is muted and false otherwise.
     */
    public boolean isMusicMuted() {
        return isMusicMuted;
    }

    /**
     * Checkes the current muted state of the SFX
     * @return True if SFX is muted and false otherwise
     */
    public boolean isSFXMuted() {
        return isSFXMuted;
    }

    /**
     * Starts playing the BGM if it's not currently playing
     */
    public void startBgm() {
        if (bgm != null && !isMusicMuted) {
            bgm.playBgm();
        }
    }

    /**
     * Stops the currently playing BGM
     */
    public void stopBgm() {
        if (bgm != null) {
            bgm.stopCurrent();
        }
    }

    /**
     * Adjusts the speed of the automatic brick fall events.
     * <p>
     *     Stops the existing {@code Timeline}, updates the {@code KeyFrame} duration
     *     to the {@code newSpeed}, and restarts the timeline.
     * </p>
     * @param newSpeed The new delay time in milliseconds between down movements.
     */
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

    /**
     * Initializes appearance and click SFX for pause button
     */
    private void setupPauseButton() {
        pauseButton = new ButtonSFX("/images/Pause_Button.png", "/images/Pause_After.png");
        pauseButton.setFitWidth(55);
        pauseButton.setFitHeight(55);
        pauseButton.setLayoutX(5);
        pauseButton.setLayoutY(10);

        pauseButton.setOnMouseClicked(event -> {
            if (isGameOver.getValue() == Boolean.FALSE) {
                pauseGame(null);
            }
        });

        pauseButton.setSFXMuted(isSFXMuted);
        buttonGroup.getChildren().add(pauseButton);
    }

    //Getters for FXML elements

    /**
     * Returns the main board
     * @return The game panel grid
     */
    public GridPane getGamePanel() {
        return gamePanel;
    }

    /**
     * Returns the {@code GameOverPanel} instance
     * @return Game over panel component
     */
    public GameOverPanel getGameOverPanel() {
        return gameOverPanel;
    }
}
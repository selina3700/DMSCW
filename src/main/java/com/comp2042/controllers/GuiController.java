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
 * Main GUI Controller - now focused on coordination rather than implementation
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

        // Clip game panel
        Rectangle clipRect = new Rectangle();
        clipRect.widthProperty().bind(gamePanel.widthProperty());
        clipRect.heightProperty().bind(gamePanel.heightProperty());
        gamePanel.setClip(clipRect);

        // Hide game over panel initially
        gameOverPanel.setVisible(false);

        // Visual effects (unused but kept for compatibility)
        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);

        setupPauseButton();

        // Initialize audio
        bgm = new BgmManager("/sounds/bgm.mp3", "/sounds/gameOver.mp3");
        brickLandSFX = new BrickLandSFX();
        clearLineSFX = new ClearLineSFX();

        // Initialize handlers (eventListener set later)
        moveDownHandler = new MoveDownHandler(
                null, this, isPause, clearLineSFX, brickLandSFX, groupNotification
        );

        keyboardInputHandler = new KeyboardInputHandler(
                null, this, isPause, isGameOver
        );

        brickRenderer = new BrickRenderer(gamePanel, brickPanel, isPause);


        menuManager = new MenuManager(this);

        // GameStateManager will be initialized after timeLine is created
    }

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

        // Initialize renderer components
        brickRenderer.initializeBackground(boardMatrix);
        brickRenderer.initializeFallingBrick(brick);
        brickRenderer.initializeBrickPreview(nextBrickPanel, holdBrickPanel);

        // Create game loop timeline
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(initialSpeed),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();

        // Now initialize GameStateManager with timeline
        gameStateManager = new GameStateManager(
                this, isPause, isGameOver, timeLine, bgm,
                gameOverPanel, gamePanel, eventListener, menuManager
        );
        gameStateManager.setPauseButton(pauseButton);

        // Render initial preview
        brickRenderer.renderInitialNextBrick(brick.getNextBrickData());
    }

    // ==================== PUBLIC METHODS FOR HANDLERS ====================

    public void refreshBrick(ViewData brick) {
        brickRenderer.refreshBrick(brick);
    }

    public void refreshGameBackground(int[][] board) {
        brickRenderer.refreshGameBackground(board);
    }

    private void moveDown(MoveEvent event) {
        if (moveDownHandler != null) {
            moveDownHandler.execute(event);
        }
    }

    // Public wrapper for KeyboardInputHandler
    public void moveDownPublic(MoveEvent event) {
        moveDown(event);
    }

    public void requestGamePanelFocus() {
        gamePanel.requestFocus();
    }

    private void hardDrop() {
        if (hardDropHandler != null) {
            hardDropHandler.execute(isPause.getValue(), isGameOver.getValue());
            gamePanel.requestFocus();
        }
    }

    // Public wrapper for KeyboardInputHandler
    public void hardDropPublic() {
        hardDrop();
    }

    // ==================== EVENT LISTENER SETUP ====================

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

    // ==================== UI UPDATES ====================

    public void bindScore(IntegerProperty scoreProperty) {
        Font font = Font.loadFont(getClass().getResourceAsStream("/font/digital.ttf"), 38);
        scoreLabel.getStyleClass().add("bindScoreStyle");
        scoreLabel.setFont(font);
        scoreLabel.textProperty().bind(scoreProperty.asString("Score: %d"));
    }

    public void setLevel(int level) {
        levelLabel.setText("Level: " + level);
    }

    public void setLinesCleared(int lines) {
        linesLabel.setText("Lines: " + lines);
    }

    public void showNotification(NotificationPanel notificationPanel) {
        groupNotification.getChildren().add(notificationPanel);
        notificationPanel.showScore(groupNotification.getChildren());
    }

    // ==================== GAME STATE DELEGATION ====================

    public void gameOver() {
        gameStateManager.gameOver();
    }

    public void newGame(ActionEvent actionEvent) {
        gameStateManager.newGame(actionEvent);
    }

    public void pauseGame(ActionEvent actionEvent) {
        gameStateManager.pauseGame(actionEvent);
    }

    public void resumeGame() {
        gameStateManager.resumeGame();
    }

    // Made public so GameStateManager and GameOverPanel can access it
    public void setPauseButtonEnabled(boolean enabled) {
        gameStateManager.setPauseButtonEnabled(enabled);
    }

    // ==================== MENU DELEGATION ====================

    public void showPauseMenu() {
        menuManager.showPauseMenu();
    }

    public void hidePauseMenu() {
        menuManager.hidePauseMenu();
    }

    public void showMainMenu() {
        if (timeLine != null) timeLine.stop();
        menuManager.showMainMenu();
    }

    public void hideMainMenu() {
        menuManager.hideMainMenu();
    }

    public boolean isMainMenuOpen() {
        return menuManager.isMainMenuOpen();
    }

    public void showOptionsMenu() {
        menuManager.showOptionsMenu();
    }

    public void showOptionsMenu(Pane specificRoot) {
        menuManager.showOptionsMenu(specificRoot);
    }

    public void hideOptionsMenu() {
        menuManager.hideOptionsMenu();
    }

    public void showOptionsMenuFromGameOver() {
        menuManager.showOptionsMenuFromGameOver();
    }

    public void showControlsMenuFromMenu(StackPane parentMenu) {
        menuManager.showControlsMenuFromMenu(parentMenu);
    }

    public void setCurrentControlsMenu(Pane pane) {
        menuManager.setCurrentControlsMenu(pane);
    }

    // ==================== AUDIO MANAGEMENT ====================

    public void setSFXMute(boolean mute) {
        this.isSFXMuted = mute;
        if (clearLineSFX != null) clearLineSFX.setMuted(mute);
        if (pauseButton != null) pauseButton.setSFXMuted(mute);
        if (hardDropHandler != null) hardDropHandler.setSFXMuted(mute);
        if (brickLandSFX != null) brickLandSFX.setMuted(mute);
    }

    public void setMusicMuted(boolean mute) {
        isMusicMuted = mute;
        bgm.setMuted(mute);
    }

    public boolean isMusicMuted() {
        return isMusicMuted;
    }

    public boolean isSFXMuted() {
        return isSFXMuted;
    }

    public void startBgm() {
        if (bgm != null && !isMusicMuted) {
            bgm.playBgm();
        }
    }

    // ==================== GAME SPEED ====================

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

    // ==================== PAUSE BUTTON SETUP ====================

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

    // ==================== GETTERS ====================

    public GridPane getGamePanel() {
        return gamePanel;
    }

    public GameOverPanel getGameOverPanel() {
        return gameOverPanel;
    }
}
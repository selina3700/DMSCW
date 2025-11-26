package com.comp2042.managers;

import com.comp2042.menu.GameOverPanel;
import com.comp2042.managers.BgmManager;
import com.comp2042.managers.MenuManager;
import com.comp2042.sounds.ButtonSFX;
import com.comp2042.controllers.GuiController;
import com.comp2042.events.InputEventListener;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Manages the game state including pause, resume, game over, and new game.
 * Centralizes all state transitions and their associated behaviors.
 */
public class GameStateManager {

    private final GuiController guiController;
    private final BooleanProperty isPause;
    private final BooleanProperty isGameOver;
    private final Timeline timeLine;
    private final BgmManager bgm;
    private final GameOverPanel gameOverPanel;
    private final GridPane gamePanel;
    private final InputEventListener eventListener;
    private final MenuManager menuManager;

    private ButtonSFX pauseButton;

    /**
     * Constructor for GameStateManager
     */
    public GameStateManager(GuiController guiController,
                            BooleanProperty isPause,
                            BooleanProperty isGameOver,
                            Timeline timeLine,
                            BgmManager bgm,
                            GameOverPanel gameOverPanel,
                            GridPane gamePanel,
                            InputEventListener eventListener,
                            MenuManager menuManager) {
        this.guiController = guiController;
        this.isPause = isPause;
        this.isGameOver = isGameOver;
        this.timeLine = timeLine;
        this.bgm = bgm;
        this.gameOverPanel = gameOverPanel;
        this.gamePanel = gamePanel;
        this.eventListener = eventListener;
        this.menuManager = menuManager;
    }

    /**
     * Pauses or resumes the game
     */
    public void pauseGame(ActionEvent actionEvent) {
        if (isPause.get()) {
            resumeGame();
            menuManager.hidePauseMenu();
            bgm.resume();  // CHANGED: Use resume() instead of playBgm()
        } else {
            timeLine.pause();
            isPause.set(true);
            bgm.pause();
            menuManager.showPauseMenu();
        }
        gamePanel.requestFocus();
    }

    /**
     * Resumes the game from pause
     */
    public void resumeGame() {
        timeLine.play();
        isPause.set(false);
        bgm.resume();  // CHANGED: Use resume() instead of playBgm()
        menuManager.hidePauseMenu();  // Hide the pause menu when resuming
        gamePanel.requestFocus();  // Refocus the game panel
    }

    /**
     * Handles game over state
     */
    public void gameOver() {
        timeLine.stop();
        setPauseButtonEnabled(false);
        isGameOver.setValue(Boolean.TRUE);

        // Play game over music
        if (bgm != null) {
            bgm.stopCurrent();
            bgm.playGameOverMusic();
        }

        // Show game over panel
        Pane root = (Pane) gamePanel.getScene().getRoot();
        if (!root.getChildren().contains(gameOverPanel)) {
            gameOverPanel.prefWidthProperty().bind(root.widthProperty());
            gameOverPanel.prefHeightProperty().bind(root.heightProperty());
            root.getChildren().add(gameOverPanel);
        }

        gameOverPanel.setGuiController(guiController);
        gameOverPanel.setVisible(true);
    }

    /**
     * Resets the board and starts a new game
     */
    public void newGame(ActionEvent actionEvent) {
        // 1. Swap Root back to GamePanel if needed
        StackPane mainMenu = menuManager.getMainMenu();
        if (gamePanel.getScene() == null && mainMenu != null && mainMenu.getScene() != null) {
            mainMenu.getScene().setRoot(gamePanel);
        }

        // 2. Reset state
        menuManager.hideMainMenu();

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
        bgm.restart();
    }

    /**
     * Enables or disables the pause button
     */
    public void setPauseButtonEnabled(boolean enabled) {
        if (pauseButton != null) {
            pauseButton.setDisable(!enabled);
            pauseButton.setOpacity(enabled ? 1.0 : 0.5);
        }
    }

    /**
     * Sets the pause button reference (called during initialization)
     */
    public void setPauseButton(ButtonSFX pauseButton) {
        this.pauseButton = pauseButton;
    }

    /**
     * Returns the current pause state
     */
    public boolean isPaused() {
        return isPause.get();
    }

    /**
     * Returns the current game over state
     */
    public boolean isGameOver() {
        return isGameOver.get();
    }
}
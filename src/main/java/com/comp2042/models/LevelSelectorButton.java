package com.comp2042.models;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.function.Consumer;

/**
 * A custom interactive JavaFX component for cycling through and selecting
 * the game level.
 * <p>
 *     This button increments the level within a defined range and provides visual and audio
 *     feedback.
 * </p>
 */
public class LevelSelectorButton extends StackPane {

    private int currentLevel = 1;
    private static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 20;

    private Label levelLabel;
    private ImageView buttonImage;
    private Image normalImage;
    private Image hoverImage;
    private MediaPlayer clickSound;
    private boolean isSFXMuted = false;
    private Consumer<Integer> onLevelChange;

    /**
     * Constructs the LevelSelectorButton, initializing the visual appeareance,
     * setting up mouse event handlers,
     */
    public LevelSelectorButton() {
        setupButton();
        setupClickHandler();
        loadSFX();
    }

    /**
     * Initializes the visual elements of the button: loads normal/hover images,
     * creates the {@code ImageView} and the {@code Label} for the level display,
     * and assembles them within the {@code StackPane}.
     */
    private void setupButton() {
        normalImage = new Image(getClass().getResource("/images/Level_Button.png").toExternalForm());
        hoverImage = new Image(getClass().getResource("/images/Level_After.png").toExternalForm());

        // Create image view
        buttonImage = new ImageView(normalImage);
        buttonImage.setFitWidth(276);
        buttonImage.setFitHeight(57);
        buttonImage.setPreserveRatio(false);

        // Level number label - CHANGED to include "Level " prefix
        levelLabel = new Label("Level 1");
        levelLabel.setTextFill(Color.web("#0029E0"));

        // Try to load the pixel font, fallback to system font
        Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/font/PixelifySans.ttf"), 40);
        levelLabel.setFont(pixelFont);

        // Make label non-interactive so clicks pass through
        levelLabel.setMouseTransparent(true);

        // Assemble the button
        getChildren().addAll(buttonImage, levelLabel);
        setAlignment(Pos.CENTER);

        // Set size
        setPrefWidth(276);
        setPrefHeight(57);
        setMaxWidth(276);
        setMaxHeight(57);
    }

    /**
     * Sets up the mouse event handlers for hover and click
     */
    private void setupClickHandler() {
        setOnMouseEntered(this::onMouseEnter);
        setOnMouseExited(this::onMouseExit);
        setOnMouseClicked(this::onMouseClick);
        setCursor(javafx.scene.Cursor.HAND);
    }

    /**
     * Handles the mouse enter event by changing the button's image to the
     * hover state
     * @param e The mouse event triggering the hover.
     */
    private void onMouseEnter(MouseEvent e) {
        if (hoverImage != null) {
            buttonImage.setImage(hoverImage);
        }
    }

    /**
     * Handles the mouse exit event by restoring the button's image to the
     * hover state
     * @param e The mouse event triggering the hover.
     */
    private void onMouseExit(MouseEvent e) {
        if (normalImage != null) {
            buttonImage.setImage(normalImage);
        }
    }

    /**
     * Handles the mouse click event: increments the level, updates the display
     * plays the click sound, and executes the {@code onLevelChange} callback.
     */
    private void onMouseClick(MouseEvent e) {
        currentLevel++;
        if (currentLevel > MAX_LEVEL) {
            currentLevel = MIN_LEVEL;
        }

        updateLevelDisplay();
        playClickSound();

        if (onLevelChange != null) {
            onLevelChange.accept(currentLevel);
        }
    }

    /**
     * Updates the text of the internal label to show the current level.
     */
    private void updateLevelDisplay() {
        levelLabel.setText("Level " + currentLevel);
    }

    /**
     * Loads the audio resource for the button
     */
    private void loadSFX() {
        Media sound = new Media(getClass().getResource("/sounds/buttonClick.mp3").toExternalForm());
        clickSound = new MediaPlayer(sound);
    }

    /**
     * Plays the button click sound effect if the SFX isn't muted
     * <p>
     *     Ensures that the sound restarts from the beginning if it was currently playing
     * </p>
     */
    private void playClickSound() {
        if (clickSound != null && !isSFXMuted) {
            clickSound.stop();
            clickSound.play();
        }
    }

    public void setSFXMuted(boolean muted) {
        this.isSFXMuted = muted;
    }

    public void setOnLevelChange(Consumer<Integer> callback) {
        this.onLevelChange = callback;
    }
}
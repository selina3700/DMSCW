package com.comp2042.menu;

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

    public LevelSelectorButton() {
        setupButton();
        setupClickHandler();
        loadSFX();
    }

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

    private void setupClickHandler() {
        setOnMouseEntered(this::onMouseEnter);
        setOnMouseExited(this::onMouseExit);
        setOnMouseClicked(this::onMouseClick);
        setCursor(javafx.scene.Cursor.HAND);
    }

    private void onMouseEnter(MouseEvent e) {
        if (hoverImage != null) {
            buttonImage.setImage(hoverImage);
        }
    }

    private void onMouseExit(MouseEvent e) {
        if (normalImage != null) {
            buttonImage.setImage(normalImage);
        }
    }

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

    private void updateLevelDisplay() {
        // CHANGED: Include "Level " prefix
        levelLabel.setText("Level " + currentLevel);
    }

    private void loadSFX() {
        try {
            Media sound = new Media(getClass().getResource("/sounds/buttonClick.mp3").toExternalForm());
            clickSound = new MediaPlayer(sound);
        } catch (Exception e) {
            System.out.println("Click sound not found, using silent mode");
        }
    }

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

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int level) {
        if (level >= MIN_LEVEL && level <= MAX_LEVEL) {
            this.currentLevel = level;
            updateLevelDisplay();
        }
    }
}
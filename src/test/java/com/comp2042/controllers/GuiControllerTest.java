package com.comp2042.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuiControllerTest {
    private GuiController guiController;

    @BeforeEach
    void setUp() {
        guiController = new GuiController();
    }

    @Test
    void initialize() {
    }

    @Test
    void initGameView() {
    }

    @Test
    void refreshBrick() {
    }

    @Test
    void refreshGameBackground() {
    }

    @Test
    void moveDownPublic() {
    }

    @Test
    void requestGamePanelFocus() {
    }

    @Test
    void hardDropPublic() {
    }

    @Test
    void setEventListener() {
    }

    @Test
    void bindScore() {
    }

    @Test
    void setLevel() {
    }

    @Test
    void setLinesCleared() {
    }

    @Test
    void showNotification() {
    }

    @Test
    void gameOver() {
    }

    @Test
    void newGame() {
    }

    @Test
    void pauseGame() {
    }

    @Test
    void resumeGame() {
    }

    @Test
    void showPauseMenu() {
    }

    @Test
    void hidePauseMenu() {
    }

    @Test
    void showMainMenu() {
    }

    @Test
    void isMainMenuOpen() {
    }

    @Test
    void showOptionsMenu() {
    }

    @Test
    void testShowOptionsMenu() {
    }

    @Test
    void hideOptionsMenu() {
    }

    @Test
    void showOptionsMenuFromGameOver() {
    }

    @Test
    void showControlsMenuFromMenu() {
    }

    @Test
    void setCurrentControlsMenu() {
    }

    @Test
    void setSFXMute() {
        assertDoesNotThrow(() -> guiController.setSFXMute(true),
                "setSFXMute(true) should not throw exception");
        assertDoesNotThrow(() -> guiController.setSFXMute(false),
                "setSFXMute(false) should not throw exception");
    }

    @Test
    void isSFXMuted() {
        assertFalse(guiController.isSFXMuted(),
                "SFX should not be muted initially");
        guiController.setSFXMute(true);
        assertTrue(guiController.isSFXMuted(),
                "SFX should be muted after calling setSFXMute(true)");
        guiController.setSFXMute(false);
        assertFalse(guiController.isSFXMuted(),
                "SFX should not be muted after calling setSFXMute(false)");
    }

    @Test
    void startBgm() {
        assertDoesNotThrow(() -> guiController.startBgm(),
                "startBgm() should not throw exception");
    }

    @Test
    void stopBgm() {
        assertDoesNotThrow(() -> guiController.stopBgm(),
                "stopBgm() should not throw exception");
    }

    @Test
    void setGameSpeed() {
    }

    @Test
    void getGamePanel() {
    }

    @Test
    void getGameOverPanel() {
    }
}
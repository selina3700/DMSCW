package com.comp2042.models;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Display temporary stylized notifications, for score bonuses in the game.
 * <p>
 *     The panel is initialized with text and configured with visual effects
 *     before being added to the scene. It automatically handles its own animation
 *     and removal from the scene graph.
 * </p>
 */
public class NotificationPanel extends BorderPane {

    /**
     * Constructs a new notification panel with the specified text.
     * <p>
     *     Initializes the panel's size, creates a centered {@code Label}
     *     and applies a {@code Glow} effect for visibility.
     * </p>
     * @param text The string content to be displayed.
     */
    public NotificationPanel(String text) {
        setMinHeight(200);
        setMinWidth(220);
        final Label score = new Label(text);
        score.getStyleClass().add("bonusStyle");
        final Effect glow = new Glow(0.6);
        score.setEffect(glow);
        score.setTextFill(Color.WHITE);
        setCenter(score);

    }

    /**
     * Triggers the animation sequence for the notification panel to fade out and move up.
     * <p>
     *     A {@code ParallelTransition} is used to combine the {@code FadeTransition} and
     *     {@code TranslateTransition}. Once the animation is completed, the panel is removed from the
     *     parent's node list.
     * </p>
     * @param list The {@code ObservableList} of nodes (typically the parent's container's children list)
     *             from which the panel should be removed upon animation completion
     */
    public void showScore(ObservableList<Node> list) {
        FadeTransition ft = new FadeTransition(Duration.millis(2000), this);
        TranslateTransition tt = new TranslateTransition(Duration.millis(2500), this);
        tt.setToY(this.getLayoutY() - 40);
        ft.setFromValue(1);
        ft.setToValue(0);
        ParallelTransition transition = new ParallelTransition(tt, ft);
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                list.remove(NotificationPanel.this);
            }
        });
        transition.play();
    }
}

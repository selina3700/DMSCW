package com.comp2042.events;

/**
 * Represents specific movements requested within the game
 * <p>
 *     Contains 2 pieces of information:
 *     <ul>
 *         <li>{@code EventType} what kind of action is requested</li>
 *         <li>{@code EventSource} who initiated the action</li>
 *     </ul>
 *     This allows the {@code GameController} to differentiate between movements triggered by the
 *     game loop and movements triggered by the player.
 * </p>
 */
public final class MoveEvent {
    private final EventType eventType;
    private final EventSource eventSource;

    /**
     * Constructs a new MoveEvent
     * @param eventType The specific type of movement or
     * @param eventSource The source that initiated the event
     */
    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    /**
     * Gets the specific action requested by this event.
     * @return The {@code EventType} of the move
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Gets the source that initiated this event.
     * @return The {@code EventSource}
     */
    public EventSource getEventSource() {
        return eventSource;
    }
}

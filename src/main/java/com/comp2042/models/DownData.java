package com.comp2042.models;

/**
 * A data transfer object (DTO) that encapsulates the result of the downward movement
 * of a brick
 * <p>
 *     This object is returned by the {@code onDownEvent} method in the {@code GameController}
 *     and provides the view with:
 *     <ul>
 *         <li>Information about any completed line clears</li>
 *         <li>The updated graphical view data for rendering</li>
 *         <li>A flag indicating whether the brick successfully moved or locked</li>
 *     </ul>
 * </p>
 */
public final class DownData {
    private final ClearRow clearRow;
    private final ViewData viewData;
    private final boolean moved;

    /**
     * Constructs a new DownData result object
     * @param clearRow The result of a line clear operation. This may be {@code null} if no rows were cleared or contain
     * a matrix and score if the brick locked.
     * @param viewData The updated view data containing the current brick position, shape, and previews.
     * @param moved {@code true} if the brick successfully moved down one unit; {@code false} if it locked into place.
     */
    public DownData(ClearRow clearRow, ViewData viewData, boolean moved) {
        this.clearRow = clearRow;
        this.viewData = viewData;
        this.moved = moved;
    }

    /**
     * Gets the line clearing result
     * @return The {@code ClearRow} object, which may
     */
    public ClearRow getClearRow() {
        return clearRow;
    }

    /**
     * Gets the updated view data required for rendering the current game state
     * @return The {@code ViewData} object
     */
    public ViewData getViewData() {
        return viewData;
    }

    /**
     * Checks if the brick successfully moved down one line
     * @return
     */
    public boolean isMoved() {
        return moved;
    }
}

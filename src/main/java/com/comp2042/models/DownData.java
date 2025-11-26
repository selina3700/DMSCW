package com.comp2042.models;

public final class DownData {
    private final ClearRow clearRow;
    private final ViewData viewData;
    private final boolean moved;

    public DownData(ClearRow clearRow, ViewData viewData, boolean moved) {
        this.clearRow = clearRow;
        this.viewData = viewData;
        this.moved = moved;
    }

    public ClearRow getClearRow() {
        return clearRow;
    }

    public ViewData getViewData() {
        return viewData;
    }

    public boolean isMoved() {
        return moved;
    }
}

package com.bigfat.itemtouchhelperdemo;

public interface ItemTouchHelperAdapter {
    void onDragStarted();

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

    void onDragEnded();
}
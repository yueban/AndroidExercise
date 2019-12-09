package com.juphoon.cloud.doodle;

public class StickerModel {

    public int imageResourceId;
    public int widthInPixel;
    public int heightInPixel;
    public String stickerName;
    public int tempOriginalX;
    public int tempOriginalY;
    public int tempWidthInPixel;
    public int tempHeightInPixel;
    public boolean isEmoji;
    public String emojiText;
    public int emojiSizePX;
    public int emojiPadding;

    public StickerModel minimallyCopy() {
        StickerModel model = new StickerModel();
        model.imageResourceId = imageResourceId;
        model.widthInPixel = widthInPixel;
        model.heightInPixel = heightInPixel;
        model.stickerName = stickerName;
        model.emojiText = emojiText;
        model.emojiSizePX = emojiSizePX;
        model.emojiPadding = emojiPadding;
        return model;
    }
}

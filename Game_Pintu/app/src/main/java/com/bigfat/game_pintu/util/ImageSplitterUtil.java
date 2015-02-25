package com.bigfat.game_pintu.util;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/16
 */
public class ImageSplitterUtil {
    /**
     * 将Bitmap切割成拼图碎片ImagePiece
     *
     * @param bitmap 要切的大图
     * @param piece  切成piece*piece的小块
     * @return 拼图碎片列表
     */
    public static List<ImagePiece> splitImage(Bitmap bitmap, int piece) {
        List<ImagePiece> imagePieces = new ArrayList<>();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //切图的左/上起始位置
        int left = 0;
        int top = 0;
        if (width > height) {
            left = (width - height) / 2;
        } else {
            top = (height - width) / 2;
        }
        //每一块拼图的宽度
        int pieceWidth = Math.min(width, height) / piece;
        for (int x = 0; x < piece; x++) {
            for (int y = 0; y < piece; y++) {
                Bitmap pieceBitmap = Bitmap.createBitmap(bitmap, left + y * pieceWidth, top + x * pieceWidth, pieceWidth, pieceWidth);
                ImagePiece imagePiece = new ImagePiece(x * piece + y, pieceBitmap);
                imagePieces.add(imagePiece);
            }
        }
        return imagePieces;
    }
}

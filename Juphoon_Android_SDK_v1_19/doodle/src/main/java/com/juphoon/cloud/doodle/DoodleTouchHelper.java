package com.juphoon.cloud.doodle;

import android.graphics.PointF;
import android.view.MotionEvent;

public class DoodleTouchHelper {
    public interface Listener {
        void onDrawStart(float startX, float startY);

        void onDrawing(float preX, float preY, float x, float y);

        void onDrawStop();

        void onDrawPoint(float pointX, float pointY);

        void onStickerDragStart(float x, float y);

        void onStickerDragging(float preX, float preY, float x, float y);

        void onStickerDragDone();

        void onStickerReshapeStart();

        void onStickerReshaping(PointF scalePoint, float scale);
    }

    public static final int MODE_NONE = 0;
    public static final int MODE_DRAW_BASIC_SHAPE = 1;
    public static final int MODE_DRAW_STICKER = 2;
    public static final int MODE_ZOOM_STICKER = 3;

    private static final float TOUCH_TOLERANCE = 4;

    private int mode;
    private float startX;
    private float startY;
    private float preX;
    private float preY;
    private double originalDistance;
    private PointF middlePoint;

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    public boolean routeTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                touchDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(event);
                break;
            case MotionEvent.ACTION_UP:
                touchUp(event);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                pointerDown(event);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                pointerUp(event);
                break;
            default:
                break;
        }
        return true;
    }

    public static boolean isMoved(float sourceX, float sourceY, float targetX, float targetY) {
        return isMoved(sourceX, sourceY, targetX, targetY, TOUCH_TOLERANCE);
    }

    private static boolean isMoved(float sourceX, float sourceY, float targetX, float targetY, float tolerance) {
        return Math.abs(sourceX - targetX) > tolerance || Math.abs(sourceY - targetY) > tolerance;
    }

    private void touchDown(MotionEvent event) {
        preX = startX = event.getX();
        preY = startY = event.getY();
        if (mode == MODE_NONE) {
            mode = MODE_DRAW_BASIC_SHAPE;
        }
        if (listener != null) {
            if (mode == MODE_DRAW_BASIC_SHAPE) {
                listener.onDrawStart(startX, startY);
            } else if (mode == MODE_DRAW_STICKER) {
                listener.onStickerDragStart(startX, startY);
            }
        }
    }

    private void pointerDown(MotionEvent event) {
        if (event.getPointerCount() <= 1) {
            return;
        }
        if (mode == MODE_DRAW_BASIC_SHAPE) {
            touchUp(event);
        } else if (mode == MODE_DRAW_STICKER) {
            mode = MODE_ZOOM_STICKER;
            try {
                originalDistance = calculateDistance(event);
                middlePoint = calculateMiddlePoint(event);
            } catch (Exception e) {
                e.printStackTrace();
                originalDistance = 0;
                middlePoint = null;
            }
            if (listener != null) {
                listener.onStickerReshapeStart();
            }
        }
    }

    private void pointerUp(MotionEvent event) {
        if (mode == MODE_ZOOM_STICKER) {
            if (event.getPointerCount() <= 2) {
                mode = MODE_DRAW_STICKER;
                if (event.getPointerCount() == 2) {
                    int leftPointIndex = 1 - event.getActionIndex();
                    preX = startX = event.getX(leftPointIndex);
                    preY = startY = event.getY(leftPointIndex);
                }
            }
        }
    }

    private void touchMove(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (mode == MODE_DRAW_BASIC_SHAPE && isMoved(preX, preY, x, y)) {
            if (listener != null) {
                listener.onDrawing(preX, preY, x, y);
            }
            preX = x;
            preY = y;
        } else if (mode == MODE_DRAW_STICKER && isMoved(preX, preY, x, y, 12)) {
            if (listener != null) {
                listener.onStickerDragging(preX, preY, x, y);
            }
            preX = x;
            preY = y;
        } else if (mode == MODE_ZOOM_STICKER) {
            if (listener != null) {
                float scale;
                try {
                    scale = (float) (calculateDistance(event) / originalDistance);
                } catch (Exception e) {
                    e.printStackTrace();
                    scale = 1.0f;
                }
                if (middlePoint != null) {
                    listener.onStickerReshaping(middlePoint, scale);
                }
            }

        }
    }

    private void touchUp(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (mode != MODE_NONE) {
            if (mode == MODE_DRAW_BASIC_SHAPE) {
                if (listener != null) {
                    if (isMoved(x, y, startX, startY)) {
                        listener.onDrawing(preX, preY, x, y);
                    } else {
                        listener.onDrawPoint(x, y);
                    }
                    listener.onDrawStop();
                }
                mode = MODE_NONE;
            } else if (mode == MODE_DRAW_STICKER) {
                if (listener != null) {
                    listener.onStickerDragDone();
                }
            }
            preX = 0;
            preY = 0;
        }
    }

    private double calculateDistance(MotionEvent event) {
        float dx = event.getX(0) - event.getX(1);
        float dy = event.getY(0) - event.getY(1);
        return Math.sqrt(dx * dx + dy * dy);
    }

    private PointF calculateMiddlePoint(MotionEvent event) {
        return new PointF((event.getX(0) + event.getX(1)) / 2, (event.getY(0) + event.getY(1)) / 2);
    }
}

package android.media;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.util.Log;

import java.lang.IllegalArgumentException;

/**
 * 在一个图像对象中识别人像
 */
public class FaceDetector {

    /**
     * Face指明在一个Bitmap中一个人像的位置
     */
    public class Face {
        /** 最小人像识别因数 */
        public static final float CONFIDENCE_THRESHOLD = 0.4f;
        /** 表示人像在x轴上 */
        public static final int EULER_X = 0;
        /** 表示人像在y轴上 */
        public static final int EULER_Y = 1;
        /** 表示人像在z轴上 */
        public static final int EULER_Z = 2;

        /** 
         * 获取人像识别因数，一般在0.3以上表示较好的识别
         */
        public float confidence() {
            return mConfidence;
        }
        /**
         * 设置两眼中点
         */
        public void getMidPoint(PointF point) {
            point.set(mMidPointX, mMidPointY);
        }
        /**
         * 获取两眼间距
         */
        public float eyesDistance() {
            return mEyesDist;
        }
        /**
         * 返回人像在坐标系中的旋转轴
         */
        public float pose(int euler) {
            if (euler == EULER_X)
                return mPoseEulerX;
            else if (euler == EULER_Y)
                return mPoseEulerY;
            else if (euler == EULER_Z)
                return mPoseEulerZ;
           throw new IllegalArgumentException();
        }

        // 私有化构造函数
        private Face() {
        }
        private float   mConfidence;
        private float   mMidPointX;
        private float   mMidPointY;
        private float   mEyesDist;
        private float   mPoseEulerX;
        private float   mPoseEulerY;
        private float   mPoseEulerZ;
    }


    /**
     * 构造FaceDetector对象
     * @param 图像宽
     * @param 图像高
     * @param 最大识别人像数
     */
    public FaceDetector(int width, int height, int maxFaces)
    {
        if (!sInitialized) {
            return;
        }
        fft_initialize(width, height, maxFaces);
        mWidth = width;
        mHeight = height;
        mMaxFaces = maxFaces;
        mBWBuffer = new byte[width * height];
    }

    /**
     * 识别图像中的人像
     * @param 将要识别的图像
     * @param 用于保存识别到的人像
     * @return 识别到的人像数量
     */
    public int findFaces(Bitmap bitmap, Face[] faces)
    {
        if (!sInitialized) {
            return 0;
        }
        if (bitmap.getWidth() != mWidth || bitmap.getHeight() != mHeight) {
            throw new IllegalArgumentException(
                    "bitmap size doesn't match initialization");
        }
        if (faces.length < mMaxFaces) {
            throw new IllegalArgumentException(
                    "faces[] smaller than maxFaces");
        }
        
        int numFaces = fft_detect(bitmap);
        if (numFaces >= mMaxFaces)
            numFaces = mMaxFaces;
        for (int i=0 ; i<numFaces ; i++) {
            if (faces[i] == null)
                faces[i] = new Face();
            fft_get_face(faces[i], i);
        }
        return numFaces;
    }


    @Override
    protected void finalize() throws Throwable {
        fft_destroy();
    }

    /*
     * 一些类初始化器，以供本地调用
     */
    private static boolean sInitialized;
    native private static void nativeClassInit();

    static {
        sInitialized = false;
        try {
            System.loadLibrary("FFTEm");
            nativeClassInit();
            sInitialized = true;
        } catch (UnsatisfiedLinkError e) {
            Log.d("FFTEm", "face detection library not found!");
        }
    }

    native private int  fft_initialize(int width, int height, int maxFaces);
    native private int  fft_detect(Bitmap bitmap);
    native private void fft_get_face(Face face, int i);
    native private void fft_destroy();

    private long    mFD;
    private long    mSDK;
    private long    mDCR;
    private int     mWidth;
    private int     mHeight;
    private int     mMaxFaces;    
    private byte    mBWBuffer[];
}
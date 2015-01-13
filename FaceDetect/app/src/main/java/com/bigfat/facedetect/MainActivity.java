package com.bigfat.facedetect;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.media.FaceDetector;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends ActionBarActivity {

    public static final String tag = "yan";

    public static final int N_MAX = 2;//最大检测人脸数

    private ImageView imgView;
    private Button detectFaceBtn;
    private FaceDetector faceDetector;
    private FaceDetector.Face[] face;
    private ProgressBar progressBar;

    private Bitmap srcImg;//源图片
    private Bitmap srcFace;//用于识别过程的Bitmap对象

    //主线程中实现异步操作的Handler对象
    private Handler mainHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0://检测成功
                    progressBar.setVisibility(View.GONE);
                    Bitmap b = (Bitmap) msg.obj;
                    imgView.setImageBitmap(b);
                    Toast.makeText(MainActivity.this, "检测完毕", Toast.LENGTH_SHORT).show();
                    break;
                case 1://开始检测
                    showProcessBar();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initFaceDetect();
    }

    /**
     * 初始化UI界面
     */
    public void initUI() {
        //绑定控件
        detectFaceBtn = (Button) findViewById(R.id.button);
        imgView = (ImageView) findViewById(R.id.imageView);
        //获取屏幕宽度
        ViewGroup.LayoutParams params = imgView.getLayoutParams();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        //读取源图片
        srcImg = BitmapFactory.decodeResource(getResources(), R.drawable.babyface);
        //修正图片显示的高宽
        int h = srcImg.getHeight();
        int w = srcImg.getWidth();
        float r = (float) h / (float) w;
        params.width = w_screen;
        params.height = (int) (params.width * r);
        imgView.setLayoutParams(params);
        imgView.setImageBitmap(srcImg);
        //为检测按钮绑定监听器
        detectFaceBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mainHandler.sendEmptyMessage(1);
                new Thread() {

                    @Override
                    public void run() {
                        Bitmap faceBitmap = detectFace();
                        Message m = new Message();
                        m.what = 0;
                        m.obj = faceBitmap;
                        mainHandler.sendMessage(m);
                    }
                }.start();
            }
        });
    }

    /**
     * 初始化人脸识别组件
     */
    public void initFaceDetect() {
        srcFace = srcImg.copy(Bitmap.Config.RGB_565, true);
        int w = srcFace.getWidth();
        int h = srcFace.getHeight();
        Log.i(tag, "待检测图像: w = " + w + "h = " + h);
        faceDetector = new FaceDetector(w, h, N_MAX);
        face = new FaceDetector.Face[N_MAX];
    }

    /**
     * 检测是否为有效人脸
     *
     * @param rect 人脸对应的矩形对象
     * @return 是否为有效人脸
     */
    public boolean checkFace(Rect rect) {
        int w = rect.width();
        int h = rect.height();
        int s = w * h;
        Log.i(tag, "人脸 宽w = " + w + "高h = " + h + "人脸面积 s = " + s);
        if (s < 10000) {
            Log.i(tag, "无效人脸，舍弃.");
            return false;
        } else {
            Log.i(tag, "有效人脸，保存.");
            return true;
        }
    }

    /**
     * 检测人脸
     *
     * @return 检测并绘制眼角和脸部轮廓后的Bitmap对象
     */
    public Bitmap detectFace() {
        //检测人脸
        int nFace = faceDetector.findFaces(srcFace, face);
        Log.i(tag, "检测到人脸：n = " + nFace);
        //遍历处理人脸
        for (int i = 0; i < nFace; i++) {
            //生成人脸矩形
            FaceDetector.Face f = face[i];
            PointF midPoint = new PointF();
            float dis = f.eyesDistance();
            f.getMidPoint(midPoint);
            int dd = (int) (dis);
            Point eyeLeft = new Point((int) (midPoint.x - dis / 2), (int) midPoint.y);
            Point eyeRight = new Point((int) (midPoint.x + dis / 2), (int) midPoint.y);
            Rect faceRect = new Rect((int) (midPoint.x - dd), (int) (midPoint.y - dd * 1.3), (int) (midPoint.x + dd), (int) (midPoint.y + dd * 1.3));
            Log.i(tag, "左眼坐标 x = " + eyeLeft.x + "y = " + eyeLeft.y);
            if (checkFace(faceRect)) {//检测是否为有效人脸
                //在眼部和面部轮廓绘制绿色线条
                Canvas canvas = new Canvas(srcFace);
                Paint p = new Paint();
                p.setAntiAlias(true);
                p.setStrokeWidth(8);
                p.setStyle(Paint.Style.STROKE);
                p.setColor(Color.GREEN);
                canvas.drawCircle(eyeLeft.x, eyeLeft.y, 20, p);
                canvas.drawCircle(eyeRight.x, eyeRight.y, 20, p);
                canvas.drawRect(faceRect, p);
            }
        }
        //将检测并绘制后的图片保存到本地
        saveBitmapAsFile(srcFace);
        Log.i(tag, "保存完毕");
        return srcFace;
    }

    /**
     * 显示检测中控件
     */
    public void showProcessBar() {
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.layout_main);
        progressBar = new ProgressBar(MainActivity.this, null, android.R.attr.progressBarStyleLargeInverse); //ViewGroup.LayoutParams.WRAP_CONTENT
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        progressBar.setVisibility(View.VISIBLE);
        //progressBar.setLayoutParams(params);
        mainLayout.addView(progressBar, params);
    }

    /**
     * 将Bitmap存储为本地图片文件
     *
     * @param bitmap
     */
    public void saveBitmapAsFile(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory(), "face1.png");
        Log.i(tag, "file path:" + file.getAbsolutePath());
        FileOutputStream out = null;
        try {
            file.createNewFile();
            file.setWritable(true);
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

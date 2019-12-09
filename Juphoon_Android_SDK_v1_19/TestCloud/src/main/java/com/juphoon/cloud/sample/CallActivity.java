package com.juphoon.cloud.sample;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.juphoon.cloud.JCCall;
import com.juphoon.cloud.JCCallItem;
import com.juphoon.cloud.JCDoodle;
import com.juphoon.cloud.JCDoodleAction;
import com.juphoon.cloud.JCDoodleCallback;
import com.juphoon.cloud.JCMediaDevice;
import com.juphoon.cloud.JCMediaDeviceVideoCanvas;
import com.juphoon.cloud.doodle.DoodleLayout;
import com.juphoon.cloud.sample.JCWrapper.JCCallUtils;
import com.juphoon.cloud.sample.JCWrapper.JCEvent.JCCallMessageEvent;
import com.juphoon.cloud.sample.JCWrapper.JCEvent.JCEvent;
import com.juphoon.cloud.sample.JCWrapper.JCManager;
import com.juphoon.cloud.sample.Toos.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CallActivity extends AppCompatActivity {

    private static final int DOODLE_VERSION_CHECKER = 1;
    private static final String DEFAULT_DIR_NAME = "juphoon_cloud";
    private static final String AUDIO_RECORD_DIR = "audio_record";
    private static final String VIDEO_RECORD_DIR = "video_record";
    private static final String SNAPSHOT = "snapshot";

    private ConstraintLayout mContentView;
    private View mAudioIn;
    private View mAudioInCall;
    private View mVideoIn;
    private View mVideoInCall;
    private TextView mTxtUserId;
    private TextView mTxtCallInfo;
    private TextView mTxtNetStatus;

    private Button mBtnHoldAudio;
    private Button mBtnMuteAudio;
    private Button mBtnSpeakerAudio;
    private Button mBtnDoodleAudio;
    private Button mBtnRecordAudio;
    private Button mBtnCaptureRemote;
    private Button mBtnCaptureLocal;
    private Button mBtnRecordLocalVideo;
    private Button mBtnRecordRemoteVideo;

    private Button mBtnMuteVideo;
    private Button mBtnSpeakerVideo;
    private Button mBtnCameraVideo;
    private Button mBtnDoodleVideo;

    private RecyclerView mListCalls;
    private TextView mTextStatistics;

    private ViewGroup mViewDoodleLayer;
    private DoodleLayout mDoodleLayout;
    private JCDoodle mJCDoodle;

    private ScheduledExecutorService mScheduledExecutor = new ScheduledThreadPoolExecutor(1);
    private ScheduledFuture mStatisticsScheduled;
    private GestureDetector mGestureDetector;

    private JCMediaDeviceVideoCanvas mLocalCanvas;
    private JCMediaDeviceVideoCanvas mRemoteCanvas;

    private AlertDialog mAlertAnswer;

    private Timer mCallInfoTimer;

    private boolean mFullScreen;

    /*
     * 遥控双击标识
     *
     */
    private boolean mWaitDoubleClick = true;

    class CallAdapter extends RecyclerView.Adapter<CallAdapter.CallViewHolder> {

        @Override
        public CallViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CallViewHolder holder = new CallViewHolder(
                    LayoutInflater.from(CallActivity.this).inflate(R.layout.view_call_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(CallViewHolder holder, int position) {
            List<JCCallItem> calls = JCManager.getInstance().call.getCallItems();
            if (position >= calls.size()) {
                return;
            }
            JCCallItem item = calls.get(position);
            holder.name.setText(item.getDisplayName());
            holder.info.setText(JCCallUtils.genCallInfo(item));
            if (JCManager.getInstance().call.getConference()) {
                holder.itemView.setOnClickListener(null);
            } else {
                holder.itemView.setTag(item);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JCManager.getInstance().call.becomeActive((JCCallItem) v.getTag());
                    }
                });
                holder.itemView.setSelected(item.getActive());
            }
        }

        @Override
        public int getItemCount() {
            return JCManager.getInstance().call.getCallItems().size();
        }

        class CallViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            TextView info;

            public CallViewHolder(View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.txtName);
                info = (TextView) view.findViewById(R.id.txtInfo);
            }
        }
    }

    private final JCDoodleCallback mDoodleCallback = new JCDoodleCallback() {
        @Override
        public void onDoodleActionGenerated(JCDoodleAction jcDoodleAction) {
            JCManager.getInstance().call.sendMessage(JCCallUtils.getActiveCall(), JCDoodle.DATA_TYPE_DOODLE, mJCDoodle.stringFromDoodleAction(jcDoodleAction));
        }
    };

    private final DoodleLayout.DoodleControllerListener mDoodleControllerListener = new DoodleLayout.DoodleControllerListener() {
        @Override
        public void onExitDoodle() {
            setDoodleVisibility(View.INVISIBLE);
            JCDoodleAction doodleAction = new JCDoodleAction.Builder(JCDoodle.ACTION_STOP).build();
            JCManager.getInstance().call.sendMessage(JCCallUtils.getActiveCall(), JCDoodle.DATA_TYPE_DOODLE, mJCDoodle.stringFromDoodleAction(doodleAction));
        }

        @Override
        public void onFaceMode() {
            Toast.makeText(CallActivity.this, "FaceMode Clicked", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onShareDoodle(Bitmap bitmap) {
            Toast.makeText(CallActivity.this, "Share Doodle Clicked", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onWillCleanDoodle() {
            AlertDialog.Builder builder = new AlertDialog.Builder(CallActivity.this);
            builder.setMessage("Confirm to clean doodle?");
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mDoodleLayout.cleanDoodle();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, null);
            builder.show();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_call);

        mContentView = (ConstraintLayout) findViewById(R.id.layoutCall);
        mAudioIn = findViewById(R.id.layoutAudioIn);
        mAudioInCall = findViewById(R.id.layoutAudioInCall);
        mVideoIn = findViewById(R.id.layoutVideoIn);
        mVideoInCall = findViewById(R.id.layoutVideoInCall);
        mTxtUserId = (TextView) findViewById(R.id.txtUserId);
        mTxtCallInfo = (TextView) findViewById(R.id.txtCallInfo);
        mTxtNetStatus = (TextView) findViewById(R.id.txtNetStatus);
        mBtnHoldAudio = (Button) findViewById(R.id.btnHoldAudioInCall);
        mBtnMuteAudio = (Button) findViewById(R.id.btnMuteAudioInCall);
        mBtnRecordAudio = (Button) findViewById(R.id.btnAudioRecord);
        mBtnCaptureRemote = (Button) findViewById(R.id.btnRemoteSnapshot);
        mBtnCaptureLocal = (Button) findViewById(R.id.btnLocalSnapshot);
        mBtnRecordLocalVideo = (Button) findViewById(R.id.btnLocalVideoRecord);
        mBtnRecordRemoteVideo = (Button) findViewById(R.id.btnRemoteVideoRecord);
        mBtnSpeakerAudio = (Button) findViewById(R.id.btnSpeakerAudioInCall);
        mBtnDoodleAudio = findViewById(R.id.btnDoodleAudioInCall);
        mBtnMuteVideo = (Button) findViewById(R.id.btnMuteVideoInCall);
        mBtnSpeakerVideo = (Button) findViewById(R.id.btnSpeakerVideoInCall);
        mBtnCameraVideo = (Button) findViewById(R.id.btnCameraVideoInCall);
        mBtnDoodleVideo = findViewById(R.id.btnDoodleVideoInCall);
        mListCalls = (RecyclerView) findViewById(R.id.listCalls);
        mListCalls.setLayoutManager(new LinearLayoutManager(this));
        mListCalls.setAdapter(new CallAdapter());
        mTextStatistics = (TextView) findViewById(R.id.textStatistics);
        mTextStatistics.setMovementMethod(ScrollingMovementMethod.getInstance());

        mViewDoodleLayer = findViewById(R.id.doodle_layer);
        mJCDoodle = JCDoodle.create(mDoodleCallback);
        mFullScreen = false;

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                mStatisticsScheduled.cancel(true);
                mStatisticsScheduled = null;
                mTextStatistics.setVisibility(View.INVISIBLE);
                return super.onDoubleTap(e);
            }
        });
        mTextStatistics.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mGestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });
        mTextStatistics.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
                    if (!keyEvent.isLongPress()) {
                        clickOrDoubleClick();
                    }
                }
                return false;
            }
        });
        EventBus.getDefault().register(this);

        updateUI();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        switchFullScreen();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        stopCallInfoTimer();
        EventBus.getDefault().unregister(this);
        removeCanvas();
        JCDoodle.destroy();
        if (mStatisticsScheduled != null) {
            mStatisticsScheduled.cancel(true);
            mStatisticsScheduled = null;
            mScheduledExecutor.shutdown();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            int direction = keyCode == KeyEvent.KEYCODE_VOLUME_UP ? AudioManager.ADJUST_RAISE : AudioManager.ADJUST_LOWER;
            int flags = AudioManager.FX_FOCUS_NAVIGATION_UP;
            AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            am.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL, direction, flags);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            onFullScreen(null);
            return true;
        }
            return super.onKeyDown(keyCode, event);
    }

    public void onAudioAnswer(View view) {
        JCManager.getInstance().call.answer(JCCallUtils.getActiveCall(), false);
    }

    public void onVideoAnswer(View view) {
        JCManager.getInstance().call.answer(JCCallUtils.getActiveCall(), true);
    }

    public void onTerm(View view) {
        JCManager.getInstance().call.term(JCCallUtils.getActiveCall(), JCCall.REASON_NONE, null);
    }

    public void onHold(View view) {
        JCManager.getInstance().call.hold(JCCallUtils.getActiveCall());
    }

    public void onMute(View view) {
        JCManager.getInstance().call.mute(JCCallUtils.getActiveCall());
    }

    public void onAudioRecord(View view) {
        JCCallItem item = JCCallUtils.getActiveCall();
        if (item.getAudioRecord()) {
            if (JCManager.getInstance().call.audioRecord(item, false, "")) {
                Toast.makeText(this, String.format("录音结束，文件保存于sdcard/juphoon_cloud/%s目录", AUDIO_RECORD_DIR), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "录音结束失败", Toast.LENGTH_LONG).show();
            }
        } else {
            String filePath = generateAudioFileName(item);
            if (!JCManager.getInstance().call.audioRecord(item, true, filePath)) {
                Toast.makeText(this, "audio record fail", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onVideoRecord(View view) {
        JCCallItem item = JCCallUtils.getActiveCall();
        if (view.getId() == R.id.btnLocalVideoRecord && item.getLocalVideoRecord()) {
            if (JCManager.getInstance().call.videoRecord(item, false, false, 0, 0, "", false)) {
                Toast.makeText(this,String.format("本端录制结束，文件保存于sdcard/juphoon_cloud/%s目录", VIDEO_RECORD_DIR), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "本端录制结束失败", Toast.LENGTH_LONG).show();
            }
        } else if (view.getId() == R.id.btnRemoteVideoRecord && item.getRemoteVideoRecord()) {
            if (JCManager.getInstance().call.videoRecord(item, false, true, 0, 0, "", false)) {
                Toast.makeText(this,String.format("远端录制结束，文件保存于sdcard/juphoon_cloud/%s目录", VIDEO_RECORD_DIR), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "远端录制结束失败", Toast.LENGTH_LONG).show();
            }
        } else {
            String filePath = generateVideoRecordFileName(item);
            if (!JCManager.getInstance().call.videoRecord(item, true, view.getId() == R.id.btnRemoteVideoRecord, 640, 360, filePath, true)) {
                Toast.makeText(this, "video record fail", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onSnapshot(View view) {
        boolean result;
        String indicatorString;
        if (view.getId() == R.id.btnLocalSnapshot) {
            result = mLocalCanvas.snapshot(-1, -1, generateSnapshotFileName(JCCallUtils.getActiveCall()));
            indicatorString = "本端";
        } else {
            result = mRemoteCanvas.snapshot(-1, -1, generateSnapshotFileName(JCCallUtils.getActiveCall()));
            indicatorString = "远端";
        }
        if (result) {
            Toast.makeText(this, String.format("%s截屏成功，文件保存于sdcard/juphoon_cloud/%s目录", indicatorString, SNAPSHOT), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, String.format("%s截屏失败", indicatorString), Toast.LENGTH_LONG).show();
        }
    }

    public void onSwitchCamera(View view) {
        JCManager.getInstance().mediaDevice.switchCamera();
    }

    public void onOpenCloseCamera(View view) {
        JCManager.getInstance().call.enableUploadVideoStream(JCCallUtils.getActiveCall());
    }

    public void onAdd(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.tip_enter_userid);
        final EditText input = new EditText(this);
        alertDialog.setView(input);
        alertDialog.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        JCManager.getInstance().call.call(input.getText().toString(), false, "AndroidTest");
                    }
                });
        alertDialog.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    public void onStatistics(View view) {
        mTextStatistics.setVisibility(View.VISIBLE);
        mTextStatistics.requestFocus();
        updateStatistics();

        mStatisticsScheduled = mScheduledExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                mTextStatistics.post(new Runnable() {
                    @Override
                    public void run() {
                        updateStatistics();
                    }
                });
            }
        }, 0, 3, TimeUnit.SECONDS);
    }

    public void onStartDoodle(View view) {
        JCDoodleAction.Builder builder = new JCDoodleAction.Builder(JCDoodle.ACTION_START);
        String action = mJCDoodle.stringFromDoodleAction(builder.build());
        JCManager.getInstance().call.sendMessage(JCCallUtils.getActiveCall(), JCDoodle.DATA_TYPE_DOODLE, action);
        setDoodleVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void onSpeaker(View view) {
        JCManager.getInstance().mediaDevice.enableSpeaker(!JCManager.getInstance().mediaDevice.isSpeakerOn());
        updateUI();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Subscribe
    public void onEvent(JCEvent event) {
        if (event.getEventType() == JCEvent.EventType.CALL_UI) {
            updateUI();
        } else if (event.getEventType() == JCEvent.EventType.CALL_UPDATE) {
            JCCallItem callItem = JCCallUtils.getActiveCall();
            if (callItem != null && callItem.getState() == JCCall.STATE_TALKING) {
                JCManager.getInstance().call.sendMessage(JCCallUtils.getActiveCall(),
                        JCDoodle.DATA_TYPE_DOODLE,
                        mJCDoodle.stringFromDoodleAction(new JCDoodleAction.Builder(JCDoodle.ACTION_EXTRA_BASE + DOODLE_VERSION_CHECKER).build()));
            }
            if (callItem != null && callItem.getAudioRecord() && (callItem.getHold() || callItem.getHeld())) {
                JCManager.getInstance().call.audioRecord(callItem, false, "");
            }
            if (callItem != null && callItem.getLocalVideoRecord() && !callItem.getUploadVideoStreamSelf()) {
                JCManager.getInstance().call.videoRecord(callItem, false, false, 0, 0, "", false);
            }
            if (callItem != null && callItem.getRemoteVideoRecord() && !callItem.getUploadVideoStreamOther()) {
                JCManager.getInstance().call.videoRecord(callItem, false, true, 0, 0, "", false);
            }
        } else if (event.getEventType() == JCEvent.EventType.CALL_MESSAGE_RECEIVED) {
            JCCallMessageEvent messageEvent = (JCCallMessageEvent) event;
            if (messageEvent.type.equals(JCDoodle.DATA_TYPE_DOODLE)) {
                JCDoodleAction doodleAction = mJCDoodle.doodleActionFromString(messageEvent.content);
                int actionType = doodleAction.getActionType();
                if (actionType == JCDoodle.ACTION_START) {
                    setDoodleVisibility(View.VISIBLE);
                } else if (actionType == JCDoodle.ACTION_STOP) {
                    setDoodleVisibility(View.INVISIBLE);
                } else if (actionType == JCDoodle.ACTION_EXTRA_BASE + DOODLE_VERSION_CHECKER) {
                    mBtnDoodleVideo.setEnabled(true);
                    mBtnDoodleAudio.setEnabled(true);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void updateUI() {
        List<JCCallItem> callItems = JCManager.getInstance().call.getCallItems();
        if (callItems.size() == 0) {
            stopCallInfoTimer();
            removeCanvas();
            finish();
        } else {
            startCallInfoTimer();
            JCCallItem item = JCCallUtils.getActiveCall();
            boolean singleCall = callItems.size() == 1;
            mTxtUserId.setVisibility(singleCall && !mFullScreen ? View.VISIBLE : View.INVISIBLE);
            mTxtCallInfo.setVisibility(singleCall && !mFullScreen ? View.VISIBLE : View.INVISIBLE);
            mTxtNetStatus.setVisibility(singleCall && !mFullScreen ? View.VISIBLE : View.INVISIBLE);
            mListCalls.setVisibility(singleCall || mFullScreen ? View.INVISIBLE : View.VISIBLE);
            if (singleCall) {
                mTxtUserId.setText(item.getDisplayName());
            }
            boolean needAnswer = item.getDirection() == JCCall.DIRECTION_IN && item.getState() == JCCall.STATE_PENDING;
            boolean video = item.getVideo();
            mAudioIn.setVisibility(!video && needAnswer && !mFullScreen ? View.VISIBLE : View.INVISIBLE);
            mAudioInCall.setVisibility(!video && !needAnswer && !mFullScreen ? View.VISIBLE : View.INVISIBLE);
            mVideoIn.setVisibility(video && needAnswer && !mFullScreen ? View.VISIBLE : View.INVISIBLE);
            mVideoInCall.setVisibility(video && !needAnswer && !mFullScreen ? View.VISIBLE : View.INVISIBLE);
            if (video) {
                dealCanvas(item);
                updateVideoInCallViews(item);
            } else {
                removeCanvas();
                updateAudioInCallViews(item);
            }
            dealNeedAnswerCall();
        }
    }

    private void dealNeedAnswerCall() {
        if (mAlertAnswer == null) {
            final JCCallItem itemNeedAnswerNotActive = JCCallUtils.getNeedAnswerNotActiveCall();
            if (itemNeedAnswerNotActive != null) {
                mAlertAnswer = new AlertDialog.Builder(this)
                        .setTitle(R.string.tip)
                        .setMessage(R.string.tip_answer_call)
                        .setPositiveButton(R.string.answer, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                JCManager.getInstance().call.answer(itemNeedAnswerNotActive, false);
                                mAlertAnswer = null;
                            }
                        })
                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                JCManager.getInstance().call.term(itemNeedAnswerNotActive, JCCall.REASON_BUSY, "");
                                mAlertAnswer = null;
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        }
    }

    private void updateAudioInCallViews(JCCallItem item) {
        mBtnSpeakerAudio.setSelected(JCManager.getInstance().mediaDevice.isSpeakerOn());
        if (item.getState() == JCCall.STATE_TALKING) {
            mBtnMuteAudio.setEnabled(true);
            mBtnRecordAudio.setEnabled(true);
            mBtnHoldAudio.setEnabled(!item.getHeld());
            mBtnHoldAudio.setSelected(item.getHold());
            mBtnMuteAudio.setSelected(item.getMute());
            mBtnRecordAudio.setSelected(item.getAudioRecord());
        } else {
            mBtnMuteAudio.setEnabled(false);
            mBtnHoldAudio.setEnabled(false);
            mBtnDoodleAudio.setEnabled(false);
            mBtnRecordAudio.setEnabled(false);
        }
    }

    private void updateVideoInCallViews(JCCallItem item) {
        mBtnSpeakerVideo.setSelected(JCManager.getInstance().mediaDevice.isSpeakerOn());
        if (item.getState() == JCCall.STATE_TALKING) {
            mBtnCameraVideo.setEnabled(true);
            mBtnMuteVideo.setEnabled(true);
            mBtnMuteVideo.setSelected(item.getMute());
            mBtnCameraVideo.setSelected(item.getUploadVideoStreamSelf());
            mBtnCaptureRemote.setEnabled(item.getUploadVideoStreamOther());
            mBtnCaptureLocal.setEnabled(item.getUploadVideoStreamSelf());
            mBtnRecordLocalVideo.setEnabled(item.getUploadVideoStreamSelf());
            mBtnRecordRemoteVideo.setEnabled(item.getUploadVideoStreamOther());
            mBtnRecordLocalVideo.setSelected(item.getLocalVideoRecord());
            mBtnRecordRemoteVideo.setSelected(item.getRemoteVideoRecord());
        } else {
            mBtnMuteVideo.setEnabled(false);
            mBtnHoldAudio.setEnabled(false);
            mBtnCameraVideo.setEnabled(false);
            mBtnDoodleVideo.setEnabled(false);
            mBtnCaptureRemote.setEnabled(false);
            mBtnRecordRemoteVideo.setEnabled(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void dealCanvas(JCCallItem item) {
        boolean change = false; // 是否有视频窗口变化
        if (mLocalCanvas == null && item.getUploadVideoStreamSelf()) {
            mLocalCanvas = JCManager.getInstance().mediaDevice.startCameraVideo(JCMediaDevice.RENDER_FULL_CONTENT);
            if (mLocalCanvas != null) {
                mLocalCanvas.getVideoView().setZOrderMediaOverlay(true);
                mLocalCanvas.getVideoView().setId(View.generateViewId());
                mContentView.addView(mLocalCanvas.getVideoView(), 0);
                change = true;
            }
        } else if (mLocalCanvas != null && !item.getUploadVideoStreamSelf()) {
            JCManager.getInstance().mediaDevice.stopVideo(mLocalCanvas);
            mContentView.removeView(mLocalCanvas.getVideoView());
            mLocalCanvas = null;
            change = true;
        }

        if (item.getState() == JCCall.STATE_TALKING) {
            if (mRemoteCanvas == null && item.getUploadVideoStreamOther()) {
                mRemoteCanvas = JCManager.getInstance().mediaDevice.startVideo(item.getRenderId(), JCMediaDevice.RENDER_FULL_CONTENT);
                mRemoteCanvas.getVideoView().setId(View.generateViewId());
                mContentView.addView(mRemoteCanvas.getVideoView(), 0);
                change = true;
            } else if (mRemoteCanvas != null && !item.getUploadVideoStreamOther()) {
                JCManager.getInstance().mediaDevice.stopVideo(mRemoteCanvas);
                mContentView.removeView(mRemoteCanvas.getVideoView());
                mRemoteCanvas = null;
                change = true;
            }
        }

        // 处理视频窗口大小
        if (change) {
            if (mLocalCanvas != null && mRemoteCanvas != null) {
                mContentView.removeView(mLocalCanvas.getVideoView());
                mContentView.removeView(mRemoteCanvas.getVideoView());
                mContentView.addView(mRemoteCanvas.getVideoView(), 0);
                mContentView.addView(mLocalCanvas.getVideoView(), 1);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(mContentView);
                constraintSet.constrainWidth(mLocalCanvas.getVideoView().getId(), Utils.dip2px(this, 80));
                constraintSet.constrainHeight(mLocalCanvas.getVideoView().getId(), Utils.dip2px(this, 120));
                constraintSet.connect(mLocalCanvas.getVideoView().getId(),
                        ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, Utils.dip2px(this, 8));
                constraintSet.connect(mLocalCanvas.getVideoView().getId(),
                        ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, Utils.dip2px(this, 24));
                constraintSet.applyTo(mContentView);
            } else if (mLocalCanvas != null) {
                mContentView.removeView(mLocalCanvas.getVideoView());
                mContentView.addView(mLocalCanvas.getVideoView(), 0,
                        new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            } else if (mRemoteCanvas != null) {
                mContentView.removeView(mRemoteCanvas.getVideoView());
                mContentView.addView(mRemoteCanvas.getVideoView(), 0,
                        new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }
    }

    private void removeCanvas() {
        if (mLocalCanvas != null) {
            mContentView.removeView(mLocalCanvas.getVideoView());
            JCManager.getInstance().mediaDevice.stopVideo(mLocalCanvas);
            mLocalCanvas = null;
        }
        if (mRemoteCanvas != null) {
            mContentView.removeView(mRemoteCanvas.getVideoView());
            JCManager.getInstance().mediaDevice.stopVideo(mRemoteCanvas);
            mRemoteCanvas = null;
        }
    }

    private void startCallInfoTimer() {
        if (mCallInfoTimer != null) {
            stopCallInfoTimer();
        }
        mCallInfoTimer = new Timer();
        mCallInfoTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mTxtCallInfo.post(new Runnable() {
                    @Override
                    public void run() {
                        if (JCManager.getInstance().call.getCallItems().size() == 1) {
                            JCCallItem item = JCCallUtils.getActiveCall();
                            if (item != null) {
                                mTxtCallInfo.setText(JCCallUtils.genCallInfo(item));
                                mTxtNetStatus.setText(JCCallUtils.genNetStatus(item));
                            }
                        } else {
                            mListCalls.getAdapter().notifyDataSetChanged();
                        }
                    }
                });

            }
        }, 0, 1000);
    }

    private void stopCallInfoTimer() {
        if (mCallInfoTimer != null) {
            mCallInfoTimer.cancel();
            mCallInfoTimer = null;
        }
    }

    public void onFullScreen(View view) {
        JCCallItem item = JCCallUtils.getActiveCall();
        if (item != null && item.getVideo()) {
            mFullScreen = !mFullScreen;
            switchFullScreen();
        }
    }

    private void switchFullScreen() {
        Utils.showSystemUI(this, !mFullScreen);
        Utils.setActivityFullScreen(this, mFullScreen);
        JCCallItem item = JCCallUtils.getActiveCall();
        if (item != null && item.getVideo()) {
            mTxtUserId.setVisibility(mFullScreen ? View.INVISIBLE : View.VISIBLE);
            mTxtCallInfo.setVisibility(mFullScreen ? View.INVISIBLE : View.VISIBLE);
            mTxtNetStatus.setVisibility(mFullScreen ? View.INVISIBLE : View.VISIBLE);
            if (mFullScreen) {
                mVideoIn.setVisibility(View.INVISIBLE);
                mVideoInCall.setVisibility(View.INVISIBLE);
            } else {
                if (item.getDirection() == JCCall.DIRECTION_IN && (item.getState() < JCCall.STATE_CONNECTING || item.getState() > JCCall.STATE_OK)) {
                    mVideoIn.setVisibility(View.VISIBLE);
                } else {
                    mVideoInCall.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void updateStatistics() {
        try {
            JSONObject object = new JSONObject(JCManager.getInstance().call.getStatistics());
            StringBuilder builder = new StringBuilder();
            builder.append("*********Audio*********\n")
                    .append(object.optString("Audio"))
                    .append("\n")
                    .append("*********Video*********\n")
                    .append(object.optString("Video"))
                    .append("\n")
                    .append("*********Mtp*********\n")
                    .append(object.optString("Mtp"))
                    .append("\n");
            mTextStatistics.setText(builder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setDoodleVisibility(int visibility) {
        if (mDoodleLayout == null) {
            mDoodleLayout = new DoodleLayout(this);
            mViewDoodleLayer.addView(mDoodleLayout);
            mDoodleLayout.injectJCDoodle(mJCDoodle);
            mDoodleLayout.setDoodleControllerListener(mDoodleControllerListener);
            mJCDoodle.bindDoodleInteractor(mDoodleLayout);
        }
        mViewDoodleLayer.setVisibility(visibility);
    }

    private String generateAudioFileName(JCCallItem item) {
        File rootPath = new File(Environment.getExternalStorageDirectory()
                + File.separator + DEFAULT_DIR_NAME + File.separator + AUDIO_RECORD_DIR);
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.PRC);
        String currentDate = sdf.format(new Date());
        return rootPath.getAbsolutePath() + File.separator + item.getUserId() + "_" + currentDate + ".wmv";
    }

    private String generateSnapshotFileName(JCCallItem item) {
        File rootPath = new File(Environment.getExternalStorageDirectory()
                + File.separator + DEFAULT_DIR_NAME + File.separator + SNAPSHOT);
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.PRC);
        String currentDate = sdf.format(new Date());
        return rootPath.getAbsolutePath() + File.separator + item.getUserId() + "_" + currentDate + ".jpg";
    }

    private String generateVideoRecordFileName(JCCallItem item) {
        File rootPath = new File(Environment.getExternalStorageDirectory()
                + File.separator + DEFAULT_DIR_NAME + File.separator + VIDEO_RECORD_DIR);
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.PRC);
        String currentDate = sdf.format(new Date());
        return rootPath.getAbsolutePath() + File.separator + item.getUserId() + "_" + currentDate + ".mp4";
    }

    /**
     * 键盘事件双击判断
     */
    private void clickOrDoubleClick() {
        if (mWaitDoubleClick) {
            mWaitDoubleClick = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!mWaitDoubleClick) {
                        mWaitDoubleClick = true;
                    }
                }
            }, 350);
        } else {
            mWaitDoubleClick = true;
            mStatisticsScheduled.cancel(true);
            mStatisticsScheduled = null;
            mTextStatistics.setVisibility(View.INVISIBLE);
            if (findViewById(R.id.btnStatisticsVideoInCall).getVisibility() == View.VISIBLE) {
                findViewById(R.id.btnStatisticsVideoInCall).requestFocus();
            } else if (findViewById(R.id.btnStatisticsInCall).getVisibility() == View.VISIBLE) {
                findViewById(R.id.btnStatisticsInCall).requestFocus();
            }
        }
    }
}

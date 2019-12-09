package com.juphoon.cloud.sample;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.juphoon.cloud.JCDoodle;
import com.juphoon.cloud.JCDoodleAction;
import com.juphoon.cloud.JCDoodleCallback;
import com.juphoon.cloud.JCMediaChannel;
import com.juphoon.cloud.JCMediaChannelParticipant;
import com.juphoon.cloud.JCMediaChannelUtils;
import com.juphoon.cloud.JCMediaDevice;
import com.juphoon.cloud.JCMediaDeviceVideoCanvas;
import com.juphoon.cloud.doodle.DoodleLayout;
import com.juphoon.cloud.sample.JCWrapper.JCConfUtils;
import com.juphoon.cloud.sample.JCWrapper.JCEvent.JCConfMessageEvent;
import com.juphoon.cloud.sample.JCWrapper.JCEvent.JCConfStopEvent;
import com.juphoon.cloud.sample.JCWrapper.JCEvent.JCEvent;
import com.juphoon.cloud.sample.JCWrapper.JCEvent.JCJoinEvent;
import com.juphoon.cloud.sample.JCWrapper.JCManager;
import com.juphoon.cloud.sample.Toos.ConfStatistic;
import com.juphoon.cloud.sample.Toos.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ConferenceActivity extends AppCompatActivity {

    private static final int DOODLE_VERSION_CHECKER = 1;

    private FrameLayout mPartpLayout;
    private View mControlLayout;
    private boolean mFullScreen;
    private Button mBtnSpeaker;
    private Button mBtnSendAudio;
    private Button mBtnSendVideo;
    private Button mBtnAudioOut;
    private Button mBtnScreenShare;
    private Button mBtnCdn;
    private Button mBtnRecord;
    private Button mBtnDoodleVideo;
    private TextView mTextStatistics;
    private ScrollView mScoolTextStatistics;
    private TextView mLocalSendStatistics;
    private TextView mLocalRecvStatistics;
    private TextView mPartPStatistics;

    private ScheduledExecutorService mScheduledExecutor = new ScheduledThreadPoolExecutor(1);
    private ScheduledFuture mStatisticsScheduled;
    private GestureDetector mGestureDetector;

    // 旋转角度计数
    private int mAngleIndex = 0;

    /*
     * 遥控双击标识
     *
     */
    private boolean mWaitDoubleClick = true;

    private JCDoodle mJCDoodle;
    private ViewGroup mViewDoodleLayer;
    private DoodleLayout mDoodleLayout;

    class Item {
        JCMediaChannelParticipant partp;
        JCConfUtils.SubViewRect rect;
        JCMediaDeviceVideoCanvas canvas;

        ConstraintLayout constraintLayout;
        TextView txtInfo;

        Item() {
            constraintLayout = (ConstraintLayout) ConferenceActivity.this.getLayoutInflater().inflate(R.layout.view_partp, null);
            txtInfo = (TextView) constraintLayout.findViewById(R.id.txtInfo);
        }

        void reset() {
            if (canvas != null) {
                // 关闭视频请求
                if (!isSelf(this)) {
                    JCManager.getInstance().mediaChannel.requestVideo(partp, JCMediaChannel.PICTURESIZE_NONE);
                }
                JCManager.getInstance().mediaDevice.stopVideo(canvas);
                constraintLayout.removeView(canvas.getVideoView());
                canvas = null;
            }
        }

        void delete() {
            reset();
            mPartpLayout.removeView(constraintLayout);
        }
    }

    private List<Item> mItems = new ArrayList<>();
    private JCMediaDeviceVideoCanvas mScreenShare;

    private final JCDoodleCallback mDoodleCallback = new JCDoodleCallback() {
        @Override
        public void onDoodleActionGenerated(JCDoodleAction jcDoodleAction) {
            JCManager.getInstance().mediaChannel.sendMessage(JCDoodle.DATA_TYPE_DOODLE, mJCDoodle.stringFromDoodleAction(jcDoodleAction), null);
        }
    };

    private final DoodleLayout.DoodleControllerListener mDoodleControllerListener = new DoodleLayout.DoodleControllerListener() {
        @Override
        public void onExitDoodle() {
            setDoodleVisibility(View.INVISIBLE);
            JCDoodleAction doodleAction = new JCDoodleAction.Builder(JCDoodle.ACTION_STOP).build();
            JCManager.getInstance().mediaChannel.sendMessage(JCDoodle.DATA_TYPE_DOODLE, mJCDoodle.stringFromDoodleAction(doodleAction), null);
        }

        @Override
        public void onFaceMode() {
            Toast.makeText(ConferenceActivity.this, "FaceMode Clicked", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onShareDoodle(Bitmap bitmap) {
            Toast.makeText(ConferenceActivity.this, "Share Doodle Clicked", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onWillCleanDoodle() {
            AlertDialog.Builder builder = new AlertDialog.Builder(ConferenceActivity.this);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_conference);

        mFullScreen = false;
        mControlLayout = findViewById(R.id.controlLayout);
        mPartpLayout = (FrameLayout) findViewById(R.id.partpLayout);

        mBtnSpeaker = (Button) findViewById(R.id.btnSpeaker);
        mBtnSendAudio = (Button) findViewById(R.id.btnSendAudio);
        mBtnSendVideo = (Button) findViewById(R.id.btnSendVideo);
        mBtnAudioOut = (Button) findViewById(R.id.btnAudioOut);
        mBtnScreenShare = (Button) findViewById(R.id.btnShowScreenShare);
        mBtnCdn = (Button) findViewById(R.id.btnCdn);
        mBtnRecord = (Button) findViewById(R.id.btnRecord);
        mTextStatistics = (TextView) findViewById(R.id.textStatistics);
        mScoolTextStatistics = findViewById(R.id.newTextStatistics);
        mLocalSendStatistics = findViewById(R.id.send_statistic);
        mLocalRecvStatistics = findViewById(R.id.recv_statistic);
        mPartPStatistics = findViewById(R.id.partP_statistic);
        mTextStatistics.setMovementMethod(ScrollingMovementMethod.getInstance());

        mBtnDoodleVideo = findViewById(R.id.btnDoodleVideo);
        mJCDoodle = JCDoodle.create(mDoodleCallback);
        mViewDoodleLayer = findViewById(R.id.doodle_layer);
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                mStatisticsScheduled.cancel(true);
                mStatisticsScheduled = null;
                mTextStatistics.setVisibility(View.INVISIBLE);
                findViewById(R.id.btnStatistics).requestFocus();
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
        if (JCManager.getInstance().mediaChannel.getState() == JCMediaChannel.STATE_IDLE) {
            showJoinFail(JCMediaChannel.REASON_OTHER);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        switchFullScreen();
        layoutPartp();
        updateControlButtons();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Item item : mItems) {
            item.delete();
        }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        EventBus.getDefault().unregister(this);
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

    public void onSwitchCamera(View view) {
        JCManager.getInstance().mediaDevice.switchCamera();
    }

    public void onSendAudio(View view) {
        JCManager.getInstance().mediaChannel.enableUploadAudioStream(!JCManager.getInstance().mediaChannel.getUploadLocalAudio());
    }

    public void onSendVideo(View view) {
        JCManager.getInstance().mediaChannel.enableUploadVideoStream(!JCManager.getInstance().mediaChannel.getUploadLocalVideo());
    }

    public void onAudioOut(View view) {
        JCManager.getInstance().mediaChannel.enableAudioOutput(!JCManager.getInstance().mediaChannel.getAudioOutput());
    }

    public void onSpeaker(View view) {
        JCManager.getInstance().mediaDevice.enableSpeaker(!JCManager.getInstance().mediaDevice.isSpeakerOn());
        updateControlButtons();
    }

    public void onLeave(View view) {
        JCManager.getInstance().mediaChannel.leave();
        finish();
    }

    public void onScreenShare(View view) {
        if (!TextUtils.isEmpty(JCManager.getInstance().mediaChannel.getScreenUserId())) {
            startScreenShare(mScreenShare == null);
            updateControlButtons();
        }
    }

    public void onSip(View view) {
        View sendView = getLayoutInflater().inflate(R.layout.view_conf_send, null);
        LinearLayout layout = sendView.findViewById(R.id.core_net_id_layout);
        layout.setVisibility(View.VISIBLE);
        LinearLayout paramlayout = sendView.findViewById(R.id.sip_param_layout);
        paramlayout.setVisibility(View.VISIBLE);
        final EditText editContent = (EditText) sendView.findViewById(R.id.editContent);
        final EditText editCoreNetId = sendView.findViewById(R.id.core_net_id_edit_text);
        final EditText displayName = (EditText) sendView.findViewById(R.id.display);
        final EditText dtmfPassowrd = (EditText) sendView.findViewById(R.id.dtmfPassowrd);
        final Switch sipUri = (Switch) sendView.findViewById(R.id.sip_uri);
        final Switch route = (Switch) sendView.findViewById(R.id.route);
        final Switch mcu = (Switch) sendView.findViewById(R.id.mcu);
        final Switch video = (Switch) sendView.findViewById(R.id.video);
        editContent.setHint(getString(R.string.prompt_content_uri));
        sipUri.setChecked(true);
        route.setChecked(false);
        mcu.setChecked(true);
        video.setChecked(true);
        sipUri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    route.setChecked(false);
                }
            }
        });
        route.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !sipUri.isChecked()) {
                    route.setChecked(false);
                }
            }
        });
        String currentCoreNetId = JCManager.getInstance().mediaChannel.getConfig(JCMediaChannel.CONFIG_SIP_CORE_NETWORK);
        editCoreNetId.setText(currentCoreNetId);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(sendView);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String content = editContent.getText().toString().trim();
                String coreNetId = editCoreNetId.getText().toString().trim();
                if (!TextUtils.isEmpty(coreNetId)) {
                    JCManager.getInstance().mediaChannel.setConfig(JCMediaChannel.CONFIG_SIP_CORE_NETWORK, coreNetId);
                }
                JCManager.getInstance().mediaChannel.inviteSipUser(content, JCMediaChannelUtils.buildSipParam(sipUri.isChecked(), route.isChecked(), displayName.getText().toString(), mcu.isChecked(), video.isChecked(), dtmfPassowrd.getText().toString()));
            }
        });
        builder.create().show();
    }

    public void onCdn(View view) {
        if (JCManager.getInstance().mediaChannel.getCdnState() != JCMediaChannel.CDN_STATE_NONE) {
            int keyInterval = TextUtils.isEmpty(PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.cloud_setting_key_conference_key_interval), "-1"))
                    ? -1 : Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.cloud_setting_key_conference_key_interval), "-1"));
            if (JCManager.getInstance().mediaChannel.getCdnState() == JCMediaChannel.CDN_STATE_READY) {
                JCManager.getInstance().mediaChannel.enableCdn(true, keyInterval);
            } else if (JCManager.getInstance().mediaChannel.getCdnState() == JCMediaChannel.CDN_STATE_RUNNING) {
                JCManager.getInstance().mediaChannel.enableCdn(false, keyInterval);
            }
        }
    }

    public void onRecord(View view) {
        if (JCManager.getInstance().mediaChannel.getRecordState() != JCMediaChannel.RECORD_STATE_NONE) {
            if (JCManager.getInstance().mediaChannel.getRecordState() == JCMediaChannel.RECORD_STATE_READY) {
                JCManager.getInstance().mediaChannel.enableRecord(true, null, null);
            } else if (JCManager.getInstance().mediaChannel.getRecordState() == JCMediaChannel.RECORD_STATE_RUNNING) {
                JCManager.getInstance().mediaChannel.enableRecord(false, null, null);
            }
        }
    }

    public void onEnableScreenShare(View view) {
        if (TextUtils.isEmpty(JCManager.getInstance().mediaChannel.getScreenUserId())) {
            JCManager.getInstance().mediaChannel.enableScreenShare(true);
        } else {
            JCManager.getInstance().mediaChannel.enableScreenShare(false);
        }
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

    public void onSendMessage(View view) {
        View sendView = getLayoutInflater().inflate(R.layout.view_conf_send, null);
        final Spinner spinnerMessageTo = (Spinner) sendView.findViewById(R.id.spinnerMessageTo);
        final EditText editContent = (EditText) sendView.findViewById(R.id.editContent);
        List<String> items = new ArrayList<>();
        items.add("All");
        for (Item item : mItems) {
            if (!isSelf(item)) {
                items.add(item.partp.getUserId());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ConferenceActivity.this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMessageTo.setAdapter(adapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(sendView);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String content = editContent.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    String sendTo = spinnerMessageTo.getSelectedItem().toString();
                    if (TextUtils.equals(sendTo, "All")) {
                        JCManager.getInstance().mediaChannel.sendMessage("Text", content, null);
                    } else {
                        JCManager.getInstance().mediaChannel.sendMessage("Text", content, sendTo);
                    }
                }
            }
        });
        builder.create().show();
    }

    public void onSendCommand(View view) {
        View sendView = getLayoutInflater().inflate(R.layout.view_conf_send, null);
        final Spinner spinnerCommands = (Spinner) sendView.findViewById(R.id.spinnerMessageTo);
        final EditText editContent = (EditText) sendView.findViewById(R.id.editContent);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                ConferenceActivity.this,
                R.array.commands,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCommands.setAdapter(adapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(sendView);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                JCManager.getInstance().mediaChannel.sendCommand(
                        spinnerCommands.getSelectedItem().toString(),
                        editContent.getText().toString().trim());
            }
        });
        builder.create().show();
    }

    public void onStop(View view) {
        JCManager.getInstance().mediaChannel.stop();
    }

    public void onRotate(View view) {
        int angle = 90 * ++mAngleIndex % 360;
        for (Item item : mItems) {
            if (item.canvas != null) {
                item.canvas.rotate(angle);
            }
        }
    }

    public void onFullScreen(View view) {
        mFullScreen = !mFullScreen;
        switchFullScreen();
    }

    public void onUri(View view) {
        String uri = JCManager.getInstance().mediaChannel.getChannelUri();
        if (TextUtils.isEmpty(uri)) {
            Toast.makeText(this, "uri 为空", Toast.LENGTH_SHORT).show();
            return;
        }
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(null, uri);
        clipboard.setPrimaryClip(clipData);
        Toast.makeText(this, "uri 已拷贝", Toast.LENGTH_SHORT).show();
    }

    public void onRequestVideoPicSize(View view) {
        final List<JCMediaChannelParticipant> items = new ArrayList<>();
        for (Item item : mItems) {
            if (!isSelf(item)) {
                items.add(item.partp);
            }
        }
        List<String> itemUids = new ArrayList<>();
        for (JCMediaChannelParticipant item : items) {
            itemUids.add(item.getUserId());
        }
        View sendView = getLayoutInflater().inflate(R.layout.view_conf_requestvideo_picsize, null);
        final Spinner requestTo = (Spinner) sendView.findViewById(R.id.spinnerRequestTo);
        final Spinner requestSize = (Spinner) sendView.findViewById(R.id.spinnerPicSize);
        requestTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                requestSize.setSelection(items.get(position).getPictureSize());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] picSizes = getResources().getStringArray(R.array.pic_size);
        ArrayAdapter<String> adapterRequestTo = new ArrayAdapter<>(ConferenceActivity.this, android.R.layout.simple_spinner_item, itemUids);
        ArrayAdapter<String> adapterRequestSize = new ArrayAdapter<>(ConferenceActivity.this, android.R.layout.simple_spinner_item, picSizes);
        requestTo.setAdapter(adapterRequestTo);
        requestSize.setAdapter(adapterRequestSize);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(sendView);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int requestItem = requestTo.getSelectedItemPosition();
                int picSize = requestSize.getSelectedItemPosition();
                if (items.isEmpty()) {
                    return;
                }
                JCManager.getInstance().mediaChannel.requestVideo(items.get(requestItem), picSize);
            }
        });
        builder.create().show();
    }

    @Subscribe
    public void onEvent(JCEvent event) {
        if (event.getEventType() == JCEvent.EventType.CONFERENCE_JOIN) {
            JCJoinEvent join = (JCJoinEvent) event;
            if (join.result) {
                layoutPartp();
            } else {
                showJoinFail(join.reason);
            }
        } else if (event.getEventType() == JCEvent.EventType.CONFERENCE_LEAVE) {
            finish();
        } else if (event.getEventType() == JCEvent.EventType.CONFERENCE_STOP) {
            JCConfStopEvent stopEvent = (JCConfStopEvent) event;
            if (stopEvent.result) {
                Toast.makeText(this, "解散成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "解散失败", Toast.LENGTH_SHORT).show();
            }
        } else if (event.getEventType() == JCEvent.EventType.CONFERENCE_PARTP_JOIN
                || event.getEventType() == JCEvent.EventType.CONFERENCE_PARTP_LEAVE) {
            layoutPartp();
        } else if (event.getEventType() == JCEvent.EventType.CONFERENCE_PARTP_UPDATE) {
            updatePartp();
        } else if (event.getEventType() == JCEvent.EventType.CONFERENCE_PROP_CHANGE) {
            if (TextUtils.isEmpty(JCManager.getInstance().mediaChannel.getScreenUserId())) {
                if (mScreenShare != null) {
                    startScreenShare(false);
                }
            }
            updateControlButtons();
        } else if (event.getEventType() == JCEvent.EventType.CONFERENCE_MESSAGE_RECEIVED) {
            JCConfMessageEvent messageEvent = (JCConfMessageEvent) event;
            if (messageEvent.type.equals(JCDoodle.DATA_TYPE_DOODLE)) {
                JCDoodleAction doodleAction = mJCDoodle.doodleActionFromString(messageEvent.content);
                int actionType = doodleAction.getActionType();
                if (actionType == JCDoodle.ACTION_START) {
                    setDoodleVisibility(View.VISIBLE);
                } else if (actionType == JCDoodle.ACTION_STOP) {
                    setDoodleVisibility(View.INVISIBLE);
                } else if (actionType == JCDoodle.ACTION_EXTRA_BASE + DOODLE_VERSION_CHECKER) {
                    mBtnDoodleVideo.setEnabled(true);
                }
            } else {
                Toast.makeText(this, String.format("收到%s发送的信息 类型:%s内容:%s", messageEvent.fromUserId, messageEvent.type, messageEvent.content), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showJoinFail(int reason) {
        int messageResId = R.string.conference_join_fail_reason_other;
        switch (reason) {
            case JCMediaChannel.REASON_INVALID_PASSWORD:
                messageResId = R.string.conference_join_fail_reason_invalid_password;
                break;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(messageResId);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }

    private void layoutPartp() {
        // 界面还未有长宽
        if (mPartpLayout.getWidth() == 0) {
            mPartpLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    layoutPartp();
                }
            }, 500);
            return;
        }
        // TODO 可优化，这里主要处理每个成员试图对象的建立，并给该对象成员赋值，显示
        List<JCMediaChannelParticipant> partps = JCManager.getInstance().mediaChannel.getParticipants();
        List<JCConfUtils.SubViewRect> subViewRects = JCConfUtils.caclSubViewRect(
                mPartpLayout.getWidth(), mPartpLayout.getHeight(), partps.size());
        for (int i = 0; ; i++) {
            if (i < partps.size()) {
                JCMediaChannelParticipant partp = partps.get(i);
                JCConfUtils.SubViewRect subViewRect = subViewRects.get(i);
                Item item;
                if (mItems.size() <= i) {
                    item = new Item();
                    mItems.add(item);
                    mPartpLayout.addView(item.constraintLayout);
                } else {
                    item = mItems.get(i);
                }
                if (item.partp != partp) {
                    item.reset();
                    item.partp = partp;
                }
                item.rect = subViewRect;

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(subViewRect.width, subViewRect.height);
                params.setMargins(subViewRect.x, subViewRect.y, 0, 0);
                item.constraintLayout.setLayoutParams(params);
                continue;
            } else if (i < mItems.size()) {
                for (int j = mItems.size() - 1; j >= i; j--) {
                    mItems.get(j).delete();
                    mItems.remove(j);
                }
            }
            break;
        }
        updatePartp();
    }

    private void updateControlButtons() {
        mBtnAudioOut.setSelected(JCManager.getInstance().mediaChannel.getAudioOutput());
        mBtnSendAudio.setSelected(JCManager.getInstance().mediaChannel.getUploadLocalAudio());
        mBtnSendVideo.setSelected(JCManager.getInstance().mediaChannel.getUploadLocalVideo());
        mBtnSpeaker.setSelected(JCManager.getInstance().mediaDevice.isSpeakerOn());
        mBtnScreenShare.setEnabled(!TextUtils.isEmpty(JCManager.getInstance().mediaChannel.getScreenRenderId()));
        mBtnScreenShare.setSelected(mScreenShare != null);
        mBtnCdn.setEnabled(JCManager.getInstance().mediaChannel.getCdnState() != JCMediaChannel.CDN_STATE_NONE);
        mBtnCdn.setSelected(JCManager.getInstance().mediaChannel.getCdnState() == JCMediaChannel.CDN_STATE_RUNNING);
        mBtnRecord.setEnabled(JCManager.getInstance().mediaChannel.getRecordState() != JCMediaChannel.RECORD_STATE_NONE);
        mBtnRecord.setSelected(JCManager.getInstance().mediaChannel.getRecordState() == JCMediaChannel.RECORD_STATE_RUNNING);
    }

    private void updatePartp() {
        // TODO 可优化，每个成员视图信息显示
        for (Item item : mItems) {
            item.txtInfo.setText(String.format(Locale.getDefault(),
                    "%s\naudio=%b\nvideo=%b\npictureSize=%d\nvolume=%d\ntype=%d\ntalking(sip)=%b\nnetwork=%s",
                    isSelf(item) ? getString(R.string.me) : item.partp.getDisplayName(),
                    item.partp.isAudio(),
                    item.partp.isVideo(),
                    item.partp.getPictureSize(),
                    item.partp.getVolumeStatus(),
                    item.partp.getType(),
                    item.partp.isTalking(),
                    JCConfUtils.genConferenceNetStatus(item.partp.getNetStatus())));
            if (isSelf(item)) {
                if (item.partp.isVideo() && mScreenShare == null) {
                    if (item.canvas == null) {
                        item.canvas = JCManager.getInstance().mediaDevice.startCameraVideo(JCMediaDevice.RENDER_FULL_CONTENT);
                        if (item.canvas != null) {
                            item.constraintLayout.addView(item.canvas.getVideoView(), 0);
                        }
                    }
                }
            } else {
                if (item.partp.isVideo() && mScreenShare == null) {
                    if (item.canvas == null) {
                        int videoSize = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(this)
                                .getString(getString(R.string.cloud_setting_key_conference_request_video_size),
                                        String.valueOf(JCMediaChannel.PICTURESIZE_LARGE)));
                        JCManager.getInstance().mediaChannel.requestVideo(item.partp, videoSize);
                        item.canvas = JCManager.getInstance().mediaDevice.startVideo(item.partp.getRenderId(), JCMediaDevice.RENDER_FULL_CONTENT);
                        item.constraintLayout.addView(item.canvas.getVideoView(), 0);
                    }
                }
            }
            // 当用户没有视频或者显示屏幕分享是关闭视频流
            if (!item.partp.isVideo() || (mScreenShare != null && !isSelf(item))) {
                if (item.canvas != null) {
                    item.reset();
                }
            }
        }
    }

    private boolean isSelf(Item item) {
        return TextUtils.equals(item.partp.getUserId(), JCManager.getInstance().client.getUserId());
    }

    private void startScreenShare(boolean start) {
        if (start) {
            JCManager.getInstance().mediaChannel.requestScreenVideo(JCManager.getInstance().mediaChannel.getScreenRenderId(),
                    JCMediaChannel.PICTURESIZE_LARGE);
            mScreenShare = JCManager.getInstance().mediaDevice.startVideo(
                    JCManager.getInstance().mediaChannel.getScreenRenderId(), JCMediaDevice.RENDER_FULL_CONTENT);
            mScreenShare.getVideoView().setZOrderOnTop(true);
            mScreenShare.getVideoView().setZOrderMediaOverlay(true);
            mPartpLayout.addView(mScreenShare.getVideoView(), mPartpLayout.getWidth(), mPartpLayout.getHeight());
        } else {
            JCManager.getInstance().mediaChannel.requestScreenVideo(JCManager.getInstance().mediaChannel.getScreenRenderId(),
                    JCMediaChannel.PICTURESIZE_NONE);
            mPartpLayout.removeView(mScreenShare.getVideoView());
            JCManager.getInstance().mediaDevice.stopVideo(mScreenShare);
            mScreenShare = null;
        }
        updatePartp();
    }

    private void switchFullScreen() {
        Utils.showSystemUI(this, !mFullScreen);
        Utils.setActivityFullScreen(this, mFullScreen);
        mControlLayout.setVisibility(mFullScreen ? View.INVISIBLE : View.VISIBLE);
        for (Item item : mItems) {
            item.txtInfo.setVisibility(mFullScreen ? View.INVISIBLE : View.VISIBLE);
        }
    }

    public void onConfStatistics(View view) {
        mScoolTextStatistics.setVisibility(View.VISIBLE);
        mScoolTextStatistics.requestFocus();
        updateConfStatistics();
        mStatisticsScheduled = mScheduledExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                mTextStatistics.post(new Runnable() {
                    @Override
                    public void run() {
                        updateConfStatistics();
                    }
                });
            }
        }, 0, 3, TimeUnit.SECONDS);
    }

    private void updateConfStatistics() {

        ConfStatistic statistic = ConfStatistic.parseStatisic(JCManager.getInstance().mediaChannel.getStatistics());

        StringBuilder localRecvBuilder = new StringBuilder();
        localRecvBuilder.append("*********接收统计*********\n");
        localRecvBuilder.append("接收总包数 :" + statistic.localStatistic.recvAllPackets + "\n");
        localRecvBuilder.append("接收总丢包数 :" + statistic.localStatistic.recvLost + "\n");
        localRecvBuilder.append("接收瞬时丢包率 :" + statistic.localStatistic.recvLostRate + "\n");
        localRecvBuilder.append("接收抖动 :" + statistic.localStatistic.recvJitter + "\n");
        localRecvBuilder.append("接收延时 :" + statistic.localStatistic.recvRTT + "\n");
        localRecvBuilder.append("接收带宽 :" + statistic.localStatistic.recvBitRate + "\n");
        StringBuilder localSendBuilder = new StringBuilder();
        localSendBuilder.append("*********发送统计*********\n");
        localSendBuilder.append("发送总包数 : " + statistic.localStatistic.sendAllPackets + "\n");
        localSendBuilder.append("发送总丢包数 : " + statistic.localStatistic.sendLost + "\n");
        localSendBuilder.append("发送瞬时丢包率 : " + statistic.localStatistic.sendLostRate + "\n");
        localSendBuilder.append("发送抖动 : " + statistic.localStatistic.sendJitter + "\n");
        localSendBuilder.append("发送延时 : " + statistic.localStatistic.sendRTT + "\n");
        localSendBuilder.append("发送带宽 : " + statistic.localStatistic.sendBitRate + "\n");
        localSendBuilder.append("音频编码" + statistic.localStatistic.audioCodec + "\n");
        localSendBuilder.append("视频编码" + statistic.localStatistic.videoCodec + "\n");
        StringBuilder partPBuilder = new StringBuilder();
        partPBuilder.append("*********成员统计*********\n");
        for (int i = 0; i < statistic.partpStatisticList.size(); i++) {
            ConfStatistic.PartpStatistic partPStatistic = statistic.partpStatisticList.get(i);
            partPBuilder.append("***************************\n");
            partPBuilder.append("用户名 : " + partPStatistic.uid + "\n");
            partPBuilder.append("视频数据包个数 : " + partPStatistic.videoPackets + "\n");
            partPBuilder.append("当前视频数据码率(视频带宽) : " + partPStatistic.videoBitrate + "\n");
            partPBuilder.append("当前镜头采集帧速 : " + partPStatistic.videoCaptureFr + "\n");
            partPBuilder.append("视频频冗余保护占的百分比 : " + partPStatistic.videoFecPrecent + "\n");
            partPBuilder.append("视频帧率 : " + partPStatistic.videoFPSFIR + "\n");
            partPBuilder.append("视频编码量化系数 :" + partPStatistic.videoQP + "\n");
            partPBuilder.append("视频分辨率 : " + partPStatistic.videoResolution + "\n");
            partPBuilder.append("当前视频渲染帧速 :" + partPStatistic.videoRenderFr + "\n");
            partPBuilder.append("音频数据包个数 : " + partPStatistic.audioPackets + "\n");
            partPBuilder.append("当前音频数据码率(音频带宽) : " + partPStatistic.audioBitRate + "\n");
            partPBuilder.append("音频冗余保护占的百分比 : " + partPStatistic.audioFecPrecent + "\n");
            partPBuilder.append("是否开启视频 : " + partPStatistic.video + "\n");
            partPBuilder.append("是否开启音频 : " + partPStatistic.audio + "\n");
            partPBuilder.append("是否开启屏幕共享:" + partPStatistic.screen + "\n");
        }
        mLocalRecvStatistics.setText(localRecvBuilder);
        mLocalSendStatistics.setText(localSendBuilder);
        mPartPStatistics.setText(partPBuilder);
    }

    private void updateStatistics() {
        try {
            JSONObject object = new JSONObject(JCManager.getInstance().mediaChannel.getStatistics());
            StringBuilder builder = new StringBuilder();
            builder.append("channelId:");
            builder.append(JCManager.getInstance().mediaChannel.getChannelId());
            builder.append("\n");
            builder.append("channelNumber:");
            builder.append(JCManager.getInstance().mediaChannel.getChannelNumber());
            builder.append("\n");
            builder.append("channelTitle:");
            builder.append(JCManager.getInstance().mediaChannel.getTitle());
            builder.append("\n");
            builder.append("*********Config*********\n")
                    .append(object.optString("Config"))
                    .append("\n")
                    .append("*********Network*********\n")
                    .append(object.optString("Network"))
                    .append("\n")
                    .append("*********Transport*********\n")
                    .append(object.optString("Transport"))
                    .append("\n")
                    .append("*********Participants*********\n");
            JSONArray array = object.optJSONArray("Participants");
            if (array != null) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    String key = obj.keys().next();
                    String value = obj.getString(key);
                    builder.append("UserId:")
                            .append(key)
                            .append("\n")
                            .append(value)
                            .append("\n");
                }
            }
            mTextStatistics.setText(builder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void onStartDoodle(View view) {
        JCDoodleAction.Builder builder = new JCDoodleAction.Builder(JCDoodle.ACTION_START);
        String action = mJCDoodle.stringFromDoodleAction(builder.build());
        JCManager.getInstance().mediaChannel.sendMessage(JCDoodle.DATA_TYPE_DOODLE, action, null);
        setDoodleVisibility(View.VISIBLE);
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
            findViewById(R.id.btnStatistics).requestFocus();
        }
    }

    public void onStatisticClose(View view) {
        mScoolTextStatistics.setVisibility(View.INVISIBLE);
    }
}

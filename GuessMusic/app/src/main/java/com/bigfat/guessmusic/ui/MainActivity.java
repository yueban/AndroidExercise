package com.bigfat.guessmusic.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bigfat.guessmusic.R;
import com.bigfat.guessmusic.adapter.WordGridAdapter;
import com.bigfat.guessmusic.constant.Constant;
import com.bigfat.guessmusic.model.Song;
import com.bigfat.guessmusic.model.Word;
import com.bigfat.guessmusic.observer.WordClickListener;
import com.bigfat.guessmusic.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, WordClickListener {

    public static final int WORD_COUNT = 24;//待选文字总数

    //控件
    private ImageButton mBtnPlayStart;//播放按钮
    private ImageView mViewPan;//唱片盘片
    private ImageView mViewPanBar;//唱片拨杆
    private GridView mWordGridView;//文字选择器
    private LinearLayout mSelectedWordsContainer;//已选文字容器
    private ArrayList<Button> mSelectedButtonList;//已选文字按钮

    //数据等对象
    private WordGridAdapter mWordAdapter;
    private ArrayList<Word> mAllWords;//待选文字
    private ArrayList<Word> mSelectedWords;//已选文字

    //唱片相关动画
    private Animation mPanAnim;
    private LinearInterpolator mPanLin;

    private Animation mBarInAnim;
    private LinearInterpolator mBarInLin;

    private Animation mBarOutAnim;
    private LinearInterpolator mBarOutLin;

    //状态量
    private boolean mIsRunning = false;//唱片旋转动画是否正在播放
    private Song mCurrentSong;//当前播放歌曲
    private int mCurrentStageIndex = -1;//当前关卡索引

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAnim();//初始化动画
        initAnimListener();

        initData();//初始化数据
        initView();
        initViewListener();
    }

    private void initData() {
        //读取当前歌曲信息
        mCurrentSong = loadStageSongInfo(++mCurrentStageIndex);
        //初始化已选文字
        mSelectedWords = getSelectedWords();
        //初始化待选文字
        mAllWords = getAllWords();
    }

    private void initAnim() {
        mPanAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        mPanLin = new LinearInterpolator();
        mPanAnim.setInterpolator(mPanLin);

        mBarInAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_45);
//        mBarInAnim.setFillAfter(true);
        mBarInLin = new LinearInterpolator();
        mBarInAnim.setInterpolator(mBarInLin);

        mBarOutAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_d_45);
//        mBarOutAnim.setFillAfter(true);
        mBarOutLin = new LinearInterpolator();
        mBarOutAnim.setInterpolator(mBarOutLin);
    }

    private void initAnimListener() {
        mPanAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mViewPanBar.startAnimation(mBarOutAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mBarInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mViewPan.startAnimation(mPanAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mBarOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsRunning = false;
                mBtnPlayStart.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initView() {
        mBtnPlayStart = (ImageButton) findViewById(R.id.btn_play_start);
        mViewPan = (ImageView) findViewById(R.id.imageView1);
        mViewPanBar = (ImageView) findViewById(R.id.imageView2);
        mWordGridView = (GridView) findViewById(R.id.name_select_grid_view);
        mSelectedWordsContainer = (LinearLayout) findViewById(R.id.word_selected_container);

        initSelectedWordsView();

        mWordAdapter = new WordGridAdapter(MainActivity.this, mAllWords);
        mWordAdapter.setWordClickListener(this);
        mWordGridView.setAdapter(mWordAdapter);
    }

    private void initSelectedWordsView() {
        if (mSelectedButtonList == null) {
            mSelectedButtonList = new ArrayList<>();
        } else {
            mSelectedButtonList.clear();
        }
        mSelectedWordsContainer.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(140, 140);
        for (int i = 0; i < mSelectedWords.size(); i++) {
            final int j = i;
            Button wordButton = (Button) LayoutInflater.from(MainActivity.this).inflate(R.layout.word_grid_view_item, null);
            wordButton.setTextColor(0xFFFFFFFF);
            wordButton.setText("");
            wordButton.setBackgroundResource(R.mipmap.game_wordblank);
            wordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearSelectedWord(j);
                }
            });

            mSelectedButtonList.add(wordButton);
            mSelectedWordsContainer.addView(wordButton, params);
        }
    }

    private void initViewListener() {
        mBtnPlayStart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play_start:
                if (mViewPanBar != null) {
                    if (!mIsRunning) {
                        mViewPanBar.startAnimation(mBarInAnim);
                        mIsRunning = true;
                        mBtnPlayStart.setVisibility(View.INVISIBLE);
                    }
                }
                break;
        }
    }

    @Override
    public void onWordClick(Button wordButton, Word word) {
        setSelectedWord(word);
        checkAnswer();//检验答案
    }

    /**
     * 获得关卡歌曲信息
     */
    private Song loadStageSongInfo(int stageIndex) {
        Song song = new Song();
        String[] stage = Constant.SONG_INFO[stageIndex];
        song.setFileName(stage[Constant.INDEX_SONG_FILE_NAME]);
        song.setName(stage[Constant.INDEX_SONG_NAME]);
        return song;
    }

    /**
     * 获得所有待选文字
     */
    private ArrayList<Word> getAllWords() {
        ArrayList<Word> data = new ArrayList<>();
        //加入当前歌曲文字
        for (int i = 0; i < mSelectedWords.size(); i++) {
            Word word = new Word();
            word.setText(mSelectedWords.get(i).getText());
            data.add(word);
        }
        //加入随机汉字
        for (int i = mSelectedWords.size(); i < WORD_COUNT; i++) {
            Word word = new Word();
            word.setText(Utils.getRandomHanzi());
            data.add(word);
        }
        //打乱顺序
        Collections.shuffle(data, new Random());
        return data;
    }

    /**
     * 获得所有已选文字
     */
    private ArrayList<Word> getSelectedWords() {
        ArrayList<Word> data = new ArrayList<>();
        for (char songNameChar : mCurrentSong.getNameCharacters()) {
            Word word = new Word();
            word.setText(String.valueOf(songNameChar));
            word.setVisiable(false);
            data.add(word);
        }
        return data;
    }

    /**
     * 设置已选文字
     */
    private void setSelectedWord(Word word) {
        for (int i = 0; i < mSelectedWords.size(); i++) {
            if (TextUtils.isEmpty(mSelectedButtonList.get(i).getText())) {//如果已选文字框有空位，则将点选的文字显示在已选文字框中
                mSelectedWords.get(i).setIndex(word.getIndex());
                mSelectedButtonList.get(i).setText(word.getText());
                word.setVisiable(false);
                mWordAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    /**
     * 清除已选文字
     */
    private void clearSelectedWord(int position) {
        if (!TextUtils.isEmpty(mSelectedButtonList.get(position).getText())) {
            mAllWords.get(mSelectedWords.get(position).getIndex()).setVisiable(true);
            mWordAdapter.notifyDataSetChanged();
            mSelectedButtonList.get(position).setText("");
        }
    }

    /**
     * 检查答案
     */
    private void checkAnswer() {
        if (isAnswerComplete()) {//检查答案是否完整
            if (isAnswerCorrect()) {//答案正确

            } else {//答案错误

            }
        }
    }

    /**
     * 答案是否完整
     */
    private boolean isAnswerComplete() {
        //检验已选字是否填满
        for (Button wordButton : mSelectedButtonList) {
            if (TextUtils.isEmpty(wordButton.getText())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 答案是否正确
     */
    private boolean isAnswerCorrect() {
        StringBuilder sb = new StringBuilder();
        for (Button wordButton : mSelectedButtonList) {
            sb.append(wordButton.getText());
        }
        return sb.toString().equals(mCurrentSong.getName());
    }

    @Override
    protected void onPause() {
        mViewPan.clearAnimation();
        super.onPause();
    }
}
package com.juphoon.cloud.sample;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juphoon.cloud.sample.JCWrapper.JCManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Clive
 */
public class CCodecActivity extends AppCompatActivity {

    private List<String> mAudioList;
    private List<String> mVideoList;
    private CustomAdapter mAudioAdapter;
    private CustomAdapter mVideoAdapter;

    private String[] mAudioEnable;
    private String[] mVideoEnable;
    private String[] mAudioSupport;
    private String[] mVideoSupport;

    //Check
    private boolean[] mCheckItems;
    private List<String> mCheckList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccodec);
        mVideoList = new ArrayList<>();
        mAudioList = new ArrayList<>();
        RecyclerView mAudioRecyclerView = (RecyclerView) findViewById(R.id.rv_audio);
        RecyclerView mVideoRecyclerView = (RecyclerView) findViewById(R.id.rv_video);
        TextView moreVideoView = findViewById(R.id.tv_more_video);
        TextView moreAudioView = findViewById(R.id.tv_more_audio);
        updateAudioList();
        updateVideoList();

        moreAudioView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreAudio();
            }
        });
        mAudioRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAudioAdapter = new CustomAdapter(this, mAudioList, CustomAdapter.AUDIO);
        mAudioRecyclerView.setAdapter(mAudioAdapter);
        ItemTouchHelper.Callback callback = new RecycleItemTouchHelper(mAudioAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mAudioRecyclerView);

        moreVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreVideo();
            }
        });
        mVideoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mVideoAdapter = new CustomAdapter(this, mVideoList, CustomAdapter.VIDEO);
        mVideoRecyclerView.setAdapter(mVideoAdapter);
        ItemTouchHelper.Callback callback2 = new RecycleItemTouchHelper(mVideoAdapter);
        ItemTouchHelper itemTouchHelper2 = new ItemTouchHelper(callback2);
        itemTouchHelper2.attachToRecyclerView(mVideoRecyclerView);

    }

    private void updateAudioList() {
        mAudioSupport = JCManager.getInstance().config.call.getSupportAudioCodec();
        mAudioEnable = JCManager.getInstance().config.call.getEnableAudioCodec();
        mAudioList.clear();
        Collections.addAll(mAudioList, mAudioEnable);
    }

    private void updateVideoList() {
        mVideoSupport = JCManager.getInstance().config.call.getSupportVideoCodec();
        mVideoEnable = JCManager.getInstance().config.call.getEnableVideoCodec();
        mVideoList.clear();
        Collections.addAll(mVideoList, mVideoEnable);
    }

    private void showMoreAudio() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CCodecActivity.this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(getString(R.string.audio_codec));
        List<String> audioEnableList = new ArrayList<>(Arrays.asList(mAudioEnable));
        mCheckList = new ArrayList<>(Arrays.asList(mAudioSupport));
        mCheckList.removeAll(audioEnableList);
        mCheckItems = new boolean[mCheckList.size()];
        for (int i = 0; i < mCheckList.size(); i++) {
            mCheckItems[i] = false;
        }
        builder.setMultiChoiceItems(mCheckList.toArray(new String[mCheckList.size()]), mCheckItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {

            }
        });
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < mCheckItems.length; i++) {
                    if (mCheckItems[i]) {
                        JCManager.getInstance().config.call.setAudioCodecEnable(mCheckList.get(i), true);
                        JCManager.getInstance().config.call.setAudioCodecByPriority(mCheckList.get(i), (short) (mAudioList.size()));
                        updateAudioList();
                    }
                }
                mAudioAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mCheckList != null) {
                    mCheckList.clear();
                    mCheckList = null;
                    mCheckItems = null;
                }
            }
        });
        builder.show();
    }

    private void showMoreVideo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(CCodecActivity.this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(getString(R.string.video_codec));
        List<String> videoEnableList = new ArrayList<>(Arrays.asList(mVideoEnable));
        mCheckList = new ArrayList<>(Arrays.asList(mVideoSupport));
        mCheckList.removeAll(videoEnableList);
        mCheckItems = new boolean[mCheckList.size()];
        for (int i = 0; i < mCheckList.size(); i++) {
            mCheckItems[i] = false;
        }
        builder.setMultiChoiceItems(mCheckList.toArray(new String[mCheckList.size()]), mCheckItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {

            }
        });
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < mCheckItems.length; i++) {
                    if (mCheckItems[i]) {
                        JCManager.getInstance().config.call.setVideoCodecEnable(mCheckList.get(i), true);
                        JCManager.getInstance().config.call.setVideoCodecByPriority(mCheckList.get(i), (short) (mVideoList.size()));
                        updateVideoList();
                    }
                }
                mVideoAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mCheckList != null) {
                    mCheckList.clear();
                    mCheckList = null;
                    mCheckItems = null;
                }
            }
        });
        builder.show();
    }

    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomeViewHolder> implements RecycleItemTouchHelper.ItemTouchHelperCallback {

        public final static int AUDIO = 0;
        public final static int VIDEO = 1;
        private Context context;
        private List<String> list;
        private int mediaType = -1;

        public CustomAdapter(Context context, List<String> list, int mediaType) {
            this.context = context;
            this.list = list;
            this.mediaType = mediaType;
        }

        @Override
        public CustomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_ccodec, parent, false);
            return new CustomeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CustomeViewHolder holder, int position) {
            holder.mItemTitle.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void onItemDelete(int positon) {
            if (mediaType == VIDEO) {
                JCManager.getInstance().config.call.setVideoCodecEnable(list.get(positon), false);
                updateVideoList();
            } else if (mediaType == AUDIO) {
                JCManager.getInstance().config.call.setAudioCodecEnable(list.get(positon), false);
                updateAudioList();
            }
            notifyItemRemoved(positon);
        }

        @Override
        public void onMove(int fromPosition, int toPosition) {
            Collections.swap(list, fromPosition, toPosition);
            for (int i = 0; i < list.size(); i++) {
                if (mediaType == VIDEO) {
                    JCManager.getInstance().config.call.setVideoCodecByPriority(list.get(i), (short) i);
                } else if (mediaType == AUDIO) {
                    JCManager.getInstance().config.call.setAudioCodecByPriority(list.get(i), (short) i);
                }
            }

            updateAudioList();
            updateVideoList();
            notifyItemMoved(fromPosition, toPosition);
        }

        public class CustomeViewHolder extends RecyclerView.ViewHolder {
            TextView mItemTitle;

            public CustomeViewHolder(View itemView) {
                super(itemView);
                mItemTitle = (TextView) itemView.findViewById(R.id.ccodec);
            }
        }
    }

    static class RecycleItemTouchHelper extends ItemTouchHelper.Callback {
        private static final String TAG = "RecycleItemTouchHelper";
        private final ItemTouchHelperCallback helperCallback;

        public RecycleItemTouchHelper(ItemTouchHelperCallback helperCallback) {
            this.helperCallback = helperCallback;
        }

        /**
         * 设置滑动类型标记
         *
         * @param recyclerView
         * @param viewHolder
         * @return 返回一个整数类型的标识，用于判断Item那种移动行为是允许的
         */
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            Log.e(TAG, "getMovementFlags: ");
            //START  右向左 END左向右 LEFT  向左 RIGHT向右  UP向上
            //如果某个值传0，表示不触发该操作
            return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.END);
        }

        /**
         * Item是否支持长按拖动
         *
         * @return true  支持长按操作
         * false 不支持长按操作
         */
        @Override
        public boolean isLongPressDragEnabled() {
            return super.isLongPressDragEnabled();
        }

        /**
         * Item是否支持滑动
         *
         * @return true  支持滑动操作
         * false 不支持滑动操作
         */
        @Override
        public boolean isItemViewSwipeEnabled() {
            return super.isItemViewSwipeEnabled();
        }

        /**
         * 拖拽切换Item的回调
         *
         * @param recyclerView
         * @param viewHolder
         * @param target
         * @return 如果Item切换了位置，返回true；反之，返回false
         */
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            Log.e(TAG, "onMove: ");
            helperCallback.onMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        /**
         * 滑动Item
         *
         * @param viewHolder
         * @param direction  Item滑动的方向
         */
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            Log.e(TAG, "onSwiped: ");
            helperCallback.onItemDelete(viewHolder.getAdapterPosition());
        }

        /**
         * Item被选中时候回调
         *
         * @param viewHolder
         * @param actionState 当前Item的状态
         *                    ItemTouchHelper.ACTION_STATE_IDLE   闲置状态
         *                    ItemTouchHelper.ACTION_STATE_SWIPE  滑动中状态
         *                    ItemTouchHelper#ACTION_STATE_DRAG   拖拽中状态
         */
        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
        }

        /**
         * 移动过程中绘制Item
         *
         * @param c
         * @param recyclerView
         * @param viewHolder
         * @param dX                X轴移动的距离
         * @param dY                Y轴移动的距离
         * @param actionState       当前Item的状态
         * @param isCurrentlyActive 如果当前被用户操作为true，反之为false
         */
        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            //滑动时自己实现背景及图片
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                //dX大于0时向右滑动，小于0向左滑动
                View itemView = viewHolder.itemView;//获取滑动的view
                Resources resources = itemView.getContext().getResources();
                Bitmap bitmap = BitmapFactory.decodeResource(resources, R.mipmap.delete);//获取删除指示的背景图片
                int padding = 10;//图片绘制的padding
                int maxDrawWidth = 2 * padding + bitmap.getWidth();//最大的绘制宽度
                Paint paint = new Paint();
                paint.setColor(resources.getColor(R.color.colorAccent));
                int x = Math.round(Math.abs(dX));
                int drawWidth = Math.min(x, maxDrawWidth);//实际的绘制宽度，取实时滑动距离x和最大绘制距离maxDrawWidth最小值
                int itemTop = itemView.getBottom() - itemView.getHeight();//绘制的top位置
                //向右滑动
                if (dX > 0) {
                    //根据滑动实时绘制一个背景
                    c.drawRect(itemView.getLeft(), itemTop, drawWidth, itemView.getBottom(), paint);
                    //在背景上面绘制图片
                    if (x > padding) {//滑动距离大于padding时开始绘制图片
                        //指定图片绘制的位置
                        Rect rect = new Rect();//画图的位置
                        rect.left = itemView.getLeft() + padding;
                        rect.top = itemTop + (itemView.getBottom() - itemTop - bitmap.getHeight()) / 2;//图片居中
                        int maxRight = rect.left + bitmap.getWidth();
                        rect.right = Math.min(x, maxRight);
                        rect.bottom = rect.top + bitmap.getHeight();
                        //指定图片的绘制区域
                        Rect rect1 = null;
                        if (x < maxRight) {
                            rect1 = new Rect();//不能再外面初始化，否则dx大于画图区域时，删除图片不显示
                            rect1.left = 0;
                            rect1.top = 0;
                            rect1.bottom = bitmap.getHeight();
                            rect1.right = x - padding;
                        }
                        c.drawBitmap(bitmap, rect1, rect, paint);
                    }
                    float alpha = 1.0f - Math.abs(dX) / (float) itemView.getWidth();
                    itemView.setAlpha(alpha);
                    //绘制时需调用平移动画，否则滑动看不到反馈
                    itemView.setTranslationX(dX);
                } else {
                    //如果在getMovementFlags指定了向左滑动（ItemTouchHelper。START）时则绘制工作可参考向右的滑动绘制，也可直接使用下面语句交友系统自己处理
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            } else {
                //拖动时有系统自己完成
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

        public interface ItemTouchHelperCallback {
            void onItemDelete(int positon);

            void onMove(int fromPosition, int toPosition);
        }
    }
}


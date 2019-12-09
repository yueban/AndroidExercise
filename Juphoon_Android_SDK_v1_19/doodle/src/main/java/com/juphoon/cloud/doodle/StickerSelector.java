package com.juphoon.cloud.doodle;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StickerSelector extends RelativeLayout {

    private static final int COLUMN_NUMBER = 5;

    private static final int[] EMOJI_UNICODE = new int[]{
            0x1F525, 0x1F47B, 0x1F4A9, 0x1F4A3, 0x1F52B,
            0x26BD, 0x1F3A9, 0x1F388, 0x1F4A4, 0x1F4A6,
            0x1F381, 0x1F495, 0x1F48B, 0x1F339, 0x1F48E,
            0x1F354, 0x1F35F, 0x1F355, 0x1F357, 0x1F37B,
            0x1F370, 0x1F382, 0x1F366, 0x1F36D, 0x1F34E,
            0x1F43C, 0x1F430, 0x1F436, 0x1F431, 0x1F42E,
            0x1F419, 0x1F427, 0x1F411, 0x1F43D, 0x1F43E
    };

    private static final String[] STICKER_NAMES = new String[]{
            "cry",
            "tongue",
            "heart_eyes",
            "red_lips",
            "sun_glass",
            "rabbit",
            "cat",
            "pig",
            "dog",
            "grass_on_head",
            "super_man",
            "purple_man",
            "black_man",
            "arm_man",
            "green_man",
    };

    private static final int[] THUMBNAIL_IDS = new int[]{
            R.mipmap.sticker_thumbnail_cry,
            R.mipmap.sticker_thumbnail_tongue,
            R.mipmap.sticker_thumbnail_hearteyes,
            R.mipmap.sticker_thumbnail_redlips,
            R.mipmap.sticker_thumbnail_sunglass,
            R.mipmap.sticker_thumbnail_rabbit,
            R.mipmap.sticker_thumbnail_cat,
            R.mipmap.sticker_thumbnail_pig,
            R.mipmap.sticker_thumbnail_dog,
            R.mipmap.sticker_thumbnail_grassonhead,
            R.mipmap.sticker_thumbnail_superman,
            R.mipmap.sticker_thumbnail_purpleman,
            R.mipmap.sticker_thumbnail_blackman,
            R.mipmap.sticker_thumbnail_armman,
            R.mipmap.sticker_thumbnail_greenman,
    };

    private static final int[] STICKER_IDS = new int[]{
            R.mipmap.sticker_cry,
            R.mipmap.sticker_tongue,
            R.mipmap.sticker_hearteyes,
            R.mipmap.sticker_redlips,
            R.mipmap.sticker_sunglass,
            R.mipmap.sticker_rabbit,
            R.mipmap.sticker_cat,
            R.mipmap.sticker_pig,
            R.mipmap.sticker_dog,
            R.mipmap.sticker_grassonhead,
            R.mipmap.sticker_superman,
            R.mipmap.sticker_purpleman,
            R.mipmap.sticker_blackman,
            R.mipmap.sticker_armman,
            R.mipmap.sticker_greenman,
    };

    private static final int[] WIDTH_IDS = new int[]{
            R.dimen.doodle_sticker_width_cry,
            R.dimen.doodle_sticker_width_tongue,
            R.dimen.doodle_sticker_width_hearteyes,
            R.dimen.doodle_sticker_width_redlips,
            R.dimen.doodle_sticker_width_sunglass,
            R.dimen.doodle_sticker_width_rabbit,
            R.dimen.doodle_sticker_width_cat,
            R.dimen.doodle_sticker_width_pig,
            R.dimen.doodle_sticker_width_dog,
            R.dimen.doodle_sticker_width_grassonhead,
            R.dimen.doodle_sticker_width_superman,
            R.dimen.doodle_sticker_width_purpleman,
            R.dimen.doodle_sticker_width_blackman,
            R.dimen.doodle_sticker_width_armman,
            R.dimen.doodle_sticker_width_greenman,
    };

    private static final int[] HEIGHT_IDS = new int[]{
            R.dimen.doodle_sticker_height_cry,
            R.dimen.doodle_sticker_height_tongue,
            R.dimen.doodle_sticker_height_hearteyes,
            R.dimen.doodle_sticker_height_redlips,
            R.dimen.doodle_sticker_height_sunglass,
            R.dimen.doodle_sticker_height_rabbit,
            R.dimen.doodle_sticker_height_cat,
            R.dimen.doodle_sticker_height_pig,
            R.dimen.doodle_sticker_height_dog,
            R.dimen.doodle_sticker_height_grassonhead,
            R.dimen.doodle_sticker_height_superman,
            R.dimen.doodle_sticker_height_purpleman,
            R.dimen.doodle_sticker_height_blackman,
            R.dimen.doodle_sticker_height_armman,
            R.dimen.doodle_sticker_height_greenman,
    };

    public static int findImageResourceId(String stickerName) {
        for (int i = 0; i < STICKER_NAMES.length; i++) {
            if (STICKER_NAMES[i].equals(stickerName)) {
                return STICKER_IDS[i];
            }
        }
        return 0;
    }

    private RecyclerView mRecyclerView;
    private OnClickListener mItemClickListener;

    public StickerSelector(Context context) {
        super(context);
        init();
    }

    public StickerSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setItemOnClickListener(OnClickListener listener) {
        mItemClickListener = listener;
    }

    private void init() {
        Context context = getContext();
        Resources resources = context.getResources();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(resources.getDimensionPixelOffset(R.dimen.sticker_recycler_width), ViewGroup.LayoutParams.WRAP_CONTENT);
        mRecyclerView = new RecyclerView(context);
        mRecyclerView.setAdapter(new StickerAdapter(context));
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, COLUMN_NUMBER));
        mRecyclerView.setLayoutParams(params);
        mRecyclerView.setPadding(5, 0, 5, 0);
        addView(mRecyclerView);
    }

    private final class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.StickerHolder> {

        private final static int ITEM_TYPE_TEXT = 1;
        private final static int ITEM_TYPE_IMAGE = 2;

        private final int EMOJI_SIZE_PX;
        private final int STICKER_SIZE;
        private final int STICKER_PADDING;
        private final Context mContext;
        private final Resources mResources;

        StickerAdapter(Context context) {
            mContext = context;
            mResources = context.getResources();
            STICKER_SIZE = mResources.getDimensionPixelSize(R.dimen.doodle_sticker_size);
            STICKER_PADDING = mResources.getDimensionPixelSize(R.dimen.doodle_sticker_padding);
            EMOJI_SIZE_PX = mResources.getDimensionPixelSize(R.dimen.doodle_sticker_emoji_default_text_size);
        }

        @Override
        public StickerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(STICKER_SIZE, STICKER_SIZE);
            if (viewType == ITEM_TYPE_TEXT) {
                TextView textView = new TextView(mContext);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, EMOJI_SIZE_PX);
                textView.setTextColor(Color.BLACK);
                textView.setGravity(Gravity.CENTER);
                textView.setOnClickListener(mItemClickListener);
                textView.setLayoutParams(params);
                return new StickerHolder(textView);
            } else {
                ImageView imageView = new ImageView(mContext);
                imageView.setPadding(STICKER_PADDING, STICKER_PADDING, STICKER_PADDING, STICKER_PADDING);
                imageView.setOnClickListener(mItemClickListener);
                imageView.setLayoutParams(params);
                return new StickerHolder(imageView);
            }
        }

        @Override
        public void onBindViewHolder(StickerHolder stickerHolder, int position) {
            StickerModel model = new StickerModel();
            if (getItemViewType(position) == ITEM_TYPE_TEXT) {
                model.isEmoji = true;
                int unicode = EMOJI_UNICODE[position];
                model.emojiText = new String(Character.toChars(unicode));
                model.emojiSizePX = EMOJI_SIZE_PX;
                model.widthInPixel = mResources.getDimensionPixelOffset(R.dimen.doodle_floating_sticker_default_size);
                model.heightInPixel = mResources.getDimensionPixelOffset(R.dimen.doodle_floating_sticker_default_size);
                stickerHolder.itemView.setTag(model);
                ((TextView) stickerHolder.itemView).setText(model.emojiText);
            } else {
                int index = position - EMOJI_UNICODE.length;
                model.isEmoji = false;
                model.imageResourceId = STICKER_IDS[index];
                model.stickerName = STICKER_NAMES[index];
                int padding = mResources.getDimensionPixelOffset(R.dimen.doodle_floating_sticker_padding);
                model.emojiPadding = padding;
                model.widthInPixel = mResources.getDimensionPixelSize(WIDTH_IDS[index]) + padding * 2;
                model.heightInPixel = mResources.getDimensionPixelSize(HEIGHT_IDS[index]) + padding * 2;
                stickerHolder.itemView.setTag(model);
                ((ImageView) stickerHolder.itemView).setImageResource(THUMBNAIL_IDS[index]);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position < EMOJI_UNICODE.length) {
                return ITEM_TYPE_TEXT;
            }
            return ITEM_TYPE_IMAGE;
        }

        @Override
        public int getItemCount() {
            return THUMBNAIL_IDS.length + EMOJI_UNICODE.length;
        }

        final class StickerHolder extends RecyclerView.ViewHolder {

            StickerHolder(View itemView) {
                super(itemView);
            }
        }
    }
}

package com.bigfat.game_pintu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bigfat.game_pintu.util.ImagePiece;
import com.bigfat.game_pintu.util.ImageSplitterUtil;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    private GridView gridView;
    private List<ImagePiece> imagePieces;
    private int piece = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        gridView = (GridView) findViewById(R.id.id_gridView);
        gridView.setNumColumns(piece);
        gridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return imagePieces.size();
            }

            @Override
            public ImagePiece getItem(int position) {
                return imagePieces.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ImageView img = new ImageView(MainActivity.this);
                img.setImageBitmap(getItem(position).getBitmap());
                return img;
            }
        });
    }

    private void initData() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test);
        imagePieces = ImageSplitterUtil.splitImage(bitmap, piece);
        bitmap.recycle();
    }
}

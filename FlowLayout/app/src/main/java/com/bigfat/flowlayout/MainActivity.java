package com.bigfat.flowlayout;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.bigfat.flowlayout.view.FlowLayout;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private String[] mValues = {
            "芮舒晖", "濮志远", "余然孜", "范柏舟", "吴金浪",
            "芮舒晖123", "濮志远123", "余然孜123", "范柏舟123", "吴金浪123",
            "芮舒晖123456", "濮志远123456", "余然孜123456", "范柏舟123456", "吴金浪123456",
    };
    private List<String> mDatas;

    private FlowLayout mFlowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFlowLayout = (FlowLayout) findViewById(R.id.id_flow_layout);

        initData();
    }

    private void initData() {
        mDatas = Arrays.asList(mValues);
        Collections.shuffle(mDatas);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        for (String str : mDatas) {
            TextView textView = (TextView) inflater.inflate(R.layout.tv, mFlowLayout, false);
            textView.setText(str);
            mFlowLayout.addView(textView);
//            Button button = new Button(MainActivity.this);
//            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT);
//            button.setText(str);
//            button.setTextSize(12);
//            mFlowLayout.addView(button, lp);
        }
    }
}

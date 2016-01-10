package com.bigfat.scrollviewedittexttest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigfat.draggedviewpager.utils.MDA_DraggedViewPagerController;
import com.bigfat.draggedviewpager.view.MDA_DraggedViewPager;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private MDA_DraggedViewPager draggedViewPager;
    private MDA_DraggedViewPagerController<Page, Item> controller;
    private List<Page> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        data = generateData();

        draggedViewPager = (MDA_DraggedViewPager) findViewById(R.id.dvp_main);
        controller = new MDA_DraggedViewPagerController<Page, Item>(data, R.layout.item_page, R.layout.item) {
            @Override
            public void bindPageData(View pageView, int pageIndex) {
                final TextView tvAddTask = (TextView) pageView.findViewById(R.id.tv_item_task_section_add_task);
                final RelativeLayout rlAddTask = (RelativeLayout) pageView.findViewById(R.id.rl_item_task_section_add_task);

                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvAddTask.setVisibility(View.GONE);
                        rlAddTask.setVisibility(View.VISIBLE);
                    }
                };
                tvAddTask.setOnClickListener(onClickListener);
            }

            @Override
            public void bindItemData(View itemView, int pageIndex, int itemIndex) {
                TextView tv = (TextView) itemView.findViewById(R.id.tv_item_title);
                tv.setText(data.get(pageIndex).getData().get(itemIndex).getTitle());
            }
        };
        draggedViewPager.setController(controller);
    }

    private List<Page> generateData() {
        List<Page> list = new ArrayList<>();
        list.add(new Page(generateItemList(10)));
        list.add(new Page(generateItemList(5)));
        list.add(new Page(generateItemList(15)));
        list.add(new Page(generateItemList(20)));
        return list;
    }

    private List<Item> generateItemList(int capacity) {
        List<Item> list = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            Item item = new Item(i + "");
            list.add(item);
        }
        return list;
    }
}

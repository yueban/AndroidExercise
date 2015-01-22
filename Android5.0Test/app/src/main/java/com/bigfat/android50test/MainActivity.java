package com.bigfat.android50test;

import android.graphics.Outline;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    //删除按钮
    private FrameLayout mDeleteBar;
    //添加按钮
    private View fabView;
    //RecyclerView对象
    private RecyclerView recyclerView;
    //适配器
    private SampleRecyclerAdapter adapter;
    //分割线
    private SampleDivider sampleDivider;
    //线性布局管理器（默认是垂直方向）
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //绑定控件
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mDeleteBar = (FrameLayout) findViewById(R.id.deleteBar);
        //创建左下角圆形按钮
        //创建RecyclerView控件
        //创建上方delete面板
        ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                //按钮尺寸
                int fabSize = getResources().getDimensionPixelSize(R.dimen.fab_size);
                //设置轮廓尺寸
                outline.setOval(-4, -4, fabSize + 2, fabSize + 2);
            }
        };
        //获得/裁剪右下角圆形按钮对象
        fabView = findViewById(R.id.fab_add);
        fabView.setOutlineProvider(viewOutlineProvider);

        //配置RecyclerView对象
        layoutManager = new LinearLayoutManager(this);
        sampleDivider = new SampleDivider(this);
        adapter = new SampleRecyclerAdapter();

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(sampleDivider);
        recyclerView.setAdapter(adapter);

        //设置监听器
        mDeleteBar.setOnClickListener(this);
        fabView.setOnClickListener(this);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.deleteBar:
                int positionToRemove = layoutManager.findFirstCompletelyVisibleItemPosition();
                adapter.removeData(positionToRemove);
                break;

            case R.id.fab_add:
                int positionToAdd = layoutManager.findFirstCompletelyVisibleItemPosition();
                adapter.addItem(positionToAdd);
                break;
        }
    }
}

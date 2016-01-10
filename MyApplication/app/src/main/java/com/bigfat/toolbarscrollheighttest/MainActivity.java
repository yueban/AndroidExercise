package com.bigfat.toolbarscrollheighttest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements AbsListView.OnScrollListener {

    // The height of your fully expanded header view (same than in the xml layout)
    private int headerHeight;
    // The height of your fully collapsed header view. Actually the Toolbar height (56dp)
    private int minHeaderHeight;
    // The left margin of the Toolbar title (according to specs, 72dp)
    private int toolbarTitleLeftMargin;
    // Added after edit
    private int minHeaderTranslation;

    private ListView listView;

    // Header views
    private View headerView;
    private RelativeLayout headerContainer;
    private TextView headerTitle;
    private TextView headerSubtitle;
    private ImageView headerFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init the headerHeight and minHeaderTranslation values
        headerHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        minHeaderHeight = getResources().getDimensionPixelSize(R.dimen.action_bar_height);
        toolbarTitleLeftMargin = getResources().getDimensionPixelSize(R.dimen.toolbar_left_margin);
        minHeaderTranslation = -headerHeight + minHeaderHeight;

        listView = (ListView) findViewById(R.id.lv_main);

        // Inflate your header view
        headerView = LayoutInflater.from(this).inflate(R.layout.include_header, listView, false);

        // Retrieve the header views
        headerContainer = (RelativeLayout) headerView.findViewById(R.id.header_container);
        headerTitle = (TextView) headerView.findViewById(R.id.header_title);
        headerSubtitle = (TextView) headerView.findViewById(R.id.header_subtitle);
        headerFab = (ImageView) headerView.findViewById(R.id.header_fab);

        List<String> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.activity_list_item, android.R.id.text1, data);
        listView.setAdapter(adapter);
        // Add the headerView to your listView
        listView.addHeaderView(headerView, null, false);

        // Set the onScrollListener
        listView.setOnScrollListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Integer scrollY = getScrollY(view);

        // This will collapse the header when scrolling, until its height reaches
        // the toolbar height
        headerView.setTranslationY(Math.max(0, scrollY + minHeaderTranslation));

        // Scroll ratio (0 <= ratio <= 1).
        // The ratio value is 0 when the header is completely expanded,
        // 1 when it is completely collapsed
        float offset = 1 - Math.max((float) (-minHeaderTranslation - scrollY) / -minHeaderTranslation, 0f);


        // Now that we have this ratio, we only have to apply translations, scales,
        // alpha, etc. to the header views

        // For instance, this will move the toolbar title & subtitle on the X axis
        // from its original position when the ListView will be completely scrolled
        // down, to the Toolbar title position when it will be scrolled up.
        headerTitle.setTranslationX(toolbarTitleLeftMargin * offset);
        headerSubtitle.setTranslationX(toolbarTitleLeftMargin * offset);

        // Or we can make the FAB disappear when the ListView is scrolled
        headerFab.setAlpha(1 - offset);
    }

    public int getScrollY(AbsListView view) {
        View c = view.getChildAt(0);

        if (c == null)
            return 0;

        int firstVisiblePosition = view.getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1)
            headerHeight = this.headerHeight;

        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }
}

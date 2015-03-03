package com.bigfat.arcmenu;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import com.bigfat.arcmenu.view.ArcMenu;
import com.bigfat.arcmenu.view.util.OnMenuItemClickListener;


public class MainActivity extends ActionBarActivity {

    private ArcMenu arcMenuLeftBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arcMenuLeftBottom = (ArcMenu) findViewById(R.id.id_arcmenu_left_bottom);

        arcMenuLeftBottom.setOnMenuClickListener(new OnMenuItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                Toast.makeText(MainActivity.this, pos + ":" + view.getTag(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

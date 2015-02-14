package com.bigfat.guaguaka;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.bigfat.guaguaka.view.GuaGuaKa;


public class MainActivity extends ActionBarActivity {

    private GuaGuaKa mGuaGuaKa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGuaGuaKa = (GuaGuaKa) findViewById(R.id.id_guaguaka);

        mGuaGuaKa.setOnGuaGuaKaCompleteListener(new GuaGuaKa.OnGuaGuaKaCompleteListener() {
            @Override
            public void onComplete() {
                Toast.makeText(MainActivity.this, "刮完了", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

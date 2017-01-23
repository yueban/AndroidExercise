package bigfat.com.rxbusdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import bigfat.com.rxbusdemo.event.CommonEvent;
import bigfat.com.rxbusdemo.rxbus.RxBus;
import rx.functions.Action1;

public class Activity2 extends BaseActivity {
    private static final String TAG = Activity2.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        //RxBus.postEventSticky(new CommonEvent(TAG + "_sticky"));
        RxBus.postEvent(new CommonEvent(TAG + "_normal"));

        //noinspection ConstantConditions
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity2.this, Activity3.class));
            }
        });

        mBusSticky
            .ofType(CommonEvent.class)
            .subscribe(new Action1<CommonEvent>() {
                @Override
                public void call(CommonEvent commonEvent) {
                    Log.i(TAG, commonEvent.message);
                }
            });

        mBus
            .ofType(CommonEvent.class)
            .subscribe(new Action1<CommonEvent>() {
                @Override
                public void call(CommonEvent commonEvent) {
                    Log.i(TAG, commonEvent.message);
                }
            });
    }
}

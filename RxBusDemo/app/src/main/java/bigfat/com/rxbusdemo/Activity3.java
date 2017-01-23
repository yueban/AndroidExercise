package bigfat.com.rxbusdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import bigfat.com.rxbusdemo.event.CommonEvent;
import bigfat.com.rxbusdemo.rxbus.RxBus;
import rx.functions.Action1;

public class Activity3 extends BaseActivity {
    private static final String TAG = Activity3.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        RxBus.postEventSticky(new CommonEvent(TAG + "_sticky"));
        RxBus.postEvent(new CommonEvent(TAG + "_normal"));

        //noinspection ConstantConditions
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity3.this, MainActivity.class).addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            }
        });

        mBusSticky
            .ofType(CommonEvent.class)
            .compose(this.<CommonEvent>bindToLifecycle())
            .subscribe(new Action1<CommonEvent>() {
                @Override
                public void call(CommonEvent commonEvent) {
                    Log.i(TAG, commonEvent.message);
                }
            });

        mBus
            .ofType(CommonEvent.class)
            .compose(this.<CommonEvent>bindToLifecycle())
            .subscribe(new Action1<CommonEvent>() {
                @Override
                public void call(CommonEvent commonEvent) {
                    Log.i(TAG, commonEvent.message);
                }
            });
    }
}

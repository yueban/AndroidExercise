package bigfat.com.rxbusdemo;

import android.app.Application;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by yueban on 15:16 31/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}

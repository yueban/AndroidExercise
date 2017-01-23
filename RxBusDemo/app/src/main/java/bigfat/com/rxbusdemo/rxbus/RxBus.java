package bigfat.com.rxbusdemo.rxbus;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

/**
 * Created by yueban on 12:47 31/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class RxBus {
    private static final String TAG = RxBus.class.getSimpleName();

    private SerializedSubject<Object, Object> rxBusStandard;
    private SerializedSubject<Object, Object> rxBusSticky;

    @SuppressWarnings("unchecked")
    private RxBus() {
        rxBusStandard = new SerializedSubject(PublishSubject.create());
        rxBusSticky = new SerializedSubject(BehaviorSubject.create());
    }

    private static RxBus getInstance() {
        return RxBusHolder.RX_BUS;
    }

    public static void postEvent(Object event) {
        getInstance().rxBusStandard.onNext(event);
    }

    public static void postEventSticky(Object event) {
        getInstance().rxBusSticky.onNext(event);
    }

    public static Observable<Object> toObservable() {
        return getInstance().rxBusStandard.asObservable().onBackpressureBuffer();
    }

    public static Observable<Object> toObservableSticky() {
        return getInstance().rxBusSticky.asObservable().share().onBackpressureBuffer();
    }

    public static boolean hasObservers() {
        return getInstance().rxBusStandard.hasObservers();
    }

    public static boolean hasObserversSticky() {
        return getInstance().rxBusSticky.hasObservers();
    }

    private static class RxBusHolder {
        private static final RxBus RX_BUS = new RxBus();
    }
}

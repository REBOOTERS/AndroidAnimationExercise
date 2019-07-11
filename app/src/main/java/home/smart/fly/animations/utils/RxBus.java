package home.smart.fly.animations.utils;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author zhuyongging @ Zhihu Inc.
 * @since 07-11-2019
 */
public class RxBus {
    private static final RxBus sRxBus = new RxBus();

    public static RxBus getInstance() {
        return sRxBus;
    }

    private PublishSubject<Object> mSubject = PublishSubject.create();

    private RxBus() {
    }

    public void post(Object object) {
        mSubject.onNext(object);
    }

    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mSubject.ofType(eventType);
    }

    public Observable<Object> toObservable() {
        return mSubject.hide();
    }

    public boolean hasObservers() {
        return mSubject.hasObservers();
    }
}

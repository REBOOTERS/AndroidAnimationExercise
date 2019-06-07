package com.engineer.dateview;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.engineer.dateview.dao.ActivityDao;
import com.engineer.dateview.model.ActModel;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: zhuyongging
 * @since: 2019-06-04
 */
public class ActivityRepository {

    private ActivityDao mActivityDao;
    private LiveData<ActModel> mActModelLiveData;

    public ActivityRepository(Application application) {
        ActivityRoomDatabase database = ActivityRoomDatabase.getInstance(application);
        mActivityDao = database.mActivityDao();
    }

    public Observable<ActModel> getActivityByName(String name) {
        return mActivityDao.getActivityByName(name);
    }

    public Observable<List<ActModel>> getAll() {
        return mActivityDao.getAllActivities();
    }

    @SuppressLint("CheckResult")
    public void insert(final ActModel actModel) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) {
                Log.e("room", "subscribe: insert actModel begin" + actModel.toString());
                mActivityDao.insert(actModel);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }
}

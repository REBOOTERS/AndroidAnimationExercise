package com.engineer.dateview.api;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.Lifecycle;

import com.engineer.dateview.model.ActModel;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2019/6/27.
 *
 * @author rookie
 */
public class UpdateModelDelegate {
    
    private static ActivityRepository mActivityRepository;

    static void injectRepository(ActivityRepository activityRepository) {
        mActivityRepository = activityRepository;
    }

    public static void saveLifeCycleData(Activity activity,  Lifecycle.Event event) {

        if (mActivityRepository == null) {
            throw new RuntimeException("please call init first");
        }

        String name = activity.getClass().getCanonicalName();
        Log.e("room", "name ==" + name);
        Log.e("room", "event ==" + event.name());

        Disposable d = Observable.create((ObservableOnSubscribe<ActModel>) emitter -> {
            ActModel actModel = mActivityRepository.getActivityByName(name);
            if (actModel == null) {
                actModel = new ActModel();
                actModel.setName(name);
            }

            emitter.onNext(actModel);
            emitter.onComplete();

        })

                .subscribeOn(Schedulers.io())
                .map(actModel -> {
                    int count;
                    switch (event) {
                        case ON_CREATE:
                            count = actModel.getOnActivityCreateCount();
                            actModel.setOnActivityCreateCount(++count);
                            return actModel;
                        case ON_START:
                            count = actModel.getOnActivityStartedCount();
                            actModel.setOnActivityStartedCount(++count);
                            return actModel;
                        case ON_RESUME:
                            count = actModel.getOnActivityResumedCount();
                            actModel.setOnActivityResumedCount(++count);
                            return actModel;
                        case ON_PAUSE:
                            count = actModel.getOnActivityPausedCount();
                            actModel.setOnActivityPausedCount(++count);
                            return actModel;
                        case ON_STOP:
                            count = actModel.getOnActivityStoppedCount();
                            actModel.setOnActivityPausedCount(++count);
                            return actModel;
                        case ON_DESTROY:
                            count = actModel.getOnActivityDestroyedCount();
                            actModel.setOnActivityDestroyedCount(++count);
                            return actModel;
                        default:
                            count = actModel.getOnActivitySaveInstanceStateCount();
                            actModel.setOnActivitySaveInstanceStateCount(++count);
                            return actModel;

                    }
                })
                .subscribe(actModel -> {
                            Log.e("room", "start insert : " + actModel.toString());
                            mActivityRepository.insert(actModel);
                        },
                        throwable -> {
                            Log.e("room", "wrong with " + throwable.getMessage());
                            throwable.printStackTrace();
                        });

    }
}

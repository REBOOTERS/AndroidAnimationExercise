package com.engineer.dateview;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.lifecycle.LiveData;

import com.engineer.dateview.dao.ActivityDao;
import com.engineer.dateview.model.ActModel;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

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

    public ActModel getActivityByName(String name) {
        return mActivityDao.getActivityByName(name);
    }

    public Observable<List<ActModel>> getAll() {
        return mActivityDao.getAllActivities();
    }

    @SuppressLint("CheckResult")
    public void insert(final ActModel actModel) {
        mActivityDao.insert(actModel);
    }
}

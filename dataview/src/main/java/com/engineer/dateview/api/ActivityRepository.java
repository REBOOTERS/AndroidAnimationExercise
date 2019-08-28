package com.engineer.dateview.api;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.lifecycle.LiveData;

import com.engineer.dateview.api.ActivityRoomDatabase;
import com.engineer.dateview.dao.ActivityDao;
import com.engineer.dateview.model.ActModel;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created on 2019/6/27.
 *
 * @author rookie
 */
public class ActivityRepository {
    private ActivityDao mActivityDao;
    private LiveData<ActModel> mActModelLiveData;

    ActivityRepository(Application application) {
        ActivityRoomDatabase database = ActivityRoomDatabase.getInstance(application);
        mActivityDao = database.mActivityDao();
    }

    ActModel getActivityByName(String name) {
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

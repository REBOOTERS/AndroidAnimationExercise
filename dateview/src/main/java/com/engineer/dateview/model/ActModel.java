package com.engineer.dateview.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author: zhuyongging
 * @since: 2019-06-04
 */
@Entity(tableName = "activity_table")
public class ActModel {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="class_name")
    private String name;

    @ColumnInfo(name = "create_count")
    private int onActivityCreateCount;

    @ColumnInfo(name = "start_count")
    private int onActivityStartedCount;

    @ColumnInfo(name = "resume_count")
    private int onActivityResumedCount;

    @ColumnInfo(name = "pause_count")
    private int onActivityPausedCount;

    @ColumnInfo(name = "stop_count")
    private int onActivityStoppedCount;

    @ColumnInfo(name = "save_count")
    private int onActivitySaveInstanceStateCount;

    @ColumnInfo(name = "destory_count")
    private int onActivityDestroyedCount;

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getOnActivityCreateCount() {
        return onActivityCreateCount;
    }

    public void setOnActivityCreateCount(int onActivityCreateCount) {
        this.onActivityCreateCount = onActivityCreateCount;
    }

    public int getOnActivityStartedCount() {
        return onActivityStartedCount;
    }

    public void setOnActivityStartedCount(int onActivityStartedCount) {
        this.onActivityStartedCount = onActivityStartedCount;
    }

    public int getOnActivityResumedCount() {
        return onActivityResumedCount;
    }

    public void setOnActivityResumedCount(int onActivityResumedCount) {
        this.onActivityResumedCount = onActivityResumedCount;
    }

    public int getOnActivityPausedCount() {
        return onActivityPausedCount;
    }

    public void setOnActivityPausedCount(int onActivityPausedCount) {
        this.onActivityPausedCount = onActivityPausedCount;
    }

    public int getOnActivityStoppedCount() {
        return onActivityStoppedCount;
    }

    public void setOnActivityStoppedCount(int onActivityStoppedCount) {
        this.onActivityStoppedCount = onActivityStoppedCount;
    }

    public int getOnActivitySaveInstanceStateCount() {
        return onActivitySaveInstanceStateCount;
    }

    public void setOnActivitySaveInstanceStateCount(int onActivitySaveInstanceStateCount) {
        this.onActivitySaveInstanceStateCount = onActivitySaveInstanceStateCount;
    }

    public int getOnActivityDestroyedCount() {
        return onActivityDestroyedCount;
    }

    public void setOnActivityDestroyedCount(int onActivityDestroyedCount) {
        this.onActivityDestroyedCount = onActivityDestroyedCount;
    }

    @Override
    public String toString() {
        return "ActModel{" +
                "name='" + name + '\'' +
                ", onActivityCreateCount=" + onActivityCreateCount +
                ", onActivityStartedCount=" + onActivityStartedCount +
                ", onActivityResumedCount=" + onActivityResumedCount +
                ", onActivityPausedCount=" + onActivityPausedCount +
                ", onActivityStoppedCount=" + onActivityStoppedCount +
                ", onActivitySaveInstanceStateCount=" + onActivitySaveInstanceStateCount +
                ", onActivityDestroyedCount=" + onActivityDestroyedCount +
                '}';
    }
}

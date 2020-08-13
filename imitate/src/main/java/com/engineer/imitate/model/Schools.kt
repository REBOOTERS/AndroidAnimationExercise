package com.engineer.imitate.model

import android.os.Parcel
import android.os.Parcelable

class Schools() : Parcelable {
    var province: String? = null

    var schoolList: List<School>? = null

    override fun toString(): String {
        return "Schools(province=$province, schoolList=$schoolList)"
    }

    constructor(source: Parcel) : this()

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {}

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Schools> = object : Parcelable.Creator<Schools> {
            override fun createFromParcel(source: Parcel): Schools = Schools(source)
            override fun newArray(size: Int): Array<Schools?> = arrayOfNulls(size)
        }
    }
}
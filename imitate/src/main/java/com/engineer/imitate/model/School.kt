package com.engineer.imitate.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Ignore

class School : Parcelable {
    class Location {
        var lat = 0f
        var lng = 0f
        override fun toString(): String {
            return "Location{" +
                    "lat=" + lat +
                    ", lng=" + lng +
                    '}'
        }
    }

    var name: String? = null

    var province: String? = null

    var city: String? = null

    var area: String? = null

    var address: String? = null

    var location: Location? = null

    override fun toString(): String {
        return "School{" +
                "name='" + name + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", address='" + address + '\'' +
                ", location=" + location +
                '}'
    }

    @Ignore
    constructor(){}


    constructor(source: Parcel) : this()



    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {}

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<School> = object : Parcelable.Creator<School> {
            override fun createFromParcel(source: Parcel): School = School(source)
            override fun newArray(size: Int): Array<School?> = arrayOfNulls(size)
        }
    }
}
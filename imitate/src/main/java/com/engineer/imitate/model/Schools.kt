package com.engineer.imitate.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.engineer.imitate.room.SchoolConverters

@Entity
@TypeConverters(SchoolConverters::class)
data class Schools(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var province: String? = null,
    var schoolList: List<School>? = null,
    var type: Int = 0,

    @ColumnInfo(name = "code")
    var code: String? = "aa"
) : Parcelable {


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
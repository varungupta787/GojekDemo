package com.gojek.demo.data.model

import android.os.Parcel
import android.os.Parcelable

class RepoItem() : Parcelable{
    var id: Int? = null
    var forks_count: Int? = null
    var language: String? = null
    var watchers_count: String? = null
    var owner: Owner? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        forks_count = parcel.readValue(Int::class.java.classLoader) as? Int
        language = parcel.readString()
        watchers_count = parcel.readString()
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<RepoItem> {
        override fun createFromParcel(parcel: Parcel): RepoItem {
            return RepoItem(parcel)
        }

        override fun newArray(size: Int): Array<RepoItem?> {
            return arrayOfNulls(size)
        }
    }
}
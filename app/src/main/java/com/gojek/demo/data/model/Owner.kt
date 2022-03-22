package com.gojek.demo.data.model

import android.os.Parcel
import android.os.Parcelable

data class Owner(var login: String? = null) : Parcelable {

    constructor(parcel: Parcel) : this() {
        login = parcel.readString()
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Owner> {
        override fun createFromParcel(parcel: Parcel): Owner {
            return Owner(parcel)
        }

        override fun newArray(size: Int): Array<Owner?> {
            return arrayOfNulls(size)
        }
    }
}
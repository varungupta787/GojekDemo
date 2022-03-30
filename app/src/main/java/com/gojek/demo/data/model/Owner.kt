package com.gojek.demo.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Owner(
    @SerializedName("login")
    var login: String? = null,
    @SerializedName("avatar_url")
    var avatar_url: String? = null
) : Parcelable
package com.gojek.demo.data.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.google.gson.annotations.SerializedName

@Parcelize
data class RepoItem(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("forks_count")
    var forks_count: Int? = null,
    @SerializedName("language")
    var language: String? = null,
    @SerializedName("watchers_count")
    var watchers_count: Int? = null,
    @SerializedName("html_url")
    var html_url: String? = null,
    @SerializedName("forks_url")
    var forks_url: String? = null,
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("owner")
    var owner: Owner? = null
) : Parcelable
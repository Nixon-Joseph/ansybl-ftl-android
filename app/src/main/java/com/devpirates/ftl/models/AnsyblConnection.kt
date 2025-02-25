package com.devpirates.ftl.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnsyblItemAttribute(
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String
) : Parcelable

@Parcelize
data class AnsyblItem(
    @SerializedName("type") val type: String,
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("content") val content: String,
    @SerializedName("attributedTo") val attributedTo: List<AnsyblItemAttribute>,
) : Parcelable

@Parcelize
data class AnsyblConnection(
    @SerializedName("@context") val context: String,
    @SerializedName("summary") val summary: String,
    @SerializedName("type") val type: String,
    @SerializedName("id") val id: String,
    @SerializedName("totalItems") val totalItems: Int,
    @SerializedName("items") val items: List<AnsyblItem>,
) : Parcelable
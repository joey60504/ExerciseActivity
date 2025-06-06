package com.example.exerciseactivity.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ParkResponse(
    @SerializedName("result")
    val result: ParkResult
)

data class ParkResult(
    @SerializedName("limit")
    val limit: Int?,
    @SerializedName("offset")
    val offset: Int?,
    @SerializedName("count")
    val count: Int?,
    @SerializedName("sort")
    val sort: String?,
    @SerializedName("results")
    val parks: List<Park>
)

@Parcelize
data class Park(
    @SerializedName("_id")
    val id: Int,
    @SerializedName("_importdate")
    val importdate: ImportDate,
    @SerializedName("e_no")
    val no: String,
    @SerializedName("e_category")
    val category: String,
    @SerializedName("e_name")
    val name: String,
    @SerializedName("e_pic_url")
    val picUrl: String,
    @SerializedName("e_info")
    val info: String,
    @SerializedName("e_memo")
    val memo: String,
    @SerializedName("e_geo")
    val geo: String,
    @SerializedName("e_url")
    val url: String,
) : Parcelable

@Parcelize
data class ImportDate(
    @SerializedName("date")
    val date: String,
    @SerializedName("timezone_type")
    val timezoneType: Int,
    @SerializedName("timezone")
    val timezone: String,
) : Parcelable
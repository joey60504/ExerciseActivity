package com.example.exerciseactivity.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ParkResponseInfo(
    @SerializedName("result")
    val result: ParkResultInfo
)

data class ParkResultInfo(
    @SerializedName("limit")
    val limit: Int?,
    @SerializedName("offset")
    val offset: Int?,
    @SerializedName("count")
    val count: Int?,
    @SerializedName("sort")
    val sort: String?,
    @SerializedName("results")
    val parks: List<ParkInfo>
)

@Parcelize
data class ParkInfo(
    @SerializedName("_id")
    val id: Int,
    @SerializedName("_importdate")
    val importDate: ImportDateInfo,
    @SerializedName("a_name_ch")
    val nameCh: String,
    @SerializedName("a_summary")
    val summary: String,
    @SerializedName("a_keywords")
    val keywords: String,
    @SerializedName("a_alsoknown")
    val alsoknown: String,
    @SerializedName("a_geo")
    val geo: String,
    @SerializedName("a_location")
    val location: String,
    @SerializedName("a_poigroup")
    val poigroup: String,
    @SerializedName("a_name_en")
    val nameEn: String,
    @SerializedName("a_name_latin")
    val nameLatin: String,
    @SerializedName("a_phylum")
    val phylum: String,
    @SerializedName("a_class")
    val animalClass: String,
    @SerializedName("a_order")
    val order: String,
    @SerializedName("a_family")
    val family: String,
    @SerializedName("a_conservation")
    val conservation: String,
    @SerializedName("a_distribution")
    val distribution: String,
    @SerializedName("a_habitat")
    val habitat: String,
    @SerializedName("a_feature")
    val feature: String,
    @SerializedName("a_behavior")
    val behavior: String,
    @SerializedName("a_diet")
    val diet: String,
    @SerializedName("a_crisis")
    val crisis: String,
    @SerializedName("a_interpretation")
    val interpretation: String,
    @SerializedName("a_theme_name")
    val themeName: String,
    @SerializedName("a_theme_url")
    val themeUrl: String,
    @SerializedName("a_adopt")
    val adopt: String,
    @SerializedName("a_code")
    val code: String,
    @SerializedName("a_pic01_alt")
    val pic01Alt: String,
    @SerializedName("a_pic01_url")
    val pic01Url: String,
    @SerializedName("a_pic02_alt")
    val pic02Alt: String,
    @SerializedName("a_pic02_url")
    val pic02Url: String,
    @SerializedName("a_pic03_alt")
    val pic03Alt: String,
    @SerializedName("a_pic03_url")
    val pic03Url: String,
    @SerializedName("a_pic04_alt")
    val pic04Alt: String,
    @SerializedName("a_pic04_url")
    val pic04Url: String,
    @SerializedName("a_pdf01_alt")
    val pdf01Alt: String,
    @SerializedName("a_pdf01_url")
    val pdf01Url: String,
    @SerializedName("a_pdf02_alt")
    val pdf02Alt: String,
    @SerializedName("a_pdf02_url")
    val pdf02Url: String,
    @SerializedName("a_voice01_alt")
    val voice01Alt: String,
    @SerializedName("a_voice01_url")
    val voice01Url: String,
    @SerializedName("a_voice02_alt")
    val voice02Alt: String,
    @SerializedName("a_voice02_url")
    val voice02Url: String,
    @SerializedName("a_voice03_alt")
    val voice03Alt: String,
    @SerializedName("a_voice03_url")
    val voice03Url: String,
    @SerializedName("a_vedio_url")
    val videoUrl: String,
    @SerializedName("a_update")
    val update: String,
    @SerializedName("a_cid")
    val cid: String,
    var park: Park? = null
) : Parcelable

@Parcelize
data class ImportDateInfo(
    @SerializedName("date")
    val date: String,
    @SerializedName("timezone_type")
    val timezoneType: Int,
    @SerializedName("timezone")
    val timezone: String
) : Parcelable
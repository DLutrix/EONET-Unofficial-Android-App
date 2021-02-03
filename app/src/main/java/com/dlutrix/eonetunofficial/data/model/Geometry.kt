package com.dlutrix.eonetunofficial.data.model


import com.google.gson.annotations.SerializedName

data class Geometry(
    @SerializedName("coordinates")
    val coordinates: List<Any>,
    @SerializedName("date")
    val date: String,
    @SerializedName("magnitudeUnit")
    val magnitudeUnit: Any?,
    @SerializedName("magnitudeValue")
    val magnitudeValue: Any?,
    @SerializedName("type")
    val type: String
)
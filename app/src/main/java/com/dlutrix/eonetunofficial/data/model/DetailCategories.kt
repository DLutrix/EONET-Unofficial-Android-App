package com.dlutrix.eonetunofficial.data.model


import com.google.gson.annotations.SerializedName

data class DetailCategories(
    @SerializedName("description")
    val description: String,
    @SerializedName("events")
    val events: List<Event>,
    @SerializedName("link")
    val link: String,
    @SerializedName("title")
    val title: String
)
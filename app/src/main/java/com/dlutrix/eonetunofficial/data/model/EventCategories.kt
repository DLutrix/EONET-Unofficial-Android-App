package com.dlutrix.eonetunofficial.data.model


import com.google.gson.annotations.SerializedName

data class EventCategories(
    @SerializedName("categories")
    val categories: List<CategoryX>,
    @SerializedName("description")
    val description: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("title")
    val title: String
)
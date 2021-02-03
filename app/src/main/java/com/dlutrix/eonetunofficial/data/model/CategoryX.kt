package com.dlutrix.eonetunofficial.data.model


import com.google.gson.annotations.SerializedName

data class CategoryX(
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("layers")
    val layers: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("title")
    val title: String
) {
    override fun toString(): String {
        return title
    }
}
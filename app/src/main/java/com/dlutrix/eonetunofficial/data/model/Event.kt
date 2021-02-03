package com.dlutrix.eonetunofficial.data.model


import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("categories")
    val categories: List<Category>,
    @SerializedName("closed")
    val closed: Any?,
    @SerializedName("description")
    val description: Any?,
    @SerializedName("geometry")
    val geometry: List<Geometry>,
    @SerializedName("id")
    val id: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("sources")
    val sources: List<Source>,
    @SerializedName("title")
    val title: String
) {
    override fun toString(): String {
        return title
    }
}
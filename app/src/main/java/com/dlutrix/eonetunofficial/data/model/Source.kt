package com.dlutrix.eonetunofficial.data.model


import com.google.gson.annotations.SerializedName

data class Source(
    @SerializedName("id")
    val id: String,
    @SerializedName("url")
    val url: String
)
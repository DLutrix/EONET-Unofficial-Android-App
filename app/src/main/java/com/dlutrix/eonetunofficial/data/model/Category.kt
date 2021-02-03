package com.dlutrix.eonetunofficial.data.model


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String
)
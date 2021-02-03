package com.dlutrix.eonetunofficial.data.source

import com.dlutrix.eonetunofficial.data.model.EventCategories
import com.dlutrix.eonetunofficial.data.model.DetailCategories
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EonetApiService {

    @GET("categories")
    suspend fun getCategories(): Response<EventCategories>


    @GET("categories/{categoryName}")
    suspend fun getDetailCategories(
        @Path("categoryName") categoryName: String
    ): Response<DetailCategories>
}
package com.dlutrix.eonetunofficial.data.source.eventCategories

import com.dlutrix.eonetunofficial.data.model.EventCategories
import com.dlutrix.eonetunofficial.data.source.EonetApiService
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventCategoriesRemoteDataSource @Inject constructor(
    private val api: EonetApiService
) {

    val categories = flow {
            emit(api.getCategories())
    }
}